package vswe.stevescarts.upgrades;

import net.minecraft.network.chat.Component;
import vswe.stevescarts.api.upgrades.BaseUpgradeEffect;

public class Deployer extends BaseUpgradeEffect
{
    @Override
    public Component getName()
    {
        return Component.translatable("info.stevescarts.effectDeployer");
    }
}
