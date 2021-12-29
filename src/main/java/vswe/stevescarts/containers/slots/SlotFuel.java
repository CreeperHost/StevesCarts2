package vswe.stevescarts.containers.slots;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ForgeHooks;
import vswe.stevescarts.modules.engines.ModuleCoalBase;

import javax.annotation.Nonnull;

public class SlotFuel extends SlotBase
{
    public SlotFuel(final IInventory iinventory, final int i, final int j, final int k)
    {
        super(iinventory, i, j, k);
    }

    @Override
    public boolean mayPlace(@Nonnull ItemStack itemstack)
    {
        return ForgeHooks.getBurnTime(itemstack) != 0;
    }

    //TODO
    private int getItemBurnTime(@Nonnull ItemStack itemstack)
    {
        return ForgeHooks.getBurnTime(itemstack);
    }

    public static int getItemBurnTime(final ModuleCoalBase engine, @Nonnull ItemStack itemstack)
    {
        return (int) (ForgeHooks.getBurnTime(itemstack) * engine.getFuelMultiplier());
    }
}
