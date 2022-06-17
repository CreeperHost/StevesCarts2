package vswe.stevescarts.api;

import net.minecraft.resources.ResourceLocation;
import vswe.stevescarts.modules.data.ModuleData;

import java.util.HashMap;
import java.util.Map;

public class StevesCartsAPI
{
    public static final Map<ResourceLocation, ModuleData> MODULE_REGISTRY = new HashMap<>();

    public static ModuleData registerModule(ResourceLocation resourceLocation, ModuleData moduleData)
    {
        MODULE_REGISTRY.put(resourceLocation, moduleData);
        return moduleData;
    }
}
