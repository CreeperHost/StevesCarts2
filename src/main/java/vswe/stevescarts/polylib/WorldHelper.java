package vswe.stevescarts.polylib;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;

public class WorldHelper
{
    public static boolean isAir(Level level, BlockPos blockPos)
    {
        if(level == null) return false;

        if(level.getBlockState(blockPos).isAir()) return true;
        if(level.getBlockState(blockPos).getBlock() == Blocks.AIR) return true;

        return false;
    }
}
