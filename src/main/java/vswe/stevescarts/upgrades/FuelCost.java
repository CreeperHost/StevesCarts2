package vswe.stevescarts.upgrades;

import net.minecraft.client.resources.language.I18n;
import vswe.stevescarts.api.upgrades.BaseUpgradeEffect;
import vswe.stevescarts.helpers.Localization;

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
        return I18n.get("info.stevescarts.effectFuelCost") + (((getPercentage() >= 0) ? "+" : "") + getPercentage());
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
