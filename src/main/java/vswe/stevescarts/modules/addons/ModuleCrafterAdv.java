package vswe.stevescarts.modules.addons;

import vswe.stevescarts.entities.ModularMinecart;

public class ModuleCrafterAdv extends ModuleCrafter {
    public ModuleCrafterAdv(ModularMinecart cart) {
        super(cart);
    }

    @Override
    protected boolean canUseAdvancedFeatures() {
        return true;
    }
}
