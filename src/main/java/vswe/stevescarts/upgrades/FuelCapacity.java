package vswe.stevescarts.upgrades;

import net.minecraft.client.resources.language.I18n;
import vswe.stevescarts.api.upgrades.BaseUpgradeEffect;
import vswe.stevescarts.helpers.Localization;

public class FuelCapacity extends BaseUpgradeEffect
{
    private final int capacity;

    public FuelCapacity(final int capacity)
    {
        this.capacity = capacity;
    }

    @Override
    public String getName()
    {
        return I18n.get("info.stevescarts.effectFuelCapacity") + (((capacity >= 0) ? "+" : "") + capacity);
    }

    public int getFuelCapacity()
    {
        return capacity;
    }
}
