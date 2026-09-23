package vswe.stevescarts.modules.addons.mobdetectors;

import net.minecraft.network.chat.Component;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ambient.Bat;
import vswe.stevescarts.entities.ModularMinecart;

public class ModuleBat extends ModuleMobdetector {
    public ModuleBat(ModularMinecart cart) {
        super(cart);
    }

    @Override
    public String getName() {
        return Component.translatable("modules.addons.stevescarts.detectorBats").getString();
    }

    @Override
    public boolean isValidTarget(final Entity target) {
        return target instanceof Bat;
    }
}
