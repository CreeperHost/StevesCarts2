package vswe.stevescarts.polylib;

import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.context.ContextKeySet;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CookingFuel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.providers.number.ints.ConditionalValue;
import net.minecraft.world.level.storage.loot.providers.number.ints.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProvider;
import net.minecraft.world.level.storage.loot.providers.number.ints.Quotient;
import net.minecraft.world.level.storage.loot.providers.number.ints.ResolvableInt;
import net.neoforged.neoforge.common.loot.NeoForgeLootContextParams;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class FuelHelper {
    private static final ContextKeySet FUEL_CONTEXT = new ContextKeySet.Builder()
            .optional(NeoForgeLootContextParams.QUERIED_STACK)
            .build();

    public static boolean isItemFuel(@NotNull ItemStack itemStack, Level level) {
        return itemStack.has(DataComponents.COOKING_FUEL);
    }

    public static int getItemBurnTime(@NotNull ItemStack itemStack, Level level) {
        CookingFuel fuel = itemStack.get(DataComponents.COOKING_FUEL);
        if (fuel == null) {
            return 0;
        }

        if (level instanceof ServerLevel serverLevel) {
            LootParams params = new LootParams.Builder(serverLevel)
                    .withParameter(NeoForgeLootContextParams.QUERIED_STACK, itemStack)
                    .create(FUEL_CONTEXT);
            LootContext context = new LootContext.Builder(params).create(Optional.empty());
            return fuel.burnTime().get(context, 0);
        }

        return resolveClientFuelValue(fuel.burnTime(), level);
    }

    private static int resolveClientFuelValue(ResolvableInt burnTime, Level level) {
        if (burnTime instanceof ResolvableInt.Constant constant) {
            return constant.value();
        }
        if (burnTime instanceof ResolvableInt.Reference reference) {
            return level.registryAccess()
                    .lookup(Registries.CONTEXT_INT_PROVIDER)
                    .flatMap(registry -> registry.get(reference.key()))
                    .map(holder -> resolveNormalCookingValue(holder.value(), 0))
                    .orElse(0);
        }
        return 0;
    }

    private static int resolveNormalCookingValue(ContextIntProvider provider, int depth) {
        if (depth > 16) {
            return 0;
        }
        if (provider instanceof ConstantValue constant) {
            return constant.value();
        }
        if (provider instanceof ConditionalValue conditional) {
            return resolveNormalCookingValue(conditional.onFalse().value(), depth + 1);
        }
        if (provider instanceof Quotient quotient) {
            int divisor = resolveNormalCookingValue(quotient.right().value(), depth + 1);
            return divisor == 0 ? 0 : resolveNormalCookingValue(quotient.left().value(), depth + 1) / divisor;
        }
        return 0;
    }
}
