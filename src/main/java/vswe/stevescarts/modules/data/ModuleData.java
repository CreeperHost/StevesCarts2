package vswe.stevescarts.modules.data;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
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
import vswe.stevescarts.entitys.EntityMinecartModular;
import vswe.stevescarts.helpers.Localization;
import vswe.stevescarts.helpers.ResourceHelper;
import vswe.stevescarts.init.ModItems;
import vswe.stevescarts.items.ItemCartModule;
import vswe.stevescarts.modules.ModuleBase;
import vswe.stevescarts.modules.addons.*;
import vswe.stevescarts.modules.addons.mobdetectors.*;
import vswe.stevescarts.modules.addons.plants.ModuleNetherwart;
import vswe.stevescarts.modules.addons.plants.ModulePlantSize;
import vswe.stevescarts.modules.addons.projectiles.*;
import vswe.stevescarts.modules.engines.*;
import vswe.stevescarts.modules.hull.*;
import vswe.stevescarts.modules.realtimers.*;
import vswe.stevescarts.modules.storages.ModuleStorage;
import vswe.stevescarts.modules.storages.chests.*;
import vswe.stevescarts.modules.storages.tanks.*;
import vswe.stevescarts.modules.workers.*;
import vswe.stevescarts.modules.workers.tools.*;

import javax.annotation.Nonnull;
import java.util.*;

public class ModuleData
{
    @Deprecated(forRemoval = true)
    private static HashMap<Byte, ModuleData> moduleList = new HashMap<>();
    @Deprecated(forRemoval = true)
    private static Class[] moduleGroups = new Class[]{ModuleHull.class, ModuleEngine.class, ModuleTool.class, ModuleStorage.class, ModuleAddon.class};
    @Deprecated(forRemoval = true)
    private static Localization.MODULE_INFO[] moduleGroupNames;

    private ResourceLocation id;
    private Class<? extends ModuleBase> moduleClass;
    private String name;
    private int modularCost;
    private int groupID;
    private ArrayList<SIDE> renderingSides;
    private boolean allowDuplicate;
    private ArrayList<ModuleData> nemesis;
    private ArrayList<ModuleDataGroup> requirement;
    private ModuleData parent;
    private boolean isLocked;
    private boolean defaultLock;
    private ArrayList<Localization.MODULE_INFO> message;
    @OnlyIn(Dist.CLIENT)
    private HashMap<String, ModelCartbase> models;
    @OnlyIn(Dist.CLIENT)
    private HashMap<String, ModelCartbase> modelsPlaceholder;
    private ArrayList<String> removedModels;
    private float modelMult;
    private boolean useExtraData;
    private byte extraDataDefaultValue;
    private static final int MAX_MESSAGE_ROW_LENGTH = 30;

    @Deprecated(forRemoval = true)
    public static HashMap<Byte, ModuleData> getList()
    {
        return ModuleData.moduleList;
    }

    public static Collection<ModuleData> getModules()
    {
        return getList().values();
    }

    public static void init()
    {
        ModuleData.moduleGroups = new Class[]{ModuleHull.class, ModuleEngine.class, ModuleTool.class, ModuleStorage.class, ModuleAddon.class};
        ModuleData.moduleGroupNames = new Localization.MODULE_INFO[]{Localization.MODULE_INFO.HULL_CATEGORY, Localization.MODULE_INFO.ENGINE_CATEGORY, Localization.MODULE_INFO.TOOL_CATEGORY, Localization.MODULE_INFO.STORAGE_CATEGORY, Localization.MODULE_INFO.ADDON_CATEGORY, Localization.MODULE_INFO.ATTACHMENT_CATEGORY};
        ModuleData.moduleList = new HashMap<>();
        final ModuleDataGroup engineGroup = new ModuleDataGroup(Localization.MODULE_INFO.ENGINE_GROUP);


        final ModuleData coalStandard = new ModuleData(0, "Coal Engine", ModuleCoalStandard.class, 15);
        final ModuleData coalTiny = new ModuleData(44, "Tiny Coal Engine", ModuleCoalTiny.class, 2);
        addNemesis(coalTiny, coalStandard);



        final ModuleData solar1 = new ModuleData(1, "Solar Engine", ModuleSolarStandard.class, 20).addSides(new SIDE[]{SIDE.CENTER, SIDE.TOP});
        final ModuleData solar2 = new ModuleData(45, "Basic Solar Engine", ModuleSolarBasic.class, 12).addSides(new SIDE[]{SIDE.CENTER, SIDE.TOP});
        final ModuleData compactsolar = new ModuleData(56, "Compact Solar Engine", ModuleSolarCompact.class, 32).addSides(new SIDE[]{SIDE.RIGHT, SIDE.LEFT});


        new ModuleData(2, "Side Chests", ModuleSideChests.class, 3).addSides(new SIDE[]{SIDE.RIGHT, SIDE.LEFT});
        new ModuleData(3, "Top Chest", ModuleTopChest.class, 6).addSide(SIDE.TOP);
        final ModuleData frontChest = new ModuleData(4, "Front Chest", ModuleFrontChest.class, 5).addSide(SIDE.FRONT);
        new ModuleData(5, "Internal Storage", ModuleInternalStorage.class, 25).setAllowDuplicate();
        new ModuleData(6, "Extracting Chests", ModuleExtractingChests.class, 75).addSides(new SIDE[]{SIDE.CENTER, SIDE.RIGHT, SIDE.LEFT});

        new ModuleData(7, "Torch Placer", ModuleTorch.class, 14).addSides(new SIDE[]{SIDE.RIGHT, SIDE.LEFT});

        final ModuleData drill = new ModuleDataTool(8, "Basic Drill", ModuleDrillDiamond.class, 10, false).addSide(SIDE.FRONT);
        final ModuleData ironDrill = new ModuleDataTool(42, "Iron Drill", ModuleDrillIron.class, 3, false).addSide(SIDE.FRONT);
        final ModuleData hardeneddrill = new ModuleDataTool(43, "Hardened Drill", ModuleDrillHardened.class, 45, false).addSide(SIDE.FRONT);
        final ModuleData galgdrill = new ModuleDataTool(9, "Galgadorian Drill", ModuleDrillGalgadorian.class, 150, true).addSide(SIDE.FRONT);
        final ModuleDataGroup drillGroup = new ModuleDataGroup(Localization.MODULE_INFO.DRILL_GROUP);
        drillGroup.add(drill);
        drillGroup.add(ironDrill);
        drillGroup.add(hardeneddrill);
        drillGroup.add(galgdrill);

        final ModuleData railer = new ModuleData(10, "Railer", ModuleRailer.class, 3);
        final ModuleData largerailer = new ModuleData(11, "Large Railer", ModuleRailerLarge.class, 17);
        addNemesis(railer, largerailer);

        new ModuleData(12, "Bridge Builder", ModuleBridge.class, 14);
        final ModuleData remover = new ModuleData(13, "Track Remover", ModuleRemover.class, 8).addSides(new SIDE[]{SIDE.TOP, SIDE.BACK});
        addNemesis(remover, railer);
        addNemesis(remover, largerailer);

        final ModuleDataGroup farmerGroup = new ModuleDataGroup(Localization.MODULE_INFO.FARMER_GROUP);
        final ModuleData farmerbasic = new ModuleDataTool(14, "Basic Farmer", ModuleFarmerDiamond.class, 36, false).addSide(SIDE.FRONT);
        final ModuleData farmergalg = new ModuleDataTool(84, "Galgadorian Farmer", ModuleFarmerGalgadorian.class, 55, true).addSide(SIDE.FRONT);
        farmerGroup.add(farmerbasic);
        farmerGroup.add(farmergalg);

        final ModuleDataGroup woodcutterGroup = new ModuleDataGroup(Localization.MODULE_INFO.CUTTER_GROUP);
        final ModuleData woodcutter = new ModuleDataTool(15, "Basic Wood Cutter", ModuleWoodcutterDiamond.class, 34, false).addSide(SIDE.FRONT);
        final ModuleData woodcutterHardened = new ModuleDataTool(79, "Hardened Wood Cutter", ModuleWoodcutterHardened.class, 65, false).addSide(SIDE.FRONT);
        final ModuleData woodcutterGalgadorian = new ModuleDataTool(80, "Galgadorian Wood Cutter", ModuleWoodcutterGalgadorian.class, 120, true).addSide(SIDE.FRONT);
        final ModuleData woodCutterNetherite = new ModuleDataTool(102, "Netherite Wood Cutter", ModuleWoodcutterNetherite.class, 120, false).addSide(SIDE.FRONT);
        woodcutterGroup.add(woodcutter);
        woodcutterGroup.add(woodcutterHardened);
        woodcutterGroup.add(woodcutterGalgadorian);
        woodcutterGroup.add(woodCutterNetherite);

        final ModuleDataGroup tankGroup = new ModuleDataGroup(Localization.MODULE_INFO.TANK_GROUP);
        new ModuleData(16, "Hydrator", ModuleHydrater.class, 6).addRequirement(tankGroup);
        new ModuleData(18, "Fertilizer", ModuleFertilizer.class, 10);
        new ModuleData(19, "Height Controller", ModuleHeightControl.class, 20);
        final ModuleData liquidsensors = new ModuleData(20, "Liquid Sensors", ModuleLiquidSensors.class, 27).addRequirement(drillGroup);
        final ModuleData seat = new ModuleData(25, "Seat", ModuleSeat.class, 3).addSides(new SIDE[]{SIDE.CENTER, SIDE.TOP});
        new ModuleData(26, "Brake Handle", ModuleBrake.class, 12).addSide(SIDE.RIGHT).addParent(seat);
        new ModuleData(27, "Advanced Control System", ModuleAdvControl.class, 38).addSide(SIDE.RIGHT).addParent(seat);

        final ModuleDataGroup detectorGroup = new ModuleDataGroup(Localization.MODULE_INFO.ENTITY_GROUP);
        final ModuleDataGroup shooterGroup = new ModuleDataGroup(Localization.MODULE_INFO.SHOOTER_GROUP);
        final ModuleData shooter = new ModuleData(28, "Shooter", ModuleShooter.class, 15).addSide(SIDE.TOP);
        final ModuleData advshooter = new ModuleData(29, "Advanced Shooter", ModuleShooterAdv.class, 50).addSide(SIDE.TOP).addRequirement(detectorGroup);
        shooterGroup.add(shooter);
        shooterGroup.add(advshooter);

        final ModuleData animal = new ModuleData(21, "Entity Detector: Animal", ModuleAnimal.class, 1).addParent(advshooter);
        final ModuleData player = new ModuleData(22, "Entity Detector: Player", ModulePlayer.class, 7).addParent(advshooter);
        final ModuleData villager = new ModuleData(23, "Entity Detector: Villager", ModuleVillager.class, 1).addParent(advshooter);
        final ModuleData monster = new ModuleData(24, "Entity Detector: Monster", ModuleMonster.class, 1).addParent(advshooter);
        final ModuleData bats = new ModuleData(48, "Entity Detector: Bat", ModuleBat.class, 1).addParent(advshooter);
        detectorGroup.add(animal);
        detectorGroup.add(player);
        detectorGroup.add(villager);
        detectorGroup.add(monster);
        detectorGroup.add(bats);

        final ModuleData cleaner = new ModuleData(30, "Cleaning Machine", ModuleCleaner.class, 23).addSide(SIDE.CENTER);
        addNemesis(frontChest, cleaner);
        new ModuleData(31, "Dynamite Carrier", ModuleDynamite.class, 3).addSide(SIDE.TOP);
        new ModuleData(32, "Divine Shield", ModuleShield.class, 60);
        final ModuleData melter = new ModuleData(33, "Melter", ModuleMelter.class, 10);
        final ModuleData extrememelter = new ModuleData(34, "Extreme Melter", ModuleMelterExtreme.class, 19);
        addNemesis(melter, extrememelter);
        new ModuleData(36, "Invisibility Core", ModuleInvisible.class, 21);
//        new ModuleDataHull(37, "Wooden Hull", ModuleWood.class).setCapacity(50).setEngineMax(1).setAddonMax(0).setComplexityMax(15);
//        new ModuleDataHull(38, "Standard Hull", ModuleStandard.class).setCapacity(200).setEngineMax(3).setAddonMax(6).setComplexityMax(50);
//        new ModuleDataHull(39, "Reinforced Hull", ModuleReinforced.class).setCapacity(500).setEngineMax(5).setAddonMax(12).setComplexityMax(150);
//        final ModuleData pumpkinhull = new ModuleDataHull(47, "Pumpkin chariot", ModulePumpkin.class).setCapacity(40).setEngineMax(1).setAddonMax(0).setComplexityMax(15);
//        new ModuleDataHull(62, "Mechanical Pig", ModulePig.class).setCapacity(150).setEngineMax(2).setAddonMax(4).setComplexityMax(50).addSide(SIDE.FRONT).addMessage(Localization.MODULE_INFO.PIG_MESSAGE);
//        new ModuleDataHull(76, "Creative Hull", ModuleCheatHull.class).setCapacity(10000).setEngineMax(5).setAddonMax(12).setComplexityMax(150);
//        new ModuleDataHull(81, "Galgadorian Hull", ModuleGalgadorian.class).setCapacity(1000).setEngineMax(5).setAddonMax(12).setComplexityMax(150);

        new ModuleData(40, "Note Sequencer", ModuleNote.class, 30).addSides(new SIDE[]{SIDE.RIGHT, SIDE.LEFT});
        final ModuleData colorizer = new ModuleData(41, "Colorizer", ModuleColorizer.class, 15);

        final ModuleData colorRandomizer = new ModuleData(101, "Color Randomizer", ModuleColorRandomizer.class, 20);
        addNemesis(colorizer, colorRandomizer);

        new ModuleData(49, "Chunk Loader", ModuleChunkLoader.class, 84);

        new ModuleData(51, "Projectile: Potion", ModulePotion.class, 10).addRequirement(shooterGroup);
        new ModuleData(52, "Projectile: Fire Charge", ModuleFireball.class, 10).lockByDefault().addRequirement(shooterGroup);
        new ModuleData(53, "Projectile: Egg", ModuleEgg.class, 10).addRequirement(shooterGroup);
        final ModuleData snowballshooter = new ModuleData(54, "Projectile: Snowball", ModuleSnowball.class, 10).addRequirement(shooterGroup);
        final ModuleData cake = new ModuleData(90, "Projectile: Cake", ModuleCake.class, 10).addRequirement(shooterGroup).lock();

        final ModuleData snowgenerator = new ModuleData(55, "Freezer", ModuleSnowCannon.class, 24);
        addNemesis(snowgenerator, melter);
        addNemesis(snowgenerator, extrememelter);

        final ModuleData cage = new ModuleData(57, "Cage", ModuleCage.class, 7).addSides(new SIDE[]{SIDE.TOP, SIDE.CENTER});
        new ModuleData(58, "Crop: Nether Wart", ModuleNetherwart.class, 20).addRequirement(farmerGroup);
        new ModuleData(59, "Firework display", ModuleFirework.class, 45);
        final ModuleData cheatengine = new ModuleData(61, "Creative Engine", ModuleCheatEngine.class, 1);
        final ModuleData internalTank = new ModuleData(63, "Internal SCTank", ModuleInternalTank.class, 37).setAllowDuplicate();
        final ModuleData sideTank = new ModuleData(64, "Side Tanks", ModuleSideTanks.class, 10).addSides(new SIDE[]{SIDE.RIGHT, SIDE.LEFT});
        final ModuleData topTank = new ModuleData(65, "Top SCTank", ModuleTopTank.class, 22).addSide(SIDE.TOP);
        final ModuleData advancedTank = new ModuleData(66, "Advanced SCTank", ModuleAdvancedTank.class, 54).addSides(new SIDE[]{SIDE.CENTER, SIDE.TOP});
        final ModuleData frontTank = new ModuleData(67, "Front SCTank", ModuleFrontTank.class, 15).addSide(SIDE.FRONT);
        final ModuleData creativeTank = new ModuleData(72, "Creative SCTank", ModuleCheatTank.class, 1).setAllowDuplicate().addMessage(Localization.MODULE_INFO.OCEAN_MESSAGE);
        final ModuleData topTankOpen = new ModuleData(73, "Open SCTank", ModuleOpenTank.class, 31).addSide(SIDE.TOP).addMessage(Localization.MODULE_INFO.OPEN_TANK);
        addNemesis(frontTank, cleaner);
        tankGroup.add(internalTank).add(sideTank).add(topTank).add(advancedTank).add(frontTank).add(creativeTank).add(topTankOpen);

        new ModuleData(68, "Incinerator", ModuleIncinerator.class, 23).addRequirement(tankGroup).addRequirement(drillGroup);

        final ModuleData thermal0 = new ModuleData(69, "Thermal Engine", ModuleThermalStandard.class, 28).addRequirement(tankGroup);
        final ModuleData thermal2 = new ModuleData(70, "Advanced Thermal Engine", ModuleThermalAdvanced.class, 58).addRequirement(tankGroup.copy(2));
        addNemesis(thermal0, thermal2);

        final ModuleData cleanerliquid = new ModuleData(71, "Liquid Cleaner", ModuleLiquidDrainer.class, 30).addSide(SIDE.CENTER).addParent(liquidsensors).addRequirement(tankGroup);
        addNemesis(frontTank, cleanerliquid);
        addNemesis(frontChest, cleanerliquid);

        final ModuleData eggBasket = new ModuleData(74, "Egg Basket", ModuleEggBasket.class, 14)
        {
            @Override
            public String getModuleInfoText(final byte b)
            {
                if (b == 0)
                {
                    return Localization.MODULE_INFO.STORAGE_EMPTY.translate();
                }
                return Localization.MODULE_INFO.EGG_STORAGE_FULL.translate();
            }

            @Override
            public String getCartInfoText(final String name, final byte b)
            {
                if (b == 0)
                {
                    return Localization.MODULE_INFO.STORAGE_EMPTY.translate() + " " + name;
                }
                return Localization.MODULE_INFO.STORAGE_FULL.translate() + " " + name;
            }
        }.addSide(SIDE.TOP).useExtraData((byte) 1);

        new ModuleData(75, "Drill Intelligence", ModuleDrillIntelligence.class, 21).addRequirement(drillGroup);
        new ModuleData(77, "Power Observer", ModulePowerObserver.class, 12).addRequirement(engineGroup);
        engineGroup.add(coalTiny);
        engineGroup.add(coalStandard);
        engineGroup.add(solar2);
        engineGroup.add(solar1);
        engineGroup.add(thermal0);
        engineGroup.add(thermal2);
        engineGroup.add(compactsolar);
        engineGroup.add(cheatengine);

        new ModuleData(78, "Steve's Arcade", ModuleArcade.class, 10).addParent(seat);

        final ModuleDataGroup toolGroup = ModuleDataGroup.getCombinedGroup(Localization.MODULE_INFO.TOOL_GROUP, drillGroup, woodcutterGroup);
        toolGroup.add(farmerGroup);
        final ModuleDataGroup enchantableGroup = ModuleDataGroup.getCombinedGroup(Localization.MODULE_INFO.TOOL_OR_SHOOTER_GROUP, toolGroup, shooterGroup);
        new ModuleData(82, "Enchanter", ModuleEnchants.class, 72).addRequirement(enchantableGroup);
        new ModuleData(83, "Ore Extractor", ModuleOreTracker.class, 80).addRequirement(drillGroup);
        new ModuleData(85, "Lawn Mower", ModuleFlowerRemover.class, 38).addSides(new SIDE[]{SIDE.RIGHT, SIDE.LEFT});
        new ModuleData(86, "Milker", ModuleMilker.class, 26).addParent(cage);
        new ModuleData(87, "Crafter", ModuleCrafter.class, 22).setAllowDuplicate();
        new ModuleData(89, "Planter Range Extender", ModulePlantSize.class, 20).addRequirement(woodcutterGroup);
        new ModuleData(91, "Smelter", ModuleSmelter.class, 22).setAllowDuplicate();
        new ModuleData(92, "Advanced Crafter", ModuleCrafterAdv.class, 42).setAllowDuplicate();
        new ModuleData(93, "Advanced Smelter", ModuleSmelterAdv.class, 42).setAllowDuplicate();
        new ModuleData(94, "Information Provider", ModuleLabel.class, 12);
        new ModuleData(95, "Experience Bank", ModuleExperience.class, 36);
        new ModuleData(96, "Creative Incinerator", ModuleCreativeIncinerator.class, 1).addRequirement(drillGroup);
        new ModuleData(97, "Creative Supplies", ModuleCreativeSupplies.class, 1);
        new ModuleData(99, "Cake Server", ModuleCakeServer.class, 10).addSide(SIDE.TOP).addMessage(Localization.MODULE_INFO.ALPHA_MESSAGE);
        final ModuleData trickOrTreat = new ModuleData(100, "Trick-or-Treat Cake Server", ModuleCakeServerDynamite.class, 15).addSide(SIDE.TOP);
    }

    @OnlyIn(Dist.CLIENT)
    public static void initModels()
    {
        ModuleData.moduleList.get((byte) 0).addModel("Engine", new ModelEngineFrame()).addModel("Fire", new ModelEngineInside());
        ModuleData.moduleList.get((byte) 44).addModel("Engine", new ModelEngineFrame()).addModel("Fire", new ModelEngineInside());
        ModuleData.moduleList.get((byte) 1).addModel("SolarPanelBase", new ModelSolarPanelBase()).addModel("SolarPanels", new ModelSolarPanelHeads(4)).removeModel("Top");
        ModuleData.moduleList.get((byte) 45).addModel("SolarPanelBase", new ModelSolarPanelBase()).addModel("SolarPanels", new ModelSolarPanelHeads(2)).removeModel("Top");
        ModuleData.moduleList.get((byte) 56).addModel("SolarPanelSide", new ModelCompactSolarPanel());
        ModuleData.moduleList.get((byte) 2).addModel("SideChest", new ModelSideChests());
        ModuleData.moduleList.get((byte) 3).removeModel("Top").addModel("TopChest", new ModelTopChest());
        ModuleData.moduleList.get((byte) 4).addModel("FrontChest", new ModelFrontChest()).setModelMult(0.68f);
        ModuleData.moduleList.get((byte) 6).addModel("SideChest", new ModelExtractingChests());
        ModuleData.moduleList.get((byte) 7).addModel("Torch", new ModelTorchplacer());
        ModuleData.moduleList.get((byte) 8).addModel("Drill", new ModelDrill(ResourceHelper.getResource("/models/drillModelDiamond.png"))).addModel("Plate", new ModelToolPlate());
        ModuleData.moduleList.get((byte) 42).addModel("Drill", new ModelDrill(ResourceHelper.getResource("/models/drillModelIron.png"))).addModel("Plate", new ModelToolPlate());
        ModuleData.moduleList.get((byte) 43).addModel("Drill", new ModelDrill(ResourceHelper.getResource("/models/drillModelHardened.png"))).addModel("Plate", new ModelToolPlate());
        ModuleData.moduleList.get((byte) 9).addModel("Drill", new ModelDrill(ResourceHelper.getResource("/models/drillModelMagic.png"))).addModel("Plate", new ModelToolPlate());
        ModuleData.moduleList.get((byte) 10).addModel("Rails", new ModelRailer(3));
        ModuleData.moduleList.get((byte) 11).addModel("Rails", new ModelRailer(6));
        ModuleData.moduleList.get((byte) 12).addModel("Bridge", new ModelBridge()).addModel("Plate", new ModelToolPlate());
        ModuleData.moduleList.get((byte) 13).addModel("Remover", new ModelTrackRemover()).setModelMult(0.6f);
        ModuleData.moduleList.get((byte) 14).addModel("Farmer", new ModelFarmer(ResourceHelper.getResource("/models/farmerModelDiamond.png"))).setModelMult(0.45f);
        ModuleData.moduleList.get((byte) 84).addModel("Farmer", new ModelFarmer(ResourceHelper.getResource("/models/farmerModelGalgadorian.png"))).setModelMult(0.45f);
        ModuleData.moduleList.get((byte) 15).addModel("WoodCutter", new ModelWoodCutter(ResourceHelper.getResource("/models/woodCutterModelDiamond.png"))).addModel("Plate", new ModelToolPlate());
        ModuleData.moduleList.get((byte) 79).addModel("WoodCutter", new ModelWoodCutter(ResourceHelper.getResource("/models/woodCutterModelHardened.png"))).addModel("Plate", new ModelToolPlate());
        ModuleData.moduleList.get((byte) 80).addModel("WoodCutter", new ModelWoodCutter(ResourceHelper.getResource("/models/woodCutterModelGalgadorian.png"))).addModel("Plate", new ModelToolPlate());
        ModuleData.moduleList.get((byte) 102).addModel("WoodCutter", new ModelWoodCutter(ResourceHelper.getResource("/models/woodcuttermodelnetherite.png"))).addModel("Plate", new ModelToolPlate());

        ModuleData.moduleList.get((byte) 20).addModel("Sensor", new ModelLiquidSensors());
        ModuleData.moduleList.get((byte) 25).removeModel("Top").addModel("Chair", new ModelSeat());
        ModuleData.moduleList.get((byte) 26).addModel("Lever", new ModelLever(ResourceHelper.getResource("/models/leverModel.png")));
        ModuleData.moduleList.get((byte) 27).addModel("Lever", new ModelLever(ResourceHelper.getResource("/models/leverModel2.png"))).addModel("Wheel", new ModelWheel());
        final ArrayList<Integer> pipes = new ArrayList<>();
        for (int i = 0; i < 9; ++i)
        {
            if (i != 4)
            {
                pipes.add(i);
            }
        }
        ModuleData.moduleList.get((byte) 28).addModel("Rig", new ModelShootingRig()).addModel("Pipes", new ModelGun(pipes));
        ModuleData.moduleList.get((byte) 29).addModel("Rig", new ModelShootingRig()).addModel("MobDetector", new ModelMobDetector()).addModel("Pipes", new ModelSniperRifle());
        ModuleData.moduleList.get((byte) 30).addModel("Top", new ModelHullTop(ResourceHelper.getResource("/models/cleanerModelTop.png"))).addModel("Cleaner", new ModelCleaner());
        ModuleData.moduleList.get((byte) 31).addModel("Tnt", new ModelDynamite());
        ModuleData.moduleList.get((byte) 32).addModel("Shield", new ModelShield()).setModelMult(0.68f);
        ModuleData.moduleList.get((byte) 37).addModel("Hull", new ModelHull(ResourceHelper.getResource("/models/hullModelWooden.png"))).addModel("Top", new ModelHullTop(ResourceHelper.getResource("/models/hullModelWoodenTop.png")));
        ModuleData.moduleList.get((byte) 38).addModel("Hull", new ModelHull(ResourceHelper.getResource("/models/hullModelStandard.png"))).addModel("Top", new ModelHullTop(ResourceHelper.getResource("/models/hullModelStandardTop.png")));
        ModuleData.moduleList.get((byte) 39).addModel("Hull", new ModelHull(ResourceHelper.getResource("/models/hullModelLarge.png"))).addModel("Top", new ModelHullTop(ResourceHelper.getResource("/models/hullModelLargeTop.png")));
        ModuleData.moduleList.get((byte) 47).addModel("Hull", new ModelPumpkinHull(ResourceHelper.getResource("/models/hullModelPumpkin.png"), ResourceHelper.getResource("/models/hullModelWooden.png"))).addModel("Top", new ModelPumpkinHullTop(ResourceHelper.getResource("/models/hullModelPumpkinTop.png"), ResourceHelper.getResource("/models/hullModelWoodenTop.png")));
        ModuleData.moduleList.get((byte) 62).addModel("Hull", new ModelHull(ResourceHelper.getResource("/models/hullModelPig.png"))).addModel("Top", new ModelHullTop(ResourceHelper.getResource("/models/hullModelPigTop.png"))).addModel("Head", new ModelPigHead()).addModel("Tail", new ModelPigTail());
        ModuleData.moduleList.get((byte) 76).addModel("Hull", new ModelHull(ResourceHelper.getResource("/models/hullModelCreative.png"))).addModel("Top", new ModelHullTop(ResourceHelper.getResource("/models/hullModelCreativeTop.png")));
        ModuleData.moduleList.get((byte) 81).addModel("Hull", new ModelHull(ResourceHelper.getResource("/models/hullModelGalgadorian.png"))).addModel("Top", new ModelHullTop(ResourceHelper.getResource("/models/hullModelGalgadorianTop.png")));
        ModuleData.moduleList.get((byte) 40).setModelMult(0.65f).addModel("Speakers", new ModelNote());
        ModuleData.moduleList.get((byte) 57).removeModel("Top").addModel("Cage", new ModelCage(), false).addModel("Cage", new ModelCage(), true).setModelMult(0.65f);
        ModuleData.moduleList.get((byte) 64).addModel("SideTanks", new ModelSideTanks());
        ModuleData.moduleList.get((byte) 65).addModel("TopTank", new ModelTopTank());
        ModuleData.moduleList.get((byte) 66).addModel("LargeTank", new ModelAdvancedTank()).removeModel("Top");
        ModuleData.moduleList.get((byte) 67).setModelMult(0.68f).addModel("FrontTank", new ModelFrontTank());
        ModuleData.moduleList.get((byte) 71).addModel("Top", new ModelHullTop(ResourceHelper.getResource("/models/cleanerModelTop.png"))).addModel("Cleaner", new ModelLiquidDrainer());
        ModuleData.moduleList.get((byte) 74).addModel("TopChest", new ModelEggBasket());
        ModuleData.moduleList.get((byte) 85).addModel("LawnMower", new ModelLawnMower()).setModelMult(0.4f);
        ModuleData.moduleList.get((byte) 99).addModel("Cake", new ModelCake());
        ModuleData.moduleList.get((byte) 100).addModel("Cake", new ModelCake());
    }

    @Deprecated(forRemoval = true)
    public ModuleData(final int id, final String name, final Class<? extends ModuleBase> moduleClass, final int modularCost)
    {
        nemesis = null;
        requirement = null;
        parent = null;
        modelMult = 0.75f;
//        this.id = id;
        this.moduleClass = moduleClass;
        this.name = name;
        this.modularCost = modularCost;
        groupID = ModuleData.moduleGroups.length;
        for (int i = 0; i < ModuleData.moduleGroups.length; ++i)
        {
            if (ModuleData.moduleGroups[i].isAssignableFrom(moduleClass))
            {
                groupID = i;
                break;
            }
        }
//        if (ModuleData.moduleList.containsKey(this.id))
//        {
//            throw new Error("WARNING! " + name + " can't be added with ID " + id + " since that ID is already occupied by " + ModuleData.moduleList.get(this.id).getName());
//        }
//        else
//        {
//            ModuleData.moduleList.put(this.id, this);
//        }
    }

    ModuleType moduleType;

    public ModuleData(final ResourceLocation id, final String name, final Class<? extends ModuleBase> moduleClass, ModuleType moduleType, final int modularCost)
    {
        this.nemesis = null;
        this.requirement = null;
        this.parent = null;
        this.modelMult = 0.75f;
        this.id = id;
        this.moduleClass = moduleClass;
        this.name = name;
        this.modularCost = modularCost;
        this.moduleType = moduleType;
    }

    public Class<? extends ModuleBase> getModuleClass()
    {
        return moduleClass;
    }

    @Deprecated(forRemoval = true)
    public boolean getIsValid()
    {
        return true;
    }

    @Deprecated(forRemoval = true)
    public boolean getIsLocked()
    {
        return isLocked;
    }

    protected ModuleData lock()
    {
        isLocked = true;
        return this;
    }

    public boolean getEnabledByDefault()
    {
        return !defaultLock;
    }

    protected ModuleData lockByDefault()
    {
        defaultLock = true;
        return this;
    }

    protected ModuleData setAllowDuplicate()
    {
        allowDuplicate = true;
        return this;
    }

    protected boolean getAllowDuplicate()
    {
        return allowDuplicate;
    }

    protected ModuleData addSide(final SIDE side)
    {
        if (renderingSides == null)
        {
            renderingSides = new ArrayList<>();
        }
        renderingSides.add(side);
        if (side == SIDE.TOP)
        {
            removeModel("Rails");
        }
        return this;
    }

    public ModuleData useExtraData(final byte defaultValue)
    {
        extraDataDefaultValue = defaultValue;
        useExtraData = true;
        return this;
    }

    public boolean isUsingExtraData()
    {
        return useExtraData;
    }

    public byte getDefaultExtraData()
    {
        return extraDataDefaultValue;
    }

    public ArrayList<SIDE> getRenderingSides()
    {
        return renderingSides;
    }

    protected ModuleData addSides(final SIDE[] sides)
    {
        for (int i = 0; i < sides.length; ++i)
        {
            addSide(sides[i]);
        }
        return this;
    }

    protected ModuleData addParent(final ModuleData parent)
    {
        this.parent = parent;
        return this;
    }

    protected ModuleData addMessage(final Localization.MODULE_INFO s)
    {
        if (message == null)
        {
            message = new ArrayList<>();
        }
        message.add(s);
        return this;
    }

    protected void addNemesis(final ModuleData nemesis)
    {
        if (this.nemesis == null)
        {
            this.nemesis = new ArrayList<>();
        }
        this.nemesis.add(nemesis);
    }

    protected ModuleData addRequirement(final ModuleDataGroup requirement)
    {
        if (this.requirement == null)
        {
            this.requirement = new ArrayList<>();
        }
        this.requirement.add(requirement);
        return this;
    }

    protected static void addNemesis(final ModuleData m1, final ModuleData m2)
    {
        m2.addNemesis(m1);
        m1.addNemesis(m2);
    }

    @OnlyIn(Dist.CLIENT)
    public float getModelMult()
    {
        return modelMult;
    }

    @OnlyIn(Dist.CLIENT)
    protected ModuleData setModelMult(final float val)
    {
        modelMult = val;
        return this;
    }

    @OnlyIn(Dist.CLIENT)
    public ModuleData addModel(final String tag, final ModelCartbase model)
    {
        addModel(tag, model, false);
        addModel(tag, model, true);
        return this;
    }

    @OnlyIn(Dist.CLIENT)
    protected ModuleData addModel(final String tag, final ModelCartbase model, final boolean placeholder)
    {
        if (placeholder)
        {
            if (modelsPlaceholder == null)
            {
                modelsPlaceholder = new HashMap<>();
            }
            modelsPlaceholder.put(tag, model);
        }
        else
        {
            if (models == null)
            {
                models = new HashMap<>();
            }
            models.put(tag, model);
        }
        return this;
    }

    @OnlyIn(Dist.CLIENT)
    public HashMap<String, ModelCartbase> getModels(final boolean placeholder)
    {
        if (placeholder)
        {
            return modelsPlaceholder;
        }
        return models;
    }

    @OnlyIn(Dist.CLIENT)
    public boolean haveModels(final boolean placeholder)
    {
        if (placeholder)
        {
            return modelsPlaceholder != null;
        }
        return models != null;
    }

    protected ModuleData removeModel(final String tag)
    {
        if (removedModels == null)
        {
            removedModels = new ArrayList<>();
        }
        if (!removedModels.contains(tag))
        {
            removedModels.add(tag);
        }
        return this;
    }

    public ArrayList<String> getRemovedModels()
    {
        return removedModels;
    }

    public boolean haveRemovedModels()
    {
        return removedModels != null;
    }

    public String getName()
    {
        return "module_" + getRawName();
    }

    public String getUnlocalizedName()
    {
        return "item.SC2." + getRawName() + ".name";
    }

    public ResourceLocation getID()
    {
        return id;
    }

    public int getCost()
    {
        return modularCost;
    }

    protected ModuleData getParent()
    {
        return parent;
    }

    protected ArrayList<ModuleData> getNemesis()
    {
        return nemesis;
    }

    protected ArrayList<ModuleDataGroup> getRequirement()
    {
        return requirement;
    }

    public String getModuleInfoText(final byte b)
    {
        return null;
    }

    public String getCartInfoText(final String name, final byte b)
    {
        return name;
    }

    @Deprecated
    public static NonNullList<ItemStack> getModularItems(@Nonnull ItemStack cart)
    {
        final NonNullList<ItemStack> modules = NonNullList.create();
        if (!cart.isEmpty() && cart.getItem() == ModItems.CARTS.get() && cart.getTag() != null)
        {
            final CompoundTag info = cart.getTag();
            if (info.contains("Modules"))
            {
                final byte[] IDs = info.getByteArray("Modules");
                for (int i = 0; i < IDs.length; ++i)
                {
                    final byte id = IDs[i];
                    //					@Nonnull
                    //					ItemStack module = new ItemStack(ModItems.MODULES.get(), 1);
                    //TODO
                    //					ModItems.MODULES.get().addExtraDataToModule(module, info, i);
                    //					modules.add(module);
                }
            }
        }
        return modules;
    }

    public static ItemStack createModularCart(final EntityMinecartModular parentcart)
    {
        @Nonnull ItemStack cart = new ItemStack(ModItems.CARTS.get(), 1);
        final CompoundTag save = new CompoundTag();
        final byte[] moduleIDs = new byte[parentcart.getModules().size()];
        for (int i = 0; i < parentcart.getModules().size(); ++i)
        {
            final ModuleBase module = parentcart.getModules().get(i);
            for (final ModuleData moduledata : ModuleData.moduleList.values())
            {
                if (module.getClass() == moduledata.moduleClass)
                {
                    //TODO API CHANGE, NO MORE BYTES
//                    moduleIDs[i] = moduledata.getID();
                    break;
                }
            }
            //TODO
            //			ModItems.MODULES.addExtraDataToModule(save, module, i);
        }
        save.putByteArray("Modules", moduleIDs);
        cart.setTag(save);
        return cart;
    }

    public static ItemStack createModularCartFromItems(final NonNullList<ItemStack> modules)
    {
        ItemStack cart = new ItemStack(ModItems.CARTS.get(), 1);
        ListTag modulesTag = new ListTag();
        for (int i = 0; i < modules.size(); i++)
        {
            CompoundTag moduleTag = new CompoundTag();
            ItemCartModule cartModule = (ItemCartModule) modules.get(i).getItem();
            moduleTag.putString(String.valueOf(i), cartModule.getModuleData().getID().toString());
            modulesTag.add(i, moduleTag);
        }
        cart.getOrCreateTag().put("modules", modulesTag);
        return cart;
    }

    @Deprecated(forRemoval = true)
    public static boolean isItemOfModularType(@Nonnull ItemStack itemstack, final Class<? extends ModuleBase> validClass)
    {
        //		if (itemstack.getItem() == ModItems.MODULES.get()) {
        //TODO
        //			final ModuleData module = ModItems.MODULES.getModuleData(itemstack);
        //			if (module != null && validClass.isAssignableFrom(module.moduleClass)) {
        //				return true;
        //			}
        //		}
        return false;
    }

    @Nonnull
    public ItemStack getItemStack()
    {
        ItemStack stack = new ItemStack(Items.AIR);
        if (ModItems.MODULES.get(this) != null)
        {
            stack = new ItemStack(ModItems.MODULES.get(this).get());
        }
        return stack;
    }

    public static boolean isValidModuleItem(final int validGroup, @Nonnull ItemStack itemstack)
    {
        if (itemstack.getItem() instanceof ItemCartModule itemCartModule)
        {
            final ModuleData module = itemCartModule.getModuleData();
            return isValidModuleItem(validGroup, module);
        }
        return false;
    }

    public static boolean isValidModuleItem(final int validGroup, final ModuleData module)
    {
        if (module != null)
        {
            if (validGroup < 0)
            {
                for (int i = 0; i < ModuleData.moduleGroups.length; ++i)
                {
                    if (ModuleData.moduleGroups[i].isAssignableFrom(module.moduleClass))
                    {
                        return false;
                    }
                }
                return true;
            }
            if (ModuleData.moduleGroups[validGroup].isAssignableFrom(module.moduleClass))
            {
                return true;
            }
        }
        return false;
    }

    public static boolean isValidModuleCombo(final ModuleDataHull hull, final ArrayList<ModuleData> modules)
    {
        final int[] max = {1, hull.getEngineMax(), 1, 4, hull.getAddonMax(), 6};
        final int[] current = new int[max.length];
        for (final ModuleData module : modules)
        {
            int id = 5;
            for (int i = 0; i < 5; ++i)
            {
                if (isValidModuleItem(i, module))
                {
                    id = i;
                    break;
                }
            }
            final int[] array = current;
            final int n = id;
            ++array[n];
            if (current[id] > max[id])
            {
                return false;
            }
        }
        return true;
    }

    @OnlyIn(Dist.CLIENT)
    public void addExtraMessage(final List<Component> list)
    {
        if (message != null)
        {
            list.add(Component.literal(""));
            for (final Localization.MODULE_INFO m : message)
            {
                final String str = m.translate();
                if (str.length() <= MAX_MESSAGE_ROW_LENGTH)
                {
                    addExtraMessage(list, str);
                }
                else
                {
                    final String[] words = str.split(" ");
                    String row = "";
                    for (final String word : words)
                    {
                        final String next = (row + " " + word).trim();
                        if (next.length() <= MAX_MESSAGE_ROW_LENGTH)
                        {
                            row = next;
                        }
                        else
                        {
                            addExtraMessage(list, row);
                            row = word;
                        }
                    }
                    addExtraMessage(list, row);
                }
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    private void addExtraMessage(final List<Component> list, final String str)
    {
        list.add(Component.literal(ChatFormatting.DARK_GRAY + (ChatFormatting.ITALIC + str + ChatFormatting.RESET)));
    }

    @OnlyIn(Dist.CLIENT)
    public final void addInformation(final List<Component> list, final CompoundTag compound)
    {
        list.add(Component.literal(ChatFormatting.GRAY + Localization.MODULE_INFO.MODULAR_COST.translate() + ": " + modularCost));
        if (compound != null && compound.contains("Data"))
        {
            final String extradatainfo = getModuleInfoText(compound.getByte("Data"));
            if (extradatainfo != null)
            {
                list.add(Component.literal(ChatFormatting.WHITE + extradatainfo));
            }
        }
        if (Screen.hasShiftDown())
        {
            if (getRenderingSides() == null || getRenderingSides().size() == 0)
            {
                list.add(Component.literal(ChatFormatting.DARK_AQUA + Localization.MODULE_INFO.NO_SIDES.translate()));
            }
            else
            {
                String sides = "";
                for (int i = 0; i < getRenderingSides().size(); ++i)
                {
                    final SIDE side = getRenderingSides().get(i);
                    if (i == 0)
                    {
                        sides += side.toString();
                    }
                    else if (i == getRenderingSides().size() - 1)
                    {
                        sides = sides + " " + Localization.MODULE_INFO.AND.translate() + " " + side.toString();
                    }
                    else
                    {
                        sides = sides + ", " + side.toString();
                    }
                }
                list.add(Component.literal(ChatFormatting.DARK_AQUA + Localization.MODULE_INFO.OCCUPIED_SIDES.translate(sides, String.valueOf(getRenderingSides().size()))));
            }
            if (getNemesis() != null && getNemesis().size() != 0)
            {
                if (getRenderingSides() == null || getRenderingSides().size() == 0)
                {
                    list.add(Component.literal(ChatFormatting.RED + Localization.MODULE_INFO.CONFLICT_HOWEVER.translate() + ":"));
                }
                else
                {
                    list.add(Component.literal(ChatFormatting.RED + Localization.MODULE_INFO.CONFLICT_ALSO.translate() + ":"));
                }
                for (final ModuleData module : getNemesis())
                {
                    list.add(Component.literal(ChatFormatting.RED + module.getName()));
                }
            }
            if (parent != null)
            {
                list.add(Component.literal(ChatFormatting.YELLOW + Localization.MODULE_INFO.REQUIREMENT.translate() + " " + parent.getName()));
            }
            if (getRequirement() != null && getRequirement().size() != 0)
            {
                for (final ModuleDataGroup group : getRequirement())
                {
                    list.add(Component.literal(ChatFormatting.YELLOW + Localization.MODULE_INFO.REQUIREMENT.translate() + " " + group.getCountName() + " " + group.getName()));
                }
            }
            if (getAllowDuplicate())
            {
                list.add(Component.literal(ChatFormatting.GREEN + Localization.MODULE_INFO.DUPLICATES.translate()));
            }
        }
        else
        {
            list.add(Component.literal(ChatFormatting.DARK_AQUA + Localization.MODULE_INFO.SHIFT_FOR_MORE.translate("SHIFT")));
        }
        //TODO API CHANGE
        list.add(Component.literal(ChatFormatting.BLUE + "Module Type: " + ChatFormatting.WHITE + moduleType.name()));
        addExtraMessage(list);
    }

    public static String checkForErrors(final ModuleDataHull hull, final ArrayList<ModuleData> modules)
    {
        if (getTotalCost(modules) > hull.getCapacity())
        {
            return Localization.MODULE_INFO.CAPACITY_ERROR.translate();
        }
        if (!isValidModuleCombo(hull, modules))
        {
            return Localization.MODULE_INFO.COMBINATION_ERROR.translate();
        }
        for (int i = 0; i < modules.size(); ++i)
        {
            final ModuleData mod1 = modules.get(i);
            if (mod1.getCost() > hull.getComplexityMax())
            {
                return Localization.MODULE_INFO.COMPLEXITY_ERROR.translate(mod1.getName());
            }
            if (mod1.getParent() != null && !modules.contains(mod1.getParent()))
            {
                return Localization.MODULE_INFO.PARENT_ERROR.translate(mod1.getName(), mod1.getParent().getName());
            }
            if (mod1.getNemesis() != null)
            {
                for (final ModuleData nemesis : mod1.getNemesis())
                {
                    if (modules.contains(nemesis))
                    {
                        return Localization.MODULE_INFO.NEMESIS_ERROR.translate(mod1.getName(), nemesis.getName());
                    }
                }
            }
            if (mod1.getRequirement() != null)
            {
                for (final ModuleDataGroup group : mod1.getRequirement())
                {
                    int count = 0;
                    for (final ModuleData mod2 : group.getModules())
                    {
                        for (final ModuleData mod3 : modules)
                        {
                            if (mod2.equals(mod3))
                            {
                                ++count;
                            }
                        }
                    }
                    if (count < group.getCount())
                    {
                        return Localization.MODULE_INFO.PARENT_ERROR.translate(mod1.getName(), group.getCountName() + " " + group.getName());
                    }
                }
            }
            for (int j = i + 1; j < modules.size(); ++j)
            {
                final ModuleData mod4 = modules.get(j);
                if (mod1 == mod4)
                {
                    if (!mod1.getAllowDuplicate())
                    {
                        return Localization.MODULE_INFO.DUPLICATE_ERROR.translate(mod1.getName());
                    }
                }
                else if (mod1.getRenderingSides() != null && mod4.getRenderingSides() != null)
                {
                    SIDE clash = SIDE.NONE;
                    for (final SIDE side1 : mod1.getRenderingSides())
                    {
                        for (final SIDE side2 : mod4.getRenderingSides())
                        {
                            if (side1 == side2)
                            {
                                clash = side1;
                                break;
                            }
                        }
                        if (clash != SIDE.NONE)
                        {
                            break;
                        }
                    }
                    if (clash != SIDE.NONE)
                    {
                        return Localization.MODULE_INFO.CLASH_ERROR.translate(mod1.getName(), mod4.getName(), clash.toString());
                    }
                }
            }
        }
        return null;
    }

    public static int getTotalCost(final ArrayList<ModuleData> modules)
    {
        int currentCost = 0;
        for (final ModuleData module : modules)
        {
            currentCost += module.getCost();
        }
        return currentCost;
    }

    private static long calculateCombinations()
    {
        long combinations = 0L;
        final ArrayList<ModuleData> potential = new ArrayList<>();
        for (final ModuleData module : ModuleData.moduleList.values())
        {
            if (!(module instanceof ModuleDataHull))
            {
                potential.add(module);
            }
        }
        for (final ModuleData module : ModuleData.moduleList.values())
        {
            if (module instanceof ModuleDataHull)
            {
                final ArrayList<ModuleData> modules = new ArrayList<>();
                combinations += populateHull((ModuleDataHull) module, modules, (ArrayList<ModuleData>) potential.clone(), 0);
                System.out.println("Hull added: " + combinations);
            }
        }
        return combinations;
    }

    private static long populateHull(final ModuleDataHull hull, final ArrayList<ModuleData> attached, final ArrayList<ModuleData> potential, final int depth)
    {
        if (checkForErrors(hull, attached) != null)
        {
            return 0L;
        }
        long combinations = 1L;
        final Iterator itt = potential.iterator();
        while (itt.hasNext())
        {
            final ModuleData module = (ModuleData) itt.next();
            final ArrayList<ModuleData> attachedCopy = (ArrayList<ModuleData>) attached.clone();
            attachedCopy.add(module);
            final ArrayList<ModuleData> potentialCopy = (ArrayList<ModuleData>) potential.clone();
            itt.remove();
            combinations += populateHull(hull, attachedCopy, potentialCopy, depth + 1);
            if (depth < 3)
            {
                System.out.println("Modular state[" + depth + "]: " + combinations);
            }
        }
        return combinations;
    }

    public String getRawName()
    {
        return name.replace(":", "").replace("'", "").replace(" ", "_").replace("-", "_").toLowerCase();
    }

    public enum SIDE
    {
        NONE(Localization.MODULE_INFO.SIDE_NONE), TOP(Localization.MODULE_INFO.SIDE_TOP), CENTER(Localization.MODULE_INFO.SIDE_CENTER), BOTTOM(Localization.MODULE_INFO.SIDE_BOTTOM), BACK(Localization.MODULE_INFO.SIDE_BACK), LEFT(Localization.MODULE_INFO.SIDE_LEFT), RIGHT(Localization.MODULE_INFO.SIDE_RIGHT), FRONT(Localization.MODULE_INFO.SIDE_FRONT);

        private Localization.MODULE_INFO name;

        SIDE(final Localization.MODULE_INFO name)
        {
            this.name = name;
        }

        @Override
        public String toString()
        {
            return name.translate();
        }
    }
}
