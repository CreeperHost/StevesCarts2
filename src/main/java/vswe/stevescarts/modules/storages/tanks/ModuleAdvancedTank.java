package vswe.stevescarts.modules.storages.tanks;

import vswe.stevescarts.entities.ModularMinecart;

public class ModuleAdvancedTank extends ModuleTank {
    public ModuleAdvancedTank(ModularMinecart cart) {
        super(cart);
    }

    @Override
    protected int getTankSize() {
        return 32000;
    }
}
