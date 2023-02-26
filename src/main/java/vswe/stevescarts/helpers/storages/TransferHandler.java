package vswe.stevescarts.helpers.storages;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import vswe.stevescarts.containers.slots.ISpecialItemTransferValidator;
import vswe.stevescarts.containers.slots.ISpecialSlotValidator;

import javax.annotation.Nonnull;

public class TransferHandler
{
    public static boolean isSlotOfType(final Slot slot, final Class slotType)
    {
        if (slot instanceof final ISpecialSlotValidator specSlot)
        {
            return specSlot.isSlotValid();
        }
        return slotType.isInstance(slot);
    }

    public static boolean isItemValidForTransfer(final Slot slot, @Nonnull ItemStack item, final TRANSFER_TYPE type)
    {
        if (slot instanceof final ISpecialItemTransferValidator specSlot)
        {
            return specSlot.isItemValidForTransfer(item, type);
        }
        return slot.mayPlace(item);
    }

    public static void TransferItem(@Nonnull ItemStack iStack, final Container inv, final AbstractContainerMenu cont, final int maxItems)
    {
        TransferItem(iStack, inv, cont, Slot.class, null, maxItems);
    }

    public static void TransferItem(@Nonnull ItemStack iStack, final Container inv, final AbstractContainerMenu cont, final Class validSlot, final int maxItems, final TRANSFER_TYPE type)
    {
        TransferItem(iStack, inv, 0, inv.getContainerSize() - 1, cont, validSlot, null, maxItems, type, false);
    }

    public static void TransferItem(@Nonnull ItemStack iStack, final Container inv, final AbstractContainerMenu cont, final Class validSlot, final Class invalidSlot, final int maxItems)
    {
        TransferItem(iStack, inv, 0, inv.getContainerSize() - 1, cont, validSlot, invalidSlot, maxItems);
    }

    public static void TransferItem(@Nonnull ItemStack iStack, final Container inv, final int start, final int end, final AbstractContainerMenu cont, final Class validSlot, final Class invalidSlot, final int maxItems)
    {
        TransferItem(iStack, inv, start, end, cont, validSlot, invalidSlot, maxItems, TRANSFER_TYPE.OTHER, false);
    }

    public static void TransferItem(@Nonnull ItemStack iStack, final Container inv, int start, int end, final AbstractContainerMenu cont, final Class validSlot, final Class invalidSlot, int maxItems, final TRANSFER_TYPE type, final boolean fake)
    {
        start = Math.max(0, start);
        end = Math.min(inv.getContainerSize() - 1, end);
        int startEmpty = start;
        int startOccupied = start;
        int pos;
        do
        {
            pos = -1;
            for (int i = startEmpty; i <= end; ++i) {
                if (isSlotOfType(cont.getSlot(i), validSlot) &&
                        (invalidSlot == null || !isSlotOfType(cont.getSlot(i), invalidSlot)) &&
                        !inv.getItem(i).isEmpty() &&
                        inv.getItem(i).getItem() == iStack.getItem() &&
                        inv.getItem(i).isStackable() &&
                        inv.getItem(i).getCount() < inv.getItem(i).getMaxStackSize() &&
                        inv.getItem(i).getCount() < cont.getSlot(i).getMaxStackSize() &&
                        inv.getItem(i).getCount() > 0 && iStack.getCount() > 0 &&
                        (inv.getItem(i).getTag() == null || inv.getItem(i).getTag().equals(iStack.getTag()))) {

                    pos = i;
                    startEmpty = pos + 1;
                    break;
                }
            }
            if (pos == -1) {
                for (int i = startOccupied; i <= end; ++i)
                {
                    if (isSlotOfType(cont.getSlot(i), validSlot) && (invalidSlot == null || !isSlotOfType(cont.getSlot(i), invalidSlot)))
                    {
                        final Slot slot = cont.getSlot(i);
                        if (isItemValidForTransfer(slot, iStack, type) && inv.getItem(i).isEmpty())
                        {
                            pos = i;
                            startOccupied = pos + 1;
                            break;
                        }
                    }
                }
            }
            if (pos != -1)
            {
                ItemStack existingItem = null;
                if (inv.getItem(pos).isEmpty())
                {
                    @Nonnull ItemStack clone = iStack.copy();
                    clone.setCount(0);
                    if (!fake)
                    {
                        inv.setItem(pos, clone);
                    }
                    existingItem = clone;
                }
                else
                {
                    existingItem = inv.getItem(pos);
                }
                int stackSize = iStack.getCount();
                if (stackSize > existingItem.getMaxStackSize() - existingItem.getCount())
                {
                    stackSize = existingItem.getMaxStackSize() - existingItem.getCount();
                }
                if (stackSize > cont.getSlot(pos).getMaxStackSize() - existingItem.getCount())
                {
                    stackSize = cont.getSlot(pos).getMaxStackSize() - existingItem.getCount();
                }
                boolean killMe = false;
                if (maxItems != -1)
                {
                    if (stackSize > maxItems)
                    {
                        stackSize = maxItems;
                        killMe = true;
                    }
                    maxItems -= stackSize;
                }
                if (stackSize <= 0)
                {
                    pos = -1;
                }
                else
                {
                    iStack.shrink(stackSize);
                    if (!fake)
                    {
                        @Nonnull ItemStack stackInSlot = inv.getItem(pos);
                        stackInSlot.grow(stackSize);
                    }
                    if (iStack.getCount() != 0 && !killMe && maxItems != 0)
                    {
                        continue;
                    }
                    pos = -1;
                }
            }
        } while (pos != -1);
        if (!fake)
        {
            inv.setChanged();
        }
    }

    public enum TRANSFER_TYPE
    {
        SHIFT, MANAGER, OTHER
    }
}
