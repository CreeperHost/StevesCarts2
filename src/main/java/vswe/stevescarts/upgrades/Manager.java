package vswe.stevescarts.upgrades;

import net.minecraft.network.chat.Component;
import vswe.stevescarts.api.upgrades.BaseUpgradeEffect;
import vswe.stevescarts.helpers.Localization;

public class Manager extends BaseUpgradeEffect
{
    @Override
    public Component getName()
    {
        return Localization.translate("info.stevescarts.effectManagerBridge");
    }
}
