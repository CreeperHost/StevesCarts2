package vswe.stevescarts.containers;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import vswe.stevescarts.containers.slots.SlotFake;

import javax.annotation.Nullable;

public abstract class ContainerBase extends AbstractContainerMenu {
    public ContainerBase(@Nullable MenuType<?> p_i50105_1_, int p_i50105_2_) {
        super(p_i50105_1_, p_i50105_2_);
    }

    public static boolean canStacksMerge(ItemStack stack1, ItemStack stack2) {
        if (stack1.isEmpty() || stack2.isEmpty()) return false;
        return ItemStack.isSameItemSameComponents(stack1, stack2);
    }

    @Override
    public void clicked(int slotIndex, int button, ContainerInput input, Player player) {
        if (slotIndex >= 0 && slotIndex < slots.size() && slots.get(slotIndex) instanceof SlotFake ghostSlot) {
            handleGhostSlotClick(ghostSlot, button, input, player);
            return;
        }

        super.clicked(slotIndex, button, input, player);
    }

    private void handleGhostSlotClick(SlotFake slot, int button, ContainerInput input, Player player) {
        switch (input) {
            case PICKUP -> {
                if (button != 0 && button != 1) {
                    return;
                }

                ItemStack carried = getCarried();
                if (carried.isEmpty()) {
                    slot.set(ItemStack.EMPTY);
                } else if (slot.mayPlace(carried)) {
                    slot.set(carried);
                }
            }
            case SWAP -> {
                if (button < 0 || button >= player.getInventory().getContainerSize()) {
                    return;
                }

                ItemStack hotbarStack = player.getInventory().getItem(button);
                if (hotbarStack.isEmpty()) {
                    slot.set(ItemStack.EMPTY);
                } else if (slot.mayPlace(hotbarStack)) {
                    slot.set(hotbarStack);
                }
            }
            case QUICK_MOVE, THROW -> slot.set(ItemStack.EMPTY);
            case CLONE, QUICK_CRAFT, PICKUP_ALL -> {
                // A ghost item must never be cloned, dragged, collected, or thrown as a real item.
            }
        }
    }

    @Override
    public boolean canDragTo(Slot slot) {
        return !(slot instanceof SlotFake);
    }

    @Override
    public boolean canTakeItemForPickAll(ItemStack carried, Slot target) {
        return !(target instanceof SlotFake);
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int slotIndex) {
        ItemStack originalStack = ItemStack.EMPTY;
        Slot slot = slots.get(slotIndex);
        int numSlots = slots.size();
        if (slot != null && slot.hasItem()) {
            ItemStack stackInSlot = slot.getItem();
            originalStack = stackInSlot.copy();
            if (slotIndex >= numSlots - 9 * 4 && tryShiftItem(stackInSlot, numSlots)) {
                // NOOP
            } else if (slotIndex >= numSlots - 9 * 4 && slotIndex < numSlots - 9) {
                if (!shiftItemStack(stackInSlot, numSlots - 9, numSlots)) {
                    return ItemStack.EMPTY;
                }
            } else if (slotIndex >= numSlots - 9 && slotIndex < numSlots) {
                if (!shiftItemStack(stackInSlot, numSlots - 9 * 4, numSlots - 9)) {
                    return ItemStack.EMPTY;
                }
            } else if (!shiftItemStack(stackInSlot, numSlots - 9 * 4, numSlots)) {
                return ItemStack.EMPTY;
            }
            slot.onQuickCraft(stackInSlot, originalStack);
            if (stackInSlot.getCount() <= 0) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
            if (stackInSlot.getCount() == originalStack.getCount()) {
                return ItemStack.EMPTY;
            }
            slot.onTake(player, stackInSlot);
        }
        return originalStack;
    }

    protected boolean shiftItemStack(ItemStack stackToShift, int start, int end) {
        boolean changed = false;
        if (stackToShift.isStackable()) {
            for (int slotIndex = start; stackToShift.getCount() > 0 && slotIndex < end; slotIndex++) {
                Slot slot = slots.get(slotIndex);
                ItemStack stackInSlot = slot.getItem();
                if (!stackInSlot.isEmpty() && canStacksMerge(stackInSlot, stackToShift)) {
                    int resultingStackSize = stackInSlot.getCount() + stackToShift.getCount();
                    int max = Math.min(stackToShift.getMaxStackSize(), slot.getMaxStackSize());
                    if (resultingStackSize <= max) {
                        stackToShift.setCount(0);
                        stackInSlot.setCount(resultingStackSize);
                        slot.setChanged();
                        changed = true;
                    } else if (stackInSlot.getCount() < max) {
                        stackToShift.setCount(stackToShift.getCount() - (max - stackInSlot.getCount()));
                        stackInSlot.setCount(max);
                        slot.setChanged();
                        changed = true;
                    }
                }
            }
        }
        if (stackToShift.getCount() > 0) {
            for (int slotIndex = start; stackToShift.getCount() > 0 && slotIndex < end; slotIndex++) {
                Slot slot = slots.get(slotIndex);
                ItemStack stackInSlot = slot.getItem();
                if (stackInSlot.isEmpty()) {
                    int max = Math.min(stackToShift.getMaxStackSize(), slot.getMaxStackSize());
                    stackInSlot = stackToShift.copy();
                    stackInSlot.setCount(Math.min(stackToShift.getCount(), max));
                    stackToShift.setCount(stackToShift.getCount() - stackInSlot.getCount());
                    slot.set(stackInSlot);
                    slot.setChanged();
                    changed = true;
                }
            }
        }
        return changed;
    }

    private boolean tryShiftItem(ItemStack stackToShift, int numSlots) {
        for (int machineIndex = 0; machineIndex < numSlots - 9 * 4; machineIndex++) {
            Slot slot = slots.get(machineIndex);
            if (!slot.mayPlace(stackToShift)) continue;
            if (slot instanceof SlotFake) {
                slot.set(stackToShift);
                return true;
            }
            if (shiftItemStack(stackToShift, machineIndex, machineIndex + 1)) return true;
        }
        return false;
    }

    public void receiveGuiData(int id, int data) {

    }
}
