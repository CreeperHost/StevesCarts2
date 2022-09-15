package vswe.stevescarts.modules.engines;

import vswe.stevescarts.entities.EntityMinecartModular;

public class ModuleThermalStandard extends ModuleThermalBase
{
    public ModuleThermalStandard(final EntityMinecartModular cart)
    {
        super(cart);
    }

    @Override
    protected int getEfficiency()
    {
        return 25;
    }

    @Override
    protected int getCoolantEfficiency()
    {
        return 0;
    }
}
