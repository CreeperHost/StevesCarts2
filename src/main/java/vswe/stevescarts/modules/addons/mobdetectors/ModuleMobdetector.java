package vswe.stevescarts.modules.addons.mobdetectors;

import net.minecraft.world.entity.Entity;
import vswe.stevescarts.api.modules.template.ModuleAddon;
import vswe.stevescarts.entities.ModularMinecart;

public abstract class ModuleMobdetector extends ModuleAddon {
    public ModuleMobdetector(ModularMinecart cart) {
        super(cart);
    }

    public abstract String getName();

    public abstract boolean isValidTarget(final Entity p0);
}
