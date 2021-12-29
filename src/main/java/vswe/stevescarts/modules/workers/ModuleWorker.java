package vswe.stevescarts.modules.workers;

import net.minecraft.block.AbstractRailBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.state.properties.RailShape;
import net.minecraft.util.TransportationHelper;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.IFluidBlock;
import vswe.stevescarts.entitys.EntityMinecartModular;
import vswe.stevescarts.modules.ModuleBase;

public abstract class ModuleWorker extends ModuleBase
{
    private boolean preWork;
    private boolean shouldDie;

    public ModuleWorker(final EntityMinecartModular cart)
    {
        super(cart);
        preWork = true;
    }

    public abstract byte getWorkPriority();

    public abstract boolean work();

    public void startWorking(final int time)
    {
        getCart().setWorkingTime(time);
        preWork = false;
        getCart().setWorker(this);
    }

    public void stopWorking()
    {
        if (getCart().getWorker() == this)
        {
            preWork = true;
            getCart().setWorker(null);
        }
    }

    public boolean preventAutoShutdown()
    {
        return false;
    }

    public void kill()
    {
        shouldDie = true;
    }

    public boolean isDead()
    {
        return shouldDie;
    }

    public void revive()
    {
        shouldDie = false;
    }

    protected boolean doPreWork()
    {
        return preWork;
    }

    public BlockPos getLastblock()
    {
        return getNextblock(false);
    }

    public BlockPos getNextblock()
    {
        return getNextblock(true);
    }

    private BlockPos getNextblock(final boolean flag)
    {
        BlockPos pos = getCart().getExactPosition();
        if (AbstractRailBlock.isRail(getCart().level, pos.below()))
        {
            pos = pos.below();
        }
        BlockState blockState = getCart().level.getBlockState(pos);
        if (AbstractRailBlock.isRail(blockState))
        {
            RailShape direction = ((AbstractRailBlock) blockState.getBlock()).getRailDirection(blockState, getCart().level, pos, getCart());
            if (direction.isAscending())
            {
                pos = pos.above();
            }

            int[][] aint = TransportationHelper.offsetsForDirection(getCart().getMotionDirection());
            BlockPos.Mutable blockpos$mutable = new BlockPos.Mutable();
            for (int[] aint1 : aint)
            {
                blockpos$mutable.set(pos.getX() + aint1[0], pos.getY() - 1, pos.getZ() + aint1[1]);
            }

            return blockpos$mutable;
        }
        return getCart().blockPosition();
    }

    @Override
    public float getMaxSpeed()
    {
        if (!doPreWork())
        {
            return 0.0f;
        }
        return super.getMaxSpeed();
    }

    protected boolean isValidForTrack(BlockPos pos, boolean flag)
    {
        boolean result = countsAsAir(pos) && !countsAsAir(pos.below()) && !(getCart().level.getBlockState(pos.below()).getBlock() instanceof AbstractRailBlock);//!getCart().level.getBlockState(pos).canSurvive(getCart().level, pos));
        if (result)
        {
            int coordX = pos.getX() - (getCart().x() - pos.getX());
            int coordZ = pos.getZ() - (getCart().z() - pos.getZ());
            Block block = getCart().level.getBlockState(new BlockPos(coordX, pos.getY(), coordZ)).getBlock();
            boolean isWater = block == Blocks.WATER || block == Blocks.ICE;
            boolean isLava = block == Blocks.LAVA;
            boolean isOther = block instanceof IFluidBlock;
            boolean isLiquid = isWater || isLava || isOther;
            result = !isLiquid;
        }
        return result;
    }
}
