package vswe.stevescarts.api.modules;

import vswe.stevescarts.api.modules.template.*;

public enum ModuleType {
    HULL(ModuleHull.class),
    ENGINE(ModuleEngine.class),
    TOOL(ModuleTool.class),
    ATTACHMENT(null),
    STORAGE(ModuleStorage.class),
    ADDON(ModuleAddon.class),
    NONE(null);

    private final Class<?> clazz;

    ModuleType(Class<?> clazz) {
        this.clazz = clazz;
    }

    public Class<?> getClazz() {
        return clazz;
    }
}
