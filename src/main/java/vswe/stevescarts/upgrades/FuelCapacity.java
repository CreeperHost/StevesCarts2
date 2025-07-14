package vswe.stevescarts.upgrades;

import net.minecraft.network.chat.Component;
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
    public Component getName()
    {
        return Localization.translate("info.stevescarts.effectFuelCapacity", (((capacity >= 0) ? "+" : "") + capacity));
    }

    public int getFuelCapacity()
    {
        return capacity;
    }
}
