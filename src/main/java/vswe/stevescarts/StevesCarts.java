package vswe.stevescarts;

import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import vswe.stevescarts.blocks.tileentities.TileEntityCargo;
import vswe.stevescarts.client.renders.ItemStackRenderer;
import vswe.stevescarts.client.renders.RenderModulerCart;
import vswe.stevescarts.compat.CompatHandler;
import vswe.stevescarts.entitys.CartDataSerializers;
import vswe.stevescarts.handlers.EventHandler;
import vswe.stevescarts.helpers.EventHelper;
import vswe.stevescarts.helpers.GiftItem;
import vswe.stevescarts.init.*;
import vswe.stevescarts.modules.data.ModuleData;
import vswe.stevescarts.network.PacketHandler;
import vswe.stevescarts.upgrades.AssemblerUpgrade;

@Mod(Constants.MOD_ID)
public class StevesCarts
{
    public static StevesCarts instance;

    public static Logger logger = LogManager.getLogger();

    public StevesCarts()
    {
        instance = this;
        IEventBus iEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        iEventBus.addListener(this::commonSetup);
        ModuleData.init();
        AssemblerUpgrade.init();
        CompatHandler.init(iEventBus);
        ModItems.ITEMS.register(iEventBus);
        ModBlocks.BLOCKS.register(iEventBus);
        ModEntities.ENTITIES.register(iEventBus);
        ModBlocks.TILES_ENTITIES.register(iEventBus);
        ModContainers.CONTAINERS.register(iEventBus);
        iEventBus.addListener(this::clientInit);

        SCConfig.loadConfig(SCConfig.CLIENT_CONFIG, FMLPaths.CONFIGDIR.get().resolve(Constants.MOD_ID + "-client.toml"));
        SCConfig.loadConfig(SCConfig.COMMON_CONFIG, FMLPaths.CONFIGDIR.get().resolve(Constants.MOD_ID + "-common.toml"));
        ForgeMod.enableMilkFluid();
    }

    public void commonSetup(final FMLCommonSetupEvent event)
    {
        EventHelper.setupEvents();
        PacketHandler.register();

        ModuleData.init();
        AssemblerUpgrade.init();
        MinecraftForge.EVENT_BUS.register(this);

        MinecraftForge.EVENT_BUS.register(new EventHandler());

        GiftItem.init();
        TileEntityCargo.loadSelectionSettings();
        CartDataSerializers.init();
    }

    public void clientInit(final FMLClientSetupEvent event)
    {
        ModScreens.init();
        CompatHandler.initClient();
        ModuleData.initModels();

        ItemProperties.register(ModItems.CARTS.get(), new ResourceLocation(Constants.MOD_ID, ""), ItemStackRenderer.getInstance());

        EntityRenderers.register(ModEntities.MODULAR_CART.get(), RenderModulerCart::new);
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.ADVANCED_DETECTOR.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.JUNCTION.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.BATTERIES.get(), RenderType.cutout());
    }
}
