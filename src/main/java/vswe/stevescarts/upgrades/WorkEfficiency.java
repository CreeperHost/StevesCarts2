package vswe.stevescarts.upgrades;

import net.minecraft.network.chat.Component;
import vswe.stevescarts.api.upgrades.BaseUpgradeEffect;
import vswe.stevescarts.helpers.Localization;

public class WorkEfficiency extends BaseUpgradeEffect
{
    private final float efficiency;

    public WorkEfficiency(final float efficiency)
    {
        this.efficiency = efficiency;
    }

    @Override
    public Component getName()
    {
        return Component.literal(Localization.UPGRADES.EFFICIENCY.translate(((getPercentage() >= 0) ? "+" : "") + getPercentage()));
    }

    private int getPercentage()
    {
        return (int) (efficiency * 100.0f);
    }

    public float getEfficiency()
    {
        return efficiency;
    }
}
