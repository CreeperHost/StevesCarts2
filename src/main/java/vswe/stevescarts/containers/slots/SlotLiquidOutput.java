package vswe.stevescarts.containers.slots;

import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidUtil;
import vswe.stevescarts.api.slots.SlotStevesCarts;
import vswe.stevescarts.helpers.storages.TransferHandler;

import javax.annotation.Nonnull;

public class SlotLiquidOutput extends SlotStevesCarts implements ISpecialItemTransferValidator
{
    public SlotLiquidOutput(final Container iinventory, final int i, final int j, final int k)
    {
        super(iinventory, i, j, k);
    }

    @Override
    public boolean mayPlace(@Nonnull ItemStack itemstack)
    {
        return false;
    }

    @Override
    public boolean isItemValidForTransfer(@Nonnull ItemStack item, final TransferHandler.TRANSFER_TYPE type)
    {
        return type == TransferHandler.TRANSFER_TYPE.OTHER && FluidUtil.getFluidHandler(item).isPresent();
    }
}
