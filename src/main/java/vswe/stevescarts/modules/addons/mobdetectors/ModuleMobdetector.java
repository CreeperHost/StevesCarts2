package vswe.stevescarts.modules.addons.mobdetectors;

import net.minecraft.entity.Entity;
import vswe.stevescarts.entitys.EntityMinecartModular;
import vswe.stevescarts.modules.addons.ModuleAddon;

public abstract class ModuleMobdetector extends ModuleAddon
{
    public ModuleMobdetector(final EntityMinecartModular cart)
    {
        super(cart);
    }

    public abstract String getName();

    public abstract boolean isValidTarget(final Entity p0);
}
