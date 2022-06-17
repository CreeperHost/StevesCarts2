package vswe.stevescarts.init;

import net.minecraft.resources.ResourceLocation;
import vswe.stevescarts.Constants;
import vswe.stevescarts.api.StevesCartsAPI;
import vswe.stevescarts.api.modules.ModuleType;
import vswe.stevescarts.modules.data.ModuleData;
import vswe.stevescarts.modules.data.ModuleDataHull;
import vswe.stevescarts.modules.engines.ModuleCoalStandard;
import vswe.stevescarts.modules.engines.ModuleCoalTiny;
import vswe.stevescarts.modules.hull.ModuleReinforced;

public class StevesCartsModules
{
    public static ModuleData COAL_ENGINE;
    public static ModuleData TINY_COAL_ENGINE;
    public static ModuleData REINFORCED_HULL;


    public static void init()
    {
        COAL_ENGINE = StevesCartsAPI.registerModule(new ResourceLocation(Constants.MOD_ID, "coal_engine"),
                new ModuleData(new ResourceLocation(Constants.MOD_ID, "coal_engine"), "Coal Engine", ModuleCoalStandard.class, ModuleType.ENGINE, 15));

        TINY_COAL_ENGINE = StevesCartsAPI.registerModule(new ResourceLocation(Constants.MOD_ID, "tiny_coal_engine"),
                new ModuleData(new ResourceLocation(Constants.MOD_ID, "tiny_coal_engine"), "Tiny Coal Engine", ModuleCoalTiny.class, ModuleType.ENGINE, 2));

        REINFORCED_HULL = StevesCartsAPI.registerModule(new ResourceLocation(Constants.MOD_ID, "reinforced_hull"),
                new ModuleDataHull(new ResourceLocation(Constants.MOD_ID, "reinforced_hull"), "Reinforced Hull", ModuleReinforced.class, ModuleType.HULL).setCapacity(500).setEngineMax(5).setAddonMax(12).setComplexityMax(150));

    }
}
