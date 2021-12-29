package vswe.stevescarts.modules.workers;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.fluids.capability.IFluidHandler;
import vswe.stevescarts.entitys.EntityMinecartModular;
import vswe.stevescarts.helpers.BlockPosHelpers;
import vswe.stevescarts.modules.workers.tools.ModuleDrill;

import java.util.ArrayList;

public class ModuleLiquidDrainer extends ModuleWorker
{
    public ModuleLiquidDrainer(final EntityMinecartModular cart)
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
        final ArrayList<BlockPos> checked = new ArrayList<>();
        final int result = drainAt(getCart().level, drill, checked, pos, 0);
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

    private int drainAt(World world, final ModuleDrill drill, final ArrayList<BlockPos> checked, final BlockPos pos, int buckets)
    {
        int drained = 0;
        BlockState blockState = world.getBlockState(pos);
        final Block b = blockState.getBlock();
        if (!isLiquid(b))
        {
            return 0;
        }
        //		final int meta = b.getMetaFromState(blockState);
        final FluidStack liquid = getFluidStack(b, pos, !doPreWork());
        if (liquid != null)
        {
            if (doPreWork())
            {
                final FluidStack fluidStack = liquid;
                fluidStack.grow(buckets * 1000);
            }
            final int amount = getCart().fill(liquid, IFluidHandler.FluidAction.SIMULATE);
            if (amount == liquid.getAmount())
            {
                final boolean isDrainable = true;//meta == 0;
                if (!doPreWork())
                {
                    if (isDrainable)
                    {
                        getCart().fill(liquid, IFluidHandler.FluidAction.SIMULATE);
                    }
                    world.removeBlock(pos, false);
                }
                drained += (isDrainable ? 40 : 3);
                buckets += (isDrainable ? 1 : 0);
            }
        }
        checked.add(pos);
        if (checked.size() < 100 && BlockPosHelpers.getHorizontalDistToCartSquared(pos, getCart()) < 200.0)
        {
            for (int y = 1; y >= 0; --y)
            {
                for (int x = -1; x <= 1; ++x)
                {
                    for (int z = -1; z <= 1; ++z)
                    {
                        if (Math.abs(x) + Math.abs(y) + Math.abs(z) == 1)
                        {
                            BlockPos next = pos.offset(x, y, z);
                            if (!checked.contains(next))
                            {
                                drained += drainAt(world, drill, checked, next, buckets);
                            }
                        }
                    }
                }
            }
        }
        return drained;
    }

    private boolean isLiquid(final Block b)
    {
        final boolean isWater = b == Blocks.WATER || b == Blocks.ICE;
        final boolean isLava = b == Blocks.LAVA;
        final boolean isOther = b != null && b instanceof IFluidBlock;
        return isWater || isLava || isOther;
    }

    private FluidStack getFluidStack(final Block b, BlockPos pos, final boolean doDrain)
    {
        if (b == Blocks.WATER)
        {
            return new FluidStack(Fluids.WATER, 1000);
        }
        if (b == Blocks.LAVA)
        {
            return new FluidStack(Fluids.LAVA, 1000);
        }
        if (b instanceof IFluidBlock)
        {
            final IFluidBlock liquid = (IFluidBlock) b;
            return liquid.drain(getCart().level, pos, IFluidHandler.FluidAction.EXECUTE);
        }
        return null;
    }
}
