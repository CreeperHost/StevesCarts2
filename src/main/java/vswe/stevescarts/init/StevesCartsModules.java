package vswe.stevescarts.init;

import net.minecraft.resources.ResourceLocation;
import vswe.stevescarts.Constants;
import vswe.stevescarts.api.StevesCartsAPI;
import vswe.stevescarts.api.modules.ModuleType;
import vswe.stevescarts.client.models.*;
import vswe.stevescarts.client.models.engines.*;
import vswe.stevescarts.client.models.pig.ModelPigHead;
import vswe.stevescarts.client.models.pig.ModelPigTail;
import vswe.stevescarts.client.models.realtimers.ModelGun;
import vswe.stevescarts.client.models.storages.chests.ModelExtractingChests;
import vswe.stevescarts.client.models.storages.chests.ModelFrontChest;
import vswe.stevescarts.client.models.storages.chests.ModelSideChests;
import vswe.stevescarts.client.models.storages.chests.ModelTopChest;
import vswe.stevescarts.client.models.storages.tanks.ModelAdvancedTank;
import vswe.stevescarts.client.models.storages.tanks.ModelFrontTank;
import vswe.stevescarts.client.models.storages.tanks.ModelSideTanks;
import vswe.stevescarts.client.models.storages.tanks.ModelTopTank;
import vswe.stevescarts.client.models.workers.ModelLiquidDrainer;
import vswe.stevescarts.client.models.workers.ModelRailer;
import vswe.stevescarts.client.models.workers.ModelTorchplacer;
import vswe.stevescarts.client.models.workers.ModelTrackRemover;
import vswe.stevescarts.client.models.workers.tools.ModelDrill;
import vswe.stevescarts.client.models.workers.tools.ModelFarmer;
import vswe.stevescarts.client.models.workers.tools.ModelWoodCutter;
import vswe.stevescarts.helpers.Localization;
import vswe.stevescarts.helpers.ResourceHelper;
import vswe.stevescarts.modules.data.ModuleData;
import vswe.stevescarts.modules.data.ModuleDataHull;
import vswe.stevescarts.modules.engines.ModuleCoalStandard;
import vswe.stevescarts.modules.engines.ModuleCoalTiny;
import vswe.stevescarts.modules.hull.*;

import java.util.ArrayList;

public class StevesCartsModules
{
    //HULLS
    public static ModuleData WOODEN_HULL;
    public static ModuleData STANDARD_HULL;
    public static ModuleData REINFORCED_HULL;
    public static ModuleData PUMPKIN_HULL;
    public static ModuleData MACHANICAL_PIG;
    public static ModuleData CREATIVE_HULL;
    public static ModuleData GALGADORIAN_HULL;

    //ENGINES
    public static ModuleData COAL_ENGINE;
    public static ModuleData TINY_COAL_ENGINE;


    public static void init()
    {
        WOODEN_HULL = StevesCartsAPI.registerModule(new ResourceLocation(Constants.MOD_ID, "wooden_hull"),
                new ModuleDataHull(new ResourceLocation(Constants.MOD_ID, "wooden_hull"), "Wooden Hull", ModuleWood.class, ModuleType.HULL).setCapacity(50).setEngineMax(1).setAddonMax(0).setComplexityMax(15));

        STANDARD_HULL = StevesCartsAPI.registerModule(new ResourceLocation(Constants.MOD_ID, "standard_hull"),
                new ModuleDataHull(new ResourceLocation(Constants.MOD_ID, "standard_hull"), "Standard Hull", ModuleStandard.class, ModuleType.HULL).setCapacity(200).setEngineMax(3).setAddonMax(6).setComplexityMax(50));

        REINFORCED_HULL = StevesCartsAPI.registerModule(new ResourceLocation(Constants.MOD_ID, "reinforced_hull"),
                new ModuleDataHull(new ResourceLocation(Constants.MOD_ID, "reinforced_hull"), "Reinforced Hull", ModuleReinforced.class, ModuleType.HULL).setCapacity(500).setEngineMax(5).setAddonMax(12).setComplexityMax(150));

        PUMPKIN_HULL = StevesCartsAPI.registerModule(new ResourceLocation(Constants.MOD_ID, "pumpkin_chariot"),
                new ModuleDataHull(new ResourceLocation(Constants.MOD_ID, "pumpkin_chariot"), "Pumpkin Chariot", ModulePumpkin.class, ModuleType.HULL).setCapacity(40).setEngineMax(1).setAddonMax(0).setComplexityMax(15));

        MACHANICAL_PIG = StevesCartsAPI.registerModule(new ResourceLocation(Constants.MOD_ID, "mechanical_pig"),
                new ModuleDataHull(new ResourceLocation(Constants.MOD_ID, "mechanical_pig"), "Mechanical Pig", ModulePig.class, ModuleType.HULL).setCapacity(150).setEngineMax(2).setAddonMax(4).setComplexityMax(50).addSide(ModuleData.SIDE.FRONT).addMessage(Localization.MODULE_INFO.PIG_MESSAGE));

        CREATIVE_HULL = StevesCartsAPI.registerModule(new ResourceLocation(Constants.MOD_ID, "creative_hull"),
                new ModuleDataHull(new ResourceLocation(Constants.MOD_ID, "creative_hull"), "Creative Hull", ModuleCheatHull.class, ModuleType.HULL).setCapacity(10000).setEngineMax(5).setAddonMax(12).setComplexityMax(150));

        GALGADORIAN_HULL = StevesCartsAPI.registerModule(new ResourceLocation(Constants.MOD_ID, "galgadorian_hull"),
                new ModuleDataHull(new ResourceLocation(Constants.MOD_ID, "galgadorian_hull"), "Galgadorian Hull", ModuleGalgadorian.class, ModuleType.HULL).setCapacity(1000).setEngineMax(5).setAddonMax(12).setComplexityMax(150));


        COAL_ENGINE = StevesCartsAPI.registerModule(new ResourceLocation(Constants.MOD_ID, "coal_engine"),
                new ModuleData(new ResourceLocation(Constants.MOD_ID, "coal_engine"), "Coal Engine", ModuleCoalStandard.class, ModuleType.ENGINE, 15));

        TINY_COAL_ENGINE = StevesCartsAPI.registerModule(new ResourceLocation(Constants.MOD_ID, "tiny_coal_engine"),
                new ModuleData(new ResourceLocation(Constants.MOD_ID, "tiny_coal_engine"), "Tiny Coal Engine", ModuleCoalTiny.class, ModuleType.ENGINE, 2));

    }

    public static void initModels()
    {
        WOODEN_HULL.addModel("Hull", new ModelHull(ResourceHelper.getResource("/models/hullModelWooden.png"))).addModel("Top", new ModelHullTop(ResourceHelper.getResource("/models/hullModelWoodenTop.png")));
        STANDARD_HULL.addModel("Hull", new ModelHull(ResourceHelper.getResource("/models/hullModelStandard.png"))).addModel("Top", new ModelHullTop(ResourceHelper.getResource("/models/hullModelStandardTop.png")));
        REINFORCED_HULL.addModel("Hull", new ModelHull(ResourceHelper.getResource("/models/hullModelLarge.png"))).addModel("Top", new ModelHullTop(ResourceHelper.getResource("/models/hullModelLargeTop.png")));
        PUMPKIN_HULL.addModel("Hull", new ModelPumpkinHull(ResourceHelper.getResource("/models/hullModelPumpkin.png"), ResourceHelper.getResource("/models/hullModelWooden.png"))).addModel("Top", new ModelPumpkinHullTop(ResourceHelper.getResource("/models/hullModelPumpkinTop.png"), ResourceHelper.getResource("/models/hullModelWoodenTop.png")));
        MACHANICAL_PIG.addModel("Hull", new ModelHull(ResourceHelper.getResource("/models/hullModelPig.png"))).addModel("Top", new ModelHullTop(ResourceHelper.getResource("/models/hullModelPigTop.png"))).addModel("Head", new ModelPigHead()).addModel("Tail", new ModelPigTail());
        CREATIVE_HULL.addModel("Hull", new ModelHull(ResourceHelper.getResource("/models/hullModelCreative.png"))).addModel("Top", new ModelHullTop(ResourceHelper.getResource("/models/hullModelCreativeTop.png")));
        GALGADORIAN_HULL.addModel("Hull", new ModelHull(ResourceHelper.getResource("/models/hullModelGalgadorian.png"))).addModel("Top", new ModelHullTop(ResourceHelper.getResource("/models/hullModelGalgadorianTop.png")));


        COAL_ENGINE.addModel("Engine", new ModelEngineFrame()).addModel("Fire", new ModelEngineInside());
        TINY_COAL_ENGINE.addModel("Engine", new ModelEngineFrame()).addModel("Fire", new ModelEngineInside());

//        ModuleData.moduleList.get((byte) 1).addModel("SolarPanelBase", new ModelSolarPanelBase()).addModel("SolarPanels", new ModelSolarPanelHeads(4)).removeModel("Top");
//        ModuleData.moduleList.get((byte) 45).addModel("SolarPanelBase", new ModelSolarPanelBase()).addModel("SolarPanels", new ModelSolarPanelHeads(2)).removeModel("Top");
//        ModuleData.moduleList.get((byte) 56).addModel("SolarPanelSide", new ModelCompactSolarPanel());
//        ModuleData.moduleList.get((byte) 2).addModel("SideChest", new ModelSideChests());
//        ModuleData.moduleList.get((byte) 3).removeModel("Top").addModel("TopChest", new ModelTopChest());
//        ModuleData.moduleList.get((byte) 4).addModel("FrontChest", new ModelFrontChest()).setModelMult(0.68f);
//        ModuleData.moduleList.get((byte) 6).addModel("SideChest", new ModelExtractingChests());
//        ModuleData.moduleList.get((byte) 7).addModel("Torch", new ModelTorchplacer());
//        ModuleData.moduleList.get((byte) 8).addModel("Drill", new ModelDrill(ResourceHelper.getResource("/models/drillModelDiamond.png"))).addModel("Plate", new ModelToolPlate());
//        ModuleData.moduleList.get((byte) 42).addModel("Drill", new ModelDrill(ResourceHelper.getResource("/models/drillModelIron.png"))).addModel("Plate", new ModelToolPlate());
//        ModuleData.moduleList.get((byte) 43).addModel("Drill", new ModelDrill(ResourceHelper.getResource("/models/drillModelHardened.png"))).addModel("Plate", new ModelToolPlate());
//        ModuleData.moduleList.get((byte) 9).addModel("Drill", new ModelDrill(ResourceHelper.getResource("/models/drillModelMagic.png"))).addModel("Plate", new ModelToolPlate());
//        ModuleData.moduleList.get((byte) 10).addModel("Rails", new ModelRailer(3));
//        ModuleData.moduleList.get((byte) 11).addModel("Rails", new ModelRailer(6));
//        ModuleData.moduleList.get((byte) 12).addModel("Bridge", new ModelBridge()).addModel("Plate", new ModelToolPlate());
//        ModuleData.moduleList.get((byte) 13).addModel("Remover", new ModelTrackRemover()).setModelMult(0.6f);
//        ModuleData.moduleList.get((byte) 14).addModel("Farmer", new ModelFarmer(ResourceHelper.getResource("/models/farmerModelDiamond.png"))).setModelMult(0.45f);
//        ModuleData.moduleList.get((byte) 84).addModel("Farmer", new ModelFarmer(ResourceHelper.getResource("/models/farmerModelGalgadorian.png"))).setModelMult(0.45f);
//        ModuleData.moduleList.get((byte) 15).addModel("WoodCutter", new ModelWoodCutter(ResourceHelper.getResource("/models/woodCutterModelDiamond.png"))).addModel("Plate", new ModelToolPlate());
//        ModuleData.moduleList.get((byte) 79).addModel("WoodCutter", new ModelWoodCutter(ResourceHelper.getResource("/models/woodCutterModelHardened.png"))).addModel("Plate", new ModelToolPlate());
//        ModuleData.moduleList.get((byte) 80).addModel("WoodCutter", new ModelWoodCutter(ResourceHelper.getResource("/models/woodCutterModelGalgadorian.png"))).addModel("Plate", new ModelToolPlate());
//        ModuleData.moduleList.get((byte) 102).addModel("WoodCutter", new ModelWoodCutter(ResourceHelper.getResource("/models/woodcuttermodelnetherite.png"))).addModel("Plate", new ModelToolPlate());
//
//        ModuleData.moduleList.get((byte) 20).addModel("Sensor", new ModelLiquidSensors());
//        ModuleData.moduleList.get((byte) 25).removeModel("Top").addModel("Chair", new ModelSeat());
//        ModuleData.moduleList.get((byte) 26).addModel("Lever", new ModelLever(ResourceHelper.getResource("/models/leverModel.png")));
//        ModuleData.moduleList.get((byte) 27).addModel("Lever", new ModelLever(ResourceHelper.getResource("/models/leverModel2.png"))).addModel("Wheel", new ModelWheel());
//        final ArrayList<Integer> pipes = new ArrayList<>();
//        for (int i = 0; i < 9; ++i)
//        {
//            if (i != 4)
//            {
//                pipes.add(i);
//            }
//        }
//        ModuleData.moduleList.get((byte) 28).addModel("Rig", new ModelShootingRig()).addModel("Pipes", new ModelGun(pipes));
//        ModuleData.moduleList.get((byte) 29).addModel("Rig", new ModelShootingRig()).addModel("MobDetector", new ModelMobDetector()).addModel("Pipes", new ModelSniperRifle());
//        ModuleData.moduleList.get((byte) 30).addModel("Top", new ModelHullTop(ResourceHelper.getResource("/models/cleanerModelTop.png"))).addModel("Cleaner", new ModelCleaner());
//        ModuleData.moduleList.get((byte) 31).addModel("Tnt", new ModelDynamite());
//        ModuleData.moduleList.get((byte) 32).addModel("Shield", new ModelShield()).setModelMult(0.68f);
        //        ModuleData.moduleList.get((byte) 40).setModelMult(0.65f).addModel("Speakers", new ModelNote());
//        ModuleData.moduleList.get((byte) 57).removeModel("Top").addModel("Cage", new ModelCage(), false).addModel("Cage", new ModelCage(), true).setModelMult(0.65f);
//        ModuleData.moduleList.get((byte) 64).addModel("SideTanks", new ModelSideTanks());
//        ModuleData.moduleList.get((byte) 65).addModel("TopTank", new ModelTopTank());
//        ModuleData.moduleList.get((byte) 66).addModel("LargeTank", new ModelAdvancedTank()).removeModel("Top");
//        ModuleData.moduleList.get((byte) 67).setModelMult(0.68f).addModel("FrontTank", new ModelFrontTank());
//        ModuleData.moduleList.get((byte) 71).addModel("Top", new ModelHullTop(ResourceHelper.getResource("/models/cleanerModelTop.png"))).addModel("Cleaner", new ModelLiquidDrainer());
//        ModuleData.moduleList.get((byte) 74).addModel("TopChest", new ModelEggBasket());
//        ModuleData.moduleList.get((byte) 85).addModel("LawnMower", new ModelLawnMower()).setModelMult(0.4f);
//        ModuleData.moduleList.get((byte) 99).addModel("Cake", new ModelCake());
//        ModuleData.moduleList.get((byte) 100).addModel("Cake", new ModelCake());
    }
}
