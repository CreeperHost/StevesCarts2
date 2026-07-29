package vswe.stevescarts;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import vswe.stevescarts.blocks.tileentities.TileEntityCargo;
import vswe.stevescarts.client.StevesCartsCreativeTabs;
import vswe.stevescarts.events.OverlayEventHandler;
import vswe.stevescarts.helpers.ForceChunkHelper;
import vswe.stevescarts.init.*;
import vswe.stevescarts.network.PacketHandler;
import vswe.stevescarts.upgrades.AssemblerUpgrade;

@Mod(Constants.MOD_ID)
public class StevesCarts {
    public static StevesCarts INSTANCE;

    public static Logger LOGGER = LogManager.getLogger();

    public StevesCarts(IEventBus modBus, ModContainer container) {
        INSTANCE = this;
        modBus.addListener(this::commonSetup);
        StevesCartsModules.init();

        AssemblerUpgrade.init();
        ModItems.ITEMS.register(modBus);
        ModBlocks.BLOCKS.register(modBus);
        StevesCartsCreativeTabs.CREATIVE_TAB.register(modBus);
        ModEntities.ENTITIES.register(modBus);
        ModBlocks.TILES_ENTITIES.register(modBus);
        ModContainers.CONTAINERS.register(modBus);
        modBus.addListener(this::creativeTabBuildEvent);

        modBus.addListener(this::clientInit);

        container.registerConfig(ModConfig.Type.CLIENT, SCConfig.clientSpec, Constants.MOD_ID + "-client.toml");
        container.registerConfig(ModConfig.Type.COMMON, SCConfig.commonSpec, Constants.MOD_ID + "-common.toml");
        modBus.register(SCConfig.class);

        NeoForgeMod.enableMilkFluid();
        ForceChunkHelper.init(modBus);
        ModCapabilities.init(modBus);
        PacketHandler.init(modBus);
        ModSerializers.init(modBus);
        ModItemData.init(modBus);

        if (FMLEnvironment.getDist().isClient()) {
            OverlayEventHandler.init(modBus);
            StevesCartsClient.init(modBus);
        }
    }

    public void creativeTabBuildEvent(BuildCreativeModeTabContentsEvent event) {
        if (event.getTab() == StevesCartsCreativeTabs.BLOCKS.get()) {
            ModBlocks.BLOCKS.getEntries().forEach(blockRegistryObject -> event.accept(blockRegistryObject.get()));
        }
        if (event.getTab() == StevesCartsCreativeTabs.ITEMS.get()) {
            ModItems.COMPONENTS.forEach((moduleData, itemSupplier) -> event.accept(itemSupplier.get()));
        }
        if (event.getTab() == StevesCartsCreativeTabs.MODULES.get()) {
            ModItems.MODULES.forEach((moduleData, itemSupplier) -> event.accept(itemSupplier.get()));
        }
    }

    public void clientInit(final FMLClientSetupEvent event) {
        StevesCartsClient.clientInit(event);
    }

    public void commonSetup(final FMLCommonSetupEvent event) {
        AssemblerUpgrade.init();

        TileEntityCargo.loadSelectionSettings();
    }
}
