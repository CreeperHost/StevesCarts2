package vswe.stevescarts.modules.addons;

import com.google.common.collect.Lists;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.NonNullList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.context.ContextKeySet;
import net.minecraft.util.context.ContextMap;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import vswe.stevescarts.client.guis.GuiMinecart;
import vswe.stevescarts.containers.slots.SlotCartCrafter;
import vswe.stevescarts.containers.slots.SlotCartCrafterResult;
import vswe.stevescarts.entities.ModularMinecart;
import vswe.stevescarts.helpers.RecipeHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class ModuleCrafter extends ModuleRecipe
{
    private int cooldown;

    public ModuleCrafter(ModularMinecart cart)
    {
        super(cart);
        cooldown = 0;
    }

    @Override
    public void update()
    {
        if (cooldown <= 0)
        {
            if (!getCart().level().isClientSide && getValidSlot() != null)
            {
                @Nonnull ItemStack result = getResult();//dummy.getResult();
                if (!result.isEmpty() && getCart().modules() != null)
                {
                    if (result.getCount() == 0)
                    {
                        result.setCount(1);
                    }
                    prepareLists();
                    if (canCraftMoreOfResult(result))
                    {
                        final NonNullList<ItemStack> originals = NonNullList.create();
                        for (int i = 0; i < allTheSlots.size(); ++i)
                        {
                            @Nonnull ItemStack item = allTheSlots.get(i).getItem();
                            originals.add((item.isEmpty()) ? ItemStack.EMPTY : item.copy());
                        }
                        final ArrayList<ItemStack> containers = new ArrayList<>();
                        boolean valid = true;
                        boolean edited = false;
                        for (int j = 0; j < 9; ++j)
                        {
                            @Nonnull ItemStack recipe = getStack(j);
                            if (!recipe.isEmpty())
                            {
                                valid = false;
                                for (int k = 0; k < inputSlots.size(); ++k)
                                {
                                    @Nonnull ItemStack item2 = inputSlots.get(k).getItem();
                                    if (!item2.isEmpty() && ItemStack.isSameItem(item2, recipe) && ItemStack.isSameItemSameComponents(item2, recipe))
                                    {
                                        edited = true;
                                        ItemStack remainder = item2.getCraftingRemainder();
                                        if (!remainder.isEmpty())
                                        {
                                            containers.add(remainder);
                                        }
                                        @Nonnull ItemStack itemStack = item2;
                                        itemStack.shrink(1);
                                        if (item2.getCount() <= 0)
                                        {
                                            inputSlots.get(k).set(ItemStack.EMPTY);
                                        }
                                        valid = true;
                                        break;
                                    }
                                }
                                if (!valid)
                                {
                                    break;
                                }
                            }
                        }
                        if (valid && getValidSlot() != null)
                        {
                            getCart().addItemToChest(result, getValidSlot(), null);
                            if (result.getCount() > 0)
                            {
                                valid = false;
                            }
                            else
                            {
                                edited = true;
                                for (int j = 0; j < containers.size(); ++j)
                                {
                                    @Nonnull ItemStack container = containers.get(j);
                                    if (container != null)
                                    {
                                        getCart().addItemToChest(container, getValidSlot(), null);
                                        if (container.getCount() > 0)
                                        {
                                            valid = false;
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                        if (!valid && edited)
                        {
                            for (int j = 0; j < allTheSlots.size(); ++j)
                            {
                                allTheSlots.get(j).set(originals.get(j));
                            }
                        }
                    }
                }
            }
            cooldown = 40;
        }
        else
        {
            --cooldown;
        }
    }

    private CraftingInput getInput() {
        return CraftingInput.of(3, 3, Lists.newArrayList(getStack(0), getStack(1), getStack(2), getStack(3), getStack(4), getStack(5), getStack(6), getStack(7), getStack(8)));
    }

    @Nullable
    public CraftingRecipe getRecipe()
    {
        //TODO, Re write recipe handling...
        return getCart().level() instanceof ServerLevel serverLevel ? RecipeHelper.findRecipe(RecipeType.CRAFTING, getInput(), serverLevel).map(RecipeHolder::value).orElse(null) : null;
    }

    @NotNull
    public ItemStack getResult()
    {
        CraftingRecipe recipe = getRecipe();
        if (recipe != null)
        {
            List<RecipeDisplay> displays = recipe.display();
            if (!displays.isEmpty()) {
                return displays.get((getCart().tickCount / 60) % displays.size()).result().resolveForFirstStack(new ContextMap.Builder().create(new ContextKeySet.Builder().build()));
            }
        }
        return ItemStack.EMPTY;
    }

    @Override
    protected int[] getArea()
    {
        return new int[]{68, 44, 16, 16};
    }

    @Override
    public boolean hasGui()
    {
        return true;
    }

    @Override
    public int getInventorySize()
    {
        return 10;
    }

    @Override
    public int generateSlots(int slotCount)
    {
        slotGlobalStart = slotCount;
        slotList = new ArrayList<>();
        for (int y = 0; y < 3; ++y)
        {
            for (int x = 0; x < 3; ++x)
            {
                slotList.add(new SlotCartCrafter(getCart(), slotCount++, 10 + 18 * x, 15 + 18 * y));
            }
        }
        slotList.add(new SlotCartCrafterResult(getCart(), slotCount++, 67, canUseAdvancedFeatures() ? 20 : 33));
        return slotCount;
    }

    @Override
    public void onInventoryChanged()
    {
        if (getCart().level().isClientSide)
        {
            setStack(9, getResult());
        }
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void drawForeground(GuiGraphics guiGraphics, GuiMinecart gui)
    {
        super.drawForeground(guiGraphics, gui);
        drawString(guiGraphics, gui, getModuleName(), 8, 6, 4210752);
    }

    @Override
    public int guiWidth()
    {
        return canUseAdvancedFeatures() ? 120 : 95;
    }

    @Override
    public int guiHeight()
    {
        return 75;
    }

    @Override
    protected boolean canUseAdvancedFeatures()
    {
        return false;
    }

    @Override
    protected int getLimitStartX()
    {
        return 90;
    }

    @Override
    protected int getLimitStartY()
    {
        return 23;
    }
}
