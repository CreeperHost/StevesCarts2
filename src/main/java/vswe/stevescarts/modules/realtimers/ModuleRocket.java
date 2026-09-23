package vswe.stevescarts.modules.realtimers;

import net.creeperhost.polylib.data.serializable.IntData;
import vswe.stevescarts.api.modules.ModuleBase;
import vswe.stevescarts.entities.ModularMinecart;
import vswe.stevescarts.polylib.EntityData;

public class ModuleRocket extends ModuleBase {
    private final EntityData<Integer> unknown = new EntityData<>(getCart(), new IntData(0));
    public ModuleRocket(ModularMinecart cart) {
        super(cart);
    }

    @Override
    public void update() {
        //TODO Port rocket movement to the current minecart physics.
    }

    @Override
    public void activatedByRail(final int x, final int y, final int z, final boolean active) {
        if (active) {
            takeOff();
            unknown.set(1);
        }
    }

    private void takeOff() {
        //TODO Port rocket takeoff to the current minecart physics.
    }
}
