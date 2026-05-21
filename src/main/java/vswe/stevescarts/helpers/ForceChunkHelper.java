package vswe.stevescarts.helpers;

import net.minecraft.resources.Identifier;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.world.chunk.RegisterTicketControllersEvent;
import net.neoforged.neoforge.common.world.chunk.TicketController;
import vswe.stevescarts.Constants;

/**
 * Created by brandon3055 on 03/02/2024
 */
public class ForceChunkHelper {

    public static final TicketController CONTROLLER = new TicketController(Identifier.fromNamespaceAndPath(Constants.MOD_ID, "ticket_controller"));

    public static void init(IEventBus iEventBus) {
        iEventBus.addListener(ForceChunkHelper::registerTicketController);
    }

    public static void registerTicketController(RegisterTicketControllersEvent event) {
        event.register(CONTROLLER);
    }
}
