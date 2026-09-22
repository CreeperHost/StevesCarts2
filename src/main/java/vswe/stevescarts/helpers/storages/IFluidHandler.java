package vswe.stevescarts.helpers.storages;

import net.neoforged.neoforge.fluids.FluidStack;

import javax.annotation.Nonnull;

/**
 * Internal compatibility contract for the cart fluid logic while the public
 * NeoForge capability is migrated to the 26.3 transfer API.
 */
public interface IFluidHandler {
    enum FluidAction {
        EXECUTE,
        SIMULATE;

        public boolean execute() {
            return this == EXECUTE;
        }
    }

    int getTanks();

    @Nonnull
    FluidStack getFluidInTank(int tank);

    int getTankCapacity(int tank);

    boolean isFluidValid(int tank, @Nonnull FluidStack stack);

    int fill(FluidStack resource, FluidAction action);

    @Nonnull
    FluidStack drain(FluidStack resource, FluidAction action);

    @Nonnull
    FluidStack drain(int maxDrain, FluidAction action);
}
