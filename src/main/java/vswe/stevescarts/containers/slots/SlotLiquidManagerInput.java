package vswe.stevescarts.containers.slots;

import net.minecraft.item.ItemStack;
import vswe.stevescarts.blocks.tileentities.TileEntityLiquid;

public class SlotLiquidManagerInput extends SlotBase
{
    private TileEntityLiquid manager;
    private int tankid;

    public SlotLiquidManagerInput(final TileEntityLiquid manager, final int tankid, final int i, final int j, final int k)
    {
        super(manager, i, j, k);
        this.manager = manager;
        this.tankid = tankid;
    }

    @Override
    public boolean mayPlace(ItemStack itemstack)
    {
        //TODO

        //		IFluidHandler handler = FluidUtil.getFluidHandler(itemstack);
        //		if (handler == null) return false;
        //		if (tankid < 0 || tankid >= 4) return true;
        //
        //		final SCTank tank = manager.getTanks()[tankid];
        //
        //		FluidStack fluidStack = handler.drain(Fluid.BUCKET_VOLUME, false);
        //		return ((fluidStack == null || fluidStack.amount <= 0) && tank.getFluid() != null) ||
        //				((fluidStack != null && fluidStack.amount > 0) && (tank.getFluid() == null || tank.getFluid().isFluidEqual(fluidStack)));
        return true;
    }
}
