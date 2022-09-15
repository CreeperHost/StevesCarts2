package vswe.stevescarts.modules.engines;

import vswe.stevescarts.entities.EntityMinecartModular;

public class ModuleCoalStandard extends ModuleCoalBase
{
    public ModuleCoalStandard(final EntityMinecartModular cart)
    {
        super(cart);
    }

    @Override
    public double getFuelMultiplier()
    {
        return 2.25;
    }
}
