package vswe.stevescarts.containers.slots;

import net.creeperhost.polylib.helpers.FuelHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import vswe.stevescarts.api.slots.SlotStevesCarts;

import javax.annotation.Nonnull;

public class SlotFuel extends SlotStevesCarts
{
    public SlotFuel(final Container iinventory, final int i, final int j, final int k)
    {
        super(iinventory, i, j, k);
    }

    @Override
    public boolean mayPlace(@Nonnull ItemStack itemstack)
    {
        return FuelHelper.isItemFuel(itemstack);
    }
}
