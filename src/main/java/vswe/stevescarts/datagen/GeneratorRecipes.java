package vswe.stevescarts.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.Tags;
import org.jetbrains.annotations.NotNull;
import vswe.stevescarts.Constants;
import vswe.stevescarts.api.modules.data.ModuleData;
import vswe.stevescarts.helpers.ComponentTypes;
import vswe.stevescarts.init.ModBlocks;
import vswe.stevescarts.init.ModItems;
import vswe.stevescarts.init.StevesCartsModules;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class GeneratorRecipes extends RecipeProvider
{
    public GeneratorRecipes(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider)
    {
        super(output, lookupProvider);
    }

    @Override
    protected void buildRecipes(RecipeOutput consumer)
    {
        addBlockRecipes(consumer);
        addSmithingTableRecipes(consumer);
        addModuleRecipes(consumer);
    }

    private void addBlockRecipes(RecipeOutput consumer)
    {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.ADVANCED_DETECTOR.get())
                .pattern("#P#")
                .pattern("#X#")
                .pattern("#P#")
                .define('X', Tags.Items.DUSTS_REDSTONE)
                .define('#', Tags.Items.INGOTS_IRON)
                .define('P', Items.STONE_PRESSURE_PLATE)
                .group(Constants.MOD_ID)
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.GALGADORIAN_METAL.get())
                .pattern("XXX")
                .pattern("XXX")
                .pattern("XXX")
                .define('X', ModItems.COMPONENTS.get(ComponentTypes.GALGADORIAN_METAL).get())
                .group(Constants.MOD_ID)
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.REINFORCED_METAL.get())
                .pattern("XXX")
                .pattern("XXX")
                .pattern("XXX")
                .define('X', ModItems.COMPONENTS.get(ComponentTypes.REINFORCED_METAL).get())
                .group(Constants.MOD_ID)
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(consumer);

    }

    private void addModuleRecipes(RecipeOutput consumer)
    {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, getStackFromModule(StevesCartsModules.CHUNK_LOADER).getItem())
                .pattern("III")
                .pattern("GEG")
                .pattern("IDI")
                .define('I', Tags.Items.INGOTS_IRON)
                .define('G', Tags.Items.INGOTS_GOLD)
                .define('E', Items.ENDER_EYE)
                .define('D', Tags.Items.GEMS_DIAMOND)
                .group(Constants.MOD_ID)
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(consumer);
    }

    private void addSmithingTableRecipes(RecipeOutput consumer)
    {
        SmithingTransformRecipeBuilder.smithing(
                        Ingredient.of(new ItemStack(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE)),
                        Ingredient.of(getStackFromModule(StevesCartsModules.BASIC_WOOD_CUTTER)),
                        Ingredient.of(new ItemStack(Items.NETHERITE_INGOT)),
                        RecipeCategory.MISC,
                        getStackFromModule(StevesCartsModules.NETHERITE_WOOD_CUTTER).getItem()).unlocks("has_item", has(Tags.Items.INGOTS_NETHERITE))
                .save(consumer, "netherite_wood_cutter");
    }

    private ItemStack getStackFromModule(ModuleData moduleData)
    {
        if(moduleData == null) return ItemStack.EMPTY;
        Supplier<Item> itemSupplier = ModItems.MODULES.get(moduleData);
        if (itemSupplier == null || itemSupplier.get() == null) return ItemStack.EMPTY;
        return new ItemStack(itemSupplier.get());
    }
}
