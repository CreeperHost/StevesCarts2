package vswe.stevescarts.containers.slots;

import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;

public class SlotTorch extends SlotBase
{
    public SlotTorch(final Container iinventory, final int i, final int j, final int k)
    {
        super(iinventory, i, j, k);
    }

    @Override
    public boolean mayPlace(@Nonnull ItemStack itemstack)
    {
        return true;
    }
}
