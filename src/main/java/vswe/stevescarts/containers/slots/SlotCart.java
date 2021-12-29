package vswe.stevescarts.containers.slots;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import vswe.stevescarts.init.ModItems;

public class SlotCart extends Slot
{
    public SlotCart(final IInventory iinventory, final int i, final int j, final int k)
    {
        super(iinventory, i, j, k);
    }

    @Override
    public boolean mayPlace(ItemStack itemstack)
    {
        return !itemstack.isEmpty() && itemstack.getItem() == ModItems.CARTS.get() && itemstack.getTag() != null && !itemstack.getTag().contains("maxTime");
    }
}
