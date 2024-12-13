package vswe.stevescarts.upgrades;

import net.minecraft.network.chat.Component;
import vswe.stevescarts.helpers.Localization;

public class TimeFlatRemoved extends TimeFlat
{
    public TimeFlatRemoved(final int ticks)
    {
        super(ticks);
    }

    @Override
    public Component getName()
    {
        return Component.literal(Localization.UPGRADES.FLAT_REMOVED.translate(((getSeconds() >= 0) ? "+" : "") + getSeconds(), String.valueOf(getSeconds())));
    }
}
