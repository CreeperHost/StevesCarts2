package vswe.stevescarts.modules.addons.mobdetectors;

import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.BatEntity;
import vswe.stevescarts.entitys.EntityMinecartModular;
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
        return target instanceof BatEntity;
    }
}
