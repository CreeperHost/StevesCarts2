package vswe.stevescarts.modules.addons;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import vswe.stevescarts.entitys.EntityMinecartModular;

public class ModuleMelter extends ModuleAddon
{
    private int tick;

    public ModuleMelter(final EntityMinecartModular cart)
    {
        super(cart);
    }

    @Override
    public void update()
    {
        super.update();
        if (getCart().level.isClientSide)
        {
            return;
        }
        if (getCart().hasFuel())
        {
            if (tick >= getInterval())
            {
                tick = 0;
                melt();
            }
            else
            {
                ++tick;
            }
        }
    }

    protected int getInterval()
    {
        return 70;
    }

    protected int getBlocksOnSide()
    {
        return 7;
    }

    protected int getBlocksFromLevel()
    {
        return 1;
    }

    private void melt()
    {
        BlockPos cartPos = getCart().getExactPosition();
        for (int x = -getBlocksOnSide(); x <= getBlocksOnSide(); ++x)
        {
            for (int z = -getBlocksOnSide(); z <= getBlocksOnSide(); ++z)
            {
                for (int y = -getBlocksFromLevel(); y <= getBlocksFromLevel(); ++y)
                {
                    BlockPos pos = cartPos.offset(x, y, z);
                    final Block b = getCart().level.getBlockState(pos).getBlock();
                    melt(b, pos);
                }
            }
        }
    }

    protected boolean melt(final Block b, BlockPos pos)
    {
        if (b == Blocks.SNOW)
        {
            getCart().level.removeBlock(pos, false);
            return true;
        }
        return false;
    }
}
