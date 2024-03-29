package vswe.stevescarts.blocks.tileentities;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class TileEntityBase extends BlockEntity
{
    public TileEntityBase(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState)
    {
        super(blockEntityType, blockPos, blockState);
    }

    public boolean isUsableByPlayer(final Player player)
    {
        if(level == null) return false;
        return level.getBlockEntity(getBlockPos()) == this;
    }

    public short getShortFromInt(final boolean first, final int val)
    {
        if (first)
        {
            return (short) (val & 0xFFFF);
        }
        return (short) (val >> 16 & 0xFFFF);
    }

    @SuppressWarnings("unused")
    public int getIntFromShort(final boolean first, int oldVal, final short val)
    {
        if (first)
        {
            oldVal = ((oldVal & 0xFFFF0000) | val);
        }
        else
        {
            oldVal = ((oldVal & 0xFFFF) | val << 16);
        }
        return oldVal;
    }

    public int getIntFromShorts(short first, short last)
    {
        return (first & 0xFFFF) | ((last << 16) & 0xFFFF0000);
    }

    public void tick() {}
}
