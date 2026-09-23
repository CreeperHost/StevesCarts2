package vswe.stevescarts.modules.addons.mobdetectors;

import net.minecraft.network.chat.Component;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.wolf.Wolf;
import net.minecraft.world.entity.monster.Enderman;
import net.minecraft.world.entity.monster.Enemy;
import vswe.stevescarts.entities.ModularMinecart;

public class ModuleMonster extends ModuleMobdetector {
    public ModuleMonster(ModularMinecart cart) {
        super(cart);
    }

    @Override
    public String getName() {
        return Component.translatable("modules.addons.stevescarts.detectorMonsters").getString();
    }

    @Override
    public boolean isValidTarget(final Entity target) {
        return (target instanceof Enemy || (target instanceof Wolf && ((Wolf) target).isAngry())) && !(target instanceof Enderman);
    }
}
