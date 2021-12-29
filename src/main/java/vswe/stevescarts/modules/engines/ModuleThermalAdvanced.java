package vswe.stevescarts.modules.engines;

import vswe.stevescarts.entitys.EntityMinecartModular;

public class ModuleThermalAdvanced extends ModuleThermalBase
{
    public ModuleThermalAdvanced(final EntityMinecartModular cart)
    {
        super(cart);
    }

    @Override
    protected int getEfficiency()
    {
        return 60;
    }

    @Override
    protected int getCoolantEfficiency()
    {
        return 90;
    }
}
