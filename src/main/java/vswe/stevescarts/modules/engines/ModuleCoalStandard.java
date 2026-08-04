package vswe.stevescarts.modules.engines;

import vswe.stevescarts.entities.ModularMinecart;

public class ModuleCoalStandard extends ModuleCoalBase {
    public ModuleCoalStandard(ModularMinecart cart) {
        super(cart);
    }

    @Override
    public double getFuelMultiplier() {
        return 2.25;
    }
}
