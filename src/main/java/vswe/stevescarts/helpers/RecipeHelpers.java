package vswe.stevescarts.helpers;

import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

public class RecipeHelpers
{
    public static Set<Recipe<?>> findRecipesByType(RecipeType<?> typeIn, Level world)
    {
        return world != null ? world.getRecipeManager().getRecipes().stream().filter(recipe -> recipe.getType() == typeIn).collect(Collectors.toSet()) : Collections.emptySet();
    }
}
