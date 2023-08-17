package vswe.stevescarts.api.upgrades;

import net.minecraft.nbt.CompoundTag;
import vswe.stevescarts.blocks.tileentities.TileEntityUpgrade;

public abstract class BaseUpgradeEffect
{
    public void update(final TileEntityUpgrade tileEntityUpgrade)
    {
    }

    public void init(final TileEntityUpgrade tileEntityUpgrade)
    {
    }

    public void removed(final TileEntityUpgrade tileEntityUpgrade)
    {
    }

    public void load(final TileEntityUpgrade tileEntityUpgrade, final CompoundTag compound)
    {
    }

    public void save(final TileEntityUpgrade tileEntityUpgrade, final CompoundTag compound)
    {
    }

    public abstract String getName();
}
