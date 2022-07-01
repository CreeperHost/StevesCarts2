package vswe.stevescarts.init;

import net.minecraft.resources.ResourceLocation;
import vswe.stevescarts.Constants;
import vswe.stevescarts.api.StevesCartsAPI;
import vswe.stevescarts.api.modules.ModuleType;
import vswe.stevescarts.client.models.*;
import vswe.stevescarts.client.models.engines.*;
import vswe.stevescarts.client.models.pig.ModelPigHead;
import vswe.stevescarts.client.models.pig.ModelPigTail;
import vswe.stevescarts.client.models.storages.chests.ModelFrontChest;
import vswe.stevescarts.client.models.storages.chests.ModelSideChests;
import vswe.stevescarts.client.models.storages.chests.ModelTopChest;
import vswe.stevescarts.client.models.storages.tanks.ModelAdvancedTank;
import vswe.stevescarts.client.models.storages.tanks.ModelFrontTank;
import vswe.stevescarts.client.models.storages.tanks.ModelSideTanks;
import vswe.stevescarts.client.models.storages.tanks.ModelTopTank;
import vswe.stevescarts.client.models.workers.ModelRailer;
import vswe.stevescarts.client.models.workers.ModelTorchplacer;
import vswe.stevescarts.client.models.workers.ModelTrackRemover;
import vswe.stevescarts.client.models.workers.tools.ModelDrill;
import vswe.stevescarts.client.models.workers.tools.ModelFarmer;
import vswe.stevescarts.client.models.workers.tools.ModelWoodCutter;
import vswe.stevescarts.helpers.Localization;
import vswe.stevescarts.helpers.ResourceHelper;
import vswe.stevescarts.modules.addons.*;
import vswe.stevescarts.modules.addons.mobdetectors.*;
import vswe.stevescarts.modules.addons.plants.ModuleNetherwart;
import vswe.stevescarts.modules.data.ModuleData;
import vswe.stevescarts.modules.data.ModuleDataHull;
import vswe.stevescarts.modules.engines.*;
import vswe.stevescarts.modules.hull.*;
import vswe.stevescarts.modules.realtimers.*;
import vswe.stevescarts.modules.storages.chests.ModuleFrontChest;
import vswe.stevescarts.modules.storages.chests.ModuleInternalStorage;
import vswe.stevescarts.modules.storages.chests.ModuleSideChests;
import vswe.stevescarts.modules.storages.chests.ModuleTopChest;
import vswe.stevescarts.modules.storages.tanks.*;
import vswe.stevescarts.modules.workers.*;
import vswe.stevescarts.modules.workers.tools.*;

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
    public static ModuleData CREATIVE_ENGINE;
    public static ModuleData THERMAL_ENGINE;
    public static ModuleData ADVANCED_THERMAL_ENGINE;

    public static ModuleData SOLAR_ENGINE;
    public static ModuleData BASIC_SOLAR_ENGINE;
    public static ModuleData COMPACT_SOLAR_ENGINE;

    //Chest
    public static ModuleData SIDE_CHESTS;
    public static ModuleData TOP_CHEST;
    public static ModuleData FRONT_CHEST;
    public static ModuleData INTERNAL_STORAGE;

    //Tools
    public static ModuleData TORCH_PLACER;
    public static ModuleData BASIC_DRILL;
    public static ModuleData IRON_DRILL;
    public static ModuleData HARDENED_DRILL;
    public static ModuleData GALGADORIAN_DRILL;

    public static ModuleData RAILER;
    public static ModuleData LARGE_RAILER;

    public static ModuleData BRIDGE_BUILDER;
    public static ModuleData TRACK_REMOVER;

    public static ModuleData BASIC_FARMER;
    public static ModuleData GALGADORIAN_FARMER;

    public static ModuleData BASIC_WOOD_CUTTER;
    public static ModuleData HARDENED_WOOD_CUTTER;
    public static ModuleData GALGADORIAN_WOOD_CUTTER;
    public static ModuleData NETHERITE_WOOD_CUTTER;

    public static ModuleData HYDRATER;
    public static ModuleData FERTILIZER;
    public static ModuleData HEIGHT_CONTROLLER;
    public static ModuleData LIQUID_SENSORS;
    public static ModuleData SEAT;
    public static ModuleData BRAKE;
    public static ModuleData ADVANCED_CONTROL_SYSTEM;
    public static ModuleData CLEANER;
    public static ModuleData DYNAMITE_CARRIER;
    public static ModuleData DIVINE_SHIELD;
    public static ModuleData MELTER;
    public static ModuleData EXTREME_MELTER;
    public static ModuleData INVISIBILITY_CORE;
    public static ModuleData NOTE_SEQUENCER;
    public static ModuleData FREEZER;
    public static ModuleData CAGE;
    public static ModuleData FIREWORK_DISPLAY;

    //Detectors
    public static ModuleData ENTITY_DETECTOR_ANIMAL;
    public static ModuleData ENTITY_DETECTOR_PLAYER;
    public static ModuleData ENTITY_DETECTOR_VILLAGER;
    public static ModuleData ENTITY_DETECTOR_MONSTER;
    public static ModuleData ENTITY_DETECTOR_BAT;

    public static ModuleData CROP_NETHER_WART;

    public static ModuleData INTERNAL_TANK;
    public static ModuleData SIDE_TANKS;
    public static ModuleData TOP_TANK;
    public static ModuleData ADVANCED_TANK;
    public static ModuleData FRONT_TANK;
    public static ModuleData OPEN_TANK;

    public static void init()
    {
        //Hulls
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


        //Engines
        COAL_ENGINE = StevesCartsAPI.registerModule(new ResourceLocation(Constants.MOD_ID, "coal_engine"),
                new ModuleData(new ResourceLocation(Constants.MOD_ID, "coal_engine"), "Coal Engine", ModuleCoalStandard.class, ModuleType.ENGINE, 15));

        TINY_COAL_ENGINE = StevesCartsAPI.registerModule(new ResourceLocation(Constants.MOD_ID, "tiny_coal_engine"),
                new ModuleData(new ResourceLocation(Constants.MOD_ID, "tiny_coal_engine"), "Tiny Coal Engine", ModuleCoalTiny.class, ModuleType.ENGINE, 2));

        CREATIVE_ENGINE = StevesCartsAPI.registerModule(new ResourceLocation(Constants.MOD_ID, "creative_engine"),
                new ModuleData(new ResourceLocation(Constants.MOD_ID, "creative_engine"), "Creative Engine", ModuleCheatEngine.class, ModuleType.ENGINE, 1));

        THERMAL_ENGINE = StevesCartsAPI.registerModule(new ResourceLocation(Constants.MOD_ID, "thermal_engine"),
                new ModuleData(new ResourceLocation(Constants.MOD_ID, "thermal_engine"), "Thermal Engine", ModuleThermalStandard.class, ModuleType.ENGINE, 28));//.addRequirement(tankGroup);

        ADVANCED_THERMAL_ENGINE = StevesCartsAPI.registerModule(new ResourceLocation(Constants.MOD_ID, "advanced_thermal_engine"),
                new ModuleData(new ResourceLocation(Constants.MOD_ID, "advanced_thermal_engine"), "Advanced Thermal Engine", ModuleThermalAdvanced.class, ModuleType.ENGINE, 58));//.addRequirement(tankGroup);


        //Solar engine
        SOLAR_ENGINE = StevesCartsAPI.registerModule(new ResourceLocation(Constants.MOD_ID, "solar_engine"),
                new ModuleData(new ResourceLocation(Constants.MOD_ID, "solar_engine"), "Solar Engine", ModuleSolarStandard.class, ModuleType.ENGINE, 20).addSides(new ModuleData.SIDE[]{ModuleData.SIDE.CENTER, ModuleData.SIDE.TOP}));

        BASIC_SOLAR_ENGINE = StevesCartsAPI.registerModule(new ResourceLocation(Constants.MOD_ID, "basic_solar_engine"),
                new ModuleData(new ResourceLocation(Constants.MOD_ID, "basic_solar_engine"), "Basic Solar Engine", ModuleSolarBasic.class, ModuleType.ENGINE, 12).addSides(new ModuleData.SIDE[]{ModuleData.SIDE.CENTER, ModuleData.SIDE.TOP}));

        COMPACT_SOLAR_ENGINE = StevesCartsAPI.registerModule(new ResourceLocation(Constants.MOD_ID, "compact_solar_engine"),
                new ModuleData(new ResourceLocation(Constants.MOD_ID, "compact_solar_engine"), "Compact Solar Engine", ModuleSolarCompact.class, ModuleType.ENGINE, 32).addSides(new ModuleData.SIDE[]{ModuleData.SIDE.RIGHT, ModuleData.SIDE.LEFT}));


        //Chest
        SIDE_CHESTS = StevesCartsAPI.registerModule(new ResourceLocation(Constants.MOD_ID, "side_chests"),
                new ModuleData(new ResourceLocation(Constants.MOD_ID, "side_chests"), "Side Chests", ModuleSideChests.class, ModuleType.STORAGE, 3).addSides(new ModuleData.SIDE[]{ModuleData.SIDE.RIGHT, ModuleData.SIDE.LEFT}));

        TOP_CHEST = StevesCartsAPI.registerModule(new ResourceLocation(Constants.MOD_ID, "top_chest"),
                new ModuleData(new ResourceLocation(Constants.MOD_ID, "top_chest"), "Top Chest", ModuleTopChest.class, ModuleType.STORAGE, 6).addSides(new ModuleData.SIDE[]{ModuleData.SIDE.TOP}));

        FRONT_CHEST = StevesCartsAPI.registerModule(new ResourceLocation(Constants.MOD_ID, "front_chest"),
                new ModuleData(new ResourceLocation(Constants.MOD_ID, "front_chest"), "Front Chest", ModuleFrontChest.class, ModuleType.STORAGE, 6).addSides(new ModuleData.SIDE[]{ModuleData.SIDE.FRONT}));

        INTERNAL_STORAGE = StevesCartsAPI.registerModule(new ResourceLocation(Constants.MOD_ID, "internal_storage"),
                new ModuleData(new ResourceLocation(Constants.MOD_ID, "internal_storage"), "Internal Storage", ModuleInternalStorage.class, ModuleType.STORAGE, 6));


        //TOOLS
        TORCH_PLACER = StevesCartsAPI.registerModule(new ResourceLocation(Constants.MOD_ID, "torch_placer"),
                new ModuleData(new ResourceLocation(Constants.MOD_ID, "torch_placer"), "Torch Placer", ModuleTorch.class, ModuleType.TOOL, 10)).addSides(new ModuleData.SIDE[]{ModuleData.SIDE.RIGHT, ModuleData.SIDE.LEFT});

        BASIC_DRILL = StevesCartsAPI.registerModule(new ResourceLocation(Constants.MOD_ID, "basic_drill"),
                new ModuleData(new ResourceLocation(Constants.MOD_ID, "basic_drill"), "Basic Drill", ModuleDrillDiamond.class, ModuleType.TOOL, 10)).addSide(ModuleData.SIDE.FRONT);

        IRON_DRILL = StevesCartsAPI.registerModule(new ResourceLocation(Constants.MOD_ID, "iron_drill"),
                new ModuleData(new ResourceLocation(Constants.MOD_ID, "iron_drill"), "Iron Drill", ModuleDrillIron.class, ModuleType.TOOL, 10)).addSide(ModuleData.SIDE.FRONT);

        HARDENED_DRILL = StevesCartsAPI.registerModule(new ResourceLocation(Constants.MOD_ID, "hardened_drill"),
                new ModuleData(new ResourceLocation(Constants.MOD_ID, "hardened_drill"), "Hardened Drill", ModuleDrillHardened.class, ModuleType.TOOL, 45)).addSide(ModuleData.SIDE.FRONT);

        GALGADORIAN_DRILL = StevesCartsAPI.registerModule(new ResourceLocation(Constants.MOD_ID, "galgadorian_drill"),
                new ModuleData(new ResourceLocation(Constants.MOD_ID, "galgadorian_drill"), "Galgadorian Drill", ModuleDrillGalgadorian.class, ModuleType.TOOL, 45)).addSide(ModuleData.SIDE.FRONT);

        RAILER = StevesCartsAPI.registerModule(new ResourceLocation(Constants.MOD_ID, "railer"),
                new ModuleData(new ResourceLocation(Constants.MOD_ID, "railer"), "Railer", ModuleRailer.class, ModuleType.TOOL, 3));

        LARGE_RAILER = StevesCartsAPI.registerModule(new ResourceLocation(Constants.MOD_ID, "large_railer"),
                new ModuleData(new ResourceLocation(Constants.MOD_ID, "large_railer"), "Large Railer", ModuleRailerLarge.class, ModuleType.TOOL, 17));

        BRIDGE_BUILDER = StevesCartsAPI.registerModule(new ResourceLocation(Constants.MOD_ID, "bridge_builder"),
                new ModuleData(new ResourceLocation(Constants.MOD_ID, "bridge_builder"), "Bridge Builder", ModuleBridge.class, ModuleType.TOOL, 14));

        TRACK_REMOVER = StevesCartsAPI.registerModule(new ResourceLocation(Constants.MOD_ID, "track_remover"),
                new ModuleData(new ResourceLocation(Constants.MOD_ID, "track_remover"), "Track Remover", ModuleRemover.class, ModuleType.TOOL, 8).addSides(new ModuleData.SIDE[]{ModuleData.SIDE.TOP, ModuleData.SIDE.BACK}));

        BASIC_FARMER = StevesCartsAPI.registerModule(new ResourceLocation(Constants.MOD_ID, "basic_farmer"),
                new ModuleData(new ResourceLocation(Constants.MOD_ID, "basic_farmer"), "Basic Farmer", ModuleFarmerDiamond.class, ModuleType.TOOL, 36).addSide(ModuleData.SIDE.FRONT));

        GALGADORIAN_FARMER = StevesCartsAPI.registerModule(new ResourceLocation(Constants.MOD_ID, "galgadorian_farmer"),
                new ModuleData(new ResourceLocation(Constants.MOD_ID, "galgadorian_farmer"), "Galgadorian Farmer", ModuleFarmerGalgadorian.class, ModuleType.TOOL, 55).addSide(ModuleData.SIDE.FRONT));


        BASIC_WOOD_CUTTER = StevesCartsAPI.registerModule(new ResourceLocation(Constants.MOD_ID, "basic_wood_cutter"),
                new ModuleData(new ResourceLocation(Constants.MOD_ID, "basic_wood_cutter"), "Basic Wood Cutter", ModuleWoodcutterDiamond.class, ModuleType.TOOL, 34).addSide(ModuleData.SIDE.FRONT));

        HARDENED_WOOD_CUTTER = StevesCartsAPI.registerModule(new ResourceLocation(Constants.MOD_ID, "hardened_wood_cutter"),
                new ModuleData(new ResourceLocation(Constants.MOD_ID, "hardened_wood_cutter"), "Hardened Wood Cutter", ModuleWoodcutterHardened.class, ModuleType.TOOL, 65).addSide(ModuleData.SIDE.FRONT));

        GALGADORIAN_WOOD_CUTTER = StevesCartsAPI.registerModule(new ResourceLocation(Constants.MOD_ID, "galgadorian_wood_cutter"),
                new ModuleData(new ResourceLocation(Constants.MOD_ID, "galgadorian_wood_cutter"), "Galgadorian Wood Cutter", ModuleWoodcutterGalgadorian.class, ModuleType.TOOL, 120).addSide(ModuleData.SIDE.FRONT));

        NETHERITE_WOOD_CUTTER = StevesCartsAPI.registerModule(new ResourceLocation(Constants.MOD_ID, "netherite_wood_cutter"),
                new ModuleData(new ResourceLocation(Constants.MOD_ID, "netherite_wood_cutter"), "Netherite Wood Cutter", ModuleWoodcutterNetherite.class, ModuleType.TOOL, 120).addSide(ModuleData.SIDE.FRONT));


        //ADDONS
        HYDRATER = StevesCartsAPI.registerModule(new ResourceLocation(Constants.MOD_ID, "hydrator"),
                new ModuleData(new ResourceLocation(Constants.MOD_ID, "hydrator"), "Hydrator", ModuleHydrater.class, ModuleType.ADDON, 6));//.addRequirement(tankGroup);

        FERTILIZER = StevesCartsAPI.registerModule(new ResourceLocation(Constants.MOD_ID, "fertilizer"),
                new ModuleData(new ResourceLocation(Constants.MOD_ID, "fertilizer"), "Fertilizer", ModuleFertilizer.class, ModuleType.ADDON, 10));

        HEIGHT_CONTROLLER = StevesCartsAPI.registerModule(new ResourceLocation(Constants.MOD_ID, "height_controller"),
                new ModuleData(new ResourceLocation(Constants.MOD_ID, "height_controller"), "Height Controller", ModuleHeightControl.class, ModuleType.ADDON, 20));

        LIQUID_SENSORS = StevesCartsAPI.registerModule(new ResourceLocation(Constants.MOD_ID, "liquid_sensors"),
                new ModuleData(new ResourceLocation(Constants.MOD_ID, "liquid_sensors"), "Liquid Sensors", ModuleLiquidSensors.class, ModuleType.ADDON, 27));//.addRequirement(drillGroup);

        SEAT = StevesCartsAPI.registerModule(new ResourceLocation(Constants.MOD_ID, "seat"),
                new ModuleData(new ResourceLocation(Constants.MOD_ID, "seat"), "Seat", ModuleSeat.class, ModuleType.ADDON, 3).addSides(new ModuleData.SIDE[]{ModuleData.SIDE.CENTER, ModuleData.SIDE.TOP}));

        BRAKE = StevesCartsAPI.registerModule(new ResourceLocation(Constants.MOD_ID, "brake_handle"),
                new ModuleData(new ResourceLocation(Constants.MOD_ID, "brake_handle"), "Brake Handle", ModuleBrake.class, ModuleType.ADDON, 12).addParent(SEAT));

        ADVANCED_CONTROL_SYSTEM = StevesCartsAPI.registerModule(new ResourceLocation(Constants.MOD_ID, "advanced_control_system"),
                new ModuleData(new ResourceLocation(Constants.MOD_ID, "advanced_control_system"), "Advanced Control System", ModuleAdvControl.class, ModuleType.ADDON, 38).addSide(ModuleData.SIDE.RIGHT).addParent(SEAT));

        ENTITY_DETECTOR_ANIMAL = StevesCartsAPI.registerModule(new ResourceLocation(Constants.MOD_ID, "entity_detector_animal"),
                new ModuleData(new ResourceLocation(Constants.MOD_ID, "entity_detector_animal"), "Entity Detector: Animal", ModuleAnimal.class, ModuleType.ADDON, 1));

        ENTITY_DETECTOR_PLAYER = StevesCartsAPI.registerModule(new ResourceLocation(Constants.MOD_ID, "entity_detector_player"),
                new ModuleData(new ResourceLocation(Constants.MOD_ID, "entity_detector_player"), "Entity Detector: Player", ModulePlayer.class, ModuleType.ADDON, 7));

        ENTITY_DETECTOR_VILLAGER = StevesCartsAPI.registerModule(new ResourceLocation(Constants.MOD_ID, "entity_detector_villager"),
                new ModuleData(new ResourceLocation(Constants.MOD_ID, "entity_detector_villager"), "Entity Detector: Villager", ModuleVillager.class, ModuleType.ADDON, 1));

        ENTITY_DETECTOR_MONSTER = StevesCartsAPI.registerModule(new ResourceLocation(Constants.MOD_ID, "entity_detector_monster"),
                new ModuleData(new ResourceLocation(Constants.MOD_ID, "entity_detector_monster"), "Entity Detector: Monster", ModuleMonster.class, ModuleType.ADDON, 1));

        ENTITY_DETECTOR_BAT = StevesCartsAPI.registerModule(new ResourceLocation(Constants.MOD_ID, "entity_detector_bat"),
                new ModuleData(new ResourceLocation(Constants.MOD_ID, "entity_detector_bat"), "Entity Detector: Bat", ModuleBat.class, ModuleType.ADDON, 1));

        CLEANER = StevesCartsAPI.registerModule(new ResourceLocation(Constants.MOD_ID, "cleaning_machine"),
                new ModuleData(new ResourceLocation(Constants.MOD_ID, "cleaning_machine"), "Cleaning Machine", ModuleCleaner.class, ModuleType.ADDON, 23).addSide(ModuleData.SIDE.CENTER));

        DYNAMITE_CARRIER = StevesCartsAPI.registerModule(new ResourceLocation(Constants.MOD_ID, "dynamite_carrier"),
                new ModuleData(new ResourceLocation(Constants.MOD_ID, "dynamite_carrier"), "Dynamite Carrier", ModuleDynamite.class, ModuleType.ADDON, 3).addSide(ModuleData.SIDE.TOP));

        DIVINE_SHIELD = StevesCartsAPI.registerModule(new ResourceLocation(Constants.MOD_ID, "divine_shield"),
                new ModuleData(new ResourceLocation(Constants.MOD_ID, "divine_shield"), "Divine Shield", ModuleShield.class, ModuleType.ADDON, 60));

        MELTER = StevesCartsAPI.registerModule(new ResourceLocation(Constants.MOD_ID, "melter"),
                new ModuleData(new ResourceLocation(Constants.MOD_ID, "melter"), "Melter", ModuleMelter.class, ModuleType.ADDON, 10));

        EXTREME_MELTER = StevesCartsAPI.registerModule(new ResourceLocation(Constants.MOD_ID, "extreme_melter"),
                new ModuleData(new ResourceLocation(Constants.MOD_ID, "extreme_melter"), "Extreme Melter", ModuleMelterExtreme.class, ModuleType.ADDON, 19));

        INVISIBILITY_CORE = StevesCartsAPI.registerModule(new ResourceLocation(Constants.MOD_ID, "invisibility_core"),
                new ModuleData(new ResourceLocation(Constants.MOD_ID, "invisibility_core"), "Invisibility Core", ModuleInvisible.class, ModuleType.ADDON, 21));

        NOTE_SEQUENCER = StevesCartsAPI.registerModule(new ResourceLocation(Constants.MOD_ID, "note_sequencer"),
                new ModuleData(new ResourceLocation(Constants.MOD_ID, "note_sequencer"), "Note Sequencer", ModuleNote.class, ModuleType.ADDON, 30)).addSides(new ModuleData.SIDE[]{ModuleData.SIDE.RIGHT, ModuleData.SIDE.LEFT});

        FREEZER = StevesCartsAPI.registerModule(new ResourceLocation(Constants.MOD_ID, "freezer"),
                new ModuleData(new ResourceLocation(Constants.MOD_ID, "freezer"), "Freezer", ModuleSnowCannon.class, ModuleType.ADDON, 24));

        CAGE = StevesCartsAPI.registerModule(new ResourceLocation(Constants.MOD_ID, "cage"),
                new ModuleData(new ResourceLocation(Constants.MOD_ID, "cage"), "Cage", ModuleCage.class, ModuleType.ADDON, 7).addSides(new ModuleData.SIDE[]{ModuleData.SIDE.TOP, ModuleData.SIDE.CENTER}));

        CROP_NETHER_WART = StevesCartsAPI.registerModule(new ResourceLocation(Constants.MOD_ID, "crop_nether_wart"),
                new ModuleData(new ResourceLocation(Constants.MOD_ID, "crop_nether_wart"), "Crop: Nether Wart", ModuleNetherwart.class, ModuleType.ADDON, 20));//.addRequirement(BASIC_FARMER));

        FIREWORK_DISPLAY = StevesCartsAPI.registerModule(new ResourceLocation(Constants.MOD_ID, "firework_display"),
                new ModuleData(new ResourceLocation(Constants.MOD_ID, "firework_display"), "Firework display", ModuleFirework.class, ModuleType.ADDON, 45));


        INTERNAL_TANK = StevesCartsAPI.registerModule(new ResourceLocation(Constants.MOD_ID, "internal_sctank"),
                new ModuleData(new ResourceLocation(Constants.MOD_ID, "internal_sctank"), "Internal SCTank", ModuleInternalTank.class, ModuleType.STORAGE, 37).setAllowDuplicate());

        SIDE_TANKS = StevesCartsAPI.registerModule(new ResourceLocation(Constants.MOD_ID, "side_tanks"),
                new ModuleData(new ResourceLocation(Constants.MOD_ID, "side_tanks"), "Side Tanks", ModuleSideTanks.class, ModuleType.STORAGE, 10).addSides(new ModuleData.SIDE[]{ModuleData.SIDE.RIGHT, ModuleData.SIDE.LEFT}));

        TOP_TANK = StevesCartsAPI.registerModule(new ResourceLocation(Constants.MOD_ID, "top_sctank"),
                new ModuleData(new ResourceLocation(Constants.MOD_ID, "top_sctank"), "Top SCTank", ModuleTopTank.class, ModuleType.STORAGE, 22).addSide(ModuleData.SIDE.TOP));

        ADVANCED_TANK = StevesCartsAPI.registerModule(new ResourceLocation(Constants.MOD_ID, "advanced_sctank"),
                new ModuleData(new ResourceLocation(Constants.MOD_ID, "advanced_sctank"), "Advanced SCTank", ModuleAdvancedTank.class, ModuleType.STORAGE, 54).addSides(new ModuleData.SIDE[]{ModuleData.SIDE.CENTER, ModuleData.SIDE.TOP}));

        FRONT_TANK = StevesCartsAPI.registerModule(new ResourceLocation(Constants.MOD_ID, "front_sctank"),
                new ModuleData(new ResourceLocation(Constants.MOD_ID, "front_sctank"), "Front SCTank", ModuleFrontTank.class, ModuleType.STORAGE, 15).addSide(ModuleData.SIDE.FRONT));

        OPEN_TANK = StevesCartsAPI.registerModule(new ResourceLocation(Constants.MOD_ID, "open_sctank"),
                new ModuleData(new ResourceLocation(Constants.MOD_ID, "open_sctank"), "Open SCTank", ModuleOpenTank.class, ModuleType.STORAGE, 31).addSide(ModuleData.SIDE.TOP).addMessage(Localization.MODULE_INFO.OPEN_TANK));

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

        SOLAR_ENGINE.addModel("SolarPanelBase", new ModelSolarPanelBase()).addModel("SolarPanels", new ModelSolarPanelHeads(4)).removeModel("Top");
        BASIC_SOLAR_ENGINE.addModel("SolarPanelBase", new ModelSolarPanelBase()).addModel("SolarPanels", new ModelSolarPanelHeads(2)).removeModel("Top");
        COMPACT_SOLAR_ENGINE.addModel("SolarPanelSide", new ModelCompactSolarPanel());

        SIDE_CHESTS.addModel("SideChest", new ModelSideChests());
        TOP_CHEST.removeModel("Top").addModel("TopChest", new ModelTopChest());
        FRONT_CHEST.addModel("FrontChest", new ModelFrontChest()).setModelMult(0.68f);

        TORCH_PLACER.addModel("Torch", new ModelTorchplacer());
        BASIC_DRILL.addModel("Drill", new ModelDrill(ResourceHelper.getResource("/models/drillModelDiamond.png"))).addModel("Plate", new ModelToolPlate());
        IRON_DRILL.addModel("Drill", new ModelDrill(ResourceHelper.getResource("/models/drillModelIron.png"))).addModel("Plate", new ModelToolPlate());
        HARDENED_DRILL.addModel("Drill", new ModelDrill(ResourceHelper.getResource("/models/drillModelHardened.png"))).addModel("Plate", new ModelToolPlate());
        GALGADORIAN_DRILL.addModel("Drill", new ModelDrill(ResourceHelper.getResource("/models/drillModelMagic.png"))).addModel("Plate", new ModelToolPlate());

        RAILER.addModel("Rails", new ModelRailer(3));
        LARGE_RAILER.addModel("Rails", new ModelRailer(6));
        BRIDGE_BUILDER.addModel("Bridge", new ModelBridge()).addModel("Plate", new ModelToolPlate());
        TRACK_REMOVER.addModel("Remover", new ModelTrackRemover()).setModelMult(0.6f);
        BASIC_FARMER.addModel("Farmer", new ModelFarmer(ResourceHelper.getResource("/models/farmerModelDiamond.png"))).setModelMult(0.45f);
        GALGADORIAN_FARMER.addModel("Farmer", new ModelFarmer(ResourceHelper.getResource("/models/farmerModelGalgadorian.png"))).setModelMult(0.45f);

        BASIC_WOOD_CUTTER.addModel("WoodCutter", new ModelWoodCutter(ResourceHelper.getResource("/models/woodCutterModelDiamond.png"))).addModel("Plate", new ModelToolPlate());
        HARDENED_WOOD_CUTTER.addModel("WoodCutter", new ModelWoodCutter(ResourceHelper.getResource("/models/woodCutterModelHardened.png"))).addModel("Plate", new ModelToolPlate());
        GALGADORIAN_WOOD_CUTTER.addModel("WoodCutter", new ModelWoodCutter(ResourceHelper.getResource("/models/woodCutterModelGalgadorian.png"))).addModel("Plate", new ModelToolPlate());
        NETHERITE_WOOD_CUTTER.addModel("WoodCutter", new ModelWoodCutter(ResourceHelper.getResource("/models/woodcuttermodelnetherite.png"))).addModel("Plate", new ModelToolPlate());

        LIQUID_SENSORS.addModel("Sensor", new ModelLiquidSensors());
        SEAT.removeModel("Top").addModel("Chair", new ModelSeat());
        BRAKE.addModel("Lever", new ModelLever(ResourceHelper.getResource("/models/leverModel.png")));
        ADVANCED_CONTROL_SYSTEM.addModel("Lever", new ModelLever(ResourceHelper.getResource("/models/leverModel2.png"))).addModel("Wheel", new ModelWheel());


        CLEANER.addModel("Top", new ModelHullTop(ResourceHelper.getResource("/models/cleanerModelTop.png"))).addModel("Cleaner", new ModelCleaner());
        DYNAMITE_CARRIER.addModel("Tnt", new ModelDynamite());
        DIVINE_SHIELD.addModel("Shield", new ModelShield()).setModelMult(0.68f);
        NOTE_SEQUENCER.setModelMult(0.65f).addModel("Speakers", new ModelNote());
        CAGE.removeModel("Top").addModel("Cage", new ModelCage(), false).addModel("Cage", new ModelCage(), true).setModelMult(0.65f);
        SIDE_TANKS.addModel("SideTanks", new ModelSideTanks());
        TOP_TANK.addModel("TopTank", new ModelTopTank());
        ADVANCED_TANK.addModel("LargeTank", new ModelAdvancedTank()).removeModel("Top");
        FRONT_TANK.setModelMult(0.68f).addModel("FrontTank", new ModelFrontTank());
//        ModuleData.moduleList.get((byte) 71).addModel("Top", new ModelHullTop(ResourceHelper.getResource("/models/cleanerModelTop.png"))).addModel("Cleaner", new ModelLiquidDrainer());
//        ModuleData.moduleList.get((byte) 85).addModel("LawnMower", new ModelLawnMower()).setModelMult(0.4f);
//        ModuleData.moduleList.get((byte) 99).addModel("Cake", new ModelCake());
//        ModuleData.moduleList.get((byte) 100).addModel("Cake", new ModelCake());
    }
}
