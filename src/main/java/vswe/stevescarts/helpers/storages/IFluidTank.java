package vswe.stevescarts.helpers.storages;

import net.neoforged.neoforge.fluids.FluidStack;

import javax.annotation.Nonnull;

public interface IFluidTank extends IFluidHandler {
    @Nonnull
    FluidStack getFluid();

    int getFluidAmount();

    int getCapacity();

    boolean isFluidValid(FluidStack stack);

    @Override
    default int getTanks() {
        return 1;
    }

    @Nonnull
    @Override
    default FluidStack getFluidInTank(int tank) {
        if (tank != 0) {
            throw new IndexOutOfBoundsException(tank);
        }
        return getFluid();
    }

    @Override
    default int getTankCapacity(int tank) {
        if (tank != 0) {
            throw new IndexOutOfBoundsException(tank);
        }
        return getCapacity();
    }

    @Override
    default boolean isFluidValid(int tank, @Nonnull FluidStack stack) {
        if (tank != 0) {
            throw new IndexOutOfBoundsException(tank);
        }
        return isFluidValid(stack);
    }
}
