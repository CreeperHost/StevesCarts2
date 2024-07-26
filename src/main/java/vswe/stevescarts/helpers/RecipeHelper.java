package vswe.stevescarts.helpers;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

import java.util.Optional;

/**
 * Created by brandon3055 on 10/02/2024
 */
//TODO, Replace the broken RecipeHelper in Poly
public class RecipeHelper {

    public static Optional<RecipeHolder<SmeltingRecipe>> findSmeltRecipe(ItemStack stack, Level level) {
        return findRecipe(RecipeType.SMELTING, new SingleRecipeInput(stack), level);
    }

    public static <I extends RecipeInput, T extends Recipe<I>> Optional<RecipeHolder<T>> findRecipe(RecipeType<T> type, I input, Level level) {
        return level.getRecipeManager().getRecipeFor(type, input, level);
    }
}
