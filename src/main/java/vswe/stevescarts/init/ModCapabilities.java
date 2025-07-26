package vswe.stevescarts.init;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.items.wrapper.InvWrapper;
import net.neoforged.neoforge.items.wrapper.SidedInvWrapper;
import vswe.stevescarts.SCConfig;
import vswe.stevescarts.helpers.CartInvWrapper;

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
        if(SCConfig.assemblerInsertFuel.get()) {
            event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, ModBlocks.CART_ASSEMBLER_TILE.get(), SidedInvWrapper::new);
        }

        event.registerEntity(Capabilities.ItemHandler.ENTITY_AUTOMATION, ModEntities.MODULAR_CART.get(), (cart, context) -> new CartInvWrapper(cart));
        event.registerEntity(Capabilities.FluidHandler.ENTITY, ModEntities.MODULAR_CART.get(), (cart, context) -> cart);
    }
}
