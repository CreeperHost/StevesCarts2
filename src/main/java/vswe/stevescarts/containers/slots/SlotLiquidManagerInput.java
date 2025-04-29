package vswe.stevescarts.containers.slots;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidUtil;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import vswe.stevescarts.api.slots.SlotStevesCarts;
import vswe.stevescarts.blocks.tileentities.TileEntityLiquid;
import vswe.stevescarts.helpers.storages.SCTank;

public class SlotLiquidManagerInput extends SlotStevesCarts {
    private TileEntityLiquid manager;
    private int tankid;

    public SlotLiquidManagerInput(final TileEntityLiquid manager, final int tankid, final int i, final int j, final int k) {
        super(manager, i, j, k);
        this.manager = manager;
        this.tankid = tankid;
    }

    @Override
    public boolean mayPlace(ItemStack itemstack) {
        IFluidHandler handler = FluidUtil.getFluidHandler(itemstack).orElse(null);
        if (handler == null) {
            return false;
        }
        if (tankid < 0 || tankid >= 4) {
            return true;
        }
        SCTank tank = manager.getTanks()[tankid];

        FluidStack fluidStack = handler.drain(1000, IFluidHandler.FluidAction.SIMULATE);
        return (fluidStack.isEmpty() && !tank.getFluid().isEmpty()) || (!fluidStack.isEmpty() && (tank.getFluid().isEmpty() || tank.getFluid().is(fluidStack.getFluid())));
    }
}
