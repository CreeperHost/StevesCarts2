package vswe.stevescarts.api;

import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import vswe.stevescarts.api.detector.AdvancedDetectorHandler;
import vswe.stevescarts.api.farms.ICropModule;
import vswe.stevescarts.api.farms.ITreeModule;
import vswe.stevescarts.api.modules.data.ModuleData;
import vswe.stevescarts.api.upgrades.AssemblerUpgrade;

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
    private static final Map<Identifier, AssemblerUpgrade> ASSEMBLER_UPGRADE_REGISTRY = new LinkedHashMap<>();
    private static final Map<Integer, AssemblerUpgrade> LEGACY_ASSEMBLER_UPGRADES = new LinkedHashMap<>();
    private static final Map<Block, AdvancedDetectorHandler> ADVANCED_DETECTOR_HANDLERS = new LinkedHashMap<>();
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

    public static AssemblerUpgrade registerAssemblerUpgrade(AssemblerUpgrade upgrade) {
        Objects.requireNonNull(upgrade, "upgrade");
        AssemblerUpgrade previous = ASSEMBLER_UPGRADE_REGISTRY.putIfAbsent(upgrade.getId(), upgrade);
        if (previous != null) {
            throw new IllegalArgumentException("An assembler upgrade is already registered as " + upgrade.getId());
        }

        if (upgrade.getLegacyId().isPresent()) {
            int legacyId = upgrade.getLegacyId().getAsInt();
            AssemblerUpgrade previousLegacy = LEGACY_ASSEMBLER_UPGRADES.putIfAbsent(legacyId, upgrade);
            if (previousLegacy != null) {
                ASSEMBLER_UPGRADE_REGISTRY.remove(upgrade.getId());
                throw new IllegalArgumentException("Legacy assembler upgrade id " + legacyId
                        + " is already used by " + previousLegacy.getId());
            }
        }
        return upgrade;
    }

    @Nullable
    public static AssemblerUpgrade getAssemblerUpgrade(Identifier id) {
        return ASSEMBLER_UPGRADE_REGISTRY.get(id);
    }

    @Nullable
    public static AssemblerUpgrade getAssemblerUpgradeByLegacyId(int legacyId) {
        return LEGACY_ASSEMBLER_UPGRADES.get(legacyId);
    }

    public static Map<Identifier, AssemblerUpgrade> getRegisteredAssemblerUpgrades() {
        return Collections.unmodifiableMap(ASSEMBLER_UPGRADE_REGISTRY);
    }

    /**
     * Registers an adjacent-block handler for the advanced detector rail.
     * Registration should be performed after the block has been registered.
     */
    public static AdvancedDetectorHandler registerAdvancedDetectorHandler(Block block, AdvancedDetectorHandler handler) {
        Objects.requireNonNull(block, "block");
        Objects.requireNonNull(handler, "handler");
        AdvancedDetectorHandler previous = ADVANCED_DETECTOR_HANDLERS.putIfAbsent(block, handler);
        if (previous != null) {
            throw new IllegalArgumentException("An advanced detector handler is already registered for " + block);
        }
        return handler;
    }

    @Nullable
    public static AdvancedDetectorHandler getAdvancedDetectorHandler(Block block) {
        return ADVANCED_DETECTOR_HANDLERS.get(block);
    }

    public static Map<Block, AdvancedDetectorHandler> getRegisteredAdvancedDetectorHandlers() {
        return Collections.unmodifiableMap(ADVANCED_DETECTOR_HANDLERS);
    }
}
