package vswe.stevescarts.containers.slots;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import vswe.stevescarts.helpers.storages.TransferHandler;

import javax.annotation.Nonnull;

public class SlotBridge extends SlotBase implements ISpecialItemTransferValidator
{
    public SlotBridge(final IInventory iinventory, final int i, final int j, final int k)
    {
        super(iinventory, i, j, k);
    }

    @Override
    public boolean mayPlace(@Nonnull ItemStack itemstack)
    {
        //		return isBridgeMaterial(itemstack);
        return true;
    }

    @Override
    public boolean isItemValidForTransfer(@Nonnull ItemStack p0, TransferHandler.TRANSFER_TYPE p1)
    {
        return false;
    }

    //	public static boolean isBridgeMaterial(@Nonnull ItemStack itemstack) {
    //		final Block b = Block.getBlockFromItem(itemstack.getItem());
    //		return b == Blocks.PLANKS || b == Blocks.BRICK_BLOCK || b == Blocks.STONE || (b == Blocks.STONEBRICK && itemstack.getItemDamage() == 0);
    //	}

    //	@Override
    //	public boolean isItemValidForTransfer(@Nonnull ItemStack item, final TransferHandler.TRANSFER_TYPE type) {
    //		return isItemValid(item) && type != TransferHandler.TRANSFER_TYPE.OTHER;
    //	}
}
