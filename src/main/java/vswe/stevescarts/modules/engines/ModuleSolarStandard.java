package vswe.stevescarts.modules.engines;

import vswe.stevescarts.SCConfig;
import vswe.stevescarts.entitys.EntityMinecartModular;

public class ModuleSolarStandard extends ModuleSolarTop
{
    public ModuleSolarStandard(final EntityMinecartModular cart)
    {
        super(cart);
    }

    @Override
    protected int getPanelCount()
    {
        return 4;
    }

    @Override
    protected int getMaxCapacity()
    {
        return 800000;
    }

    @Override
    protected int getGenSpeed()
    {
        return SCConfig.standard_solar_production.get();
    }
}
