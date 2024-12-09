package vswe.stevescarts.modules.engines;

import vswe.stevescarts.SCConfig;
import vswe.stevescarts.entities.ModularMinecart;

public class ModuleSolarBasic extends ModuleSolarTop
{
    public ModuleSolarBasic(ModularMinecart cart)
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
        return 10000;
    }

    @Override
    protected int getGenSpeed()
    {
        return SCConfig.COMMON.basic_solar_production.get();
    }
}
