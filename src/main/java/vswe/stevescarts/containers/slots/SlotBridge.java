package vswe.stevescarts.containers.slots;

import net.minecraft.tags.ItemTags;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import vswe.stevescarts.helpers.storages.TransferHandler;

import javax.annotation.Nonnull;

public class SlotBridge extends SlotBase implements ISpecialItemTransferValidator
{
    public SlotBridge(final Container iinventory, final int i, final int j, final int k)
    {
        super(iinventory, i, j, k);
    }

    @Override
    public boolean mayPlace(@Nonnull ItemStack itemstack)
    {
        return isBridgeMaterial(itemstack);
    }

    public static boolean isBridgeMaterial(@Nonnull ItemStack itemstack)
    {
        return itemstack.is(ItemTags.PLANKS) || itemstack.is(ItemTags.STONE_BRICKS);
    }

    @Override
    public boolean isItemValidForTransfer(@Nonnull ItemStack item, final TransferHandler.TRANSFER_TYPE type)
    {
        return mayPlace(item) && type != TransferHandler.TRANSFER_TYPE.OTHER;
    }
}
