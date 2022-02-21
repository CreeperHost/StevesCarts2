package vswe.stevescarts.modules.data;

import vswe.stevescarts.modules.ModuleBase;


public class ModuleDataHull extends ModuleData
{
    private int modularCapacity;
    private int engineMaxCount;
    private int addonMaxCount;
    private int complexityMax;

    public ModuleDataHull(final int id, final String name, final Class<? extends ModuleBase> moduleClass)
    {
        super(id, name, moduleClass, 0);
    }

    public ModuleDataHull setCapacity(final int val)
    {
        modularCapacity = val;
        return this;
    }

    public ModuleDataHull setEngineMax(final int val)
    {
        engineMaxCount = val;
        return this;
    }

    public ModuleDataHull setAddonMax(final int val)
    {
        addonMaxCount = val;
        return this;
    }

    public ModuleDataHull setComplexityMax(final int val)
    {
        complexityMax = val;
        return this;
    }

    public int getEngineMax()
    {
        return engineMaxCount;
    }

    public int getAddonMax()
    {
        return addonMaxCount;
    }

    public int getCapacity()
    {
        return modularCapacity;
    }

    public int getComplexityMax()
    {
        return complexityMax;
    }
}
