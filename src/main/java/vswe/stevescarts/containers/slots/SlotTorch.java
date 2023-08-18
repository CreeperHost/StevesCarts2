package vswe.stevescarts.containers.slots;

import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import vswe.stevescarts.api.slots.SlotStevesCarts;

import javax.annotation.Nonnull;

public class SlotTorch extends SlotStevesCarts
{
    public SlotTorch(final Container iinventory, final int i, final int j, final int k)
    {
        super(iinventory, i, j, k);
    }

    @Override
    public boolean mayPlace(@Nonnull ItemStack itemstack)
    {
        return itemstack.is(Items.TORCH);
    }
}
