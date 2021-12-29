package vswe.stevescarts.containers.slots;

import net.minecraft.item.ItemStack;
import vswe.stevescarts.helpers.storages.TransferHandler;

import javax.annotation.Nonnull;

public interface ISpecialItemTransferValidator
{
    boolean isItemValidForTransfer(@Nonnull ItemStack p0, final TransferHandler.TRANSFER_TYPE p1);
}
