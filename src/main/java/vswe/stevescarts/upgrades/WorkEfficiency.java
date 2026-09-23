package vswe.stevescarts.upgrades;

import net.minecraft.network.chat.Component;
import vswe.stevescarts.api.upgrades.BaseUpgradeEffect;

public class WorkEfficiency extends BaseUpgradeEffect {
    private final float efficiency;

    public WorkEfficiency(final float efficiency) {
        this.efficiency = efficiency;
    }

    @Override
    public Component getName() {
        return Component.literal(Component.translatable("info.stevescarts.effectEfficiency", ((getPercentage() >= 0) ? "+" : "") + getPercentage()).getString());
    }

    private int getPercentage() {
        return (int) (efficiency * 100.0f);
    }

    public float getEfficiency() {
        return efficiency;
    }
}
