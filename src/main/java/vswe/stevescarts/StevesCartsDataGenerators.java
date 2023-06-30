package vswe.stevescarts;

import com.google.common.collect.ImmutableList;
import net.creeperhost.polylib.helpers.RegistryNameHelper;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.SmithingTransformRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.LanguageProvider;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import vswe.stevescarts.api.modules.data.ModuleData;
import vswe.stevescarts.init.ModBlocks;
import vswe.stevescarts.init.ModItems;
import vswe.stevescarts.init.StevesCartsModules;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class StevesCartsDataGenerators
{
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event)
    {
        DataGenerator generator = event.getGenerator();

        if (event.includeServer())
        {
            generator.addProvider(true, new GeneratorRecipes(generator.getPackOutput()));
            generator.addProvider(true, new GeneratorLoots(generator.getPackOutput()));
        }

        if (event.includeClient())
        {
            generator.addProvider(true, new GeneratorBlockTags(generator.getPackOutput(), event.getLookupProvider(), generator, event.getExistingFileHelper()));
//            generator.addProvider(true, new GeneratorLanguage(generator));
//            generator.addProvider(true, new GeneratorBlockStates(generator, event.getExistingFileHelper()));
            generator.addProvider(true, new GeneratorItemModels(generator, event.getExistingFileHelper()));
        }
    }

    static class GeneratorBlockStates extends BlockStateProvider
    {
        public GeneratorBlockStates(DataGenerator gen, ExistingFileHelper exFileHelper)
        {
            super(gen.getPackOutput(), Constants.MOD_ID, exFileHelper);
        }

        @Override
        protected void registerStatesAndModels()
        {
            simpleBlock(ModBlocks.ENHANCED_GALGADORIAN_METAL.get(), new ModelFile.UncheckedModelFile(modLoc("block/storage_enhanced_galgadorian")));
            simpleBlock(ModBlocks.REINFORCED_METAL.get(), new ModelFile.UncheckedModelFile(modLoc("block/storage_reinforced_metal")));
            simpleBlock(ModBlocks.GALGADORIAN_METAL.get(), new ModelFile.UncheckedModelFile(modLoc("block/storage_galgadorian")));
        }

        public void registerSidedBlock(Block block, String folder)
        {
            horizontalBlock(block, models().orientableWithBottom(getResourceLocation(block).getPath(),
                    modLoc("block/" + folder + "/side"),
                    modLoc("block/" + folder + "/front"),
                    modLoc("block/" + folder + "/bottom"),
                    modLoc("block/" + folder + "/top")));
        }

        public ResourceLocation getResourceLocation(Block block)
        {
            return ForgeRegistries.BLOCKS.getKey(block);
        }
    }

    static class GeneratorItemModels extends ItemModelProvider
    {
        public GeneratorItemModels(DataGenerator generator, ExistingFileHelper existingFileHelper)
        {
            super(generator.getPackOutput(), Constants.MOD_ID, existingFileHelper);
        }

        @Override
        protected void registerModels()
        {
            ModItems.MODULES.forEach((moduleData, itemSupplier) -> singleTexture(RegistryNameHelper.getRegistryName(itemSupplier.get()).get().getPath(),
                    mcLoc("item/generated"), "layer0", modLoc("item/" + moduleData.getRawName() + "_icon")));

            ModItems.COMPONENTS.forEach((moduleData, itemSupplier) -> singleTexture(RegistryNameHelper.getRegistryName(itemSupplier.get()).get().getPath(),
                    mcLoc("item/generated"), "layer0", modLoc("item/" + moduleData.getRawName() + "_icon")));

//            ModBlocks.CHARGERS.forEach((chargerTypes, blockSupplier) -> registerDefaultItemBlockModel(blockSupplier.get()));
//            ModBlocks.POWER_CELLS.forEach((chargerTypes, blockSupplier) -> registerDefaultItemBlockModel(blockSupplier.get()));
        }

        public void registerDefaultItemBlockModel(Block block)
        {
            String path = RegistryNameHelper.getRegistryName(block).get().getPath();
            getBuilder(path).parent(new ModelFile.UncheckedModelFile(modLoc("block/" + path)));
        }

        @Override
        public @NotNull String getName()
        {
            return "Item Models";
        }
    }

    static class GeneratorLanguage extends LanguageProvider
    {
        public GeneratorLanguage(DataGenerator gen)
        {
            super(gen.getPackOutput(), Constants.MOD_ID, "en_us");
        }

        @Override
        protected void addTranslations()
        {
//            add("itemGroup.SC2Modules", "Steve's Carts 2 Modules");
//            add("itemGroup.SC2Items", "Steve's Carts 2 Components");
//            add("itemGroup.SC2Blocks", "Steve's Carts 2 Blocks");
        }
    }


    static class GeneratorLoots extends LootTableProvider
    {
        public GeneratorLoots(PackOutput output)
        {
            super(output, Set.of(), ImmutableList.of(new SubProviderEntry(Blocks::new, LootContextParamSets.BLOCK)));
        }

        private static class Blocks extends BlockLootSubProvider
        {
            protected Blocks()
            {
                super(Set.of(), FeatureFlags.REGISTRY.allFlags());
            }
            @Override
            protected void generate()
            {
                ModBlocks.BLOCKS.getEntries().forEach(blockRegistryObject -> dropSelf(blockRegistryObject.get()));
            }

            @Override
            protected @NotNull Iterable<Block> getKnownBlocks()
            {
                List<Block> list = new ArrayList<>();
                ModBlocks.BLOCKS.getEntries().forEach(blockRegistryObject -> list.add(blockRegistryObject.get()));
                return ImmutableList.copyOf(list);
            }
        }
    }

    static class GeneratorRecipes extends RecipeProvider
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
    }

    static ItemStack getStackFromModule(ModuleData moduleData)
    {
        if(moduleData == null) return ItemStack.EMPTY;
        return moduleData.getItemStack();
    }

    static class GeneratorBlockTags extends BlockTagsProvider
    {
        public GeneratorBlockTags(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, DataGenerator generator, ExistingFileHelper helper) {
            super(output, lookupProvider, Constants.MOD_ID, helper);
        }

        @Override
        protected void addTags(HolderLookup.Provider provider)
        {
            ModBlocks.BLOCKS.getEntries().forEach(blockRegistryObject -> tag(BlockTags.MINEABLE_WITH_PICKAXE).add(blockRegistryObject.get()));
            tag(BlockTags.RAILS).add(ModBlocks.ADVANCED_DETECTOR.get()).add(ModBlocks.JUNCTION.get());
            tag(BlockTags.BEACON_BASE_BLOCKS).add(ModBlocks.GALGADORIAN_METAL.get()).add(ModBlocks.REINFORCED_METAL.get()).add(ModBlocks.ENHANCED_GALGADORIAN_METAL.get());
        }
    }
}
