package vswe.stevescarts.modules.addons.mobdetectors;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.monster.*;
import vswe.stevescarts.entities.EntityMinecartModular;
import vswe.stevescarts.helpers.Localization;

public class ModuleMonster extends ModuleMobdetector
{
    public ModuleMonster(final EntityMinecartModular cart)
    {
        super(cart);
    }

    @Override
    public String getName()
    {
        return Localization.MODULES.ADDONS.DETECTOR_MONSTERS.translate();
    }

    @Override
    public boolean isValidTarget(final Entity target)
    {
        return (target instanceof Enemy || (target instanceof Wolf && ((Wolf) target).isAngry())) && !(target instanceof EnderMan);
    }
}
