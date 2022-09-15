package vswe.stevescarts.modules.addons.mobdetectors;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ambient.Bat;
import vswe.stevescarts.entities.EntityMinecartModular;
import vswe.stevescarts.helpers.Localization;

public class ModuleBat extends ModuleMobdetector
{
    public ModuleBat(final EntityMinecartModular cart)
    {
        super(cart);
    }

    @Override
    public String getName()
    {
        return Localization.MODULES.ADDONS.DETECTOR_BATS.translate();
    }

    @Override
    public boolean isValidTarget(final Entity target)
    {
        return target instanceof Bat;
    }
}
