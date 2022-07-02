package vswe.stevescarts;

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
import vswe.stevescarts.entitys.CartDataSerializers;
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
        ModEntities.ENTITIES.register(iEventBus);
        ModBlocks.TILES_ENTITIES.register(iEventBus);
        ModContainers.CONTAINERS.register(iEventBus);

        iEventBus.addListener(this::clientInit);

        SCConfig.loadConfig(SCConfig.CLIENT_CONFIG, FMLPaths.CONFIGDIR.get().resolve(Constants.MOD_ID + "-client.toml"));
        SCConfig.loadConfig(SCConfig.COMMON_CONFIG, FMLPaths.CONFIGDIR.get().resolve(Constants.MOD_ID + "-common.toml"));
        ForgeMod.enableMilkFluid();
    }

    public void clientInit(final FMLClientSetupEvent event)
    {
        StevesCartsClient.clientInit(event);
    }

    public void commonSetup(final FMLCommonSetupEvent event)
    {
        PacketHandler.register();

        AssemblerUpgrade.init();
        MinecraftForge.EVENT_BUS.register(this);

        TileEntityCargo.loadSelectionSettings();
        CartDataSerializers.init();
    }
}
