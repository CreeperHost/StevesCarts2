package vswe.stevescarts.modules.addons.mobdetectors;

import net.minecraft.entity.Entity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.item.EnderCrystalEntity;
import net.minecraft.entity.monster.EndermanEntity;
import net.minecraft.entity.monster.GhastEntity;
import net.minecraft.entity.monster.SlimeEntity;
import net.minecraft.entity.passive.WolfEntity;
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
        return (target instanceof MobEntity || target instanceof EnderDragonEntity || target instanceof GhastEntity || target instanceof SlimeEntity || target instanceof EnderCrystalEntity || (target instanceof WolfEntity && ((WolfEntity) target).isAngry())) && !(target instanceof EndermanEntity);
    }
}
