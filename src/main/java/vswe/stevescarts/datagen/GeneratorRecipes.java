package vswe.stevescarts.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.data.recipes.packs.VanillaRecipeProvider;
import net.minecraft.tags.BlockTags;
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
    public GeneratorRecipes(HolderLookup.Provider provider, RecipeOutput output) {
        super(provider, output);
    }

    @Override
    protected void buildRecipes() {
        addBlockRecipes();
        addSmithingTableRecipes();
        addModuleRecipes();
    }

    private void addBlockRecipes()
    {
        shaped(RecipeCategory.MISC, ModBlocks.ADVANCED_DETECTOR.get())
                .pattern("#P#")
                .pattern("#X#")
                .pattern("#P#")
                .define('X', Tags.Items.DUSTS_REDSTONE)
                .define('#', Tags.Items.INGOTS_IRON)
                .define('P', Items.STONE_PRESSURE_PLATE)
                .group(Constants.MOD_ID)
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output);

        shaped(RecipeCategory.MISC, ModBlocks.GALGADORIAN_METAL.get())
                .pattern("XXX")
                .pattern("XXX")
                .pattern("XXX")
                .define('X', ModItems.COMPONENTS.get(ComponentTypes.GALGADORIAN_METAL).get())
                .group(Constants.MOD_ID)
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output);

        shaped(RecipeCategory.MISC, ModBlocks.REINFORCED_METAL.get())
                .pattern("XXX")
                .pattern("XXX")
                .pattern("XXX")
                .define('X', ModItems.COMPONENTS.get(ComponentTypes.REINFORCED_METAL).get())
                .group(Constants.MOD_ID)
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output);
    }

    private void addModuleRecipes()
    {
        shaped(RecipeCategory.MISC, getStackFromModule(StevesCartsModules.CHUNK_LOADER).getItem())
                .pattern("III")
                .pattern("GEG")
                .pattern("IDI")
                .define('I', Tags.Items.INGOTS_IRON)
                .define('G', Tags.Items.INGOTS_GOLD)
                .define('E', Items.ENDER_EYE)
                .define('D', Tags.Items.GEMS_DIAMOND)
                .group(Constants.MOD_ID)
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output);
    }

    private void addSmithingTableRecipes()
    {
        SmithingTransformRecipeBuilder.smithing(
                        Ingredient.of(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE),
                        Ingredient.of(getStackFromModule(StevesCartsModules.BASIC_WOOD_CUTTER).getItem()),
                        Ingredient.of(Items.NETHERITE_INGOT),
                        RecipeCategory.MISC,
                        getStackFromModule(StevesCartsModules.NETHERITE_WOOD_CUTTER).getItem()).unlocks("has_item", has(Tags.Items.INGOTS_NETHERITE))
                .save(output, "netherite_wood_cutter");
    }

    private ItemStack getStackFromModule(ModuleData moduleData)
    {
        if(moduleData == null) return ItemStack.EMPTY;
        Supplier<Item> itemSupplier = ModItems.MODULES.get(moduleData);
        if (itemSupplier == null || itemSupplier.get() == null) return ItemStack.EMPTY;
        return new ItemStack(itemSupplier.get());
    }

    public static class Runner extends RecipeProvider.Runner {
        public Runner(PackOutput p_365442_, CompletableFuture<HolderLookup.Provider> p_362168_) {
            super(p_365442_, p_362168_);
        }

        @Override
        protected RecipeProvider createRecipeProvider(HolderLookup.Provider p_364945_, RecipeOutput p_362956_) {
            return new GeneratorRecipes(p_364945_, p_362956_);
        }

        @Override
        public String getName() {
            return "Steves Carts Recipes";
        }
    }
}
