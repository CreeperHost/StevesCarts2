package vswe.stevescarts.containers.slots;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import vswe.stevescarts.helpers.storages.TransferHandler;

import javax.annotation.Nonnull;

public class SlotLiquidFilter extends SlotBase implements ISpecialItemTransferValidator
{
    public SlotLiquidFilter(final IInventory iinventory, final int i, final int j, final int k)
    {
        super(iinventory, i, j, k);
    }

    @Override
    public boolean isItemValidForTransfer(@Nonnull ItemStack item, final TransferHandler.TRANSFER_TYPE type)
    {
        return false;
    }

    @Override
    public boolean mayPlace(@Nonnull ItemStack itemstack)
    {
        return true;
    }
}
