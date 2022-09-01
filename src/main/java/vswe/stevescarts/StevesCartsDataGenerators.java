package vswe.stevescarts;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import net.creeperhost.polylib.helpers.RegistryNameHelper;
import net.minecraft.core.Registry;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.loot.BlockLoot;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.LootTables;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.CopyNameFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.predicates.ExplosionCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.LanguageProvider;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;
import vswe.stevescarts.init.ModBlocks;
import vswe.stevescarts.init.ModItems;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class StevesCartsDataGenerators
{
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event)
    {
        DataGenerator generator = event.getGenerator();

        if (event.includeServer())
        {
            generator.addProvider(true, new GeneratorRecipes(generator));
            generator.addProvider(true, new GeneratorLoots(generator));
        }

        if (event.includeClient())
        {
            generator.addProvider(true, new GeneratorBlockTags(generator, event.getExistingFileHelper()));
            generator.addProvider(true, new GeneratorLanguage(generator));
            generator.addProvider(true, new GeneratorBlockStates(generator, event.getExistingFileHelper()));
            generator.addProvider(true, new GeneratorItemModels(generator, event.getExistingFileHelper()));
        }
    }

    static class GeneratorBlockStates extends BlockStateProvider
    {
        public GeneratorBlockStates(DataGenerator gen, ExistingFileHelper exFileHelper)
        {
            super(gen, Constants.MOD_ID, exFileHelper);
        }

        @Override
        protected void registerStatesAndModels()
        {
//            ModBlocks.CHARGERS.forEach((chargerTypes, blockSupplier) -> registerSidedBlock(blockSupplier.get(), "charger"));
//            ModBlocks.POWER_CELLS.forEach((chargerTypes, blockSupplier) -> registerSidedBlock(blockSupplier.get(), "power_cell"));
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
            return Registry.BLOCK.getKey(block);
        }
    }

    static class GeneratorItemModels extends ItemModelProvider
    {
        public GeneratorItemModels(DataGenerator generator, ExistingFileHelper existingFileHelper)
        {
            super(generator, Constants.MOD_ID, existingFileHelper);
        }

        @Override
        protected void registerModels()
        {
            ModItems.MODULES.forEach((moduleData, itemSupplier) -> singleTexture(RegistryNameHelper.getRegistryName(itemSupplier.get()).get().getPath(),
                    mcLoc("item/generated"), "layer0", modLoc("items/" + moduleData.getRawName() + "_icon")));

            ModItems.COMPONENTS.forEach((moduleData, itemSupplier) -> singleTexture(RegistryNameHelper.getRegistryName(itemSupplier.get()).get().getPath(),
                    mcLoc("item/generated"), "layer0", modLoc("items/" + moduleData.getRawName() + "_icon")));

//            ModBlocks.CHARGERS.forEach((chargerTypes, blockSupplier) -> registerDefaultItemBlockModel(blockSupplier.get()));
//            ModBlocks.POWER_CELLS.forEach((chargerTypes, blockSupplier) -> registerDefaultItemBlockModel(blockSupplier.get()));
//
//            ModItems.BATTERIES.forEach((batteryTypes, itemSupplier) -> singleTexture(getResourceLocation(itemSupplier.get()).getPath(),
//                    mcLoc("item/generated"), "layer0", modLoc("item/" + batteryTypes.getName())));
//
//            ModItems.DRILL_BIT_TYPES.forEach((batteryTypes, itemSupplier) -> singleTexture(getResourceLocation(itemSupplier.get()).getPath(),
//                    mcLoc("item/generated"), "layer0", modLoc("item/" + batteryTypes.getName())));
//
//            ModItems.DRILL_HANDLE_TYPES.forEach((batteryTypes, itemSupplier) -> singleTexture(getResourceLocation(itemSupplier.get()).getPath(),
//                    mcLoc("item/generated"), "layer0", modLoc("item/drill_handle")));
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
            super(gen, Constants.MOD_ID, "en_us");
        }

        @Override
        protected void addTranslations()
        {
//            addBlock(ModBlocks.CHARGERS.get(ChargerTypes.BASIC), "Basic Charger");
//            addBlock(ModBlocks.CHARGERS.get(ChargerTypes.ADVANCED), "Advanced Charger");
//            addBlock(ModBlocks.CHARGERS.get(ChargerTypes.ULTIMATE), "Ultimate Charger");
//            addBlock(ModBlocks.CHARGERS.get(ChargerTypes.CREATIVE), "Creative Charger");
//
//            addBlock(ModBlocks.POWER_CELLS.get(PowerCellTypes.BASIC), "Basic Energy cell");
//            addBlock(ModBlocks.POWER_CELLS.get(PowerCellTypes.ADVANCED), "Advanced Energy cell");
//            addBlock(ModBlocks.POWER_CELLS.get(PowerCellTypes.ULTIMATE), "Ultimate Energy cell");
//            addBlock(ModBlocks.POWER_CELLS.get(PowerCellTypes.CREATIVE), "Creative Energy cell");
//
//            addItem(ModItems.DRILL_BIT_TYPES.get(DrillBitTypes.IRON), "Iron Drill bit");
//            addItem(ModItems.DRILL_BIT_TYPES.get(DrillBitTypes.GOLD), "Gold Drill bit");
//            addItem(ModItems.DRILL_BIT_TYPES.get(DrillBitTypes.DIAMOND), "Diamond Drill bit");
//            addItem(ModItems.DRILL_BIT_TYPES.get(DrillBitTypes.NETHERITE), "Netherite Drill bit");
//
//            addItem(ModItems.DRILL_HANDLE_TYPES.get(DyeColor.WHITE), "White Drill handle");
//            addItem(ModItems.DRILL_HANDLE_TYPES.get(DyeColor.ORANGE), "Orange Drill handle");
//            addItem(ModItems.DRILL_HANDLE_TYPES.get(DyeColor.MAGENTA), "Magenta Drill handle");
//            addItem(ModItems.DRILL_HANDLE_TYPES.get(DyeColor.LIGHT_BLUE), "Light Blue Drill handle");
//            addItem(ModItems.DRILL_HANDLE_TYPES.get(DyeColor.YELLOW), "Yellow Drill handle");
//            addItem(ModItems.DRILL_HANDLE_TYPES.get(DyeColor.LIME), "Lime Drill handle");
//            addItem(ModItems.DRILL_HANDLE_TYPES.get(DyeColor.PINK), "Pink Drill handle");
//            addItem(ModItems.DRILL_HANDLE_TYPES.get(DyeColor.GRAY), "Gray Drill handle");
//            addItem(ModItems.DRILL_HANDLE_TYPES.get(DyeColor.LIGHT_GRAY), "Light Gray Drill handle");
//            addItem(ModItems.DRILL_HANDLE_TYPES.get(DyeColor.CYAN), "Cyan Drill handle");
//            addItem(ModItems.DRILL_HANDLE_TYPES.get(DyeColor.PURPLE), "Purple Drill handle");
//            addItem(ModItems.DRILL_HANDLE_TYPES.get(DyeColor.BLUE), "Blue Drill handle");
//            addItem(ModItems.DRILL_HANDLE_TYPES.get(DyeColor.BROWN), "Brown Drill handle");
//            addItem(ModItems.DRILL_HANDLE_TYPES.get(DyeColor.GREEN), "Green Drill handle");
//            addItem(ModItems.DRILL_HANDLE_TYPES.get(DyeColor.RED), "Red Drill handle");
//            addItem(ModItems.DRILL_HANDLE_TYPES.get(DyeColor.BLACK), "Black Drill handle");
//
//
//            add("itemGroup.techmod", "Tech-Mod");
//            add("screen.techmod.energy", "Energy: %s/%s FE");
//            add("screen.techmod.no_fuel", "Fuel source empty");
//            add("screen.techmod.burn_time", "Burn time left: %ss");
//            add("item.techmod.basic_battery", "Basic Battery");
//            add("item.techmod.advanced_battery", "Advanced Battery");
//            add("item.techmod.ultimate_battery", "Ultimate Battery");
        }
    }


    static class GeneratorLoots extends LootTableProvider
    {
        public GeneratorLoots(DataGenerator dataGeneratorIn)
        {
            super(dataGeneratorIn);
        }

        @Override
        protected @NotNull List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootContextParamSet>> getTables()
        {
            return ImmutableList.of(Pair.of(Blocks::new, LootContextParamSets.BLOCK));
        }

        private static class Blocks extends BlockLoot
        {
            @Override
            protected void addTables()
            {
                ModBlocks.BLOCKS.getEntries().forEach(blockRegistryObject -> this.add(blockRegistryObject.get(), LootTable.lootTable().withPool(create(blockRegistryObject.get()))));
            }

            public LootPool.Builder create(Block block)
            {
                return LootPool.lootPool().name(RegistryNameHelper.getRegistryName(block).get().toString())
                        .setRolls(ConstantValue.exactly(1)).when(ExplosionCondition.survivesExplosion())
                        .add(LootItem.lootTableItem(block)
                                .apply(CopyNameFunction.copyName(CopyNameFunction.NameSource.BLOCK_ENTITY)));

            }

            @Override
            protected @NotNull Iterable<Block> getKnownBlocks()
            {
                List<Block> list = new ArrayList<>();
                ModBlocks.BLOCKS.getEntries().forEach(blockRegistryObject -> list.add(blockRegistryObject.get()));
                return ImmutableList.copyOf(list);
            }
        }

        @Override
        protected void validate(Map<ResourceLocation, LootTable> map, @NotNull ValidationContext validationtracker)
        {
            map.forEach((name, table) -> LootTables.validate(validationtracker, name, table));
        }
    }

    static class GeneratorRecipes extends RecipeProvider
    {
        public GeneratorRecipes(DataGenerator generator)
        {
            super(generator);
        }

        @Override
        protected void buildCraftingRecipes(@NotNull Consumer<FinishedRecipe> consumer)
        {
//            Block block = ModBlocks.CHARGERS.get(ChargerTypes.BASIC).get();
//            ShapedRecipeBuilder.shaped(block)
//                    .define('i', Tags.Items.INGOTS_IRON)
//                    .define('r', Tags.Items.DUSTS_REDSTONE)
//                    .define('l', Tags.Items.STORAGE_BLOCKS_COAL)
//                    .define('d', Tags.Items.GEMS_LAPIS)
//                    .pattern("iri")
//                    .pattern("drd")
//                    .pattern("ili")
//                    .unlockedBy("has_diamonds", has(Tags.Items.GEMS_DIAMOND)).save(consumer);
        }
    }

    static class GeneratorBlockTags extends BlockTagsProvider
    {
        public GeneratorBlockTags(DataGenerator generator, @Nullable ExistingFileHelper existingFileHelper)
        {
            super(generator, Constants.MOD_ID, existingFileHelper);
        }

        @Override
        protected void addTags()
        {
            ModBlocks.BLOCKS.getEntries().forEach(blockRegistryObject -> tag(BlockTags.MINEABLE_WITH_PICKAXE).add(blockRegistryObject.get()));
            tag(BlockTags.RAILS).add(ModBlocks.ADVANCED_DETECTOR.get()).add(ModBlocks.JUNCTION.get());
            tag(BlockTags.BEACON_BASE_BLOCKS).add(ModBlocks.GALGADORIAN_METAL.get()).add(ModBlocks.REINFORCED_METAL.get()).add(ModBlocks.ENHANCED_GALGADORIAN_METAL.get());
        }
    }
}
