package vswe.stevescarts;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.Logging;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;

import java.nio.file.Path;

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
        public final ModConfigSpec.ConfigValue<String> woodcutterRepairHardened;
        public final ModConfigSpec.ConfigValue<String> woodcutterRepairNetherite;

        public final ModConfigSpec.ConfigValue<String> drillRepairIron;
        public final ModConfigSpec.ConfigValue<String> drillRepairDiamond;
        public final ModConfigSpec.ConfigValue<String> drillRepairHardened;

        public final ModConfigSpec.ConfigValue<String> farmerRepairDiamond;
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
            woodcutterRepairHardened = builder.comment("Repair item for Hardened Woodcutter").define("woodcutterRepairHardened", "stevescarts:component_reinforced_metal");
            woodcutterRepairNetherite = builder.comment("Repair item for Netherite Woodcutter").define("woodcutterRepairNetherite", "minecraft:netherite_ingot");

            drillRepairIron = builder.comment("Repair item for Iron Drill").define("drillRepairIron", "minecraft:iron_ingot");
            drillRepairDiamond = builder.comment("Repair item for Diamond Drill").define("drillRepairDiamond", "minecraft:diamond");
            drillRepairHardened = builder.comment("Repair item for Hardened Drill").define("drillRepairHardened", "stevescarts:component_reinforced_metal");

            farmerRepairDiamond = builder.comment("Repair item for Diamond farmer").define("farmerRepairDiamond", "minecraft:diamond");

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
