package vswe.stevescarts;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.Logging;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;

public class SCConfig {
    public static final String CATEGORY_GENERAL = "general";

    public static class Client {
        public final ModConfigSpec.ConfigValue<Boolean> useArcadeSounds;
        public final ModConfigSpec.ConfigValue<Boolean> useArcadeMobSounds;

        Client(ModConfigSpec.Builder builder) {
            builder.comment("Client General settings").push(CATEGORY_GENERAL);
            builder.pop();
            useArcadeSounds = builder.comment("Enable arcade machine sounds").define("useArcadeSounds", true);
            useArcadeMobSounds = builder.comment("Enable mob sounds for arcade machines").define("useArcadeMobSounds", true);
        }
    }

    public static class Common {
        public final ModConfigSpec.ConfigValue<Boolean> disableTimedCrafting;
        public final ModConfigSpec.ConfigValue<Integer> maxDynamites;
        public final ModConfigSpec.ConfigValue<Integer> drillSize;
        public final ModConfigSpec.ConfigValue<Integer> basic_solar_production;
        public final ModConfigSpec.ConfigValue<Integer> compact_solar_production;
        public final ModConfigSpec.ConfigValue<Integer> standard_solar_production;

        public final ModConfigSpec.ConfigValue<Boolean> allowCartToRunWithRepairItems;

        public final ModConfigSpec.ConfigValue<String> woodcutterRepairDiamond;
        public final ModConfigSpec.ConfigValue<Integer> woodcutterRepairAmountDiamond;
        public final ModConfigSpec.ConfigValue<Integer> woodcutterDurabilityDiamond;
        public final ModConfigSpec.ConfigValue<String> woodcutterRepairHardened;
        public final ModConfigSpec.ConfigValue<Integer> woodcutterRepairAmountHardened;
        public final ModConfigSpec.ConfigValue<Integer> woodcutterDurabilityHardened;
        public final ModConfigSpec.ConfigValue<String> woodcutterRepairNetherite;
        public final ModConfigSpec.ConfigValue<Integer> woodcutterRepairAmountNetherite;
        public final ModConfigSpec.ConfigValue<Integer> woodcutterDurabilityNetherite;

        public final ModConfigSpec.ConfigValue<String> drillRepairIron;
        public final ModConfigSpec.ConfigValue<Integer> drillRepairAmountIron;
        public final ModConfigSpec.ConfigValue<Integer> drillDurabilityIron;
        public final ModConfigSpec.ConfigValue<String> drillRepairDiamond;
        public final ModConfigSpec.ConfigValue<Integer> drillRepairAmountDiamond;
        public final ModConfigSpec.ConfigValue<Integer> drillDurabilityDiamond;
        public final ModConfigSpec.ConfigValue<String> drillRepairHardened;
        public final ModConfigSpec.ConfigValue<Integer> drillRepairAmountHardened;
        public final ModConfigSpec.ConfigValue<Integer> drillDurabilityHardened;

        public final ModConfigSpec.ConfigValue<String> farmerRepairDiamond;
        public final ModConfigSpec.ConfigValue<Integer> farmerRepairAmountDiamond;
        public final ModConfigSpec.ConfigValue<Integer> farmerDurabilityDiamond;

        public final ModConfigSpec.ConfigValue<Boolean> assemblerInsertFuel;

        Common(ModConfigSpec.Builder builder) {
            builder.comment("General settings").push(CATEGORY_GENERAL);
            builder.pop();

            disableTimedCrafting = builder.comment("Disabled timed crafting").define("disableTimedCrafting", false);
            maxDynamites = builder.comment("The max amount of dynamites that can be held").define("maxDynamites", 50);
            drillSize = builder.comment("Max drill size").define("drillSize", 4);

            basic_solar_production = builder.comment("The amount of power the basic solar module will produce").define("basicSolarProduction", 10);
            compact_solar_production = builder.comment("The amount of power the compact solar module will produce").define("compactSolarProduction", 20);
            standard_solar_production = builder.comment("The amount of power the standard solar module will produce").define("standardSolarProduction", 20);


            woodcutterRepairDiamond = builder.comment("Repair item for Diamond Woodcutter").define("woodcutterRepairDiamond", "minecraft:diamond");
            woodcutterRepairAmountDiamond = builder.comment("Repair value for Diamond Woodcutter repair item").define("woodcutterRepairAmountDiamond", 160000);
            woodcutterDurabilityDiamond = builder.comment("Diamond Woodcutter max durability").define("woodcutterDurabilityDiamond", 320000);

            woodcutterRepairHardened = builder.comment("Repair item for Hardened Woodcutter").define("woodcutterRepairHardened", "stevescarts:component_reinforced_metal");
            woodcutterRepairAmountHardened = builder.comment("Repair value for Hardened Woodcutter repair item").define("woodcutterRepairAmountHardened", 320000);
            woodcutterDurabilityHardened = builder.comment("Hardened Woodcutter max durability").define("woodcutterDurabilityHardened", 640000);

            woodcutterRepairNetherite = builder.comment("Repair item for Netherite Woodcutter").define("woodcutterRepairNetherite", "minecraft:netherite_ingot");
            woodcutterRepairAmountNetherite = builder.comment("Repair value for Netherite Woodcutter repair item").define("woodcutterRepairAmountNetherite", 450000);
            woodcutterDurabilityNetherite = builder.comment("Netherite Woodcutter max durability").define("woodcutterDurabilityNetherite", 1000000);


            drillRepairIron = builder.comment("Repair item for Iron Drill").define("drillRepairIron", "minecraft:iron_ingot");
            drillRepairAmountIron = builder.comment("Repair value for Iron Drill repair item").define("drillRepairAmountIron", 20000);
            drillDurabilityIron = builder.comment("Iron Drill max durability").define("drillDurabilityIron", 50000);

            drillRepairDiamond = builder.comment("Repair item for Diamond Drill").define("drillRepairDiamond", "minecraft:diamond");
            drillRepairAmountDiamond = builder.comment("Repair value for Diamond Drill repair item").define("drillRepairAmountDiamond", 100000);
            drillDurabilityDiamond = builder.comment("Diamond Drill max durability").define("drillDurabilityDiamond", 300000);

            drillRepairHardened = builder.comment("Repair item for Hardened Drill").define("drillRepairHardened", "stevescarts:component_reinforced_metal");
            drillRepairAmountHardened = builder.comment("Repair value for Hardened Drill repair item").define("drillRepairAmountHardened", 450000);
            drillDurabilityHardened = builder.comment("Hardened Drill max durability").define("drillDurabilityHardened", 1000000);


            farmerRepairDiamond = builder.comment("Repair item for Diamond farmer").define("farmerRepairDiamond", "minecraft:diamond");
            farmerRepairAmountDiamond = builder.comment("Repair value for Diamond farmer repair item").define("farmerRepairAmountDiamond", 150000);
            farmerDurabilityDiamond = builder.comment("Diamond farmer max durability").define("farmerDurabilityDiamond", 300000);


            allowCartToRunWithRepairItems = builder.comment("Allow carts to run with items in the tool repair slot").define("allowCartToRunWithRepairItems", false);
            assemblerInsertFuel = builder.comment("Allow fuel to be auto inserted into the cart assembler").define("assemblerInsertFuel", false);
        }
    }

    static final ModConfigSpec clientSpec;
    public static final Client CLIENT;

    static {
        final Pair<Client, ModConfigSpec> specPair = new ModConfigSpec.Builder().configure(Client::new);
        clientSpec = specPair.getRight();
        CLIENT = specPair.getLeft();
    }

    static final ModConfigSpec commonSpec;
    public static final Common COMMON;

    static {
        final Pair<Common, ModConfigSpec> specPair = new ModConfigSpec.Builder().configure(Common::new);
        commonSpec = specPair.getRight();
        COMMON = specPair.getLeft();
    }

//    public static void loadConfig(ModConfigSpec spec, Path path) {
//        CommentedFileConfig configData = CommentedFileConfig
//                .builder(path)
//                .sync()
//                .autosave()
//                .writingMode(WritingMode.REPLACE)
//                .build();
//
//
////        ConfigTracker.INSTANCE.registerConfig()
//
//        configData.load();
//        spec.acceptConfig(configData);
//    }

    @SubscribeEvent
    public static void onLoad(final ModConfigEvent.Loading configEvent) {
        LogManager.getLogger().debug(Logging.FORGEMOD, "Loaded Steves config file {}", configEvent.getConfig().getFileName());
    }

    @SubscribeEvent
    public static void onFileChange(final ModConfigEvent.Reloading configEvent) {
        LogManager.getLogger().debug(Logging.FORGEMOD, "Steves Carts config just got changed on the file system!");
    }
}
