package vswe.stevescarts.modules.engines;

import vswe.stevescarts.SCConfig;
import vswe.stevescarts.entitys.EntityMinecartModular;

public class ModuleSolarBasic extends ModuleSolarTop
{
    public ModuleSolarBasic(final EntityMinecartModular cart)
    {
        super(cart);
    }

    @Override
    protected int getPanelCount()
    {
        return 2;
    }

    @Override
    protected int getMaxCapacity()
    {
        return 100000;
    }

    @Override
    protected int getGenSpeed()
    {
        return SCConfig.basic_solar_production.get();
    }
}
