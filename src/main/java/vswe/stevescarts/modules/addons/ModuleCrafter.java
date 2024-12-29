package vswe.stevescarts.modules.addons;

import com.google.common.collect.Lists;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.NonNullList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import vswe.stevescarts.api.slots.SlotStevesCarts;
import vswe.stevescarts.client.guis.GuiMinecart;
import vswe.stevescarts.containers.slots.SlotCartCrafter;
import vswe.stevescarts.containers.slots.SlotCartCrafterResult;
import vswe.stevescarts.entities.ModularMinecart;

import java.util.ArrayList;
import java.util.Optional;

public class ModuleCrafter extends ModuleRecipe {
    private int cooldown;
    private RecipeHolder<CraftingRecipe> lastRecipe = null;
    private boolean inventoryDirty = true;

    public ModuleCrafter(ModularMinecart cart) {
        super(cart);
        cooldown = 0;
    }

    @Override
    public void update() {
        if (!(getCart().level() instanceof ServerLevel level) || getValidSlot() == null) {
            return;
        }

        if (inventoryDirty) {
            outputDisplay.set(getResult(level));
            inventoryDirty = false;
        }

        if (--cooldown > 0) {
            return;
        }
        cooldown = 40;

        ItemStack result = getResult(level);
        outputDisplay.set(result);
        if (result.isEmpty()) {
            return;
        }

        prepareLists();

        if (!canCraftMoreOfResult(result)) {
            return;
        }

        NonNullList<ItemStack> originals = NonNullList.create();
        for (SlotStevesCarts slot : allTheSlots) {
            ItemStack item = slot.getItem();
            originals.add((item.isEmpty()) ? ItemStack.EMPTY : item.copy());
        }

        ArrayList<ItemStack> containers = new ArrayList<>();
        boolean valid = true;
        boolean edited = false;

        for (int j = 0; j < 9; ++j) {
            ItemStack recipe = getStack(j);
            if (!recipe.isEmpty()) {
                valid = false;
                for (SlotStevesCarts inputSlot : inputSlots) {
                    ItemStack stack = inputSlot.getItem();
                    if (!stack.isEmpty() && ItemStack.isSameItem(stack, recipe) && ItemStack.isSameItemSameComponents(stack, recipe)) {
                        edited = true;
                        ItemStack remainder = stack.getCraftingRemainder();
                        if (!remainder.isEmpty()) {
                            containers.add(remainder);
                        }
                        stack.shrink(1);
                        if (stack.getCount() <= 0) {
                            inputSlot.set(ItemStack.EMPTY);
                        }
                        valid = true;
                        break;
                    }
                }
                if (!valid) {
                    break;
                }
            }
        }

        if (valid && getValidSlot() != null) {
            getCart().addItemToChest(result, getValidSlot(), null);
            if (result.getCount() > 0) {
                valid = false;
            } else {
                edited = true;
                for (ItemStack container : containers) {
                    if (container != null) {
                        getCart().addItemToChest(container, getValidSlot(), null);
                        if (container.getCount() > 0) {
                            valid = false;
                            break;
                        }
                    }
                }
            }
        }

        if (!valid && edited) {
            for (int j = 0; j < allTheSlots.size(); ++j) {
                allTheSlots.get(j).set(originals.get(j));
            }
        }
    }

    @Override
    public void onInventoryChanged() {
        inventoryDirty = true;
    }

    private CraftingInput getInput() {
        return CraftingInput.of(3, 3, Lists.newArrayList(getStack(0), getStack(1), getStack(2), getStack(3), getStack(4), getStack(5), getStack(6), getStack(7), getStack(8)));
    }

    private ItemStack getResult(ServerLevel level) {
        CraftingInput input = getInput();
        Optional<RecipeHolder<CraftingRecipe>> optional = level.getServer().getRecipeManager().getRecipeFor(RecipeType.CRAFTING, input, level, lastRecipe);
        if (optional.isPresent()) {
            lastRecipe = optional.get();
            CraftingRecipe recipe = lastRecipe.value();
            ItemStack result = recipe.assemble(input, level.registryAccess());
            if (result.isItemEnabled(level.enabledFeatures())) {
                return result;
            }
        }
        return ItemStack.EMPTY;
    }

    @Override
    protected int[] getArea() {
        return new int[]{68, 44, 16, 16};
    }

    @Override
    public boolean hasGui() {
        return true;
    }

    @Override
    public int getInventorySize() {
        return 10;
    }

    @Override
    public int generateSlots(int slotCount) {
        slotGlobalStart = slotCount;
        slotList = new ArrayList<>();
        for (int y = 0; y < 3; ++y) {
            for (int x = 0; x < 3; ++x) {
                slotList.add(new SlotCartCrafter(getCart(), slotCount++, 10 + 18 * x, 15 + 18 * y));
            }
        }
        slotList.add(new SlotCartCrafterResult(getCart(), slotCount++, 67, canUseAdvancedFeatures() ? 20 : 33));
        return slotCount;
    }

    @OnlyIn (Dist.CLIENT)
    @Override
    public void drawForeground(GuiGraphics guiGraphics, GuiMinecart gui) {
        super.drawForeground(guiGraphics, gui);
        setStack(9, outputDisplay.get());
        drawString(guiGraphics, gui, getModuleName(), 8, 6, 4210752);
    }

    @Override
    public int guiWidth() {
        return canUseAdvancedFeatures() ? 120 : 95;
    }

    @Override
    public int guiHeight() {
        return 75;
    }

    @Override
    protected boolean canUseAdvancedFeatures() {
        return false;
    }

    @Override
    protected int getLimitStartX() {
        return 90;
    }

    @Override
    protected int getLimitStartY() {
        return 23;
    }
}
