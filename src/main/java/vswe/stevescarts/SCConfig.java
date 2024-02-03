package vswe.stevescarts;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.nio.file.Path;

//@Mod.EventBusSubscriber()
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
