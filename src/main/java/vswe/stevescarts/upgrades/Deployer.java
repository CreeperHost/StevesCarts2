package vswe.stevescarts.upgrades;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import vswe.stevescarts.api.upgrades.BaseUpgradeEffect;
import vswe.stevescarts.helpers.Localization;

public class Deployer extends BaseUpgradeEffect
{
    @Override
    public Component getName()
    {
        return Localization.translate("info.stevescarts.effectDeployerUnavailable").copy().withStyle(ChatFormatting.RED);
    }
}
