package vswe.stevescarts.modules.storages.tanks;

import vswe.stevescarts.entities.ModularMinecart;

public class ModuleFrontTank extends ModuleTank {
    public ModuleFrontTank(ModularMinecart cart) {
        super(cart);
    }

    @Override
    protected int getTankSize() {
        return 8000;
    }
}
