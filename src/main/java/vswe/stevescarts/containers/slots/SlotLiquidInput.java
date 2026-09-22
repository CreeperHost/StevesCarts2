package vswe.stevescarts.containers.slots;

import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.transfer.fluid.FluidUtil;
import org.jetbrains.annotations.NotNull;
import vswe.stevescarts.api.slots.SlotStevesCarts;
import vswe.stevescarts.helpers.storages.SCTank;
import vswe.stevescarts.helpers.storages.FluidContainerHelper;

public class SlotLiquidInput extends SlotStevesCarts {
    private final SCTank tank;
    private final int maxsize;

    public SlotLiquidInput(final Container iinventory, final SCTank tank, final int maxsize, final int i, final int j, final int k) {
        super(iinventory, i, j, k);
        this.tank = tank;
        this.maxsize = maxsize;
    }

    @Override
    public boolean mayPlace(@NotNull ItemStack itemStack) {
        if (!FluidContainerHelper.hasHandler(itemStack)) return false;
        FluidStack fluidStack = FluidUtil.getFirstStackContained(itemStack);
        //Can Fill Container
        if (fluidStack.isEmpty() && !tank.getFluid().isEmpty()) return true;
        //Can Empty Container
        return tank.getFluid().isEmpty() || FluidStack.isSameFluidSameComponents(tank.getFluid(), fluidStack);
    }

    @Override
    public int getMaxStackSize() {
        if (maxsize != -1) {
            return maxsize;
        }
        return Math.min(8, tank.getCapacity() / FluidType.BUCKET_VOLUME);
    }
}
