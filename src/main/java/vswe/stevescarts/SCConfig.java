package vswe.stevescarts;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.nio.file.Path;

public class SCConfig
{
    public static final String CATEGORY_GENERAL = "general";
    private static final ModConfigSpec.Builder COMMON_BUILDER = new ModConfigSpec.Builder();
    private static final ModConfigSpec.Builder CLIENT_BUILDER = new ModConfigSpec.Builder();

    public static ModConfigSpec COMMON_CONFIG;
    public static ModConfigSpec CLIENT_CONFIG;

    public static ModConfigSpec.ConfigValue<Boolean> disableTimedCrafting;
    public static ModConfigSpec.ConfigValue<Integer> maxDynamites;
    public static ModConfigSpec.ConfigValue<Boolean> useArcadeSounds;
    public static ModConfigSpec.ConfigValue<Boolean> useArcadeMobSounds;
    public static ModConfigSpec.ConfigValue<Integer> drillSize;
    public static ModConfigSpec.ConfigValue<Integer> basic_solar_production;
    public static ModConfigSpec.ConfigValue<Integer> compact_solar_production;
    public static ModConfigSpec.ConfigValue<Integer> standard_solar_production;

    public static ModConfigSpec.ConfigValue<Boolean> allowCartToRunWithRepairItems;

    public static ModConfigSpec.ConfigValue<String> woodcutterRepairDiamond;
    public static ModConfigSpec.ConfigValue<String> woodcutterRepairHardened;
    public static ModConfigSpec.ConfigValue<String> woodcutterRepairNetherite;

    public static ModConfigSpec.ConfigValue<String> drillRepairIron;
    public static ModConfigSpec.ConfigValue<String> drillRepairDiamond;
    public static ModConfigSpec.ConfigValue<String> drillRepairHardened;

    public static ModConfigSpec.ConfigValue<String> farmerRepairDiamond;
    public static ModConfigSpec.ConfigValue<Boolean> assemblerInsertFuel;

    static
    {
        COMMON_BUILDER.comment("General settings").push(CATEGORY_GENERAL);
        COMMON_BUILDER.pop();

        CLIENT_BUILDER.comment("Client General settings").push(CATEGORY_GENERAL);
        CLIENT_BUILDER.pop();

        disableTimedCrafting = COMMON_BUILDER.comment("Disabled timed crafting").define("disableTimedCrafting", false);
        maxDynamites = COMMON_BUILDER.comment("The max amount of dynamites that can be held").define("maxDynamites", 50);
        drillSize = COMMON_BUILDER.comment("Max drill size").define("drillSize", 4);

        basic_solar_production = COMMON_BUILDER.comment("The amount of power the basic solar module will produce").define("basicSolarProduction", 10);
        compact_solar_production = COMMON_BUILDER.comment("The amount of power the compact solar module will produce").define("compactSolarProduction", 20);
        standard_solar_production = COMMON_BUILDER.comment("The amount of power the standard solar module will produce").define("standardSolarProduction", 20);

        woodcutterRepairDiamond = COMMON_BUILDER.comment("Repair item for Diamond Woodcutter").define("woodcutterRepairDiamond", "minecraft:diamond");
        woodcutterRepairHardened = COMMON_BUILDER.comment("Repair item for Hardened Woodcutter").define("woodcutterRepairHardened", "stevescarts:component_reinforced_metal");
        woodcutterRepairNetherite = COMMON_BUILDER.comment("Repair item for Netherite Woodcutter").define("woodcutterRepairNetherite", "minecraft:netherite_ingot");

        drillRepairIron = COMMON_BUILDER.comment("Repair item for Iron Drill").define("drillRepairIron", "minecraft:iron_ingot");
        drillRepairDiamond = COMMON_BUILDER.comment("Repair item for Diamond Drill").define("drillRepairDiamond", "minecraft:diamond");
        drillRepairHardened = COMMON_BUILDER.comment("Repair item for Hardened Drill").define("drillRepairHardened", "stevescarts:component_reinforced_metal");

        farmerRepairDiamond = COMMON_BUILDER.comment("Repair item for Diamond farmer").define("farmerRepairDiamond", "minecraft:diamond");

        allowCartToRunWithRepairItems = COMMON_BUILDER.comment("Allow carts to run with items in the tool repair slot").define("allowCartToRunWithRepairItems", false);
        assemblerInsertFuel = COMMON_BUILDER.comment("Allow fuel to be auto inserted into the cart assembler").define("assemblerInsertFuel", false);


        //Client
        useArcadeSounds = CLIENT_BUILDER.comment("Enable arcade machine sounds").define("useArcadeSounds", true);
        useArcadeMobSounds = CLIENT_BUILDER.comment("Enable mob sounds for arcade machines").define("useArcadeMobSounds", true);


        COMMON_CONFIG = COMMON_BUILDER.build();
        CLIENT_CONFIG = CLIENT_BUILDER.build();
    }

    public static void loadConfig(ModConfigSpec spec, Path path)
    {
        final CommentedFileConfig configData = CommentedFileConfig.builder(path).sync().autosave().writingMode(WritingMode.REPLACE).build();

        configData.load();
        spec.setConfig(configData);
    }
}
