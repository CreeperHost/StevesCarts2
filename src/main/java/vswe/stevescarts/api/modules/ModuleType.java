package vswe.stevescarts.api.modules;

import vswe.stevescarts.modules.addons.ModuleAddon;
import vswe.stevescarts.modules.engines.ModuleEngine;
import vswe.stevescarts.modules.hull.ModuleHull;
import vswe.stevescarts.modules.storages.ModuleStorage;
import vswe.stevescarts.modules.workers.tools.ModuleTool;

public enum ModuleType
{
    HULL(ModuleHull.class),
    ENGINE(ModuleEngine.class),
    TOOL(ModuleTool.class),
    STORAGE(ModuleStorage.class),
    ADDON(ModuleAddon.class);

    private final Class<?> clazz;

    ModuleType(Class<?> clazz)
    {
        this.clazz = clazz;
    }

    public Class<?> getClazz()
    {
        return clazz;
    }
}
