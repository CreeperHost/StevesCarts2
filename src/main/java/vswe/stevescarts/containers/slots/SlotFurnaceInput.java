package vswe.stevescarts.containers.slots;

import net.creeperhost.polylib.helpers.RecipeHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;

import javax.annotation.Nonnull;

public class SlotFurnaceInput extends SlotFake
{
    public SlotFurnaceInput(final Container iinventory, final int i, final int j, final int k)
    {
        super(iinventory, i, j, k);
    }

    @Override
    public boolean mayPlace(@Nonnull ItemStack itemstack)
    {
        var list = RecipeHelper.findRecipesByType(RecipeType.SMELTING, Minecraft.getInstance().level);
        for (Recipe<?> recipe : list)
        {
            ItemStack out = recipe.getResultItem(RegistryAccess.EMPTY);
            if(ItemStack.isSameItem(itemstack, out))
            {
                return true;
            }
        }
        return false;
    }
}
