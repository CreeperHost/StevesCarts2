package vswe.stevescarts.modules.engines;

import vswe.stevescarts.entities.ModularMinecart;

public class ModuleThermalStandard extends ModuleThermalBase {
    public ModuleThermalStandard(ModularMinecart cart) {
        super(cart);
    }

    @Override
    protected int getEfficiency() {
        return 25;
    }

    @Override
    protected int getCoolantEfficiency() {
        return 0;
    }
}
