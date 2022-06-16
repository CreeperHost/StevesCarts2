package vswe.stevescarts.modules.addons.mobdetectors;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.monster.Ghast;
import net.minecraft.world.entity.monster.Slime;
import vswe.stevescarts.entitys.EntityMinecartModular;
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
        return (target instanceof Mob || target instanceof EnderDragon || target instanceof Ghast || target instanceof Slime || (target instanceof Wolf && ((Wolf) target).isAngry())) && !(target instanceof EnderMan);
    }
}
