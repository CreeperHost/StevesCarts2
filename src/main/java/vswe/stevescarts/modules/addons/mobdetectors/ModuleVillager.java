package vswe.stevescarts.modules.addons.mobdetectors;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.npc.Villager;
import vswe.stevescarts.entitys.EntityMinecartModular;
import vswe.stevescarts.helpers.Localization;

public class ModuleVillager extends ModuleMobdetector
{
    public ModuleVillager(final EntityMinecartModular cart)
    {
        super(cart);
    }

    @Override
    public String getName()
    {
        return Localization.MODULES.ADDONS.DETECTOR_VILLAGERS.translate();
    }

    @Override
    public boolean isValidTarget(final Entity target)
    {
        return target instanceof IronGolem || target instanceof Villager;
    }
}
