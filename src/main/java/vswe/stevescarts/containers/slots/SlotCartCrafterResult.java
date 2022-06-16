package vswe.stevescarts.containers.slots;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import vswe.stevescarts.helpers.storages.TransferHandler;

import javax.annotation.Nonnull;

public class SlotCartCrafterResult extends SlotBase implements ISpecialItemTransferValidator
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
    public boolean mayPickup(Player p_82869_1_)
    {
        return false;
    }

    @Override
    public boolean mayPlace(ItemStack p_75214_1_)
    {
        return false;
    }

    @Override
    public int getMaxStackSize()
    {
        return 1;
    }
}
