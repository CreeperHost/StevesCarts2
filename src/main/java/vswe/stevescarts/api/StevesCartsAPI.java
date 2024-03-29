package vswe.stevescarts.api;

import net.minecraft.resources.ResourceLocation;
import vswe.stevescarts.StevesCarts;
import vswe.stevescarts.api.farms.ICropModule;
import vswe.stevescarts.api.farms.ITreeModule;
import vswe.stevescarts.api.modules.data.ModuleData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StevesCartsAPI
{
    public static final Map<ResourceLocation, ModuleData> MODULE_REGISTRY = new HashMap<>();
    public static final List<ITreeModule> TREE_MODULES = new ArrayList<>();
    public static final List<ICropModule> CROP_MODULES = new ArrayList<>();

    public static ModuleData registerModule(ResourceLocation resourceLocation, ModuleData moduleData)
    {
        if(!MODULE_REGISTRY.containsKey(resourceLocation))
        {
            MODULE_REGISTRY.put(resourceLocation, moduleData);
        }
        else
        {
            StevesCarts.LOGGER.error("Unable to register Module " + resourceLocation + " key already in use");
        }
        return moduleData;
    }
}
