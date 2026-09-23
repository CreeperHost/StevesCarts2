package vswe.stevescarts.modules.addons.mobdetectors;

import net.minecraft.network.chat.Component;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.golem.IronGolem;
import net.minecraft.world.entity.npc.villager.Villager;
import vswe.stevescarts.entities.ModularMinecart;

public class ModuleVillager extends ModuleMobdetector {
    public ModuleVillager(ModularMinecart cart) {
        super(cart);
    }

    @Override
    public String getName() {
        return Component.translatable("modules.addons.stevescarts.detectorVillagers").getString();
    }

    @Override
    public boolean isValidTarget(final Entity target) {
        return target instanceof IronGolem || target instanceof Villager;
    }
}
