package vswe.stevescarts.modules.addons;

import vswe.stevescarts.entities.ModularMinecart;

public class ModuleSmelterAdv extends ModuleSmelter {
    public ModuleSmelterAdv(ModularMinecart cart) {
        super(cart);
    }

    @Override
    protected boolean canUseAdvancedFeatures() {
        return true;
    }
}
