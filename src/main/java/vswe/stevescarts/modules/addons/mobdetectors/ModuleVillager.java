package vswe.stevescarts.modules.addons.mobdetectors;

import net.minecraft.entity.Entity;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.passive.GolemEntity;
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
        return target instanceof GolemEntity || target instanceof VillagerEntity;
    }
}
