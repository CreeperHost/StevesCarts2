package vswe.stevescarts.api.modules.data;

import net.minecraft.network.chat.Component;
import vswe.stevescarts.api.modules.ModuleType;

import javax.annotation.Nullable;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/** Validates a complete set of modules against a hull without depending on assembler UI state. */
public final class ModuleAssemblyValidator {
    private ModuleAssemblyValidator() {
    }

    public static Result validate(@Nullable ModuleDataHull hull, @Nullable List<ModuleData> modules) {
        if (hull == null || modules == null || modules.stream().anyMatch(module -> module == null)) {
            return invalid(Failure.INVALID_COMBINATION,
                    Component.translatable("info.stevescarts.impossibleCombinationError"));
        }
        if (getTotalCost(modules) > hull.getCapacity()) {
            return invalid(Failure.CAPACITY_OVERLOAD,
                    Component.translatable("info.stevescarts.capacityOverloadError"));
        }
        if (!hasValidSlotCounts(hull, modules)) {
            return invalid(Failure.INVALID_COMBINATION,
                    Component.translatable("info.stevescarts.impossibleCombinationError"));
        }

        for (int i = 0; i < modules.size(); i++) {
            ModuleData module = modules.get(i);
            if (module.getCost() > hull.getComplexityMax()) {
                return invalid(Failure.COMPLEXITY_OVERLOAD,
                        Component.translatable("info.stevescarts.complexityOverloadError", module.getName()));
            }
            if (module.getParent() != null && !modules.contains(module.getParent())) {
                return invalid(Failure.MISSING_REQUIREMENT,
                        Component.translatable("info.stevescarts.missingParentError",
                                module.getName(), module.getParent().getName()));
            }
            for (ModuleData conflict : module.getNemesis()) {
                if (modules.contains(conflict)) {
                    return invalid(Failure.CONFLICT,
                            Component.translatable("info.stevescarts.presentNemesisError",
                                    module.getName(), conflict.getName()));
                }
            }
            for (ModuleDataGroup requirement : module.getRequirement()) {
                long matchingModules = modules.stream().filter(requirement.getModules()::contains).count();
                if (matchingModules < requirement.getCount()) {
                    return invalid(Failure.MISSING_REQUIREMENT,
                            Component.translatable("info.stevescarts.missingParentError", module.getName(),
                                    requirement.getCountName() + " " + requirement.getName()));
                }
            }

            for (int j = i + 1; j < modules.size(); j++) {
                ModuleData other = modules.get(j);
                if (module == other) {
                    if (!module.getAllowDuplicate()) {
                        return invalid(Failure.DUPLICATE,
                                Component.translatable("info.stevescarts.presentDuplicateError", module.getName()));
                    }
                    continue;
                }

                ModuleData.SIDE clashingSide = findClashingSide(module, other);
                if (clashingSide != ModuleData.SIDE.NONE) {
                    return invalid(Failure.SIDE_CLASH,
                            Component.translatable("info.stevescarts.sideClashError",
                                    module.getName(), other.getName(), clashingSide.toString()));
                }
            }
        }
        return Result.VALID;
    }

    public static boolean hasValidSlotCounts(@Nullable ModuleDataHull hull, @Nullable List<ModuleData> modules) {
        if (hull == null || modules == null) {
            return false;
        }

        EnumMap<ModuleType, Integer> counts = new EnumMap<>(ModuleType.class);
        for (ModuleData module : modules) {
            if (module == null) {
                return false;
            }
            ModuleType type = module.getModuleType();
            if (counts.merge(type, 1, Integer::sum) > hull.getMaxCount(type)) {
                return false;
            }
        }
        return true;
    }

    public static int getTotalCost(List<ModuleData> modules) {
        return modules.stream().mapToInt(ModuleData::getCost).sum();
    }

    private static ModuleData.SIDE findClashingSide(ModuleData first, ModuleData second) {
        if (first.getRenderingSides() == null || second.getRenderingSides() == null) {
            return ModuleData.SIDE.NONE;
        }
        Set<ModuleData.SIDE> occupied = new HashSet<>(second.getRenderingSides());
        for (ModuleData.SIDE side : first.getRenderingSides()) {
            if (side != ModuleData.SIDE.NONE && occupied.contains(side)) {
                return side;
            }
        }
        return ModuleData.SIDE.NONE;
    }

    private static Result invalid(Failure failure, Component message) {
        return new Result(failure, message);
    }

    public enum Failure {
        NONE,
        INVALID_COMBINATION,
        CAPACITY_OVERLOAD,
        COMPLEXITY_OVERLOAD,
        MISSING_REQUIREMENT,
        CONFLICT,
        DUPLICATE,
        SIDE_CLASH
    }

    public record Result(Failure failure, @Nullable Component error) {
        private static final Result VALID = new Result(Failure.NONE, null);

        public boolean isValid() {
            return failure == Failure.NONE;
        }
    }
}
