package vswe.stevescarts.upgrades;

import vswe.stevescarts.api.upgrades.BaseUpgradeEffect;
import vswe.stevescarts.helpers.Localization;

public class CombustionFuel extends BaseUpgradeEffect
{
    @Override
    public String getName()
    {
        return Localization.UPGRADES.COMBUSTION.translate();
    }
}
