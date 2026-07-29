package vswe.stevescarts.modules.engines;

import vswe.stevescarts.entities.ModularMinecart;

public class ModuleThermalAdvanced extends ModuleThermalBase {
    public ModuleThermalAdvanced(ModularMinecart cart) {
        super(cart);
    }

    @Override
    protected int getEfficiency() {
        return 60;
    }

    @Override
    protected int getCoolantEfficiency() {
        return 90;
    }
}
