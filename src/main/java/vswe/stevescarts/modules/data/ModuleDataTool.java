package vswe.stevescarts.modules.data;

import vswe.stevescarts.helpers.Localization;
import vswe.stevescarts.modules.ModuleBase;

public class ModuleDataTool extends ModuleData
{
    private boolean unbreakable;

    public ModuleDataTool(final int id, final String name, final Class<? extends ModuleBase> moduleClass, final int modularCost, final boolean unbreakable)
    {
        super(id, name, moduleClass, modularCost);
        useExtraData((byte) 100);
        this.unbreakable = unbreakable;
    }

    @Override
    public String getModuleInfoText(final byte b)
    {
        if (unbreakable)
        {
            return Localization.MODULE_INFO.TOOL_UNBREAKABLE.translate();
        }
        return Localization.MODULE_INFO.TOOL_DURABILITY.translate(String.valueOf(b));
    }

    @Override
    public String getCartInfoText(final String name, final byte b)
    {
        if (unbreakable)
        {
            return super.getCartInfoText(name, b);
        }
        return name + " [" + b + "%]";
    }
}
