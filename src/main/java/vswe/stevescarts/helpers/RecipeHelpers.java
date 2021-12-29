package vswe.stevescarts.helpers;

import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.world.World;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

public class RecipeHelpers
{
    public static Set<IRecipe<?>> findRecipesByType(IRecipeType<?> typeIn, World world)
    {
        return world != null ? world.getRecipeManager().getRecipes().stream()
                .filter(recipe -> recipe.getType() == typeIn).collect(Collectors.toSet()) : Collections.emptySet();
    }
}
