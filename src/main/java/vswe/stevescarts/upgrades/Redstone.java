package vswe.stevescarts.upgrades;

import net.minecraft.client.resources.language.I18n;
import vswe.stevescarts.api.upgrades.BaseUpgradeEffect;
import vswe.stevescarts.helpers.Localization;

public class Redstone extends BaseUpgradeEffect
{
    @Override
    public String getName()
    {
        return I18n.get("info.stevescarts.effectRedstone");
    }
}
