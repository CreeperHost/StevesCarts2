package vswe.stevescarts.upgrades;

import vswe.stevescarts.helpers.Localization;

public class FuelCapacity extends BaseEffect
{
    private int capacity;

    public FuelCapacity(final int capacity)
    {
        this.capacity = capacity;
    }

    @Override
    public String getName()
    {
        return Localization.UPGRADES.FUEL_CAPACITY.translate(((capacity >= 0) ? "+" : "") + capacity);
    }

    public int getFuelCapacity()
    {
        return capacity;
    }
}
