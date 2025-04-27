package vswe.stevescarts.modules.addons;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import vswe.stevescarts.api.slots.SlotStevesCarts;
import vswe.stevescarts.client.guis.GuiMinecart;
import vswe.stevescarts.containers.slots.SlotCartCrafterResult;
import vswe.stevescarts.containers.slots.SlotFurnaceInput;
import vswe.stevescarts.entities.ModularMinecart;

import javax.annotation.Nonnull;
import java.util.Optional;

public class ModuleSmelter extends ModuleRecipe
{
    private int energyBuffer;
    private int cooldown;
    private RecipeHolder<SmeltingRecipe> lastRecipe = null;
    private boolean inventoryDirty = true;

    public ModuleSmelter(ModularMinecart cart)
    {
        super(cart);
        cooldown = 0;
    }

    @Override
    public void update() {
        if (!(getCart().level() instanceof ServerLevel level) || getValidSlot() == null) {
            return;
        }

        ItemStack recipe = getStack(0);
        if (inventoryDirty) {
            outputDisplay.set(getResult(level, recipe));
            inventoryDirty = false;
        }

        if (getCart().hasFuelForModule() && energyBuffer < 10) {
            ++energyBuffer;
        }

        if (--cooldown > 0) {
            return;
        }
        cooldown = 40;

        if (energyBuffer < 10) {
            return;
        }

        ItemStack result = getResult(level, recipe);
        outputDisplay.set(result);
        if (result.isEmpty()) {
            return;
        }

        prepareLists();

        if (!canCraftMoreOfResult(result)) {
            return;
        }

        NonNullList<ItemStack> originals = NonNullList.create();
        for (SlotStevesCarts allTheSlot : allTheSlots) {
            ItemStack item = allTheSlot.getItem();
            originals.add((item.isEmpty()) ? ItemStack.EMPTY : item.copy());
        }

        int i = 0;
        while (i < inputSlots.size()) {
            @Nonnull ItemStack item = inputSlots.get(i).getItem();
            if (!item.isEmpty() && ItemStack.isSameItem(item, recipe) && ItemStack.isSameItemSameComponents(item, recipe)) {
                @Nonnull ItemStack itemStack = item;
                itemStack.shrink(1);
                if (itemStack.getCount() <= 0) {
                    inputSlots.get(i).set(ItemStack.EMPTY);
                }
                getCart().addItemToChest(result, getValidSlot(), null);
                if (result.getCount() != 0) {
                    for (int j = 0; j < allTheSlots.size(); ++j) {
                        allTheSlots.get(j).set(originals.get(j));
                    }
                    break;
                }
                energyBuffer = 0;
                break;
            } else {
                ++i;
            }
        }
    }

    @Override
    public void onInventoryChanged() {
        inventoryDirty = true;
    }

    private ItemStack getResult(ServerLevel level, ItemStack inputStack) {
        SingleRecipeInput input = new SingleRecipeInput(inputStack);
        Optional<RecipeHolder<SmeltingRecipe>> optional = level.getServer().getRecipeManager().getRecipeFor(RecipeType.SMELTING, input, level, lastRecipe);
        if (optional.isPresent()) {
            lastRecipe = optional.get();
            SmeltingRecipe recipe = lastRecipe.value();
            ItemStack result = recipe.assemble(input, level.registryAccess());
            if (result.isItemEnabled(level.enabledFeatures())) {
                return result;
            }
        }
        return ItemStack.EMPTY;
    }

    @Override
    public int getConsumption(final boolean isMoving)
    {
        if (energyBuffer < 10)
        {
            return 15;
        }
        return super.getConsumption(isMoving);
    }

    @Override
    public boolean hasGui()
    {
        return true;
    }

    @Override
    protected int getInventoryWidth()
    {
        return 1;
    }

    @Override
    protected int getInventoryHeight()
    {
        return 2;
    }

    @Override
    protected SlotStevesCarts getSlot(final int slotId, final int x, final int y)
    {
        if (y == 0)
        {
            return new SlotFurnaceInput(getCart(), getCart().level(), slotId, 10 + 18 * x, 15 + 18 * y);
        }
        return new SlotCartCrafterResult(getCart(), slotId, 10 + 18 * x, 15 + 18 * y);
    }

    @Override
    public int numberOfGuiData()
    {
        return super.numberOfGuiData() + 1;
    }

    @Override
    protected void checkGuiData(final Object[] info)
    {
        super.checkGuiData(info);
        updateGuiData(info, super.numberOfGuiData() + 0, (short) energyBuffer);
    }

    @Override
    public void receiveGuiData(final int id, final short data)
    {
        super.receiveGuiData(id, data);
        if (id == super.numberOfGuiData() + 0)
        {
            energyBuffer = data;
        }
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void drawForeground(GuiGraphics guiGraphics, GuiMinecart gui)
    {
        super.drawForeground(guiGraphics, gui);
        setStack(1, outputDisplay.get());
        drawString(guiGraphics, gui, getModuleName(), 8, 6, 4210752);
    }

    @Override
    public int guiWidth()
    {
        return canUseAdvancedFeatures() ? 100 : 45;
    }

    @Override
    protected int[] getArea()
    {
        return new int[]{32, 25, 16, 16};
    }

    @Override
    protected boolean canUseAdvancedFeatures()
    {
        return false;
    }

    @Override
    protected void load(CompoundTag tag, int id, HolderLookup.Provider provider)
    {
        super.load(tag, id, provider);
        energyBuffer = tag.getByteOr(generateNBTName("Buffer", id), (byte) 0);
    }

    @Override
    protected void save(CompoundTag tag, int id, HolderLookup.Provider provider)
    {
        super.save(tag, id, provider);
        tag.putByte(generateNBTName("Buffer", id), (byte) energyBuffer);
    }

    @Override
    protected int getLimitStartX()
    {
        return 55;
    }

    @Override
    protected int getLimitStartY()
    {
        return 15;
    }
}
