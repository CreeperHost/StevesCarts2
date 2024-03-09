package vswe.stevescarts.upgrades;

import vswe.stevescarts.api.upgrades.BaseUpgradeEffect;

public class FuelCost extends BaseUpgradeEffect
{
    private final float cost;

    public FuelCost(final float cost)
    {
        this.cost = cost;
    }

    @Override
    public String getName()
    {
        return "info.stevescarts.effectFuelCost" + (((getPercentage() >= 0) ? "+" : "") + getPercentage());
    }

    private int getPercentage()
    {
        return (int) (cost * 100.0f);
    }

    public float getCost()
    {
        return cost;
    }
}
