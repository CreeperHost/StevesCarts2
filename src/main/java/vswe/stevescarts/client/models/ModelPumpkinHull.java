package vswe.stevescarts.client.models;

import net.minecraft.resources.Identifier;
import vswe.stevescarts.api.modules.ModuleBase;

public class ModelPumpkinHull extends ModelHull {
    private final Identifier resourceactive;
    private final Identifier resourceidle;

    public ModelPumpkinHull(final Identifier resourceactive, final Identifier resourceidle) {
        super(resourceactive);
        this.resourceactive = resourceactive;
        this.resourceidle = resourceidle;
    }

    @Override
    public Identifier getResource(final ModuleBase module) {
        return (module == null || isActive(module)) ? resourceactive : resourceidle;
    }

    private boolean isActive(final ModuleBase module) {
        final long time = module.getCart().level().getLevelData().getGameTime() % 24000L;
        return time >= 12000L && time <= 18000L;
    }
}
