package vswe.stevescarts.upgrades;

import net.minecraft.nbt.CompoundNBT;
import vswe.stevescarts.blocks.tileentities.TileEntityUpgrade;

public abstract class BaseEffect
{
    public void update(final TileEntityUpgrade upgrade)
    {
    }

    public void init(final TileEntityUpgrade upgrade)
    {
    }

    public void removed(final TileEntityUpgrade upgrade)
    {
    }

    public void load(final TileEntityUpgrade upgrade, final CompoundNBT compound)
    {
    }

    public void save(final TileEntityUpgrade upgrade, final CompoundNBT compound)
    {
    }

    public abstract String getName();
}
