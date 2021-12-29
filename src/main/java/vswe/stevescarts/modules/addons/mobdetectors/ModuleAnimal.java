package vswe.stevescarts.modules.addons.mobdetectors;

import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.TameableEntity;
import vswe.stevescarts.entitys.EntityMinecartModular;
import vswe.stevescarts.helpers.Localization;

public class ModuleAnimal extends ModuleMobdetector
{
    public ModuleAnimal(final EntityMinecartModular cart)
    {
        super(cart);
    }

    @Override
    public String getName()
    {
        return Localization.MODULES.ADDONS.DETECTOR_ANIMALS.translate();
    }

    @Override
    public boolean isValidTarget(final Entity target)
    {
        return target instanceof AnimalEntity && (!(target instanceof TameableEntity) || !((TameableEntity) target).isTame());
    }
}
