package vswe.stevescarts.modules.workers;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import vswe.stevescarts.api.modules.ModuleBase;
import vswe.stevescarts.api.modules.template.ModuleWorker;
import vswe.stevescarts.entities.EntityMinecartModular;
import vswe.stevescarts.modules.workers.tools.ModuleFarmer;

public class ModuleHydrater extends ModuleWorker
{
    private int range;

    public ModuleHydrater(final EntityMinecartModular cart)
    {
        super(cart);
        range = 1;
    }

    @Override
    public byte getWorkPriority()
    {
        return 82;
    }

    @Override
    public void init()
    {
        super.init();
        for (final ModuleBase module : getCart().getModules())
        {
            if (module instanceof ModuleFarmer moduleFarmer)
            {
                range = moduleFarmer.getExternalRange();
                break;
            }
        }
    }

    @Override
    public boolean work()
    {
        Level world = getCart().level();
        BlockPos next = getNextblock();
        for (int i = -range; i <= range; ++i)
        {
            for (int j = -range; j <= range; ++j)
            {
                if (hydrate(world, next.offset(i, -1, j)))
                {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean hydrate(Level world, BlockPos pos)
    {
        BlockState state = world.getBlockState(pos);
        if (state.getBlock() == Blocks.FARMLAND)
        {
            int moisture = state.getValue(FarmBlock.MOISTURE);
            if (moisture != 7)
            {
                FluidStack waterCost = new FluidStack(Fluids.WATER, 7 - moisture);
                FluidStack drained = getCart().drain(waterCost, IFluidHandler.FluidAction.SIMULATE);
                if (drained.getAmount() > 0)
                {
                    if (doPreWork())
                    {
                        startWorking(2 + drained.getAmount());
                        return true;
                    }
                    stopWorking();
                    getCart().drain(drained, IFluidHandler.FluidAction.EXECUTE);
                    world.setBlock(pos, state.setValue(FarmBlock.MOISTURE, moisture + drained.getAmount()), 3);
                }
            }
        }
        return false;
    }
}
