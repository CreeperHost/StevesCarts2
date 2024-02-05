package vswe.stevescarts.init;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.items.wrapper.SidedInvWrapper;

/**
 * Created by brandon3055 on 05/02/2024
 */
public class ModCapabilities {

    public static void init(IEventBus modBus) {
        modBus.addListener(ModCapabilities::registerCapabilities);
    }

    private static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, ModBlocks.EXTERNAL_DISTRIBUTOR_TILE.get(), SidedInvWrapper::new);
        event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, ModBlocks.EXTERNAL_DISTRIBUTOR_TILE.get(), (entity, side) -> entity.fluidHandlerMap.get(side));

        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, ModBlocks.CARGO_MANAGER_TILE.get(), (entity, side) -> entity.createHandler());
    }
}
