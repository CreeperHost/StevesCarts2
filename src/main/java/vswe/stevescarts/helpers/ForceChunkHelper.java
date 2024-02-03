package vswe.stevescarts.helpers;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.javafmlmod.FMLJavaModLoadingContext;
import net.neoforged.neoforge.common.world.chunk.RegisterTicketControllersEvent;
import net.neoforged.neoforge.common.world.chunk.TicketController;
import vswe.stevescarts.Constants;

/**
 * Created by brandon3055 on 03/02/2024
 */
public class ForceChunkHelper {

    public static final TicketController CONTROLLER = new TicketController(new ResourceLocation(Constants.MOD_ID, "ticket_controller"));

    public static void init() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.addListener(ForceChunkHelper::registerTicketController);
    }

    public static void registerTicketController(RegisterTicketControllersEvent event) {
        event.register(CONTROLLER);
    }
}
