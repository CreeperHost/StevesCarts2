package vswe.stevescarts.containers.slots;

import net.minecraft.world.Container;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import org.jetbrains.annotations.NotNull;
import vswe.stevescarts.helpers.storages.SCTank;

public class SlotLiquidInput extends SlotBase
{
    private SCTank tank;
    private int maxsize;

    public SlotLiquidInput(final Container iinventory, final SCTank tank, final int maxsize, final int i, final int j, final int k)
    {
        super(iinventory, i, j, k);
        this.tank = tank;
        this.maxsize = maxsize;
    }

    @Override
    public boolean mayPlace(@NotNull ItemStack itemStack)
    {
        LazyOptional<IFluidHandlerItem> opt = FluidUtil.getFluidHandler(itemStack);
        if (!opt.isPresent()) return false;
        IFluidHandlerItem fluidHandler = opt.orElseThrow(RuntimeException::new);

        FluidStack fluidStack = fluidHandler.drain(FluidType.BUCKET_VOLUME, IFluidHandler.FluidAction.SIMULATE);
        //Can Fill Container
        if (fluidStack.isEmpty() && !tank.getFluid().isEmpty()) return true;
        //Can Empty Container
        return tank.getFluid().isEmpty() || tank.getFluid().isFluidEqual(fluidStack);
    }

    @Override
    public int getMaxStackSize()
    {
        if (maxsize != -1) {
            return maxsize;
        }
        return Math.min(8, tank.getCapacity() / FluidType.BUCKET_VOLUME);
    }
}
