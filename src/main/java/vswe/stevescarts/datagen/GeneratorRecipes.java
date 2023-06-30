package vswe.stevescarts.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.SmithingTransformRecipeBuilder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.Tags;
import vswe.stevescarts.api.modules.data.ModuleData;
import vswe.stevescarts.init.StevesCartsModules;

import java.util.function.Consumer;

public class GeneratorRecipes extends RecipeProvider
{
    public GeneratorRecipes(PackOutput output)
    {
        super(output);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> consumer)
    {
        SmithingTransformRecipeBuilder.smithing(
                        Ingredient.of(new ItemStack(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE)),
                        Ingredient.of(getStackFromModule(StevesCartsModules.BASIC_WOOD_CUTTER)),
                        Ingredient.of(new ItemStack(Items.NETHERITE_INGOT)),
                        RecipeCategory.MISC,
                        getStackFromModule(StevesCartsModules.NETHERITE_WOOD_CUTTER).getItem()).unlocks("has_netherite", has(Tags.Items.INGOTS_NETHERITE))
                .save(consumer, "netherite_wood_cutter");
    }

    private ItemStack getStackFromModule(ModuleData moduleData)
    {
        if(moduleData == null) return ItemStack.EMPTY;
        return moduleData.getItemStack();
    }
}
