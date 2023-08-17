package vswe.stevescarts.upgrades;

import vswe.stevescarts.api.upgrades.RechargerBaseUpgrade;
import vswe.stevescarts.blocks.tileentities.TileEntityUpgrade;
import vswe.stevescarts.helpers.Localization;

public class Solar extends RechargerBaseUpgrade
{
    @Override
    protected int getAmount(final TileEntityUpgrade upgrade)
    {
        if (upgrade.getBlockPos().getY() > upgrade.getMaster().getBlockPos().getY())
        {
            return 400;
        }
        return 240;
    }

    @Override
    protected boolean canGenerate(final TileEntityUpgrade upgrade)
    {
        if(upgrade.getLevel() == null) return false;

        return upgrade.getLevel().canSeeSky(upgrade.getBlockPos()) && upgrade.getLevel().isDay();
    }

    @Override
    public String getName()
    {
        return Localization.UPGRADES.SOLAR.translate();
    }
}
