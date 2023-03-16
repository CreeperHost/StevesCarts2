package vswe.stevescarts.events;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import vswe.stevescarts.entities.EntityMinecartModular;

/**
 * Created by brandon3055 on 10/03/2023
 */
public class OverlayEventHandler {

    public static void init() {
        MinecraftForge.EVENT_BUS.addListener(OverlayEventHandler::onRenderTick);
    }

    private static void onRenderTick(TickEvent.RenderTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;

        Minecraft minecraft = Minecraft.getInstance();
        Player player = minecraft.player;
        if (minecraft.screen == null && player.getVehicle() instanceof EntityMinecartModular cart) {
            cart.renderOverlay(new PoseStack(), minecraft);
        }
    }

}
