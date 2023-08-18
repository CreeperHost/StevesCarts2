package vswe.stevescarts.api.modules.template;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraftforge.fluids.IFluidBlock;
import vswe.stevescarts.api.modules.ModuleBase;
import vswe.stevescarts.entities.EntityMinecartModular;

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
        BlockPos pos = getCart().blockPosition();
        if (BaseRailBlock.isRail(getCart().level(), pos.below()))
        {
            pos = pos.below();
        }
        BlockState blockState = getCart().level().getBlockState(pos);
        if (BaseRailBlock.isRail(blockState))
        {
            RailShape direction = ((BaseRailBlock) blockState.getBlock()).getRailDirection(blockState, getCart().level(), pos, getCart());
            if (direction.isAscending())
            {
                pos = pos.above();
            }

            int[][] logic = EntityMinecartModular.railDirectionCoordinates[direction.ordinal()];
            double pX = getCart().temppushX;
            double pZ = getCart().temppushZ;
            boolean xDir = (pX > 0.0 && logic[0][0] > 0) || pX == 0.0 || logic[0][0] == 0 || (pX < 0.0 && logic[0][0] < 0);
            boolean zDir = (pZ > 0.0 && logic[0][2] > 0) || pZ == 0.0 || logic[0][2] == 0 || (pZ < 0.0 && logic[0][2] < 0);
            int dir = ((xDir && zDir) != flag) ? 1 : 0;
            return pos.offset(logic[dir][0], logic[dir][1], logic[dir][2]);
        }
        return pos;
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

    protected boolean isValidForTrack(BlockPos pos, boolean checkBellow)
    {
        boolean result = countsAsAir(pos) && (!checkBellow || Block.canSupportRigidBlock(getCart().level(), pos.below()));
        if (result)
        {
            int coordX = pos.getX() - (getCart().x() - pos.getX());
            int coordZ = pos.getZ() - (getCart().z() - pos.getZ());
            Block block = getCart().level().getBlockState(new BlockPos(coordX, pos.getY(), coordZ)).getBlock();
            boolean isWater = block == Blocks.WATER || block == Blocks.ICE;
            boolean isLava = block == Blocks.LAVA;
            boolean isOther = block instanceof IFluidBlock;
            boolean isLiquid = isWater || isLava || isOther;
            result = !isLiquid;
        }
        return result;
    }
}
