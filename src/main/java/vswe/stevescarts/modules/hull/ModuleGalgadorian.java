package vswe.stevescarts.modules.hull;

import vswe.stevescarts.api.modules.template.ModuleHull;
import vswe.stevescarts.entities.ModularMinecart;

public class ModuleGalgadorian extends ModuleHull {
    public ModuleGalgadorian(ModularMinecart cart) {
        super(cart);
    }

    @Override
    public int getConsumption(final boolean isMoving) {
        if (!isMoving) {
            return super.getConsumption(false);
        }
        return 9;
    }
}
