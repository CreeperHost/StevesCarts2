package vswe.stevescarts.upgrades;

import net.minecraft.network.chat.Component;
import vswe.stevescarts.api.upgrades.BaseUpgradeEffect;

public class TimeFlat extends BaseUpgradeEffect {
    private final int ticks;

    public TimeFlat(final int ticks) {
        this.ticks = ticks;
    }

    @Override
    public Component getName() {
        return Component.translatable("info.stevescarts.effectTimeFlat." + (Math.abs(getSeconds()) == 1 ? "singular" : "plural"), ((getSeconds() >= 0) ? "+" : "") + getSeconds());
    }

    protected int getSeconds() {
        return ticks / 20;
    }

    public int getTicks() {
        return ticks;
    }
}
