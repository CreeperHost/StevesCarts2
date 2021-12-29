package vswe.stevescarts.upgrades;

import vswe.stevescarts.helpers.Localization;

public class Redstone extends BaseEffect
{
    @Override
    public String getName()
    {
        return Localization.UPGRADES.REDSTONE.translate();
    }

    //	@Override
    //	public void update(final TileEntityUpgrade upgrade) {
    //TODO
    //		if (upgrade.getWorld().getRedstonePowerFromNeighbors(upgrade.getPos()) >= 1 && upgrade.getMaster() != null) {
    //			upgrade.getMaster().doAssemble();
    //		}
    //	}
}
