package vswe.stevescarts;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.Mod;

import java.nio.file.Path;

@Mod.EventBusSubscriber()
public class SCConfig
{
    public static final String CATEGORY_GENERAL = "general";
    private static final ForgeConfigSpec.Builder COMMON_BUILDER = new ForgeConfigSpec.Builder();
    private static final ForgeConfigSpec.Builder CLIENT_BUILDER = new ForgeConfigSpec.Builder();

    public static ForgeConfigSpec COMMON_CONFIG;
    public static ForgeConfigSpec CLIENT_CONFIG;

    public static ForgeConfigSpec.ConfigValue<Boolean> disableTimedCrafting;
    public static ForgeConfigSpec.ConfigValue<Integer> maxDynamites;
    public static ForgeConfigSpec.ConfigValue<Boolean> useArcadeSounds;
    public static ForgeConfigSpec.ConfigValue<Boolean> useArcadeMobSounds;
    public static ForgeConfigSpec.ConfigValue<Integer> drillSize;
    public static ForgeConfigSpec.ConfigValue<Integer> basic_solar_production;
    public static ForgeConfigSpec.ConfigValue<Integer> compact_solar_production;
    public static ForgeConfigSpec.ConfigValue<Integer> standard_solar_production;

    static
    {
        COMMON_BUILDER.comment("General settings").push(CATEGORY_GENERAL);
        COMMON_BUILDER.pop();

        CLIENT_BUILDER.comment("General settings").push(CATEGORY_GENERAL);
        CLIENT_BUILDER.pop();

        disableTimedCrafting = COMMON_BUILDER.comment("Disabled timed crafting").define("disableTimedCrafting", false);
        maxDynamites = COMMON_BUILDER.comment("The max amount of dynamites that can be held").define("maxDynamites", 50);
        drillSize = COMMON_BUILDER.comment("Max drill size").define("drillSize", 4);

        basic_solar_production = COMMON_BUILDER.comment("The amount of power the basic solar module will produce").define("basicSolarProduction", 2);
        compact_solar_production = COMMON_BUILDER.comment("The amount of power the compact solar module will produce").define("compactSolarProduction", 5);
        standard_solar_production = COMMON_BUILDER.comment("The amount of power the standard solar module will produce").define("standardSolarProduction", 5);


        //Client
        useArcadeSounds = CLIENT_BUILDER.comment("Enable arcade machine sounds").define("useArcadeSounds", true);
        useArcadeMobSounds = CLIENT_BUILDER.comment("Enable mob sounds for arcade machines").define("useArcadeMobSounds", true);


        COMMON_CONFIG = COMMON_BUILDER.build();
        CLIENT_CONFIG = CLIENT_BUILDER.build();
    }

    public static void loadConfig(ForgeConfigSpec spec, Path path)
    {
        final CommentedFileConfig configData = CommentedFileConfig.builder(path).sync().autosave().writingMode(WritingMode.REPLACE).build();

        configData.load();
        spec.setConfig(configData);
    }
}
