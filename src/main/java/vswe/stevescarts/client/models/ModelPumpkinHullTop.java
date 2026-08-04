package vswe.stevescarts.client.models;

import net.minecraft.resources.Identifier;
import vswe.stevescarts.api.modules.ModuleBase;

public class ModelPumpkinHullTop extends ModelHullTop {
    private final Identifier resourceactive;
    private final Identifier resourceidle;

    public ModelPumpkinHullTop(final Identifier resourceactive, final Identifier resourceidle) {
        super(resourceactive);
        this.resourceactive = resourceactive;
        this.resourceidle = resourceidle;
    }

    @Override
    public Identifier getResource(final ModuleBase module) {
        return (module == null || isActive(module)) ? resourceactive : resourceidle;
    }

    private boolean isActive(final ModuleBase module) {
        return false;
    }
}
