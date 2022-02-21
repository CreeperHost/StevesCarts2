package vswe.stevescarts.containers.slots;

import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;

public class SlotBuilder extends SlotBase
{
    public SlotBuilder(final Container iinventory, final int i, final int j, final int k)
    {
        super(iinventory, i, j, k);
    }

    @Override
    public boolean mayPlace(ItemStack itemstack)
    {
        //TODO
        return true;
        //		return Block.getBlockFromItem(itemstack.getItem()) instanceof BlockRailBase;
    }
}
