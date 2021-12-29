package vswe.stevescarts.modules.workers;

import vswe.stevescarts.entitys.EntityMinecartModular;

public class ModuleRailerLarge extends ModuleRailer
{
    public ModuleRailerLarge(final EntityMinecartModular cart)
    {
        super(cart);
    }

    @Override
    protected int getInventoryHeight()
    {
        return 2;
    }
}
