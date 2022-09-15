package vswe.stevescarts.modules.addons;

import vswe.stevescarts.entities.EntityMinecartModular;

public class ModuleSmelterAdv extends ModuleSmelter
{
    public ModuleSmelterAdv(final EntityMinecartModular cart)
    {
        super(cart);
    }

    @Override
    protected boolean canUseAdvancedFeatures()
    {
        return true;
    }
}
