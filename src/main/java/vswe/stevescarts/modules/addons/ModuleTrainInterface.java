package vswe.stevescarts.modules.addons;

import vswe.stevescarts.api.modules.template.ModuleAddon;
import vswe.stevescarts.api.modules.interfaces.ITrainModuleAccess;
import vswe.stevescarts.entities.ModularMinecart;

public class ModuleTrainInterface extends ModuleAddon implements ITrainModuleAccess {
    public ModuleTrainInterface(ModularMinecart cart) {
        super(cart);
    }
}
