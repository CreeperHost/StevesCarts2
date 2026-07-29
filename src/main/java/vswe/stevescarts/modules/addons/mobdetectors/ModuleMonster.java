package vswe.stevescarts.modules.addons.mobdetectors;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.wolf.Wolf;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.monster.Enemy;
import vswe.stevescarts.entities.ModularMinecart;
import vswe.stevescarts.helpers.Localization;

public class ModuleMonster extends ModuleMobdetector {
    public ModuleMonster(ModularMinecart cart) {
        super(cart);
    }

    @Override
    public String getName() {
        return Localization.MODULES.ADDONS.DETECTOR_MONSTERS.translate();
    }

    @Override
    public boolean isValidTarget(final Entity target) {
        return (target instanceof Enemy || (target instanceof Wolf && ((Wolf) target).isAngry())) && !(target instanceof EnderMan);
    }
}
