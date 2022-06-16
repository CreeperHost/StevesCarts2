package vswe.stevescarts.polylib;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.common.ForgeHooks;

import javax.annotation.Nonnull;

public class FuelHelper
{
    public static boolean isItemFuel(@Nonnull ItemStack itemStack)
    {
        return getItemBurnTime(itemStack) != 0;
    }

    public static int getItemBurnTime(@Nonnull ItemStack itemstack)
    {
        //TODO figure out how to do this without using ForgeHooks
        return ForgeHooks.getBurnTime(itemstack, RecipeType.SMELTING);
    }
}
