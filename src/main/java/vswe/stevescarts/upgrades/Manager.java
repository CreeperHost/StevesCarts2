package vswe.stevescarts.upgrades;

import vswe.stevescarts.helpers.Localization;

public class Manager extends BaseEffect
{
    @Override
    public String getName()
    {
        return Localization.UPGRADES.BRIDGE.translate();
    }
}
