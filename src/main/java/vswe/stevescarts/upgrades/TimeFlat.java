package vswe.stevescarts.upgrades;

import net.minecraft.network.chat.Component;
import vswe.stevescarts.api.upgrades.BaseUpgradeEffect;
import vswe.stevescarts.helpers.Localization;

public class TimeFlat extends BaseUpgradeEffect
{
    private final int ticks;

    public TimeFlat(final int ticks)
    {
        this.ticks = ticks;
    }

    @Override
    public Component getName()
    {
        return Component.literal(Localization.UPGRADES.FLAT.translate(((getSeconds() >= 0) ? "+" : "") + getSeconds(), String.valueOf(getSeconds())));
    }

    protected int getSeconds()
    {
        return ticks / 20;
    }

    public int getTicks()
    {
        return ticks;
    }
}
