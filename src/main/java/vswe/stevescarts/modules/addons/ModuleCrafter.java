package vswe.stevescarts.modules.addons;

import com.mojang.blaze3d.vertex.PoseStack;
import net.creeperhost.polylib.helpers.RecipeHelper;
import net.minecraft.core.NonNullList;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import vswe.stevescarts.client.guis.GuiMinecart;
import vswe.stevescarts.containers.slots.SlotCartCrafter;
import vswe.stevescarts.containers.slots.SlotCartCrafterResult;
import vswe.stevescarts.entitys.EntityMinecartModular;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Set;

public class ModuleCrafter extends ModuleRecipe
{
    private int cooldown;
    CraftingContainer craftingDummy;

    public ModuleCrafter(final EntityMinecartModular cart)
    {
        super(cart);
        cooldown = 0;
        craftingDummy = new CraftingDummy(this);
    }

    @Override
    public void update()
    {
        if (cooldown <= 0)
        {
            if (!getCart().level.isClientSide && getValidSlot() != null)
            {
                @Nonnull ItemStack result = getResult();//dummy.getResult();
                if (!result.isEmpty() && getCart().getModules() != null)
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
                                    if (!item2.isEmpty() && item2.sameItem(recipe) && ItemStack.isSameIgnoreDurability(item2, recipe))
                                    {
                                        edited = true;
                                        if (item2.hasCraftingRemainingItem())
                                        {
                                            containers.add(item2.getItem().getCraftingRemainingItem(item2));
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
                        if (valid)
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

    @Nullable
    public CraftingRecipe getRecipe()
    {
        Set<Recipe<?>> recipes = RecipeHelper.findRecipesByType(RecipeType.CRAFTING, getCart().level);
        for (Recipe<?> iRecipe : recipes)
        {
            CraftingRecipe recipe = (CraftingRecipe) iRecipe;
            if (recipe.matches(craftingDummy, getCart().level))
            {
                return recipe;
            }
        }
        return null;
    }

    @Nullable
    public ItemStack getResult()
    {
        CraftingRecipe recipe = getRecipe();
        if (recipe != null)
        {
            return recipe.getResultItem().copy();
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
        if (getCart().level.isClientSide)
        {
            setStack(9, getResult() == null ? ItemStack.EMPTY : getResult());
        }
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void drawForeground(PoseStack matrixStack, GuiMinecart gui)
    {
        super.drawForeground(matrixStack, gui);
        drawString(matrixStack, gui, getModuleName(), 8, 6, 4210752);
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
