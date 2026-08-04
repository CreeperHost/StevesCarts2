package vswe.stevescarts.modules.engines;

import vswe.stevescarts.entities.ModularMinecart;

public class ModuleCoalTiny extends ModuleCoalBase {
    public ModuleCoalTiny(ModularMinecart cart) {
        super(cart);
    }

    @Override
    protected int getInventoryWidth() {
        return 1;
    }

    @Override
    public double getFuelMultiplier() {
        return 0.5;
    }
}
