package vswe.stevescarts.modules.addons.mobdetectors;

import net.minecraft.world.entity.Entity;
import vswe.stevescarts.entities.EntityMinecartModular;
import vswe.stevescarts.api.modules.template.ModuleAddon;

public abstract class ModuleMobdetector extends ModuleAddon
{
    public ModuleMobdetector(final EntityMinecartModular cart)
    {
        super(cart);
    }

    public abstract String getName();

    public abstract boolean isValidTarget(final Entity p0);
}
