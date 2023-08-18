package vswe.stevescarts.containers.slots;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import vswe.stevescarts.api.slots.SlotStevesCarts;
import vswe.stevescarts.helpers.storages.TransferHandler;

import javax.annotation.Nonnull;

public class SlotCartCrafterResult extends SlotStevesCarts implements ISpecialItemTransferValidator
{
    public SlotCartCrafterResult(final Container iinventory, final int i, final int j, final int k)
    {
        super(iinventory, i, j, k);
    }

    @Override
    public boolean isItemValidForTransfer(@Nonnull ItemStack p0, TransferHandler.TRANSFER_TYPE p1)
    {
        return false;
    }

    @Override
    public boolean mayPickup(@NotNull Player player)
    {
        return false;
    }

    @Override
    public boolean mayPlace(@NotNull ItemStack itemStack)
    {
        return false;
    }

    @Override
    public int getMaxStackSize()
    {
        return 1;
    }
}
