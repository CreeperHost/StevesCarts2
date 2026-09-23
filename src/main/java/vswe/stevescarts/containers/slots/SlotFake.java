package vswe.stevescarts.containers.slots;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import vswe.stevescarts.api.slots.SlotStevesCarts;
import vswe.stevescarts.helpers.storages.TransferHandler;

import javax.annotation.Nonnull;

public abstract class SlotFake extends SlotStevesCarts implements ISpecialItemTransferValidator {
    public SlotFake(final Container iinventory, final int i, final int j, final int k) {
        super(iinventory, i, j, k);
    }

    /**
     * Ghost slots describe a recipe or filter. They never own a real stack, so keep
     * only one representative item and prevent the vanilla menu code from taking it.
     * ContainerBase handles clicks on these slots without changing the carried stack.
     */
    @Override
    public void set(ItemStack stack) {
        super.set(stack.isEmpty() ? ItemStack.EMPTY : stack.copyWithCount(1));
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }

    @Override
    public int getMaxStackSize(ItemStack stack) {
        return 1;
    }

    @Override
    public boolean mayPickup(Player player) {
        return false;
    }

    @Override
    public ItemStack remove(int amount) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean isFake() {
        return true;
    }

    @Override
    public boolean isItemValidForTransfer(@Nonnull ItemStack item, final TransferHandler.TRANSFER_TYPE type) {
        return false;
    }
}
