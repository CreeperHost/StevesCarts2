package vswe.stevescarts.api;

import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import vswe.stevescarts.api.farms.ICropModule;
import vswe.stevescarts.api.farms.ITreeModule;
import vswe.stevescarts.api.modules.data.ModuleData;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

public final class StevesCartsAPI {
    private static final Map<Identifier, ModuleData> MODULE_REGISTRY = new LinkedHashMap<>();
    public static final List<ITreeModule> TREE_MODULES = new ArrayList<>();
    public static final List<ICropModule> CROP_MODULES = new ArrayList<>();

    private StevesCartsAPI() {
    }

    public static ModuleData registerModule(ModuleData moduleData) {
        Objects.requireNonNull(moduleData, "moduleData");
        return registerModule(moduleData.getID(), moduleData);
    }

    public static ModuleData registerModule(Identifier resourceLocation, ModuleData moduleData) {
        Objects.requireNonNull(resourceLocation, "resourceLocation");
        Objects.requireNonNull(moduleData, "moduleData");
        if (!resourceLocation.equals(moduleData.getID())) {
            throw new IllegalArgumentException("Module registry key " + resourceLocation
                    + " does not match the module id " + moduleData.getID());
        }

        ModuleData previous = MODULE_REGISTRY.putIfAbsent(resourceLocation, moduleData);
        if (previous != null) {
            throw new IllegalArgumentException("A module is already registered as " + resourceLocation);
        }

        return moduleData;
    }

    public static ModuleData registerModule(ModuleData moduleData, Supplier<? extends Item> item) {
        moduleData.setItem(item);
        return registerModule(moduleData);
    }

    @Nullable
    public static ModuleData getModule(Identifier id) {
        return MODULE_REGISTRY.get(id);
    }

    public static Map<Identifier, ModuleData> getRegisteredModules() {
        return Collections.unmodifiableMap(MODULE_REGISTRY);
    }
}
