package vswe.stevescarts;

import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
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
import vswe.stevescarts.client.models.workers.ModelLiquidDrainer;
import vswe.stevescarts.client.models.workers.ModelRailer;
import vswe.stevescarts.client.models.workers.ModelTorchplacer;
import vswe.stevescarts.client.models.workers.ModelTrackRemover;
import vswe.stevescarts.client.models.workers.tools.ModelDrill;
import vswe.stevescarts.client.models.workers.tools.ModelFarmer;
import vswe.stevescarts.client.models.workers.tools.ModelWoodCutter;
import vswe.stevescarts.client.renders.ItemStackRenderer;
import vswe.stevescarts.client.renders.RenderModulerCart;
import vswe.stevescarts.events.OverlayEventHandler;
import vswe.stevescarts.helpers.ResourceHelper;
import vswe.stevescarts.init.ModBlocks;
import vswe.stevescarts.init.ModEntities;
import vswe.stevescarts.init.ModItems;
import vswe.stevescarts.init.ModScreens;

import static vswe.stevescarts.init.StevesCartsModules.*;

public class StevesCartsClient
{
    public static void init() {
        OverlayEventHandler.init();
    }

    public static void clientInit(final FMLClientSetupEvent event)
    {
        ModScreens.init();
        initModels();
        EntityRenderers.register(ModEntities.MODULAR_CART.get(), RenderModulerCart::new);
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

        SHOOTER.removeModel("Top").addModel("Rig", new ModelShootingRig()).addModel("Pipes", new ModelGun());
        ADVANCED_SHOOTER.removeModel("Top").addModel("Rig", new ModelShootingRig()).addModel("MobDetector", new ModelMobDetector()).addModel("Pipes", new ModelSniperRifle());

        CLEANER.addModel("Top", new ModelHullTop(ResourceHelper.getResource("/models/cleanerModelTop.png"))).addModel("Cleaner", new ModelCleaner());
        DYNAMITE_CARRIER.addModel("Tnt", new ModelDynamite(ResourceHelper.getResource("/models/tntModel.png")));
        DIVINE_SHIELD.addModel("Shield", new ModelShield()).setModelMult(0.68f);
        NOTE_SEQUENCER.setModelMult(0.65f).addModel("Speakers", new ModelNote());
        CAGE.removeModel("Top").addModel("Cage", new ModelCage(), false).addModel("Cage", new ModelCage(), true).setModelMult(0.65f);
        SIDE_TANKS.addModel("SideTanks", new ModelSideTanks());
        TOP_TANK.addModel("TopTank", new ModelTopTank());
        ADVANCED_TANK.addModel("LargeTank", new ModelAdvancedTank()).removeModel("Top");
        FRONT_TANK.setModelMult(0.68f).addModel("FrontTank", new ModelFrontTank());
        CLEANER_LIQUID.addModel("Top", new ModelHullTop(ResourceHelper.getResource("/models/cleanerModelTop.png"))).addModel("Cleaner", new ModelLiquidDrainer());
        LAWN_MOWER.addModel("LawnMower", new ModelLawnMower()).setModelMult(0.4f);
        CAKE_SERVER.addModel("Cake", new ModelCake());
    }
}
