package vswe.stevescarts.containers.slots;

import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import vswe.stevescarts.helpers.storages.TransferHandler;

import javax.annotation.Nonnull;

public abstract class SlotFake extends SlotBase implements ISpecialItemTransferValidator
{
    public SlotFake(final Container iinventory, final int i, final int j, final int k)
    {
        super(iinventory, i, j, k);
    }

    //TODO
    //	@Override
    //	public int getSlotStackLimit() {
    //		return 0;
    //	}
    //
    //	@Override
    //	public int getItemStackLimit(ItemStack stack) {
    //		ItemStack copy = stack.copy();
    //		copy.setCount(1);
    //		this.inventory.setInventorySlotContents(getSlotIndex(), copy);
    //		return 0;
    //	}
    //
    //	@Override
    //	public void putStack(ItemStack stack) {
    //		if (!stack.isEmpty())
    //			super.putStack(stack);
    //		else
    //			this.onSlotChanged();
    //	}
    //
    //	@Override
    //	public boolean canTakeStack(EntityPlayer playerIn) {
    //		if (playerIn.inventory.getItemStack().isEmpty())
    //			return true;
    //		return false;
    //	}
    //
    //	@Override
    //	public ItemStack onTake(final EntityPlayer par1EntityPlayer, @Nonnull ItemStack par2ItemStack) {
    //		super.onTake(par1EntityPlayer, par2ItemStack);
    //		if (!par2ItemStack.isEmpty() && par1EntityPlayer != null && par1EntityPlayer.inventory != null) {
    //			par1EntityPlayer.inventory.setItemStack(ItemStack.EMPTY);
    //		}
    //		return ItemStack.EMPTY;
    //	}

    @Override
    public boolean isItemValidForTransfer(@Nonnull ItemStack item, final TransferHandler.TRANSFER_TYPE type)
    {
        return false;
    }
}
