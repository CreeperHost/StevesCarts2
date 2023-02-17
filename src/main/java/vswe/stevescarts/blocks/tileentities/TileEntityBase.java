package vswe.stevescarts.blocks.tileentities;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;

public abstract class TileEntityBase extends TileEntity implements ITickableTileEntity
{
    public TileEntityBase(TileEntityType<?> p_i48289_1_)
    {
        super(p_i48289_1_);
    }

    public boolean isUsableByPlayer(final PlayerEntity entityplayer)
    {
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

    @Override
    public void tick()
    {
    }
}
