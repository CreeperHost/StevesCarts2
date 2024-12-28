package vswe.stevescarts.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.Tags;
import vswe.stevescarts.Constants;
import vswe.stevescarts.api.modules.data.ModuleData;
import vswe.stevescarts.helpers.ComponentTypes;
import vswe.stevescarts.init.ModBlocks;
import vswe.stevescarts.init.ModItems;
import vswe.stevescarts.init.StevesCartsModules;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class GeneratorRecipes extends RecipeProvider {
    public GeneratorRecipes(HolderLookup.Provider provider, RecipeOutput output) {
        super(provider, output);
    }

    @Override
    protected void buildRecipes() {
        addBlockRecipes();
        addSmithingTableRecipes();
        addModuleRecipes();
        addSmelting();
        shapedCrafting();
    }

    private void addSmelting() {
        oreCooking(RecipeSerializer.SMELTING_RECIPE, SmeltingRecipe::new, itemFromName("stevescarts:component_raw_hardener"), RecipeCategory.MISC, itemFromName("stevescarts:component_refined_hardener"), 0.7F, 200, "stevescarts", "_from_smelting");
        oreCooking(RecipeSerializer.SMELTING_RECIPE, SmeltingRecipe::new, itemFromName("stevescarts:component_large_lump_of_galgador"), RecipeCategory.MISC, itemFromName("stevescarts:component_enhanced_galgadorian_metal"), 0.7F, 200, "stevescarts", "_from_smelting");
        oreCooking(RecipeSerializer.SMELTING_RECIPE, SmeltingRecipe::new, itemFromName("stevescarts:component_stabilized_metal"), RecipeCategory.MISC, itemFromName("stevescarts:component_reinforced_metal"), 0.7F, 200, "stevescarts", "_from_smelting");
        oreCooking(RecipeSerializer.SMELTING_RECIPE, SmeltingRecipe::new, itemFromName("stevescarts:component_raw_handle"), RecipeCategory.MISC, itemFromName("stevescarts:component_refined_handle"), 0.7F, 200, "stevescarts", "_from_smelting");
        oreCooking(RecipeSerializer.SMELTING_RECIPE, SmeltingRecipe::new, itemFromName("stevescarts:component_lump_of_galgador"), RecipeCategory.MISC, itemFromName("stevescarts:component_galgadorian_metal"), 0.7F, 200, "stevescarts", "_from_smelting");
    }

    private void addBlockRecipes() {
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

    private void addModuleRecipes() {
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

    private void addSmithingTableRecipes() {
        SmithingTransformRecipeBuilder.smithing(
                        Ingredient.of(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE),
                        Ingredient.of(getStackFromModule(StevesCartsModules.BASIC_WOOD_CUTTER).getItem()),
                        Ingredient.of(Items.NETHERITE_INGOT),
                        RecipeCategory.MISC,
                        getStackFromModule(StevesCartsModules.NETHERITE_WOOD_CUTTER).getItem()).unlocks("has_item", has(Tags.Items.INGOTS_NETHERITE))
                .save(output, recipeFolder(getStackFromModule(StevesCartsModules.NETHERITE_WOOD_CUTTER).getItem(), "smithing"));
    }

    private ResourceKey<Recipe<?>> recipeFolder(ItemLike result, String folder) {
        ResourceLocation key = BuiltInRegistries.ITEM.getKey(result.asItem());
        return ResourceKey.create(Registries.RECIPE, ResourceLocation.fromNamespaceAndPath("stevescarts", folder + "/" + key.getPath()));
    }

    protected <T extends AbstractCookingRecipe> void oreCooking(RecipeSerializer<T> serializer, AbstractCookingRecipe.Factory<T> recipeFactory, ItemLike input, RecipeCategory category, ItemLike result, float experience, int cookingTime, String group, String suffix) {
        SimpleCookingRecipeBuilder.generic(Ingredient.of(input), category, result, experience, cookingTime, serializer, recipeFactory).group(group).unlockedBy(getHasName(input), this.has(input)).save(this.output, recipeFolder(result, "smelting"));
    }

    private Item itemFromName(String name) {
        return BuiltInRegistries.ITEM.get(ResourceLocation.parse(name)).get().value();
    }

    private ItemStack getStackFromModule(ModuleData moduleData) {
        if (moduleData == null) return ItemStack.EMPTY;
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

    private void shapedCrafting() {
        shaped(RecipeCategory.MISC, itemFromName("stevescarts:component_simple_pcb"))
                .pattern("#X#")
                .pattern("XGX")
                .pattern("#X#")
                .define('X', itemFromName("minecraft:iron_ingot"))
                .define('G', itemFromName("minecraft:gold_ingot"))
                .define('#', itemFromName("minecraft:redstone"))
                .group("stevescarts")
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output, recipeFolder(itemFromName("stevescarts:component_simple_pcb"), "component"));


        shaped(RecipeCategory.MISC, itemFromName("stevescarts:module_compact_solar_engine"))
                .pattern("#I#")
                .pattern("XRX")
                .pattern("PIP")
                .define('X', itemFromName("stevescarts:component_advanced_pcb"))
                .define('#', itemFromName("stevescarts:component_advanced_solar_panel"))
                .define('P', itemFromName("minecraft:piston"))
                .define('R', itemFromName("minecraft:redstone"))
                .define('I', itemFromName("minecraft:iron_ingot"))
                .group("stevescarts")
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output, recipeFolder(itemFromName("stevescarts:module_compact_solar_engine"), "module"));


        shaped(RecipeCategory.MISC, itemFromName("stevescarts:module_steves_arcade"))
                .pattern(" G ")
                .pattern("X#X")
                .pattern("RXR")
                .define('#', itemFromName("stevescarts:component_simple_pcb"))
                .define('X', ItemTags.PLANKS)
                .define('R', itemFromName("minecraft:redstone"))
                .define('G', itemFromName("minecraft:glass_pane"))
                .group("stevescarts")
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output, recipeFolder(itemFromName("stevescarts:module_steves_arcade"), "module"));


        shaped(RecipeCategory.MISC, itemFromName("stevescarts:module_railer"))
                .pattern("###")
                .pattern("XRX")
                .pattern("###")
                .define('R', itemFromName("minecraft:rail"))
                .define('X', itemFromName("minecraft:iron_ingot"))
                .define('#', ItemTags.STONE_CRAFTING_MATERIALS)
                .group("stevescarts")
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output, recipeFolder(itemFromName("stevescarts:module_railer"), "module"));


        shaped(RecipeCategory.MISC, itemFromName("stevescarts:component_empty_disk"))
                .pattern("#")
                .pattern("X")
                .define('X', itemFromName("stevescarts:component_simple_pcb"))
                .define('#', itemFromName("minecraft:redstone"))
                .group("stevescarts")
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output, recipeFolder(itemFromName("stevescarts:component_empty_disk"), "component"));


        shaped(RecipeCategory.MISC, itemFromName("stevescarts:upgrade_thermal_engine"))
                .pattern("NXN")
                .pattern("PFP")
                .pattern("O#O")
                .define('P', itemFromName("minecraft:piston"))
                .define('F', itemFromName("minecraft:furnace"))
                .define('N', itemFromName("minecraft:nether_brick"))
                .define('O', itemFromName("minecraft:obsidian"))
                .define('X', itemFromName("stevescarts:component_advanced_pcb"))
                .define('#', itemFromName("stevescarts:component_blank_upgrade"))
                .group("stevescarts")
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output, recipeFolder(itemFromName("stevescarts:upgrade_thermal_engine"), "upgrade"));


        shaped(RecipeCategory.MISC, itemFromName("stevescarts:module_side_chests"))
                .pattern("#X#")
                .pattern("LCL")
                .pattern("#X#")
                .define('X', itemFromName("stevescarts:component_chest_pane"))
                .define('L', itemFromName("stevescarts:component_large_chest_pane"))
                .define('#', itemFromName("stevescarts:component_huge_chest_pane"))
                .define('C', itemFromName("stevescarts:component_chest_lock"))
                .group("stevescarts")
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output, recipeFolder(itemFromName("stevescarts:module_side_chests"), "module"));


        shaped(RecipeCategory.MISC, itemFromName("stevescarts:module_hydrator"))
                .pattern("I#I")
                .pattern(" X ")
                .define('I', itemFromName("minecraft:iron_ingot"))
                .define('X', itemFromName("minecraft:oak_fence"))
                .define('#', itemFromName("minecraft:glass_bottle"))
                .group("stevescarts")
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output, recipeFolder(itemFromName("stevescarts:module_hydrator"), "module"));


        shaped(RecipeCategory.MISC, itemFromName("stevescarts:module_invisibility_core"))
                .pattern(" X ")
                .pattern("XEX")
                .pattern(" # ")
                .define('E', itemFromName("minecraft:ender_eye"))
                .define('#', itemFromName("minecraft:golden_carrot"))
                .define('X', itemFromName("stevescarts:component_glass_o_magic"))
                .group("stevescarts")
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output, recipeFolder(itemFromName("stevescarts:module_invisibility_core"), "module"));


        shaped(RecipeCategory.MISC, itemFromName("stevescarts:component_tri_torch"))
                .pattern("###")
                .define('#', itemFromName("minecraft:torch"))
                .group("stevescarts")
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output, recipeFolder(itemFromName("stevescarts:component_tri_torch"), "component"));


        shaped(RecipeCategory.MISC, itemFromName("stevescarts:upgrade_module_input"))
                .pattern(" X ")
                .pattern("PCP")
                .pattern("I#I")
                .define('I', itemFromName("minecraft:iron_ingot"))
                .define('C', Tags.Items.CHESTS_WOODEN)
                .define('P', itemFromName("minecraft:piston"))
                .define('X', itemFromName("stevescarts:component_advanced_pcb"))
                .define('#', itemFromName("stevescarts:component_blank_upgrade"))
                .group("stevescarts")
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output, recipeFolder(itemFromName("stevescarts:upgrade_module_input"), "upgrade"));


        shaped(RecipeCategory.MISC, itemFromName("minecraft:spruce_planks"), 2)
                .pattern("X")
                .define('X', itemFromName("stevescarts:component_spruce_log"))
                .group("stevescarts")
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output, recipeFolder(itemFromName("minecraft:spruce_planks"), "component"));


        shaped(RecipeCategory.MISC, itemFromName("stevescarts:module_milker"))
                .pattern("XXX")
                .pattern("#B#")
                .pattern(" # ")
                .define('#', itemFromName("stevescarts:component_simple_pcb"))
                .define('X', itemFromName("minecraft:wheat"))
                .define('B', itemFromName("minecraft:bucket"))
                .group("stevescarts")
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output, recipeFolder(itemFromName("stevescarts:module_milker"), "module"));


        shaped(RecipeCategory.MISC, itemFromName("stevescarts:component_eye_of_galgador"))
                .pattern("X#X")
                .pattern("TET")
                .pattern("X#X")
                .define('X', itemFromName("minecraft:magma_cream"))
                .define('#', itemFromName("minecraft:fermented_spider_eye"))
                .define('E', itemFromName("minecraft:ender_eye"))
                .define('T', itemFromName("minecraft:ghast_tear"))
                .group("stevescarts")
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output, recipeFolder(itemFromName("stevescarts:component_eye_of_galgador"), "component"));


        shaped(RecipeCategory.MISC, itemFromName("stevescarts:component_huge_iron_pane"))
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .define('#', itemFromName("stevescarts:component_iron_pane"))
                .group("stevescarts")
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output, recipeFolder(itemFromName("stevescarts:component_huge_iron_pane"), "component"));


        shaped(RecipeCategory.MISC, itemFromName("stevescarts:upgrade_cart_deployer"))
                .pattern("IRI")
                .pattern("XPX")
                .pattern("I#I")
                .define('I', itemFromName("minecraft:iron_ingot"))
                .define('R', itemFromName("minecraft:rail"))
                .define('P', itemFromName("minecraft:piston"))
                .define('X', itemFromName("stevescarts:component_simple_pcb"))
                .define('#', itemFromName("stevescarts:component_blank_upgrade"))
                .group("stevescarts")
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output, recipeFolder(itemFromName("stevescarts:upgrade_cart_deployer"), "upgrade"));


        shaped(RecipeCategory.MISC, itemFromName("stevescarts:module_advanced_shooter"))
                .pattern(" D ")
                .pattern(" SP")
                .pattern("IEI")
                .define('D', itemFromName("stevescarts:component_entity_scanner"))
                .define('S', itemFromName("stevescarts:component_shooting_station"))
                .define('P', itemFromName("stevescarts:component_pipe"))
                .define('I', itemFromName("minecraft:iron_ingot"))
                .define('E', itemFromName("stevescarts:component_entity_analyzer"))
                .group("stevescarts")
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output, recipeFolder(itemFromName("stevescarts:module_advanced_shooter"), "module"));


        shaped(RecipeCategory.MISC, itemFromName("stevescarts:blockjunction"), 1)
                .pattern(" # ")
                .pattern("#X#")
                .pattern(" # ")
                .define('X', itemFromName("minecraft:rail"))
                .define('#', itemFromName("minecraft:redstone"))
                .group("stevescarts")
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output, recipeFolder(itemFromName("stevescarts:blockjunction"), "block"));


        shaped(RecipeCategory.MISC, itemFromName("stevescarts:component_galgadorian_metal"), 9)
                .pattern("X")
                .define('X', itemFromName("stevescarts:galgadorian_metal"))
                .group("stevescarts")
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output, recipeFolder(itemFromName("stevescarts:component_galgadorian_metal"), "block"));


        shaped(RecipeCategory.MISC, itemFromName("minecraft:birch_planks"), 2)
                .pattern("X")
                .define('X', itemFromName("stevescarts:component_birch_log"))
                .group("stevescarts")
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output, recipeFolder(itemFromName("minecraft:birch_planks"), "component"));


        shaped(RecipeCategory.MISC, itemFromName("stevescarts:module_track_remover"))
                .pattern("###")
                .pattern("# #")
                .pattern("#  ")
                .define('#', itemFromName("minecraft:iron_ingot"))
                .group("stevescarts")
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output, recipeFolder(itemFromName("stevescarts:module_track_remover"), "module"));


        shaped(RecipeCategory.MISC, itemFromName("stevescarts:blockdistributor"), 1)
                .pattern("X#X")
                .pattern("#R#")
                .pattern("X#X")
                .define('X', ItemTags.STONE_CRAFTING_MATERIALS)
                .define('#', itemFromName("stevescarts:component_simple_pcb"))
                .define('R', itemFromName("minecraft:redstone"))
                .group("stevescarts")
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output, recipeFolder(itemFromName("stevescarts:blockdistributor"), "block"));


        shaped(RecipeCategory.MISC, itemFromName("stevescarts:module_entity_detector_animal"))
                .pattern("#")
                .pattern("X")
                .define('X', itemFromName("stevescarts:component_empty_disk"))
                .define('#', itemFromName("minecraft:porkchop"))
                .group("stevescarts")
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output, recipeFolder(itemFromName("stevescarts:module_entity_detector_animal"), "module"));


        shaped(RecipeCategory.MISC, itemFromName("stevescarts:component_entity_analyzer"))
                .pattern("#X#")
                .pattern("#P#")
                .pattern("###")
                .define('P', itemFromName("stevescarts:component_simple_pcb"))
                .define('X', itemFromName("minecraft:redstone"))
                .define('#', itemFromName("minecraft:iron_ingot"))
                .group("stevescarts")
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output, recipeFolder(itemFromName("stevescarts:component_entity_analyzer"), "component"));


        shaped(RecipeCategory.MISC, itemFromName("stevescarts:component_fuse"), 12)
                .pattern("#")
                .pattern("#")
                .pattern("#")
                .define('#', itemFromName("minecraft:string"))
                .group("stevescarts")
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output, recipeFolder(itemFromName("stevescarts:component_fuse"), "component"));


        shaped(RecipeCategory.MISC, itemFromName("stevescarts:component_iron_blade"), 4)
                .pattern(" # ")
                .pattern("XXX")
                .pattern(" X ")
                .define('X', itemFromName("minecraft:iron_ingot"))
                .define('#', itemFromName("minecraft:shears"))
                .group("stevescarts")
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output, recipeFolder(itemFromName("stevescarts:component_iron_blade"), "component"));


        shaped(RecipeCategory.MISC, itemFromName("stevescarts:component_huge_dynamic_pane"))
                .pattern("###")
                .pattern("#X#")
                .pattern("###")
                .define('#', itemFromName("stevescarts:component_dynamic_pane"))
                .define('X', itemFromName("stevescarts:component_simple_pcb"))
                .group("stevescarts")
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output, recipeFolder(itemFromName("stevescarts:component_huge_dynamic_pane"), "component"));


        shaped(RecipeCategory.MISC, itemFromName("stevescarts:module_top_sctank"))
                .pattern("###")
                .pattern("XCX")
                .pattern("###")
                .define('X', itemFromName("stevescarts:component_sctank_pane"))
                .define('#', itemFromName("stevescarts:component_huge_sctank_pane"))
                .define('C', itemFromName("stevescarts:component_sctank_valve"))
                .group("stevescarts")
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output, recipeFolder(itemFromName("stevescarts:module_top_sctank"), "module"));


        shaped(RecipeCategory.MISC, itemFromName("stevescarts:module_front_sctank"))
                .pattern("X#X")
                .pattern("XCX")
                .pattern("###")
                .define('X', itemFromName("stevescarts:component_sctank_pane"))
                .define('#', itemFromName("stevescarts:component_large_sctank_pane"))
                .define('C', itemFromName("stevescarts:component_sctank_valve"))
                .group("stevescarts")
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output, recipeFolder(itemFromName("stevescarts:module_front_sctank"), "module"));


        shaped(RecipeCategory.MISC, itemFromName("stevescarts:module_galgadorian_farmer"))
                .pattern("DDD")
                .pattern(" I ")
                .pattern("#X#")
                .define('I', itemFromName("stevescarts:component_reinforced_metal"))
                .define('X', itemFromName("minecraft:gold_ingot"))
                .define('#', itemFromName("stevescarts:component_advanced_pcb"))
                .define('D', itemFromName("stevescarts:component_galgadorian_metal"))
                .group("stevescarts")
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output, recipeFolder(itemFromName("stevescarts:module_galgadorian_farmer"), "module"));


        shaped(RecipeCategory.MISC, itemFromName("stevescarts:module_information_provider"))
                .pattern("XXX")
                .pattern("IGI")
                .pattern("#S#")
                .define('X', itemFromName("minecraft:glass_pane"))
                .define('#', itemFromName("stevescarts:component_simple_pcb"))
                .define('S', itemFromName("minecraft:oak_sign"))
                .define('G', itemFromName("minecraft:glowstone_dust"))
                .define('I', itemFromName("minecraft:iron_ingot"))
                .group("stevescarts")
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output, recipeFolder(itemFromName("stevescarts:module_information_provider"), "module"));


        shaped(RecipeCategory.MISC, itemFromName("minecraft:oak_planks"), 2)
                .pattern("X")
                .define('X', itemFromName("stevescarts:component_oak_log"))
                .group("stevescarts")
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output, recipeFolder(itemFromName("minecraft:oak_planks"), "component"));


        shaped(RecipeCategory.MISC, itemFromName("stevescarts:component_raw_hardener"), 2)
                .pattern("# #")
                .pattern(" X ")
                .pattern("# #")
                .define('X', itemFromName("minecraft:diamond"))
                .define('#', itemFromName("minecraft:obsidian"))
                .group("stevescarts")
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output, recipeFolder(itemFromName("stevescarts:component_raw_hardener"), "component"));


        shaped(RecipeCategory.MISC, itemFromName("stevescarts:module_side_tanks"))
                .pattern("#X#")
                .pattern("LCL")
                .pattern("#X#")
                .define('X', itemFromName("stevescarts:component_sctank_pane"))
                .define('L', itemFromName("stevescarts:component_large_sctank_pane"))
                .define('#', itemFromName("stevescarts:component_huge_sctank_pane"))
                .define('C', itemFromName("stevescarts:component_sctank_valve"))
                .group("stevescarts")
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output, recipeFolder(itemFromName("stevescarts:module_side_tanks"), "module"));


        shaped(RecipeCategory.MISC, itemFromName("stevescarts:module_drill_intelligence"))
                .pattern("XXX")
                .pattern("I#I")
                .pattern("#R#")
                .define('#', itemFromName("stevescarts:component_advanced_pcb"))
                .define('X', itemFromName("minecraft:gold_ingot"))
                .define('R', itemFromName("minecraft:redstone"))
                .define('I', itemFromName("minecraft:iron_ingot"))
                .group("stevescarts")
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output, recipeFolder(itemFromName("stevescarts:module_drill_intelligence"), "module"));


        shaped(RecipeCategory.MISC, itemFromName("stevescarts:component_speed_handle"))
                .pattern("  B")
                .pattern("XH ")
                .pattern("#X ")
                .define('B', itemFromName("minecraft:blue_dye"))
                .define('X', itemFromName("minecraft:gold_ingot"))
                .define('#', itemFromName("minecraft:redstone"))
                .define('H', itemFromName("stevescarts:component_refined_handle"))
                .group("stevescarts")
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output, recipeFolder(itemFromName("stevescarts:component_speed_handle"), "component"));


        shaped(RecipeCategory.MISC, itemFromName("stevescarts:module_divine_shield"))
                .pattern("O#O")
                .pattern("#X#")
                .pattern("O#O")
                .define('O', itemFromName("minecraft:obsidian"))
                .define('X', itemFromName("minecraft:diamond_block"))
                .define('#', itemFromName("stevescarts:component_refined_hardener"))
                .group("stevescarts")
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output, recipeFolder(itemFromName("stevescarts:module_divine_shield"), "module"));


        shaped(RecipeCategory.MISC, itemFromName("stevescarts:module_height_controller"))
                .pattern(" C ")
                .pattern("#X#")
                .pattern("###")
                .define('X', itemFromName("stevescarts:component_simple_pcb"))
                .define('#', itemFromName("minecraft:paper"))
                .define('C', itemFromName("minecraft:compass"))
                .group("stevescarts")
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output, recipeFolder(itemFromName("stevescarts:module_height_controller"), "module"));


        shaped(RecipeCategory.MISC, itemFromName("stevescarts:component_green_pigment"))
                .pattern(" # ")
                .pattern("XXX")
                .pattern(" # ")
                .define('X', itemFromName("minecraft:green_dye"))
                .define('#', itemFromName("minecraft:glowstone_dust"))
                .group("stevescarts")
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output, recipeFolder(itemFromName("stevescarts:component_green_pigment"), "component"));


        shaped(RecipeCategory.MISC, itemFromName("stevescarts:component_dynamic_pane"))
                .pattern("#")
                .pattern("X")
                .define('#', itemFromName("stevescarts:component_iron_pane"))
                .define('X', itemFromName("minecraft:redstone"))
                .group("stevescarts")
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output, recipeFolder(itemFromName("stevescarts:component_dynamic_pane"), "component"));


        shaped(RecipeCategory.MISC, itemFromName("stevescarts:component_cleaning_core"))
                .pattern("C#C")
                .pattern("XXX")
                .pattern("#X#")
                .define('C', itemFromName("stevescarts:component_cleaning_fan"))
                .define('X', itemFromName("stevescarts:component_cleaning_tube"))
                .define('#', itemFromName("minecraft:iron_ingot"))
                .group("stevescarts")
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output, recipeFolder(itemFromName("stevescarts:component_cleaning_core"), "component"));


        shaped(RecipeCategory.MISC, itemFromName("stevescarts:component_huge_chest_pane"))
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .define('#', itemFromName("stevescarts:component_chest_pane"))
                .group("stevescarts")
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output, recipeFolder(itemFromName("stevescarts:component_huge_chest_pane"), "component"));


        shaped(RecipeCategory.MISC, itemFromName("stevescarts:component_chest_lock"), 8)
                .pattern("X")
                .pattern("#")
                .define('#', itemFromName("minecraft:iron_ingot"))
                .define('X', ItemTags.STONE_CRAFTING_MATERIALS)
                .group("stevescarts")
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output, recipeFolder(itemFromName("stevescarts:component_chest_lock"), "component"));


        shaped(RecipeCategory.MISC, itemFromName("stevescarts:upgrade_batteries"))
                .pattern("XXX")
                .pattern("XDX")
                .pattern("X#X")
                .define('D', itemFromName("minecraft:diamond"))
                .define('X', itemFromName("minecraft:redstone"))
                .define('#', itemFromName("stevescarts:component_blank_upgrade"))
                .group("stevescarts")
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output, recipeFolder(itemFromName("stevescarts:upgrade_batteries"), "upgrade"));


        shaped(RecipeCategory.MISC, itemFromName("stevescarts:module_galgadorian_farmer"))
                .pattern("DDD")
                .pattern(" # ")
                .pattern(" X ")
                .define('X', itemFromName("stevescarts:component_simple_pcb"))
                .define('#', itemFromName("stevescarts:module_basic_farmer"))
                .define('D', itemFromName("stevescarts:component_galgadorian_metal"))
                .group("stevescarts")
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output, recipeFolder(itemFromName("stevescarts:module_galgadorian_farmer"), "module_alt"));


        shaped(RecipeCategory.MISC, itemFromName("stevescarts:module_advanced_thermal_engine"))
                .pattern("OOO")
                .pattern("#X#")
                .pattern("P P")
                .define('O', itemFromName("minecraft:nether_brick"))
                .define('#', itemFromName("stevescarts:component_reinforced_metal"))
                .define('X', itemFromName("stevescarts:module_thermal_engine"))
                .define('P', itemFromName("minecraft:piston"))
                .group("stevescarts")
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output, recipeFolder(itemFromName("stevescarts:module_advanced_thermal_engine"), "module"));


        shaped(RecipeCategory.MISC, itemFromName("stevescarts:component_blade_arm"))
                .pattern("# #")
                .pattern(" X ")
                .pattern("# #")
                .define('X', itemFromName("minecraft:iron_ingot"))
                .define('#', itemFromName("stevescarts:component_iron_blade"))
                .group("stevescarts")
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output, recipeFolder(itemFromName("stevescarts:component_blade_arm"), "component"));


        shaped(RecipeCategory.MISC, itemFromName("stevescarts:component_large_dynamic_pane"))
                .pattern(" # ")
                .pattern("#X#")
                .pattern(" # ")
                .define('#', itemFromName("stevescarts:component_dynamic_pane"))
                .define('X', itemFromName("minecraft:redstone"))
                .group("stevescarts")
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output, recipeFolder(itemFromName("stevescarts:component_large_dynamic_pane"), "component"));


        shaped(RecipeCategory.MISC, itemFromName("stevescarts:component_galgadorian_wheels"))
                .pattern(" X ")
                .pattern("X#X")
                .pattern(" X ")
                .define('X', itemFromName("stevescarts:component_reinforced_metal"))
                .define('#', itemFromName("stevescarts:component_galgadorian_metal"))
                .group("stevescarts")
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output, recipeFolder(itemFromName("stevescarts:component_galgadorian_wheels"), "component"));


        shaped(RecipeCategory.MISC, itemFromName("stevescarts:module_coal_engine"))
                .pattern("###")
                .pattern("#X#")
                .pattern("P P")
                .define('X', itemFromName("minecraft:furnace"))
                .define('P', itemFromName("minecraft:piston"))
                .define('#', itemFromName("minecraft:iron_ingot"))
                .group("stevescarts")
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output, recipeFolder(itemFromName("stevescarts:module_coal_engine"), "module"));


        shaped(RecipeCategory.MISC, itemFromName("stevescarts:upgrade_power_crystal"))
                .pattern("DXD")
                .pattern("XEX")
                .pattern("D#D")
                .define('D', itemFromName("minecraft:diamond"))
                .define('X', itemFromName("minecraft:glowstone_dust"))
                .define('E', itemFromName("minecraft:emerald_block"))
                .define('#', itemFromName("stevescarts:upgrade_batteries"))
                .group("stevescarts")
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output, recipeFolder(itemFromName("stevescarts:upgrade_power_crystal"), "upgrade"));


        shaped(RecipeCategory.MISC, itemFromName("stevescarts:component_cleaning_fan"))
                .pattern("#X#")
                .pattern("X X")
                .pattern("#X#")
                .define('X', itemFromName("minecraft:redstone"))
                .define('#', itemFromName("minecraft:iron_bars"))
                .group("stevescarts")
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output, recipeFolder(itemFromName("stevescarts:component_cleaning_fan"), "component"));


        shaped(RecipeCategory.MISC, itemFromName("stevescarts:module_experience_bank"))
                .pattern(" X ")
                .pattern("GEG")
                .pattern("#C#")
                .define('E', itemFromName("minecraft:emerald"))
                .define('#', itemFromName("stevescarts:component_simple_pcb"))
                .define('C', itemFromName("minecraft:cauldron"))
                .define('G', itemFromName("minecraft:glowstone_dust"))
                .define('X', itemFromName("minecraft:redstone"))
                .group("stevescarts")
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output, recipeFolder(itemFromName("stevescarts:module_experience_bank"), "module"));


        shaped(RecipeCategory.MISC, itemFromName("stevescarts:component_stabilized_metal"), 5)
                .pattern("#M#")
                .pattern("###")
                .pattern("XXX")
                .define('X', itemFromName("stevescarts:component_refined_hardener"))
                .define('#', itemFromName("minecraft:iron_ingot"))
                .define('M', itemFromName("stevescarts:component_hardened_mesh"))
                .group("stevescarts")
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output, recipeFolder(itemFromName("stevescarts:component_stabilized_metal"), "component"));


        shaped(RecipeCategory.MISC, itemFromName("stevescarts:module_entity_detector_bat"))
                .pattern("#")
                .pattern("X")
                .define('X', itemFromName("stevescarts:component_empty_disk"))
                .define('#', itemFromName("minecraft:pumpkin"))
                .group("stevescarts")
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output, recipeFolder(itemFromName("stevescarts:module_entity_detector_bat"), "module"));


        shaped(RecipeCategory.MISC, itemFromName("stevescarts:module_reinforced_hull"))
                .pattern("# #")
                .pattern("###")
                .pattern("X X")
                .define('#', itemFromName("stevescarts:component_reinforced_metal"))
                .define('X', itemFromName("stevescarts:component_reinforced_wheels"))
                .group("stevescarts")
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output, recipeFolder(itemFromName("stevescarts:module_reinforced_hull"), "module"));


        shaped(RecipeCategory.MISC, itemFromName("stevescarts:module_large_railer"))
                .pattern("###")
                .pattern("XRX")
                .pattern("###")
                .define('R', itemFromName("minecraft:rail"))
                .define('#', itemFromName("minecraft:iron_ingot"))
                .define('X', itemFromName("stevescarts:module_railer"))
                .group("stevescarts")
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output, recipeFolder(itemFromName("stevescarts:module_large_railer"), "module"));


        shaped(RecipeCategory.MISC, itemFromName("stevescarts:component_advanced_pcb"))
                .pattern("#X#")
                .pattern("PXP")
                .pattern("#X#")
                .define('X', itemFromName("minecraft:iron_ingot"))
                .define('P', itemFromName("stevescarts:component_simple_pcb"))
                .define('#', itemFromName("minecraft:redstone"))
                .group("stevescarts")
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output, recipeFolder(itemFromName("stevescarts:component_advanced_pcb"), "component"));


        shaped(RecipeCategory.MISC, itemFromName("stevescarts:module_melter"))
                .pattern("O#O")
                .pattern("GXG")
                .pattern("O#O")
                .define('O', itemFromName("minecraft:nether_bricks"))
                .define('X', itemFromName("minecraft:furnace"))
                .define('#', itemFromName("minecraft:glowstone"))
                .define('G', itemFromName("minecraft:glowstone_dust"))
                .group("stevescarts")
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output, recipeFolder(itemFromName("stevescarts:module_melter"), "module"));


        shaped(RecipeCategory.MISC, itemFromName("stevescarts:component_glistering_easter_egg"))
                .pattern("###")
                .pattern("#X#")
                .pattern("#B#")
                .define('X', itemFromName("minecraft:gold_nugget"))
                .define('#', itemFromName("minecraft:gunpowder"))
                .define('B', itemFromName("minecraft:blue_dye"))
                .group("stevescarts")
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output, recipeFolder(itemFromName("stevescarts:component_glistering_easter_egg"), "component"));


        shaped(RecipeCategory.MISC, itemFromName("stevescarts:module_power_observer"))
                .pattern(" X ")
                .pattern("IRI")
                .pattern("R#R")
                .define('#', itemFromName("stevescarts:component_advanced_pcb"))
                .define('X', itemFromName("minecraft:piston"))
                .define('R', itemFromName("minecraft:redstone"))
                .define('I', itemFromName("minecraft:iron_ingot"))
                .group("stevescarts")
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output, recipeFolder(itemFromName("stevescarts:module_power_observer"), "module"));


        shaped(RecipeCategory.MISC, itemFromName("stevescarts:component_large_lump_of_galgador"))
                .pattern("XXX")
                .pattern("XXX")
                .pattern("XXX")
                .define('X', itemFromName("stevescarts:component_lump_of_galgador"))
                .group("stevescarts")
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output, recipeFolder(itemFromName("stevescarts:component_large_lump_of_galgador"), "component"));


        shaped(RecipeCategory.MISC, itemFromName("stevescarts:upgrade_module_knowledge"))
                .pattern("BSB")
                .pattern("SES")
                .pattern("X#X")
                .define('E', itemFromName("minecraft:enchanting_table"))
                .define('B', itemFromName("minecraft:book"))
                .define('S', itemFromName("minecraft:bookshelf"))
                .define('X', itemFromName("stevescarts:component_reinforced_metal"))
                .define('#', itemFromName("stevescarts:component_blank_upgrade"))
                .group("stevescarts")
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output, recipeFolder(itemFromName("stevescarts:upgrade_module_knowledge"), "upgrade"));


        shaped(RecipeCategory.MISC, itemFromName("stevescarts:module_galgadorian_wood_cutter"))
                .pattern("###")
                .pattern("#I#")
                .pattern(" X ")
                .define('I', itemFromName("minecraft:iron_ingot"))
                .define('X', itemFromName("stevescarts:module_hardened_wood_cutter"))
                .define('#', itemFromName("stevescarts:component_galgadorian_metal"))
                .group("stevescarts")
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output, recipeFolder(itemFromName("stevescarts:module_galgadorian_wood_cutter"), "module"));


        shaped(RecipeCategory.MISC, itemFromName("stevescarts:component_chocolate_easter_egg"))
                .pattern("#S#")
                .pattern("#X#")
                .pattern("#S#")
                .define('X', itemFromName("minecraft:egg"))
                .define('#', itemFromName("minecraft:cocoa_beans"))
                .define('S', itemFromName("minecraft:sugar"))
                .group("stevescarts")
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output, recipeFolder(itemFromName("stevescarts:component_chocolate_easter_egg"), "component"));


        shaped(RecipeCategory.MISC, itemFromName("stevescarts:module_top_chest"))
                .pattern("###")
                .pattern("XCX")
                .pattern("###")
                .define('X', itemFromName("stevescarts:component_chest_pane"))
                .define('#', itemFromName("stevescarts:component_huge_chest_pane"))
                .define('C', itemFromName("stevescarts:component_chest_lock"))
                .group("stevescarts")
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output, recipeFolder(itemFromName("stevescarts:module_top_chest"), "module"));


        shaped(RecipeCategory.MISC, itemFromName("stevescarts:component_pipe"))
                .pattern("XXX")
                .pattern("#  ")
                .define('X', ItemTags.STONE_CRAFTING_MATERIALS)
                .define('#', itemFromName("minecraft:iron_ingot"))
                .group("stevescarts")
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output, recipeFolder(itemFromName("stevescarts:component_pipe"), "component"));


        shaped(RecipeCategory.MISC, itemFromName("stevescarts:component_liquid_cleaning_core"))
                .pattern("C#C")
                .pattern("XXX")
                .pattern("#X#")
                .define('C', itemFromName("stevescarts:component_cleaning_fan"))
                .define('X', itemFromName("stevescarts:component_liquid_cleaning_tube"))
                .define('#', itemFromName("minecraft:iron_ingot"))
                .group("stevescarts")
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output, recipeFolder(itemFromName("stevescarts:component_liquid_cleaning_core"), "component"));


        shaped(RecipeCategory.MISC, itemFromName("stevescarts:module_liquid_cleaner"))
                .pattern("#X#")
                .pattern("# #")
                .pattern("# #")
                .define('X', itemFromName("stevescarts:component_liquid_cleaning_core"))
                .define('#', itemFromName("stevescarts:component_liquid_cleaning_tube"))
                .group("stevescarts")
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output, recipeFolder(itemFromName("stevescarts:module_liquid_cleaner"), "module"));


        shaped(RecipeCategory.MISC, itemFromName("stevescarts:component_liquid_cleaning_tube"), 2)
                .pattern("#X#")
                .pattern("#X#")
                .pattern("#X#")
                .define('#', itemFromName("minecraft:green_dye"))
                .define('X', itemFromName("minecraft:iron_ingot"))
                .group("stevescarts")
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output, recipeFolder(itemFromName("stevescarts:component_liquid_cleaning_tube"), "component"));


        shaped(RecipeCategory.MISC, itemFromName("stevescarts:blockcartassembler"), 1)
                .pattern("X#X")
                .pattern("#X#")
                .pattern("R#R")
                .define('#', itemFromName("minecraft:stone"))
                .define('X', itemFromName("minecraft:iron_ingot"))
                .define('R', itemFromName("stevescarts:component_simple_pcb"))
                .group("stevescarts")
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output, recipeFolder(itemFromName("stevescarts:blockcartassembler"), "block"));


        shaped(RecipeCategory.MISC, itemFromName("stevescarts:component_shooting_station"))
                .pattern("# #")
                .pattern("#G#")
                .pattern("DXD")
                .define('X', itemFromName("stevescarts:component_simple_pcb"))
                .define('#', itemFromName("minecraft:redstone"))
                .define('G', itemFromName("minecraft:gold_ingot"))
                .define('D', itemFromName("minecraft:dispenser"))
                .group("stevescarts")
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output, recipeFolder(itemFromName("stevescarts:component_shooting_station"), "component"));


        shaped(RecipeCategory.MISC, itemFromName("stevescarts:module_cake_server"))
                .pattern(" C ")
                .pattern("XXX")
                .pattern(" # ")
                .define('X', ItemTags.WOODEN_SLABS)
                .define('C', itemFromName("minecraft:cake"))
                .define('#', itemFromName("stevescarts:component_simple_pcb"))
                .group("stevescarts")
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output, recipeFolder(itemFromName("stevescarts:module_cake_server"), "module"));


        shaped(RecipeCategory.MISC, itemFromName("stevescarts:module_ore_extractor"))
                .pattern("T T")
                .pattern("XGX")
                .pattern("R#R")
                .define('#', itemFromName("stevescarts:component_advanced_pcb"))
                .define('X', itemFromName("stevescarts:component_eye_of_galgador"))
                .define('T', itemFromName("minecraft:redstone_torch"))
                .define('R', itemFromName("minecraft:quartz"))
                .define('G', itemFromName("stevescarts:component_galgadorian_metal"))
                .group("stevescarts")
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output, recipeFolder(itemFromName("stevescarts:module_ore_extractor"), "module"));


        shaped(RecipeCategory.MISC, itemFromName("stevescarts:module_advanced_control_system"))
                .pattern(" D ")
                .pattern("XWX")
                .pattern("##H")
                .define('D', itemFromName("stevescarts:component_graphical_interface"))
                .define('W', itemFromName("stevescarts:component_wheel"))
                .define('H', itemFromName("stevescarts:component_speed_handle"))
                .define('#', itemFromName("minecraft:iron_ingot"))
                .define('X', itemFromName("minecraft:redstone"))
                .group("stevescarts")
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output, recipeFolder(itemFromName("stevescarts:module_advanced_control_system"), "module"));


        shaped(RecipeCategory.MISC, itemFromName("stevescarts:module_crafter"))
                .pattern("X")
                .pattern("#")
                .define('X', itemFromName("stevescarts:component_simple_pcb"))
                .define('#', itemFromName("minecraft:crafting_table"))
                .group("stevescarts")
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output, recipeFolder(itemFromName("stevescarts:module_crafter"), "module"));


        shaped(RecipeCategory.MISC, itemFromName("stevescarts:upgrade_generic_engine"))
                .pattern(" X ")
                .pattern("PFP")
                .pattern("I#I")
                .define('I', itemFromName("minecraft:iron_ingot"))
                .define('F', itemFromName("minecraft:furnace"))
                .define('P', itemFromName("minecraft:piston"))
                .define('X', itemFromName("stevescarts:component_simple_pcb"))
                .define('#', itemFromName("stevescarts:component_blank_upgrade"))
                .group("stevescarts")
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output, recipeFolder(itemFromName("stevescarts:upgrade_generic_engine"), "upgrade"));


        shaped(RecipeCategory.MISC, itemFromName("stevescarts:upgrade_experienced_assembler"))
                .pattern("SBS")
                .pattern("XLX")
                .pattern("X#X")
                .define('B', itemFromName("minecraft:enchanted_book"))
                .define('L', itemFromName("stevescarts:component_advanced_pcb"))
                .define('S', itemFromName("stevescarts:component_simple_pcb"))
                .define('X', itemFromName("stevescarts:component_reinforced_metal"))
                .define('#', itemFromName("stevescarts:component_blank_upgrade"))
                .group("stevescarts")
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output, recipeFolder(itemFromName("stevescarts:upgrade_experienced_assembler"), "upgrade"));


        shaped(RecipeCategory.MISC, itemFromName("stevescarts:component_chest_lock"), 8)
                .pattern("#")
                .pattern("X")
                .define('#', itemFromName("minecraft:iron_ingot"))
                .define('X', ItemTags.STONE_CRAFTING_MATERIALS)
                .group("stevescarts")
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output, recipeFolder(itemFromName("stevescarts:component_chest_lock"), "component_alt"));


        shaped(RecipeCategory.MISC, itemFromName("stevescarts:component_saw_blade"))
                .pattern("##X")
                .define('X', itemFromName("minecraft:diamond"))
                .define('#', itemFromName("minecraft:iron_ingot"))
                .group("stevescarts")
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output, recipeFolder(itemFromName("stevescarts:component_saw_blade"), "component"));


        shaped(RecipeCategory.MISC, itemFromName("stevescarts:module_galgadorian_wood_cutter"))
                .pattern("###")
                .pattern("#I#")
                .pattern(" X ")
                .define('I', itemFromName("stevescarts:component_reinforced_metal"))
                .define('X', itemFromName("stevescarts:component_wood_cutting_core"))
                .define('#', itemFromName("stevescarts:component_galgadorian_saw_blade"))
                .group("stevescarts")
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output, recipeFolder(itemFromName("stevescarts:module_galgadorian_wood_cutter"), "module_alt"));


        shaped(RecipeCategory.MISC, itemFromName("stevescarts:component_hardened_mesh"))
                .pattern("#X#")
                .pattern("X#X")
                .pattern("#X#")
                .define('X', itemFromName("stevescarts:component_refined_hardener"))
                .define('#', itemFromName("minecraft:iron_bars"))
                .group("stevescarts")
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output, recipeFolder(itemFromName("stevescarts:component_hardened_mesh"), "component"));


        shaped(RecipeCategory.MISC, itemFromName("stevescarts:module_torch_placer"))
                .pattern("X X")
                .pattern("# #")
                .pattern("###")
                .define('X', itemFromName("stevescarts:component_tri_torch"))
                .define('#', itemFromName("minecraft:iron_ingot"))
                .group("stevescarts")
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output, recipeFolder(itemFromName("stevescarts:module_torch_placer"), "module"));


        shaped(RecipeCategory.MISC, itemFromName("stevescarts:module_mechanical_pig"))
                .pattern("# #")
                .pattern("###")
                .pattern("X X")
                .define('#', itemFromName("minecraft:porkchop"))
                .define('X', itemFromName("stevescarts:component_iron_wheels"))
                .group("stevescarts")
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output, recipeFolder(itemFromName("stevescarts:module_mechanical_pig"), "module"));


        shaped(RecipeCategory.MISC, itemFromName("stevescarts:module_smelter"))
                .pattern("X")
                .pattern("#")
                .define('X', itemFromName("stevescarts:component_simple_pcb"))
                .define('#', itemFromName("minecraft:furnace"))
                .group("stevescarts")
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output, recipeFolder(itemFromName("stevescarts:module_smelter"), "module"));


        shaped(RecipeCategory.MISC, itemFromName("stevescarts:module_advanced_sctank"))
                .pattern("XXX")
                .pattern("X#X")
                .pattern("XXX")
                .define('X', itemFromName("stevescarts:component_huge_sctank_pane"))
                .define('#', itemFromName("stevescarts:component_sctank_valve"))
                .group("stevescarts")
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output, recipeFolder(itemFromName("stevescarts:module_advanced_sctank"), "module"));


        shaped(RecipeCategory.MISC, itemFromName("stevescarts:module_thermal_engine"))
                .pattern("OOO")
                .pattern("#X#")
                .pattern("P P")
                .define('O', itemFromName("minecraft:nether_bricks"))
                .define('#', itemFromName("minecraft:obsidian"))
                .define('X', itemFromName("minecraft:furnace"))
                .define('P', itemFromName("minecraft:piston"))
                .group("stevescarts")
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output, recipeFolder(itemFromName("stevescarts:module_thermal_engine"), "module"));


        shaped(RecipeCategory.MISC, itemFromName("stevescarts:component_reinforced_wheels"))
                .pattern(" X ")
                .pattern("X#X")
                .pattern(" X ")
                .define('#', itemFromName("stevescarts:component_reinforced_metal"))
                .define('X', itemFromName("minecraft:iron_ingot"))
                .group("stevescarts")
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output, recipeFolder(itemFromName("stevescarts:component_reinforced_wheels"), "component"));


        shaped(RecipeCategory.MISC, itemFromName("stevescarts:component_hardened_saw_blade"))
                .pattern("##X")
                .define('X', itemFromName("stevescarts:component_reinforced_metal"))
                .define('#', itemFromName("minecraft:iron_ingot"))
                .group("stevescarts")
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output, recipeFolder(itemFromName("stevescarts:component_hardened_saw_blade"), "component"));


        shaped(RecipeCategory.MISC, itemFromName("stevescarts:component_wheel"))
                .pattern("#X#")
                .pattern("X#X")
                .pattern(" X ")
                .define('X', itemFromName("minecraft:stick"))
                .define('#', itemFromName("minecraft:iron_ingot"))
                .group("stevescarts")
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output, recipeFolder(itemFromName("stevescarts:component_wheel"), "component"));


        shaped(RecipeCategory.MISC, itemFromName("stevescarts:module_entity_detector_player"))
                .pattern("#")
                .pattern("X")
                .define('X', itemFromName("stevescarts:component_empty_disk"))
                .define('#', itemFromName("minecraft:diamond"))
                .group("stevescarts")
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output, recipeFolder(itemFromName("stevescarts:module_entity_detector_player"), "module"));


        shaped(RecipeCategory.MISC, itemFromName("stevescarts:module_solar_engine"))
                .pattern("I#I")
                .pattern("#X#")
                .pattern("P#P")
                .define('X', itemFromName("stevescarts:component_advanced_pcb"))
                .define('#', itemFromName("stevescarts:component_solar_panel_component"))
                .define('P', itemFromName("minecraft:piston"))
                .define('I', itemFromName("minecraft:iron_ingot"))
                .group("stevescarts")
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output, recipeFolder(itemFromName("stevescarts:module_solar_engine"), "module"));


        shaped(RecipeCategory.MISC, itemFromName("stevescarts:component_warm_hat"))
                .pattern(" #X")
                .pattern("#D#")
                .pattern("###")
                .define('D', itemFromName("minecraft:diamond"))
                .define('X', itemFromName("minecraft:white_wool"))
                .define('#', itemFromName("minecraft:red_wool"))
                .group("stevescarts")
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output, recipeFolder(itemFromName("stevescarts:component_warm_hat"), "component"));


        shaped(RecipeCategory.MISC, itemFromName("stevescarts:module_tiny_coal_engine"))
                .pattern("#X#")
                .pattern(" P ")
                .define('X', itemFromName("minecraft:furnace"))
                .define('P', itemFromName("minecraft:piston"))
                .define('#', itemFromName("minecraft:iron_ingot"))
                .group("stevescarts")
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output, recipeFolder(itemFromName("stevescarts:module_tiny_coal_engine"), "module"));


        shaped(RecipeCategory.MISC, itemFromName("stevescarts:component_burning_easter_egg"))
                .pattern("#B#")
                .pattern("#X#")
                .pattern("RMY")
                .define('X', itemFromName("minecraft:egg"))
                .define('R', itemFromName("minecraft:red_dye"))
                .define('Y', itemFromName("minecraft:yellow_dye"))
                .define('#', itemFromName("minecraft:blaze_powder"))
                .define('B', itemFromName("minecraft:blaze_rod"))
                .define('M', itemFromName("minecraft:magma_cream"))
                .group("stevescarts")
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output, recipeFolder(itemFromName("stevescarts:component_burning_easter_egg"), "component"));


        shaped(RecipeCategory.MISC, itemFromName("stevescarts:component_enhanced_galgadorian_metal"), 9)
                .pattern("X")
                .define('X', itemFromName("stevescarts:enhanced_galgadorian_metal"))
                .group("stevescarts")
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output, recipeFolder(itemFromName("stevescarts:component_enhanced_galgadorian_metal"), "block"));


        shaped(RecipeCategory.MISC, itemFromName("stevescarts:component_graphical_interface"))
                .pattern("GDG")
                .pattern("XPX")
                .pattern("#X#")
                .define('D', itemFromName("minecraft:diamond"))
                .define('X', itemFromName("minecraft:glass_pane"))
                .define('#', itemFromName("minecraft:redstone"))
                .define('G', itemFromName("minecraft:gold_ingot"))
                .define('P', itemFromName("stevescarts:component_simple_pcb"))
                .group("stevescarts")
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output, recipeFolder(itemFromName("stevescarts:component_graphical_interface"), "component"));


        shaped(RecipeCategory.MISC, itemFromName("stevescarts:component_red_gift_ribbon"))
                .pattern("###")
                .pattern("#X#")
                .pattern("###")
                .define('#', itemFromName("minecraft:string"))
                .define('X', itemFromName("minecraft:red_dye"))
                .group("stevescarts")
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output, recipeFolder(itemFromName("stevescarts:component_red_gift_ribbon"), "component"));


        shaped(RecipeCategory.MISC, itemFromName("stevescarts:module_basic_farmer"))
                .pattern("DDD")
                .pattern(" I ")
                .pattern("#X#")
                .define('I', itemFromName("minecraft:iron_ingot"))
                .define('X', itemFromName("minecraft:gold_ingot"))
                .define('#', itemFromName("stevescarts:component_simple_pcb"))
                .define('D', itemFromName("minecraft:diamond"))
                .group("stevescarts")
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output, recipeFolder(itemFromName("stevescarts:module_basic_farmer"), "module"));


        shaped(RecipeCategory.MISC, itemFromName("stevescarts:module_wooden_hull"))
                .pattern("# #")
                .pattern("###")
                .pattern("X X")
                .define('#', ItemTags.PLANKS)
                .define('X', itemFromName("stevescarts:component_wooden_wheels"))
                .group("stevescarts")
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output, recipeFolder(itemFromName("stevescarts:module_wooden_hull"), "module"));


        shaped(RecipeCategory.MISC, itemFromName("stevescarts:module_entity_detector_villager"))
                .pattern("#")
                .pattern("X")
                .define('X', itemFromName("stevescarts:component_empty_disk"))
                .define('#', itemFromName("minecraft:emerald"))
                .group("stevescarts")
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output, recipeFolder(itemFromName("stevescarts:module_entity_detector_villager"), "module"));


        shaped(RecipeCategory.MISC, itemFromName("stevescarts:module_hardened_wood_cutter"))
                .pattern("###")
                .pattern("#I#")
                .pattern(" X ")
                .define('I', itemFromName("minecraft:iron_ingot"))
                .define('X', itemFromName("stevescarts:module_basic_wood_cutter"))
                .define('#', itemFromName("stevescarts:component_reinforced_metal"))
                .group("stevescarts")
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output, recipeFolder(itemFromName("stevescarts:module_hardened_wood_cutter"), "module"));


        shaped(RecipeCategory.MISC, itemFromName("stevescarts:module_enchanter"))
                .pattern(" G ")
                .pattern("XEX")
                .pattern("R#R")
                .define('#', itemFromName("stevescarts:component_advanced_pcb"))
                .define('X', itemFromName("minecraft:book"))
                .define('E', itemFromName("minecraft:enchanting_table"))
                .define('R', itemFromName("minecraft:redstone"))
                .define('G', itemFromName("stevescarts:component_galgadorian_metal"))
                .group("stevescarts")
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output, recipeFolder(itemFromName("stevescarts:module_enchanter"), "module"));


        shaped(RecipeCategory.MISC, itemFromName("stevescarts:module_note_sequencer"))
                .pattern("# #")
                .pattern("#J#")
                .pattern("XRX")
                .define('X', ItemTags.PLANKS)
                .define('#', itemFromName("minecraft:note_block"))
                .define('J', itemFromName("minecraft:jukebox"))
                .define('R', itemFromName("minecraft:redstone"))
                .group("stevescarts")
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output, recipeFolder(itemFromName("stevescarts:module_note_sequencer"), "module"));


        shaped(RecipeCategory.MISC, itemFromName("stevescarts:module_bridge_builder"))
                .pattern(" R ")
                .pattern("X#X")
                .pattern(" P ")
                .define('X', itemFromName("minecraft:bricks"))
                .define('R', itemFromName("minecraft:redstone"))
                .define('P', itemFromName("minecraft:piston"))
                .define('#', itemFromName("stevescarts:component_simple_pcb"))
                .group("stevescarts")
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output, recipeFolder(itemFromName("stevescarts:module_bridge_builder"), "module"));


        shaped(RecipeCategory.MISC, itemFromName("stevescarts:upgrade_cotwo_friendly"))
                .pattern(" P ")
                .pattern("XFX")
                .pattern("C#C")
                .define('P', itemFromName("minecraft:piston"))
                .define('F', ItemTags.WOODEN_FENCES)
                .define('C', itemFromName("stevescarts:component_cleaning_fan"))
                .define('X', itemFromName("stevescarts:component_simple_pcb"))
                .define('#', itemFromName("stevescarts:component_blank_upgrade"))
                .group("stevescarts")
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output, recipeFolder(itemFromName("stevescarts:upgrade_cotwo_friendly"), "upgrade"));


        shaped(RecipeCategory.MISC, itemFromName("stevescarts:module_advanced_crafter"))
                .pattern(" D ")
                .pattern(" # ")
                .pattern("XCX")
                .define('#', itemFromName("stevescarts:component_advanced_pcb"))
                .define('X', itemFromName("stevescarts:component_simple_pcb"))
                .define('D', itemFromName("minecraft:diamond"))
                .define('C', itemFromName("stevescarts:module_crafter"))
                .group("stevescarts")
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output, recipeFolder(itemFromName("stevescarts:module_advanced_crafter"), "module"));


        shaped(RecipeCategory.MISC, itemFromName("stevescarts:blockcargomanager"), 1)
                .pattern("X#X")
                .pattern("#R#")
                .pattern("X#X")
                .define('X', itemFromName("stevescarts:component_large_iron_pane"))
                .define('#', itemFromName("stevescarts:component_huge_iron_pane"))
                .define('R', itemFromName("stevescarts:component_large_dynamic_pane"))
                .group("stevescarts")
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output, recipeFolder(itemFromName("stevescarts:blockcargomanager"), "block"));


        shaped(RecipeCategory.MISC, itemFromName("stevescarts:component_sctank_pane"), 32)
                .pattern("###")
                .pattern("X#X")
                .pattern("###")
                .define('#', itemFromName("minecraft:glass_pane"))
                .define('X', itemFromName("minecraft:glass"))
                .group("stevescarts")
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output, recipeFolder(itemFromName("stevescarts:component_sctank_pane"), "component"));


        shaped(RecipeCategory.MISC, itemFromName("stevescarts:component_large_chest_pane"))
                .pattern("##")
                .pattern("##")
                .define('#', itemFromName("stevescarts:component_chest_pane"))
                .group("stevescarts")
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output, recipeFolder(itemFromName("stevescarts:component_large_chest_pane"), "component"));


        shaped(RecipeCategory.MISC, itemFromName("stevescarts:component_yellow_gift_ribbon"))
                .pattern("###")
                .pattern("#X#")
                .pattern("###")
                .define('#', itemFromName("minecraft:string"))
                .define('X', itemFromName("minecraft:yellow_dye"))
                .group("stevescarts")
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output, recipeFolder(itemFromName("stevescarts:component_yellow_gift_ribbon"), "component"));


        shaped(RecipeCategory.MISC, itemFromName("stevescarts:component_solar_panel_component"))
                .pattern("#R")
                .pattern("X#")
                .define('#', itemFromName("minecraft:glowstone_dust"))
                .define('X', itemFromName("minecraft:iron_ingot"))
                .define('R', itemFromName("minecraft:redstone"))
                .group("stevescarts")
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output, recipeFolder(itemFromName("stevescarts:component_solar_panel_component"), "component"));


        shaped(RecipeCategory.MISC, itemFromName("stevescarts:module_shooter"))
                .pattern("PPP")
                .pattern("PSP")
                .pattern("PPP")
                .define('S', itemFromName("stevescarts:component_shooting_station"))
                .define('P', itemFromName("stevescarts:component_pipe"))
                .group("stevescarts")
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output, recipeFolder(itemFromName("stevescarts:module_shooter"), "module"));


        shaped(RecipeCategory.MISC, itemFromName("stevescarts:blockliquidmanager"), 1)
                .pattern("X#X")
                .pattern("# #")
                .pattern("X#X")
                .define('X', itemFromName("stevescarts:module_advanced_sctank"))
                .define('#', itemFromName("minecraft:iron_ingot"))
                .group("stevescarts")
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output, recipeFolder(itemFromName("stevescarts:blockliquidmanager"), "block"));


        shaped(RecipeCategory.MISC, itemFromName("stevescarts:component_red_pigment"))
                .pattern(" # ")
                .pattern("XXX")
                .pattern(" # ")
                .define('X', itemFromName("minecraft:red_dye"))
                .define('#', itemFromName("minecraft:glowstone_dust"))
                .group("stevescarts")
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output, recipeFolder(itemFromName("stevescarts:component_red_pigment"), "component"));


        shaped(RecipeCategory.MISC, itemFromName("stevescarts:module_internal_sctank"))
                .pattern("XXX")
                .pattern("X#X")
                .pattern("XXX")
                .define('X', itemFromName("stevescarts:component_sctank_pane"))
                .define('#', itemFromName("stevescarts:component_sctank_valve"))
                .group("stevescarts")
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output, recipeFolder(itemFromName("stevescarts:module_internal_sctank"), "module"));


        shaped(RecipeCategory.MISC, itemFromName("minecraft:stick"), 2)
                .pattern("X")
                .define('X', Ingredient.of(itemFromName("stevescarts:component_oak_twig"), itemFromName("stevescarts:component_spruce_twig"), itemFromName("stevescarts:component_birch_twig"), itemFromName("stevescarts:component_jungle_twig")))
                .group("stevescarts")
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output, ResourceKey.create(Registries.RECIPE, ResourceLocation.fromNamespaceAndPath("stevescarts",  "component/stick")));

        shaped(RecipeCategory.MISC, itemFromName("stevescarts:component_iron_pane"), 8)
                .pattern("###")
                .pattern("#X#")
                .pattern("###")
                .define('#', itemFromName("stevescarts:component_chest_pane"))
                .define('X', itemFromName("minecraft:iron_ingot"))
                .group("stevescarts")
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output, recipeFolder(itemFromName("stevescarts:component_iron_pane"), "component"));


        shaped(RecipeCategory.MISC, itemFromName("stevescarts:component_entity_scanner"))
                .pattern("GXG")
                .pattern("#L#")
                .pattern("# #")
                .define('X', itemFromName("stevescarts:component_simple_pcb"))
                .define('#', itemFromName("minecraft:redstone"))
                .define('G', itemFromName("minecraft:gold_ingot"))
                .define('L', itemFromName("stevescarts:component_advanced_pcb"))
                .group("stevescarts")
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output, recipeFolder(itemFromName("stevescarts:component_entity_scanner"), "component"));


        shaped(RecipeCategory.MISC, itemFromName("stevescarts:enhanced_galgadorian_metal"), 1)
                .pattern("XXX")
                .pattern("XXX")
                .pattern("XXX")
                .define('X', itemFromName("stevescarts:component_enhanced_galgadorian_metal"))
                .group("stevescarts")
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output, recipeFolder(itemFromName("stevescarts:enhanced_galgadorian_metal"), "block"));


        shaped(RecipeCategory.MISC, itemFromName("stevescarts:blockactivator"), 1)
                .pattern("OGB")
                .pattern("#I#")
                .pattern("RXR")
                .define('O', itemFromName("minecraft:orange_dye"))
                .define('B', itemFromName("minecraft:blue_dye"))
                .define('#', ItemTags.STONE_CRAFTING_MATERIALS)
                .define('I', itemFromName("minecraft:iron_ingot"))
                .define('G', itemFromName("minecraft:gold_ingot"))
                .define('R', itemFromName("minecraft:redstone"))
                .define('X', itemFromName("stevescarts:component_advanced_pcb"))
                .group("stevescarts")
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output, recipeFolder(itemFromName("stevescarts:blockactivator"), "block"));


        shaped(RecipeCategory.MISC, itemFromName("stevescarts:upgrade_quick_demolisher"))
                .pattern("OXO")
                .pattern("XIX")
                .pattern("O#O")
                .define('I', itemFromName("minecraft:iron_block"))
                .define('O', itemFromName("minecraft:obsidian"))
                .define('X', itemFromName("stevescarts:component_reinforced_metal"))
                .define('#', itemFromName("stevescarts:component_blank_upgrade"))
                .group("stevescarts")
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output, recipeFolder(itemFromName("stevescarts:upgrade_quick_demolisher"), "upgrade"));


        shaped(RecipeCategory.MISC, itemFromName("stevescarts:module_standard_hull"))
                .pattern(" # ")
                .pattern("X X")
                .define('#', itemFromName("minecraft:minecart"))
                .define('X', itemFromName("stevescarts:component_iron_wheels"))
                .group("stevescarts")
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output, recipeFolder(itemFromName("stevescarts:module_standard_hull"), "module"));


        shaped(RecipeCategory.MISC, itemFromName("stevescarts:module_freezer"))
                .pattern("#X#")
                .pattern("XPX")
                .pattern("#X#")
                .define('P', itemFromName("stevescarts:component_simple_pcb"))
                .define('#', itemFromName("minecraft:snow"))
                .define('X', itemFromName("minecraft:water_bucket"))
                .group("stevescarts")
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output, recipeFolder(itemFromName("stevescarts:module_freezer"), "module"));


        shaped(RecipeCategory.MISC, itemFromName("stevescarts:component_chest_pane"), 32)
                .pattern("###")
                .pattern("X#X")
                .pattern("###")
                .define('#', ItemTags.PLANKS)
                .define('X', ItemTags.LOGS)
                .group("stevescarts")
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output, recipeFolder(itemFromName("stevescarts:component_chest_pane"), "component"));


        shaped(RecipeCategory.MISC, itemFromName("stevescarts:upgrade_solar_panel"))
                .pattern("XXX")
                .pattern("DRD")
                .pattern("R#R")
                .define('D', itemFromName("minecraft:diamond"))
                .define('R', itemFromName("minecraft:redstone"))
                .define('X', itemFromName("stevescarts:component_solar_panel_component"))
                .define('#', itemFromName("stevescarts:component_blank_upgrade"))
                .group("stevescarts")
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output, recipeFolder(itemFromName("stevescarts:upgrade_solar_panel"), "upgrade"));


        shaped(RecipeCategory.MISC, itemFromName("stevescarts:module_brake_handle"))
                .pattern("  R")
                .pattern("#S ")
                .pattern("X# ")
                .define('R', itemFromName("minecraft:red_dye"))
                .define('S', itemFromName("stevescarts:component_refined_handle"))
                .define('#', itemFromName("minecraft:iron_ingot"))
                .define('X', itemFromName("minecraft:redstone"))
                .group("stevescarts")
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output, recipeFolder(itemFromName("stevescarts:module_brake_handle"), "module"));


        shaped(RecipeCategory.MISC, itemFromName("stevescarts:upgrade_redstone_control"))
                .pattern("PXP")
                .pattern("TLT")
                .pattern("R#R")
                .define('T', itemFromName("minecraft:redstone_torch"))
                .define('R', itemFromName("minecraft:redstone"))
                .define('X', itemFromName("minecraft:repeater"))
                .define('L', itemFromName("stevescarts:component_advanced_pcb"))
                .define('P', itemFromName("stevescarts:component_simple_pcb"))
                .define('#', itemFromName("stevescarts:component_blank_upgrade"))
                .group("stevescarts")
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output, recipeFolder(itemFromName("stevescarts:upgrade_redstone_control"), "upgrade"));


        shaped(RecipeCategory.MISC, itemFromName("stevescarts:component_sctank_valve"), 8)
                .pattern(" X ")
                .pattern("X#X")
                .pattern(" X ")
                .define('X', itemFromName("minecraft:iron_ingot"))
                .define('#', itemFromName("minecraft:iron_bars"))
                .group("stevescarts")
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output, recipeFolder(itemFromName("stevescarts:component_sctank_valve"), "component"));


        shaped(RecipeCategory.MISC, itemFromName("stevescarts:module_firework_display"))
                .pattern("#D#")
                .pattern("CFC")
                .pattern("XSX")
                .define('X', itemFromName("stevescarts:component_simple_pcb"))
                .define('F', itemFromName("stevescarts:component_fuse"))
                .define('#', ItemTags.WOODEN_FENCES)
                .define('D', itemFromName("minecraft:dispenser"))
                .define('S', itemFromName("minecraft:flint_and_steel"))
                .define('C', itemFromName("minecraft:crafting_table"))
                .group("stevescarts")
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output, recipeFolder(itemFromName("stevescarts:module_firework_display"), "module"));


        shaped(RecipeCategory.MISC, itemFromName("stevescarts:module_basic_wood_cutter"))
                .pattern("###")
                .pattern("#I#")
                .pattern(" X ")
                .define('I', itemFromName("minecraft:iron_ingot"))
                .define('X', itemFromName("stevescarts:component_wood_cutting_core"))
                .define('#', itemFromName("stevescarts:component_saw_blade"))
                .group("stevescarts")
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output, recipeFolder(itemFromName("stevescarts:module_basic_wood_cutter"), "module"));


        shaped(RecipeCategory.MISC, itemFromName("stevescarts:component_huge_sctank_pane"))
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .define('#', itemFromName("stevescarts:component_sctank_pane"))
                .group("stevescarts")
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output, recipeFolder(itemFromName("stevescarts:component_huge_sctank_pane"), "component"));


        shaped(RecipeCategory.MISC, itemFromName("stevescarts:upgrade_entropy"))
                .pattern("GX ")
                .pattern("DLD")
                .pattern(" #G")
                .define('D', itemFromName("minecraft:diamond"))
                .define('L', itemFromName("minecraft:lapis_block"))
                .define('G', itemFromName("stevescarts:component_eye_of_galgador"))
                .define('X', itemFromName("stevescarts:component_reinforced_metal"))
                .define('#', itemFromName("stevescarts:upgrade_quick_demolisher"))
                .group("stevescarts")
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output, recipeFolder(itemFromName("stevescarts:upgrade_entropy"), "upgrade"));


        shaped(RecipeCategory.MISC, itemFromName("stevescarts:module_entity_detector_monster"))
                .pattern("#")
                .pattern("X")
                .define('X', itemFromName("stevescarts:component_empty_disk"))
                .define('#', itemFromName("minecraft:slime_ball"))
                .group("stevescarts")
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output, recipeFolder(itemFromName("stevescarts:module_entity_detector_monster"), "module"));


        shaped(RecipeCategory.MISC, itemFromName("stevescarts:module_galgadorian_drill"))
                .pattern("#X ")
                .pattern("BDX")
                .pattern("#X ")
                .define('D', itemFromName("stevescarts:module_hardened_drill"))
                .define('#', itemFromName("stevescarts:component_galgadorian_metal"))
                .define('X', itemFromName("stevescarts:component_enhanced_galgadorian_metal"))
                .define('B', itemFromName("minecraft:diamond_block"))
                .group("stevescarts")
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output, recipeFolder(itemFromName("stevescarts:module_galgadorian_drill"), "module"));


        shaped(RecipeCategory.MISC, itemFromName("stevescarts:module_pumpkin_chariot"))
                .pattern("# #")
                .pattern("#P#")
                .pattern("X X")
                .define('#', ItemTags.PLANKS)
                .define('X', itemFromName("stevescarts:component_wooden_wheels"))
                .define('P', itemFromName("minecraft:pumpkin"))
                .group("stevescarts")
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output, recipeFolder(itemFromName("stevescarts:module_pumpkin_chariot"), "module"));


        shaped(RecipeCategory.MISC, itemFromName("stevescarts:component_galgadorian_saw_blade"))
                .pattern("##X")
                .define('X', itemFromName("stevescarts:component_galgadorian_metal"))
                .define('#', itemFromName("minecraft:iron_ingot"))
                .group("stevescarts")
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output, recipeFolder(itemFromName("stevescarts:component_galgadorian_saw_blade"), "component"));


        shaped(RecipeCategory.MISC, itemFromName("stevescarts:component_cleaning_tube"), 2)
                .pattern("#X#")
                .pattern("#X#")
                .pattern("#X#")
                .define('#', itemFromName("minecraft:orange_dye"))
                .define('X', itemFromName("minecraft:iron_ingot"))
                .group("stevescarts")
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output, recipeFolder(itemFromName("stevescarts:component_cleaning_tube"), "component"));


        shaped(RecipeCategory.MISC, itemFromName("stevescarts:component_glass_o_magic"))
                .pattern("X#X")
                .pattern("XRX")
                .pattern("XXX")
                .define('X', itemFromName("minecraft:glass_pane"))
                .define('#', itemFromName("minecraft:fermented_spider_eye"))
                .define('R', itemFromName("minecraft:redstone"))
                .group("stevescarts")
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output, recipeFolder(itemFromName("stevescarts:component_glass_o_magic"), "component"));


        shaped(RecipeCategory.MISC, itemFromName("stevescarts:component_wood_cutting_core"))
                .pattern("XXX")
                .pattern("X#X")
                .pattern("XXX")
                .define('X', ItemTags.SAPLINGS)
                .define('#', itemFromName("stevescarts:component_advanced_pcb"))
                .group("stevescarts")
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output, recipeFolder(itemFromName("stevescarts:component_wood_cutting_core"), "component"));


        shaped(RecipeCategory.MISC, itemFromName("stevescarts:module_standard_hull"))
                .pattern("# #")
                .pattern("###")
                .pattern("X X")
                .define('#', itemFromName("minecraft:iron_ingot"))
                .define('X', itemFromName("stevescarts:component_iron_wheels"))
                .group("stevescarts")
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output, recipeFolder(itemFromName("stevescarts:module_standard_hull"), "module_alt"));


        shaped(RecipeCategory.MISC, itemFromName("stevescarts:component_explosive_easter_egg"))
                .pattern("###")
                .pattern("#X#")
                .pattern("#G#")
                .define('X', itemFromName("minecraft:egg"))
                .define('#', itemFromName("minecraft:gunpowder"))
                .define('G', itemFromName("minecraft:green_dye"))
                .group("stevescarts")
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output, recipeFolder(itemFromName("stevescarts:component_explosive_easter_egg"), "component"));


        shaped(RecipeCategory.MISC, itemFromName("stevescarts:component_dynamite"))
                .pattern("#")
                .pattern("X")
                .pattern("X")
                .define('X', itemFromName("minecraft:gunpowder"))
                .define('#', itemFromName("stevescarts:component_fuse"))
                .group("stevescarts")
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output, recipeFolder(itemFromName("stevescarts:component_dynamite"), "component"));


        shaped(RecipeCategory.MISC, itemFromName("stevescarts:component_simple_pcb"))
                .pattern("X#X")
                .pattern("#G#")
                .pattern("X#X")
                .define('X', itemFromName("minecraft:iron_ingot"))
                .define('G', itemFromName("minecraft:gold_ingot"))
                .define('#', itemFromName("minecraft:redstone"))
                .group("stevescarts")
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output, recipeFolder(itemFromName("stevescarts:component_simple_pcb"), "component_alt"));


        shaped(RecipeCategory.MISC, itemFromName("stevescarts:component_iron_wheels"))
                .pattern(" X ")
                .pattern("X#X")
                .pattern(" X ")
                .define('X', itemFromName("minecraft:stick"))
                .define('#', itemFromName("minecraft:iron_ingot"))
                .group("stevescarts")
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output, recipeFolder(itemFromName("stevescarts:component_iron_wheels"), "component"));


        shaped(RecipeCategory.MISC, itemFromName("stevescarts:component_wooden_wheels"))
                .pattern("XXX")
                .pattern("X#X")
                .pattern("XXX")
                .define('X', itemFromName("minecraft:stick"))
                .define('#', ItemTags.PLANKS)
                .group("stevescarts")
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output, recipeFolder(itemFromName("stevescarts:component_wooden_wheels"), "component"));


        shaped(RecipeCategory.MISC, itemFromName("stevescarts:module_liquid_sensors"))
                .pattern("R R")
                .pattern("LDW")
                .pattern("#X#")
                .define('X', itemFromName("stevescarts:component_advanced_pcb"))
                .define('#', itemFromName("minecraft:iron_ingot"))
                .define('R', itemFromName("minecraft:redstone"))
                .define('L', itemFromName("minecraft:lava_bucket"))
                .define('W', itemFromName("minecraft:water_bucket"))
                .define('D', itemFromName("minecraft:diamond"))
                .group("stevescarts")
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output, recipeFolder(itemFromName("stevescarts:module_liquid_sensors"), "module"));


        shaped(RecipeCategory.MISC, itemFromName("stevescarts:component_blue_pigment"))
                .pattern(" # ")
                .pattern("XXX")
                .pattern(" # ")
                .define('X', itemFromName("minecraft:blue_dye"))
                .define('#', itemFromName("minecraft:glowstone_dust"))
                .group("stevescarts")
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output, recipeFolder(itemFromName("stevescarts:component_blue_pigment"), "component"));


        shaped(RecipeCategory.MISC, itemFromName("stevescarts:module_basic_drill"))
                .pattern("#X ")
                .pattern(" #X")
                .pattern("#X ")
                .define('#', itemFromName("minecraft:iron_ingot"))
                .define('X', itemFromName("minecraft:diamond"))
                .group("stevescarts")
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output, recipeFolder(itemFromName("stevescarts:module_basic_drill"), "module"));


        shaped(RecipeCategory.MISC, itemFromName("stevescarts:module_fertilizer"))
                .pattern("F F")
                .pattern("#X#")
                .pattern("XPX")
                .define('P', itemFromName("stevescarts:component_simple_pcb"))
                .define('X', itemFromName("minecraft:leather"))
                .define('#', itemFromName("minecraft:glass_bottle"))
                .define('F', itemFromName("minecraft:bone_meal"))
                .group("stevescarts")
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output, recipeFolder(itemFromName("stevescarts:module_fertilizer"), "module"));


        shaped(RecipeCategory.MISC, itemFromName("stevescarts:module_front_chest"))
                .pattern("X#X")
                .pattern("XCX")
                .pattern("###")
                .define('X', itemFromName("stevescarts:component_chest_pane"))
                .define('#', itemFromName("stevescarts:component_large_chest_pane"))
                .define('C', itemFromName("stevescarts:component_chest_lock"))
                .group("stevescarts")
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output, recipeFolder(itemFromName("stevescarts:module_front_chest"), "module"));


        shaped(RecipeCategory.MISC, itemFromName("stevescarts:upgrade_manager_bridge"))
                .pattern("IPI")
                .pattern("XDX")
                .pattern("I#I")
                .define('I', itemFromName("minecraft:iron_ingot"))
                .define('P', itemFromName("minecraft:ender_pearl"))
                .define('D', itemFromName("stevescarts:blockdistributor"))
                .define('X', itemFromName("stevescarts:component_simple_pcb"))
                .define('#', itemFromName("stevescarts:component_blank_upgrade"))
                .group("stevescarts")
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output, recipeFolder(itemFromName("stevescarts:upgrade_manager_bridge"), "upgrade"));


        shaped(RecipeCategory.MISC, itemFromName("stevescarts:component_sock"))
                .pattern("##X")
                .pattern("##B")
                .pattern("###")
                .define('B', itemFromName("minecraft:milk_bucket"))
                .define('X', itemFromName("minecraft:cookie"))
                .define('#', itemFromName("minecraft:red_wool"))
                .group("stevescarts")
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output, recipeFolder(itemFromName("stevescarts:component_sock"), "component"));


        shaped(RecipeCategory.MISC, itemFromName("stevescarts:upgrade_cart_crane"))
                .pattern("XRX")
                .pattern("PIP")
                .pattern(" # ")
                .define('I', itemFromName("minecraft:iron_ingot"))
                .define('R', itemFromName("minecraft:rail"))
                .define('X', itemFromName("minecraft:piston"))
                .define('P', itemFromName("stevescarts:component_simple_pcb"))
                .define('#', itemFromName("stevescarts:component_blank_upgrade"))
                .group("stevescarts")
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output, recipeFolder(itemFromName("stevescarts:upgrade_cart_crane"), "upgrade"));


        shaped(RecipeCategory.MISC, itemFromName("stevescarts:component_reinforced_metal"), 9)
                .pattern("X")
                .define('X', itemFromName("stevescarts:reinforced_metal"))
                .group("stevescarts")
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output, recipeFolder(itemFromName("stevescarts:component_reinforced_metal"), "block"));


        shaped(RecipeCategory.MISC, itemFromName("stevescarts:module_hardened_wood_cutter"))
                .pattern("###")
                .pattern("#I#")
                .pattern(" X ")
                .define('I', itemFromName("minecraft:diamond"))
                .define('X', itemFromName("stevescarts:component_wood_cutting_core"))
                .define('#', itemFromName("stevescarts:component_hardened_saw_blade"))
                .group("stevescarts")
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output, recipeFolder(itemFromName("stevescarts:module_hardened_wood_cutter"), "module_alt"));


        shaped(RecipeCategory.MISC, itemFromName("stevescarts:component_dynamic_pane"))
                .pattern("X")
                .pattern("#")
                .define('#', itemFromName("stevescarts:component_iron_pane"))
                .define('X', itemFromName("minecraft:redstone"))
                .group("stevescarts")
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output, recipeFolder(itemFromName("stevescarts:component_dynamic_pane"), "component_alt"));


        shaped(RecipeCategory.MISC, itemFromName("stevescarts:module_hardened_drill"))
                .pattern("#X ")
                .pattern("BDX")
                .pattern("#X ")
                .define('#', itemFromName("stevescarts:component_hardened_mesh"))
                .define('X', itemFromName("stevescarts:component_reinforced_metal"))
                .define('B', itemFromName("minecraft:diamond_block"))
                .define('D', itemFromName("stevescarts:module_basic_drill"))
                .group("stevescarts")
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output, recipeFolder(itemFromName("stevescarts:module_hardened_drill"), "module"));


        shaped(RecipeCategory.MISC, itemFromName("stevescarts:component_large_sctank_pane"))
                .pattern("##")
                .pattern("##")
                .define('#', itemFromName("stevescarts:component_sctank_pane"))
                .group("stevescarts")
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output, recipeFolder(itemFromName("stevescarts:component_large_sctank_pane"), "component"));


        shaped(RecipeCategory.MISC, itemFromName("stevescarts:module_advanced_smelter"))
                .pattern(" D ")
                .pattern(" # ")
                .pattern("XCX")
                .define('#', itemFromName("stevescarts:component_advanced_pcb"))
                .define('X', itemFromName("stevescarts:component_simple_pcb"))
                .define('D', itemFromName("minecraft:diamond"))
                .define('C', itemFromName("stevescarts:module_smelter"))
                .group("stevescarts")
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output, recipeFolder(itemFromName("stevescarts:module_advanced_smelter"), "module"));


        shaped(RecipeCategory.MISC, itemFromName("stevescarts:component_basket"))
                .pattern("XXX")
                .pattern("X X")
                .pattern("###")
                .define('X', itemFromName("minecraft:stick"))
                .define('#', itemFromName("minecraft:iron_ingot"))
                .group("stevescarts")
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output, recipeFolder(itemFromName("stevescarts:component_basket"), "component"));


        shaped(RecipeCategory.MISC, itemFromName("stevescarts:upgrade_production_line"))
                .pattern(" X ")
                .pattern("LRL")
                .pattern("I#I")
                .define('R', itemFromName("minecraft:redstone"))
                .define('X', itemFromName("stevescarts:component_simple_pcb"))
                .define('I', itemFromName("stevescarts:component_reinforced_metal"))
                .define('L', itemFromName("stevescarts:component_advanced_pcb"))
                .define('#', itemFromName("stevescarts:component_blank_upgrade"))
                .group("stevescarts")
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output, recipeFolder(itemFromName("stevescarts:upgrade_production_line"), "upgrade"));


        shaped(RecipeCategory.MISC, itemFromName("stevescarts:component_blank_upgrade"), 2)
                .pattern("XXX")
                .pattern("#R#")
                .pattern("XLX")
                .define('X', itemFromName("minecraft:iron_ingot"))
                .define('R', itemFromName("minecraft:redstone"))
                .define('#', itemFromName("stevescarts:component_reinforced_metal"))
                .define('L', itemFromName("stevescarts:component_advanced_pcb"))
                .group("stevescarts")
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output, recipeFolder(itemFromName("stevescarts:component_blank_upgrade"), "component"));


        shaped(RecipeCategory.MISC, itemFromName("stevescarts:upgrade_new_era"))
                .pattern("XBX")
                .pattern("XLX")
                .pattern("X#X")
                .define('B', itemFromName("minecraft:enchanted_book"))
                .define('L', itemFromName("stevescarts:component_advanced_pcb"))
                .define('X', itemFromName("stevescarts:component_galgadorian_metal"))
                .define('#', itemFromName("stevescarts:upgrade_experienced_assembler"))
                .group("stevescarts")
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output, recipeFolder(itemFromName("stevescarts:upgrade_new_era"), "upgrade"));


        shaped(RecipeCategory.MISC, itemFromName("stevescarts:module_basic_solar_engine"))
                .pattern("#I#")
                .pattern("IXI")
                .pattern(" P ")
                .define('X', itemFromName("stevescarts:component_simple_pcb"))
                .define('#', itemFromName("stevescarts:component_solar_panel_component"))
                .define('P', itemFromName("minecraft:piston"))
                .define('I', itemFromName("minecraft:iron_ingot"))
                .group("stevescarts")
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output, recipeFolder(itemFromName("stevescarts:module_basic_solar_engine"), "module"));


        shaped(RecipeCategory.MISC, itemFromName("stevescarts:module_extreme_melter"))
                .pattern("O#O")
                .pattern("MXM")
                .pattern("O#O")
                .define('O', itemFromName("minecraft:bricks"))
                .define('#', itemFromName("minecraft:obsidian"))
                .define('X', itemFromName("minecraft:lava_bucket"))
                .define('M', itemFromName("stevescarts:component_chest_lock"))
                .group("stevescarts")
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output, recipeFolder(itemFromName("stevescarts:module_extreme_melter"), "module"));


        shaped(RecipeCategory.MISC, itemFromName("stevescarts:module_open_sctank"))
                .pattern("X X")
                .pattern("XCX")
                .pattern("###")
                .define('X', itemFromName("stevescarts:component_sctank_pane"))
                .define('#', itemFromName("stevescarts:component_huge_sctank_pane"))
                .define('C', itemFromName("stevescarts:component_sctank_valve"))
                .group("stevescarts")
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output, recipeFolder(itemFromName("stevescarts:module_open_sctank"), "module"));


        shaped(RecipeCategory.MISC, itemFromName("stevescarts:module_lawn_mower"))
                .pattern("X X")
                .pattern(" # ")
                .pattern("X X")
                .define('#', itemFromName("stevescarts:component_simple_pcb"))
                .define('X', itemFromName("stevescarts:component_blade_arm"))
                .group("stevescarts")
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output, recipeFolder(itemFromName("stevescarts:module_lawn_mower"), "module"));


        shaped(RecipeCategory.MISC, itemFromName("stevescarts:module_internal_storage"))
                .pattern("XXX")
                .pattern("X#X")
                .pattern("XXX")
                .define('X', itemFromName("stevescarts:component_chest_pane"))
                .define('#', itemFromName("stevescarts:component_chest_lock"))
                .group("stevescarts")
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output, recipeFolder(itemFromName("stevescarts:module_internal_storage"), "module"));


        shaped(RecipeCategory.MISC, itemFromName("stevescarts:module_galgadorian_hull"))
                .pattern("# #")
                .pattern("###")
                .pattern("X X")
                .define('#', itemFromName("stevescarts:component_galgadorian_metal"))
                .define('X', itemFromName("stevescarts:component_galgadorian_wheels"))
                .group("stevescarts")
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output, recipeFolder(itemFromName("stevescarts:module_galgadorian_hull"), "module"));


        shaped(RecipeCategory.MISC, itemFromName("stevescarts:component_advanced_solar_panel"))
                .pattern("#X#")
                .pattern(" P ")
                .pattern("#X#")
                .define('#', itemFromName("stevescarts:component_solar_panel_component"))
                .define('X', itemFromName("minecraft:iron_ingot"))
                .define('P', itemFromName("stevescarts:component_simple_pcb"))
                .group("stevescarts")
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output, recipeFolder(itemFromName("stevescarts:component_advanced_solar_panel"), "component"));


        shaped(RecipeCategory.MISC, itemFromName("stevescarts:component_advanced_solar_panel"))
                .pattern("# #")
                .pattern("XPX")
                .pattern("# #")
                .define('#', itemFromName("stevescarts:component_solar_panel_component"))
                .define('X', itemFromName("minecraft:iron_ingot"))
                .define('P', itemFromName("stevescarts:component_simple_pcb"))
                .group("stevescarts")
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output, recipeFolder(itemFromName("stevescarts:component_advanced_solar_panel"), "component_alt"));


        shaped(RecipeCategory.MISC, itemFromName("stevescarts:module_planter_range_extender"))
                .pattern("R#R")
                .pattern(" S ")
                .pattern("XSX")
                .define('X', itemFromName("stevescarts:component_simple_pcb"))
                .define('#', itemFromName("stevescarts:component_advanced_pcb"))
                .define('S', ItemTags.SAPLINGS)
                .define('R', itemFromName("minecraft:redstone"))
                .group("stevescarts")
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output, recipeFolder(itemFromName("stevescarts:module_planter_range_extender"), "module"));


        shaped(RecipeCategory.MISC, itemFromName("stevescarts:upgrade_cart_modifier"))
                .pattern("I I")
                .pattern("PAP")
                .pattern("I#I")
                .define('I', itemFromName("minecraft:iron_ingot"))
                .define('A', itemFromName("minecraft:anvil"))
                .define('P', itemFromName("stevescarts:component_simple_pcb"))
                .define('#', itemFromName("stevescarts:component_blank_upgrade"))
                .group("stevescarts")
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output, recipeFolder(itemFromName("stevescarts:upgrade_cart_modifier"), "upgrade"));


        shaped(RecipeCategory.MISC, itemFromName("stevescarts:component_large_iron_pane"))
                .pattern("##")
                .pattern("##")
                .define('#', itemFromName("stevescarts:component_iron_pane"))
                .group("stevescarts")
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output, recipeFolder(itemFromName("stevescarts:component_large_iron_pane"), "component"));


        shaped(RecipeCategory.MISC, itemFromName("stevescarts:upgrade_industrial_espionage"))
                .pattern("SRS")
                .pattern("GRG")
                .pattern("R#R")
                .define('S', itemFromName("minecraft:bookshelf"))
                .define('R', itemFromName("stevescarts:component_reinforced_metal"))
                .define('G', itemFromName("stevescarts:component_eye_of_galgador"))
                .define('#', itemFromName("stevescarts:upgrade_module_knowledge"))
                .group("stevescarts")
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output, recipeFolder(itemFromName("stevescarts:upgrade_industrial_espionage"), "upgrade"));


        shaped(RecipeCategory.MISC, itemFromName("stevescarts:component_raw_handle"))
                .pattern("  X")
                .pattern(" X ")
                .pattern("X  ")
                .define('X', itemFromName("minecraft:iron_ingot"))
                .group("stevescarts")
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output, recipeFolder(itemFromName("stevescarts:component_raw_handle"), "component"));


        shaped(RecipeCategory.MISC, itemFromName("stevescarts:module_cleaning_machine"))
                .pattern("#X#")
                .pattern("# #")
                .pattern("# #")
                .define('X', itemFromName("stevescarts:component_cleaning_core"))
                .define('#', itemFromName("stevescarts:component_cleaning_tube"))
                .group("stevescarts")
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output, recipeFolder(itemFromName("stevescarts:module_cleaning_machine"), "module"));


        shaped(RecipeCategory.MISC, itemFromName("stevescarts:module_cage"))
                .pattern("###")
                .pattern("#X#")
                .pattern("###")
                .define('X', itemFromName("stevescarts:component_simple_pcb"))
                .define('#', itemFromName("minecraft:oak_fence"))
                .group("stevescarts")
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output, recipeFolder(itemFromName("stevescarts:module_cage"), "module"));


        shaped(RecipeCategory.MISC, itemFromName("stevescarts:component_lump_of_galgador"), 2)
                .pattern("#D#")
                .pattern("X#X")
                .pattern("RXR")
                .define('X', itemFromName("stevescarts:component_eye_of_galgador"))
                .define('#', itemFromName("minecraft:glowstone_dust"))
                .define('D', itemFromName("minecraft:diamond"))
                .define('R', itemFromName("stevescarts:component_stabilized_metal"))
                .group("stevescarts")
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output, recipeFolder(itemFromName("stevescarts:component_lump_of_galgador"), "component"));


        shaped(RecipeCategory.MISC, itemFromName("stevescarts:module_incinerator"))
                .pattern("OOO")
                .pattern("#X#")
                .pattern("OOO")
                .define('O', itemFromName("minecraft:nether_bricks"))
                .define('#', itemFromName("minecraft:obsidian"))
                .define('X', itemFromName("minecraft:furnace"))
                .group("stevescarts")
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output, recipeFolder(itemFromName("stevescarts:module_incinerator"), "module"));


        shaped(RecipeCategory.MISC, itemFromName("stevescarts:module_iron_drill"))
                .pattern("XX ")
                .pattern(" XX")
                .pattern("XX ")
                .define('X', itemFromName("minecraft:iron_ingot"))
                .group("stevescarts")
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output, recipeFolder(itemFromName("stevescarts:module_iron_drill"), "module"));


        shaped(RecipeCategory.MISC, itemFromName("stevescarts:module_seat"))
                .pattern(" #")
                .pattern(" #")
                .pattern("X#")
                .define('X', ItemTags.WOODEN_SLABS)
                .define('#', ItemTags.PLANKS)
                .group("stevescarts")
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output, recipeFolder(itemFromName("stevescarts:module_seat"), "module"));


        shaped(RecipeCategory.MISC, itemFromName("minecraft:jungle_planks"), 2)
                .pattern("X")
                .define('X', itemFromName("stevescarts:component_jungle_log"))
                .group("stevescarts")
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output, recipeFolder(itemFromName("minecraft:jungle_planks"), "component"));


        shaped(RecipeCategory.MISC, itemFromName("stevescarts:module_dynamite_carrier"))
                .pattern(" # ")
                .pattern("#X#")
                .pattern(" # ")
                .define('X', itemFromName("minecraft:flint_and_steel"))
                .define('#', itemFromName("stevescarts:component_dynamite"))
                .group("stevescarts")
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output, recipeFolder(itemFromName("stevescarts:module_dynamite_carrier"), "module"));


        shaped(RecipeCategory.MISC, itemFromName("stevescarts:module_crop_nether_wart"))
                .pattern("#")
                .pattern("X")
                .define('X', itemFromName("stevescarts:component_empty_disk"))
                .define('#', itemFromName("minecraft:nether_wart"))
                .group("stevescarts")
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output, recipeFolder(itemFromName("stevescarts:module_crop_nether_wart"), "module"));
    }
}
