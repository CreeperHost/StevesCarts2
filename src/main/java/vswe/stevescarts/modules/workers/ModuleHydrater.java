package vswe.stevescarts.modules.workers;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FarmlandBlock;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.capability.IFluidHandler;
import vswe.stevescarts.entitys.EntityMinecartModular;
import vswe.stevescarts.modules.ModuleBase;
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
            if (module instanceof ModuleFarmer)
            {
                range = ((ModuleFarmer) module).getExternalRange();
                break;
            }
        }
    }

    @Override
    public boolean work()
    {
        World world = getCart().level;
        BlockPos next = getNextblock();
        for (int i = -range; i <= range; ++i)
        {
            for (int j = -range; j <= range; ++j)
            {
                if (hydrate(world, next.offset(i, 0, j)))
                {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean hydrate(World world, BlockPos pos)
    {
        BlockState state = world.getBlockState(pos);
        if (state.getBlock() == Blocks.FARMLAND)
        {
            int moisture = state.getValue(FarmlandBlock.MOISTURE);
            if (moisture != 7)
            {
                int waterCost = 7 - moisture;
                waterCost = getCart().drain(Fluids.WATER, waterCost, IFluidHandler.FluidAction.SIMULATE);
                if (waterCost > 0)
                {
                    if (doPreWork())
                    {
                        startWorking(2 + waterCost);
                        return true;
                    }
                    stopWorking();
                    getCart().drain(Fluids.WATER, waterCost, IFluidHandler.FluidAction.EXECUTE);
                    world.setBlock(pos, state.setValue(FarmlandBlock.MOISTURE, moisture + waterCost), 3);
                }
            }
        }
        return false;
    }
}
