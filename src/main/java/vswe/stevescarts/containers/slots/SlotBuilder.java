package vswe.stevescarts.containers.slots;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class SlotBuilder extends SlotBase
{
    public SlotBuilder(final IInventory iinventory, final int i, final int j, final int k)
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
