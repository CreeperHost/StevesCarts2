package vswe.stevescarts.events;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.javafmlmod.FMLJavaModLoadingContext;
import net.neoforged.neoforge.client.event.RegisterGuiOverlaysEvent;
import vswe.stevescarts.Constants;
import vswe.stevescarts.entities.EntityMinecartModular;

/**
 * Created by brandon3055 on 10/03/2023
 */
public class OverlayEventHandler {

    public static void init() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.addListener(OverlayEventHandler::registerOverlay);
    }

    private static void registerOverlay(RegisterGuiOverlaysEvent event) {
        event.registerAboveAll(new ResourceLocation(Constants.MOD_ID, "cart_overlay"), (gui, guiGraphics, partialTick, screenWidth, screenHeight) -> {
            Player player = gui.getMinecraft().player;
            if (gui.getMinecraft().screen == null && player.getVehicle() instanceof EntityMinecartModular cart) {
                cart.renderOverlay(gui, guiGraphics, partialTick);
            }
        });
    }
}
