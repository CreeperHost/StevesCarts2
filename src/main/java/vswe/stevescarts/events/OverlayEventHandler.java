package vswe.stevescarts.events;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import vswe.stevescarts.Constants;
import vswe.stevescarts.entities.ModularMinecart;

/**
 * Created by brandon3055 on 10/03/2023
 */
public class OverlayEventHandler {

    public static void init(IEventBus iEventBus) {
        iEventBus.addListener(OverlayEventHandler::registerOverlay);
    }

    private static void registerOverlay(RegisterGuiLayersEvent event) {
        event.registerAboveAll(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "cart_overlay"), (graphics, deltaTracker) -> {
            Player player = Minecraft.getInstance().player;
            if (Minecraft.getInstance().screen == null && player.getVehicle() instanceof ModularMinecart cart) {
                cart.renderOverlay(graphics, deltaTracker.getGameTimeDeltaPartialTick(false));
            }
        });
    }
}
