package vswe.stevescarts.modules.addons.mobdetectors;

import net.minecraft.network.chat.Component;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.animal.Animal;
import vswe.stevescarts.entities.ModularMinecart;

public class ModuleAnimal extends ModuleMobdetector {
    public ModuleAnimal(ModularMinecart cart) {
        super(cart);
    }

    @Override
    public String getName() {
        return Component.translatable("modules.addons.stevescarts.detectorAnimals").getString();
    }

    @Override
    public boolean isValidTarget(final Entity target) {
        return target instanceof Animal && (!(target instanceof TamableAnimal) || !((TamableAnimal) target).isTame());
    }
}
