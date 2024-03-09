package vswe.stevescarts.upgrades;

import vswe.stevescarts.api.upgrades.BaseUpgradeEffect;

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
        return "info.stevescarts.effectFuelCapacity" + (((capacity >= 0) ? "+" : "") + capacity);
    }

    public int getFuelCapacity()
    {
        return capacity;
    }
}
