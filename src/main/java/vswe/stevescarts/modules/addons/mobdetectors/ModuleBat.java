package vswe.stevescarts.modules.addons.mobdetectors;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ambient.Bat;
import vswe.stevescarts.entities.ModularMinecart;
import vswe.stevescarts.helpers.Localization;

public class ModuleBat extends ModuleMobdetector {
    public ModuleBat(ModularMinecart cart) {
        super(cart);
    }

    @Override
    public String getName() {
        return Localization.MODULES.ADDONS.DETECTOR_BATS.translate();
    }

    @Override
    public boolean isValidTarget(final Entity target) {
        return target instanceof Bat;
    }
}
