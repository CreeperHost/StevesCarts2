package vswe.stevescarts.helpers;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.InvWrapper;
import vswe.stevescarts.api.modules.ModuleBase;
import vswe.stevescarts.api.modules.template.ModuleChest;
import vswe.stevescarts.entities.EntityMinecartModular;

/**
 * Created by brandon3055 on 25/07/2025
 */
public class CartInvWrapper implements IItemHandlerModifiable {

    private final EntityMinecartModular cart;

    public CartInvWrapper(EntityMinecartModular cart) {
        this.cart = cart;
    }

    @Override
    public void setStackInSlot(int slot, ItemStack stack) {
        if (cart.getModules() == null) {
            return;
        }

        for (ModuleBase module : cart.getModules()) {
            if (!(module instanceof ModuleChest)) {
                continue;
            }
            if (slot < module.getInventorySize()) {
                module.setStack(slot, stack);
                break;
            }
            slot -= module.getInventorySize();
        }
    }

    @Override
    public int getSlots() {
        int slotCount = 0;
        if (cart.getModules() == null) {
            return 0;
        }

        for (ModuleBase module : cart.getModules()) {
            if (!(module instanceof ModuleChest)) {
                continue;
            }
            slotCount += module.getInventorySize();
        }
        return slotCount;
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        if (cart.getModules() == null) {
            return ItemStack.EMPTY;
        }

        for (ModuleBase module : cart.getModules()) {
            if (!(module instanceof ModuleChest)) {
                continue;
            }
            if (slot < module.getInventorySize()) {
                return module.getStack(slot);
            }
            slot -= module.getInventorySize();
        }
        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
        if (stack.isEmpty())
            return ItemStack.EMPTY;

        ItemStack stackInSlot = getStackInSlot(slot);

        int m;
        if (!stackInSlot.isEmpty()) {
            if (stackInSlot.getCount() >= Math.min(stackInSlot.getMaxStackSize(), getSlotLimit(slot)))
                return stack;

            if (!ItemStack.isSameItemSameTags(stack, stackInSlot))
                return stack;

            if (!isItemValid(slot, stack))
                return stack;

            m = Math.min(stack.getMaxStackSize(), getSlotLimit(slot)) - stackInSlot.getCount();

            if (stack.getCount() <= m) {
                if (!simulate) {
                    ItemStack copy = stack.copy();
                    copy.grow(stackInSlot.getCount());
                    setStackInSlot(slot, copy);
                    cart.setChanged();
                }

                return ItemStack.EMPTY;
            } else {
                // copy the stack to not modify the original one
                stack = stack.copy();
                if (!simulate) {
                    ItemStack copy = stack.split(m);
                    copy.grow(stackInSlot.getCount());
                    setStackInSlot(slot, copy);
                    cart.setChanged();
                    return stack;
                } else {
                    stack.shrink(m);
                    return stack;
                }
            }
        } else {
            if (!isItemValid(slot, stack))
                return stack;

            m = Math.min(stack.getMaxStackSize(), getSlotLimit(slot));
            if (m < stack.getCount()) {
                // copy the stack to not modify the original one
                stack = stack.copy();
                if (!simulate) {
                    setStackInSlot(slot, stack.split(m));
                    cart.setChanged();
                    return stack;
                } else {
                    stack.shrink(m);
                    return stack;
                }
            } else {
                if (!simulate) {
                    setStackInSlot(slot, stack);
                    cart.setChanged();
                }
                return ItemStack.EMPTY;
            }
        }
    }

    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        if (amount == 0)
            return ItemStack.EMPTY;

        ItemStack stackInSlot = getStackInSlot(slot);

        if (stackInSlot.isEmpty())
            return ItemStack.EMPTY;

        if (simulate) {
            if (stackInSlot.getCount() < amount) {
                return stackInSlot.copy();
            } else {
                ItemStack copy = stackInSlot.copy();
                copy.setCount(amount);
                return copy;
            }
        } else {
            int m = Math.min(stackInSlot.getCount(), amount);
            ItemStack decrStackSize = removeItem(slot, m);
            cart.setChanged();
            return decrStackSize;
        }
    }

    private ItemStack removeItem(int slot, int amount) {
        ItemStack stack = getStackInSlot(slot);
        if (stack.isEmpty() || amount == 0) {
            return ItemStack.EMPTY;
        }
        stack = stack.split(amount);
        cart.setChanged();
        return stack;
    }

    @Override
    public int getSlotLimit(int slot) {
        return 64;
    }

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o){
            return true;
        }
        if (!(o instanceof CartInvWrapper wrapper)){
            return false;
        }
        return cart.equals(wrapper.cart);
    }

    @Override
    public int hashCode() {
        return cart.hashCode();
    }
}
