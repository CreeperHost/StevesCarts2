package vswe.stevescarts;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.javafmlmod.FMLJavaModLoadingContext;
import net.neoforged.fml.loading.FMLPaths;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import vswe.stevescarts.blocks.tileentities.TileEntityCargo;
import vswe.stevescarts.client.StevesCartsCreativeTabs;
import vswe.stevescarts.entities.CartDataSerializers;
import vswe.stevescarts.helpers.ForceChunkHelper;
import vswe.stevescarts.init.*;
import vswe.stevescarts.network.PacketHandler;
import vswe.stevescarts.upgrades.AssemblerUpgrade;

@Mod(Constants.MOD_ID)
public class StevesCarts
{
    public static StevesCarts INSTANCE;

    public static Logger LOGGER = LogManager.getLogger();

    public StevesCarts()
    {
        INSTANCE = this;
        IEventBus iEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        iEventBus.addListener(this::commonSetup);
        StevesCartsModules.init();

        AssemblerUpgrade.init();
        ModItems.ITEMS.register(iEventBus);
        ModBlocks.BLOCKS.register(iEventBus);
        StevesCartsCreativeTabs.CREATIVE_TAB.register(iEventBus);
        ModEntities.ENTITIES.register(iEventBus);
        ModBlocks.TILES_ENTITIES.register(iEventBus);
        ModContainers.CONTAINERS.register(iEventBus);
        iEventBus.addListener(this::creativeTabBuildEvent);

        iEventBus.addListener(this::clientInit);

        SCConfig.loadConfig(SCConfig.CLIENT_CONFIG, FMLPaths.CONFIGDIR.get().resolve(Constants.MOD_ID + "-client.toml"));
        SCConfig.loadConfig(SCConfig.COMMON_CONFIG, FMLPaths.CONFIGDIR.get().resolve(Constants.MOD_ID + "-common.toml"));
        NeoForgeMod.enableMilkFluid();
        ForceChunkHelper.init();
    }

    public void creativeTabBuildEvent(BuildCreativeModeTabContentsEvent event)
    {
        if(event.getTab() == StevesCartsCreativeTabs.BLOCKS.get())
        {
            ModBlocks.BLOCKS.getEntries().forEach(blockRegistryObject -> event.accept(blockRegistryObject.get()));
        }
        if(event.getTab() == StevesCartsCreativeTabs.ITEMS.get())
        {
            ModItems.COMPONENTS.forEach((moduleData, itemSupplier) -> event.accept(itemSupplier.get()));
        }
        if(event.getTab() == StevesCartsCreativeTabs.MODULES.get())
        {
            ModItems.MODULES.forEach((moduleData, itemSupplier) -> event.accept(itemSupplier.get()));
        }
    }

    public void clientInit(final FMLClientSetupEvent event)
    {
        StevesCartsClient.clientInit(event);
    }

    public void commonSetup(final FMLCommonSetupEvent event)
    {
        PacketHandler.register();

        AssemblerUpgrade.init();

        TileEntityCargo.loadSelectionSettings();
        CartDataSerializers.init();
    }
}
