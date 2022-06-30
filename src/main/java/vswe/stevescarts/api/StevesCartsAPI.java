package vswe.stevescarts.api;

import net.minecraft.resources.ResourceLocation;
import vswe.stevescarts.api.farms.ICropModule;
import vswe.stevescarts.api.farms.ITreeModule;
import vswe.stevescarts.modules.data.ModuleData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StevesCartsAPI
{
    public static final Map<ResourceLocation, ModuleData> MODULE_REGISTRY = new HashMap<>();
    public static List<ITreeModule> TREE_MODULES = new ArrayList<>();
    public static List<ICropModule> CROP_MODULES = new ArrayList<>();

    public static ModuleData registerModule(ResourceLocation resourceLocation, ModuleData moduleData)
    {
        MODULE_REGISTRY.put(resourceLocation, moduleData);
        return moduleData;
    }

    public static void registerTree(ITreeModule treeModule)
    {
        TREE_MODULES.add(treeModule);
    }

    public static void registerCrop(ICropModule cropModule)
    {
        CROP_MODULES.add(cropModule);
    }
}
