package vswe.stevescarts.modules.addons;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import vswe.stevescarts.entitys.EntityMinecartModular;

public class ModuleMelterExtreme extends ModuleMelter
{
    public ModuleMelterExtreme(final EntityMinecartModular cart)
    {
        super(cart);
    }

    @Override
    protected boolean melt(final Block b, BlockPos pos)
    {
        if (!super.melt(b, pos))
        {
            if (b == Blocks.SNOW)
            {
                getCart().level.removeBlock(pos, false);
                return true;
            }
            if (b == Blocks.ICE)
            {
                getCart().level.setBlock(pos, Blocks.WATER.defaultBlockState(), 4);
                return true;
            }
        }
        return false;
    }
}
