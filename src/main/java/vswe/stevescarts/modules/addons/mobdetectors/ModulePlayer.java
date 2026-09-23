package vswe.stevescarts.modules.addons.mobdetectors;

import net.minecraft.network.chat.Component;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.player.Player;
import vswe.stevescarts.entities.ModularMinecart;

public class ModulePlayer extends ModuleMobdetector {
    public ModulePlayer(ModularMinecart cart) {
        super(cart);
    }

    @Override
    public String getName() {
        return Component.translatable("modules.addons.stevescarts.detectorPlayers").getString();
    }

    @Override
    public boolean isValidTarget(Entity target) {
        return target instanceof Player || (target instanceof TamableAnimal tamable && tamable.isTame());
    }
}
