package vswe.stevescarts.modules.addons.mobdetectors;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import vswe.stevescarts.entitys.EntityMinecartModular;
import vswe.stevescarts.helpers.Localization;

public class ModulePlayer extends ModuleMobdetector
{
    public ModulePlayer(final EntityMinecartModular cart)
    {
        super(cart);
    }

    @Override
    public String getName()
    {
        return Localization.MODULES.ADDONS.DETECTOR_PLAYERS.translate();
    }

    @Override
    public boolean isValidTarget(final Entity target)
    {
        return target instanceof Player;
    }
}
