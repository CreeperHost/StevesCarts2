package vswe.stevescarts.init;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import vswe.stevescarts.client.guis.*;

public class ModScreens {
    public static void init(IEventBus modEventBus) {
        modEventBus.addListener(ModScreens::registerScreens);
    }

    private static void registerScreens(RegisterMenuScreensEvent event) {
        event.register(ModContainers.CONTAINER_CART_ASSEMBLER.get(), GuiCartAssembler::new);
        event.register(ModContainers.CONTAINER_MINECART.get(), GuiMinecart::new);
        event.register(ModContainers.CONTAINER_CARGO.get(), GuiCargo::new);
        event.register(ModContainers.CONTAINER_DISTRIBUTOR.get(), GuiDistributor::new);
        event.register(ModContainers.CONTAINER_UPGRADE.get(), GuiUpgrade::new);
        event.register(ModContainers.CONTAINER_LIQUID.get(), GuiLiquid::new);
        event.register(ModContainers.CONTAINER_ACTIVATOR.get(), GuiActivator::new);
    }
}
