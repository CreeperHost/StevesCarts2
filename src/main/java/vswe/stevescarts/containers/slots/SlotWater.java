package vswe.stevescarts.containers.slots;

import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import javax.annotation.Nonnull;

public class SlotWater extends SlotBase
{
    public SlotWater(final Container iinventory, final int i, final int j, final int k)
    {
        super(iinventory, i, j, k);
    }

    @Override
    public boolean mayPlace(@Nonnull ItemStack itemstack)
    {
        return itemstack.getItem() == Items.WATER_BUCKET;
    }
}
