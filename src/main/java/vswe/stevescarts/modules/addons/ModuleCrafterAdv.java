package vswe.stevescarts.modules.addons;

import vswe.stevescarts.entities.EntityMinecartModular;

public class ModuleCrafterAdv extends ModuleCrafter
{
    public ModuleCrafterAdv(final EntityMinecartModular cart)
    {
        super(cart);
    }

    @Override
    protected boolean canUseAdvancedFeatures()
    {
        return true;
    }
}
