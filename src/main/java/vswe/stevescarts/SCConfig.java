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
    public static ModConfigSpec.ConfigValue<Integer> woodcutterRepairAmountDiamond;
    public static ModConfigSpec.ConfigValue<Integer> woodcutterDurabilityDiamond;
    public static ModConfigSpec.ConfigValue<String> woodcutterRepairHardened;
    public static ModConfigSpec.ConfigValue<Integer> woodcutterRepairAmountHardened;
    public static ModConfigSpec.ConfigValue<Integer> woodcutterDurabilityHardened;
    public static ModConfigSpec.ConfigValue<String> woodcutterRepairNetherite;
    public static ModConfigSpec.ConfigValue<Integer> woodcutterRepairAmountNetherite;
    public static ModConfigSpec.ConfigValue<Integer> woodcutterDurabilityNetherite;

    public static ModConfigSpec.ConfigValue<String> drillRepairIron;
    public static ModConfigSpec.ConfigValue<Integer> drillRepairAmountIron;
    public static ModConfigSpec.ConfigValue<Integer> drillDurabilityIron;
    public static ModConfigSpec.ConfigValue<String> drillRepairDiamond;
    public static ModConfigSpec.ConfigValue<Integer> drillRepairAmountDiamond;
    public static ModConfigSpec.ConfigValue<Integer> drillDurabilityDiamond;
    public static ModConfigSpec.ConfigValue<String> drillRepairHardened;
    public static ModConfigSpec.ConfigValue<Integer> drillRepairAmountHardened;
    public static ModConfigSpec.ConfigValue<Integer> drillDurabilityHardened;

    public static ModConfigSpec.ConfigValue<String> farmerRepairDiamond;
    public static ModConfigSpec.ConfigValue<Integer> farmerRepairAmountDiamond;
    public static ModConfigSpec.ConfigValue<Integer> farmerDurabilityDiamond;

    public static ModConfigSpec.ConfigValue<Boolean> assemblerInsertFuel;

    static {
        CLIENT_BUILDER.comment("Client General settings").push(CATEGORY_GENERAL);
        CLIENT_BUILDER.pop();

        disableTimedCrafting = COMMON_BUILDER.comment("Disabled timed crafting").define("disableTimedCrafting", false);
        maxDynamites = COMMON_BUILDER.comment("The max amount of dynamites that can be held").define("maxDynamites", 50);
        drillSize = COMMON_BUILDER.comment("Max drill size").define("drillSize", 4);

        basic_solar_production = COMMON_BUILDER.comment("The amount of power the basic solar module will produce").define("basicSolarProduction", 10);
        compact_solar_production = COMMON_BUILDER.comment("The amount of power the compact solar module will produce").define("compactSolarProduction", 20);
        standard_solar_production = COMMON_BUILDER.comment("The amount of power the standard solar module will produce").define("standardSolarProduction", 20);

        woodcutterRepairDiamond = COMMON_BUILDER.comment("Repair item for Diamond Woodcutter").define("woodcutterRepairDiamond", "minecraft:diamond");
        woodcutterRepairAmountDiamond = COMMON_BUILDER.comment("Repair value for Diamond Woodcutter repair item").define("woodcutterRepairAmountDiamond", 160000);
        woodcutterDurabilityDiamond = COMMON_BUILDER.comment("Diamond Woodcutter max durability").define("woodcutterDurabilityDiamond", 320000);

        woodcutterRepairHardened = COMMON_BUILDER.comment("Repair item for Hardened Woodcutter").define("woodcutterRepairHardened", "stevescarts:component_reinforced_metal");
        woodcutterRepairAmountHardened = COMMON_BUILDER.comment("Repair value for Hardened Woodcutter repair item").define("woodcutterRepairAmountHardened", 320000);
        woodcutterDurabilityHardened = COMMON_BUILDER.comment("Hardened Woodcutter max durability").define("woodcutterDurabilityHardened", 640000);

        woodcutterRepairNetherite = COMMON_BUILDER.comment("Repair item for Netherite Woodcutter").define("woodcutterRepairNetherite", "minecraft:netherite_ingot");
        woodcutterRepairAmountNetherite = COMMON_BUILDER.comment("Repair value for Netherite Woodcutter repair item").define("woodcutterRepairAmountNetherite", 450000);
        woodcutterDurabilityNetherite = COMMON_BUILDER.comment("Netherite Woodcutter max durability").define("woodcutterDurabilityNetherite", 1000000);


        drillRepairIron = COMMON_BUILDER.comment("Repair item for Iron Drill").define("drillRepairIron", "minecraft:iron_ingot");
        drillRepairAmountIron = COMMON_BUILDER.comment("Repair value for Iron Drill repair item").define("drillRepairAmountIron", 20000);
        drillDurabilityIron = COMMON_BUILDER.comment("Iron Drill max durability").define("drillDurabilityIron", 50000);

        drillRepairDiamond = COMMON_BUILDER.comment("Repair item for Diamond Drill").define("drillRepairDiamond", "minecraft:diamond");
        drillRepairAmountDiamond = COMMON_BUILDER.comment("Repair value for Diamond Drill repair item").define("drillRepairAmountDiamond", 100000);
        drillDurabilityDiamond = COMMON_BUILDER.comment("Diamond Drill max durability").define("drillDurabilityDiamond", 300000);

        drillRepairHardened = COMMON_BUILDER.comment("Repair item for Hardened Drill").define("drillRepairHardened", "stevescarts:component_reinforced_metal");
        drillRepairAmountHardened = COMMON_BUILDER.comment("Repair value for Hardened Drill repair item").define("drillRepairAmountHardened", 450000);
        drillDurabilityHardened = COMMON_BUILDER.comment("Hardened Drill max durability").define("drillDurabilityHardened", 1000000);


        farmerRepairDiamond = COMMON_BUILDER.comment("Repair item for Diamond farmer").define("farmerRepairDiamond", "minecraft:diamond");
        farmerRepairAmountDiamond = COMMON_BUILDER.comment("Repair value for Diamond farmer repair item").define("farmerRepairAmountDiamond", 150000);
        farmerDurabilityDiamond = COMMON_BUILDER.comment("Diamond farmer max durability").define("farmerDurabilityDiamond", 300000);


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
