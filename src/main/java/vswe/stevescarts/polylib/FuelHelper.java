package vswe.stevescarts.polylib;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class FuelHelper {
    public static boolean isItemFuel(@NotNull ItemStack itemStack, Level level) {
        return getItemBurnTime(itemStack, level) != 0;
    }

    public static int getItemBurnTime(@NotNull ItemStack itemstack, Level level) {
        return itemstack.getBurnTime(null, level.fuelValues());
    }
}