package vswe.stevescarts.helpers;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Created by brandon3055 on 10/02/2024
 */
//TODO, Replace the broken RecipeHelper in Poly
public class RecipeHelper {


    private static final Map<RecipeType<?>, RecipeManager.CachedCheck<?, ?>> CACHES = new HashMap<>();

    public static Optional<RecipeHolder<SmeltingRecipe>> findSmeltRecipe(ItemStack stack, ServerLevel level) {
        return findRecipe(RecipeType.SMELTING, new SingleRecipeInput(stack), level);
    }

    //This may break if given different input types for the same recipe type...
    public static <I extends RecipeInput, T extends Recipe<I>> Optional<RecipeHolder<T>> findRecipe(RecipeType<T> type, I input, ServerLevel level) {
        RecipeManager.CachedCheck<?, ?> cache = CACHES.computeIfAbsent(type, recipeType -> RecipeManager.createCheck(type));
        return ((RecipeManager.CachedCheck<I, T>)cache).getRecipeFor(input, level);
    }
}
