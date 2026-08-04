package vswe.stevescarts.modules.storages.chests;

import vswe.stevescarts.api.modules.template.ModuleChest;
import vswe.stevescarts.entities.ModularMinecart;

public class ModuleSideChests extends ModuleChest {
    public ModuleSideChests(ModularMinecart cart) {
        super(cart);
    }

    @Override
    protected int getInventoryWidth() {
        return 5;
    }

    @Override
    protected int getInventoryHeight() {
        return 3;
    }
}
