package vswe.stevescarts.upgrades;

import net.minecraft.network.chat.Component;

public class TimeFlatRemoved extends TimeFlat {
    public TimeFlatRemoved(final int ticks) {
        super(ticks);
    }

    @Override
    public Component getName() {
        return Component.translatable("info.stevescarts.effectTimeFlatRemove." + (Math.abs(getSeconds()) == 1 ? "singular" : "plural"), ((getSeconds() >= 0) ? "+" : "") + getSeconds());
    }
}
