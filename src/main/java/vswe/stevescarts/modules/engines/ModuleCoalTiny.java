package vswe.stevescarts.modules.engines;

import vswe.stevescarts.entities.EntityMinecartModular;

public class ModuleCoalTiny extends ModuleCoalBase
{
    public ModuleCoalTiny(final EntityMinecartModular cart)
    {
        super(cart);
    }

    @Override
    protected int getInventoryWidth()
    {
        return 1;
    }

    @Override
    public double getFuelMultiplier()
    {
        return 0.5;
    }
}
