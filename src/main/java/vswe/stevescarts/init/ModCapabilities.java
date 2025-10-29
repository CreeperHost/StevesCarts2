package vswe.stevescarts.init;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import vswe.stevescarts.SCConfig;

/**
 * Created by brandon3055 on 05/02/2024
 */
public class ModCapabilities {

    public static void init(IEventBus modBus) {
        modBus.addListener(ModCapabilities::registerCapabilities);
    }

    private static void registerCapabilities(RegisterCapabilitiesEvent event) {
        //TODO Capabilities
//        event.registerBlockEntity(Capabilities.Item.BLOCK, ModBlocks.EXTERNAL_DISTRIBUTOR_TILE.get(), SidedInvWrapper::new);
//        event.registerBlockEntity(Capabilities.Fluid.BLOCK, ModBlocks.EXTERNAL_DISTRIBUTOR_TILE.get(), (entity, side) -> entity.fluidHandlerMap.get(side));
//
//        event.registerBlockEntity(Capabilities.Item.BLOCK, ModBlocks.CARGO_MANAGER_TILE.get(), (entity, side) -> entity.createHandler());
//        if(SCConfig.COMMON.assemblerInsertFuel.get())
//            event.registerBlockEntity(Capabilities.Item.BLOCK, ModBlocks.CART_ASSEMBLER_TILE.get(), SidedInvWrapper::new);v
    }
}
