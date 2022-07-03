package vswe.stevescarts.upgrades;

import vswe.stevescarts.helpers.Localization;

public class WorkEfficiency extends BaseEffect
{
    private final float efficiency;

    public WorkEfficiency(final float efficiency)
    {
        this.efficiency = efficiency;
    }

    @Override
    public String getName()
    {
        return Localization.UPGRADES.EFFICIENCY.translate(((getPercentage() >= 0) ? "+" : "") + getPercentage());
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
