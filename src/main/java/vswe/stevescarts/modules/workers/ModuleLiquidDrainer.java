package vswe.stevescarts.modules.workers;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.wrappers.BucketPickupHandlerWrapper;
import vswe.stevescarts.api.modules.template.ModuleWorker;
import vswe.stevescarts.entities.ModularMinecart;
import vswe.stevescarts.helpers.BlockPosHelpers;
import vswe.stevescarts.modules.workers.tools.ModuleDrill;

import java.util.ArrayList;

public class ModuleLiquidDrainer extends ModuleWorker
{
    public ModuleLiquidDrainer(ModularMinecart cart)
    {
        super(cart);
    }

    @Override
    public byte getWorkPriority()
    {
        return 0;
    }

    @Override
    public boolean work()
    {
        return false;
    }

    public void handleLiquid(final ModuleDrill drill, BlockPos pos)
    {
        ArrayList<BlockPos> checked = new ArrayList<>();
        int result = drainAt(getCart().level(), drill, checked, pos, 0);
        if (result > 0 && doPreWork())
        {
            drill.kill();
            startWorking((int) (2.5f * result));
        }
        else
        {
            stopWorking();
        }
    }

    @Override
    public boolean preventAutoShutdown()
    {
        return true;
    }

    private int drainAt(Level level, final ModuleDrill drill, final ArrayList<BlockPos> checked, final BlockPos pos, int buckets)
    {
        int drained = 0;
        BlockState state = level.getBlockState(pos);
        if (!isLiquid(state)) {
            return 0;
        }
        FluidStack liquid = getFluidStack(state, pos, !doPreWork());
        if (liquid != null) {
            if (doPreWork()) {
                liquid.grow(buckets * 1000);
            }
            int amount = getCart().fill(liquid, IFluidHandler.FluidAction.SIMULATE);
            if (amount == liquid.getAmount()) {
                if (!doPreWork()) {
                    getCart().fill(liquid, IFluidHandler.FluidAction.EXECUTE);
                    level.setBlock(pos, Blocks.AIR.defaultBlockState(), 11);
                }
                drained += 40;
                buckets += 1;
            } else if (amount == 0 && drained == 0) {
                drained = -1;
            }
        }
        checked.add(pos);
        if (checked.size() < 100 && BlockPosHelpers.getHorizontalDistToCartSquared(pos, getCart()) < 200.0) {
            for (int y = 1; y >= 0; --y) {
                for (int x = -1; x <= 1; ++x) {
                    for (int z = -1; z <= 1; ++z) {
                        if (Math.abs(x) + Math.abs(y) + Math.abs(z) == 1) {
                            BlockPos next = pos.offset(x, y, z);
                            if (!checked.contains(next)) {
                                drained += drainAt(level, drill, checked, next, buckets);
                            }
                        }
                    }
                }
            }
        }
        return drained;
    }

    private boolean isLiquid(BlockState state) {
        FluidState fluid = state.getFluidState();
        return (!fluid.isEmpty() && fluid.isSource());
    }

    private FluidStack getFluidStack(BlockState state, BlockPos pos, boolean doDrain) {
        Block block = state.getBlock();

        if (block instanceof LiquidBlock fluidBlock) {
            BucketPickupHandlerWrapper wrapper = new BucketPickupHandlerWrapper(null, fluidBlock, getCart().level(), pos);
            return wrapper.drain(FluidType.BUCKET_VOLUME, doDrain ? IFluidHandler.FluidAction.EXECUTE : IFluidHandler.FluidAction.SIMULATE);
        }

        FluidState fluid = state.getFluidState();
        if (fluid.isEmpty() || !fluid.isSource()) {
            return null;
        }

        return new FluidStack(fluid.getType(), 1000);
    }
}
