package vswe.stevescarts.network.packets;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.NetworkEvent;
import net.neoforged.neoforge.network.PlayNetworkDirection;
import vswe.stevescarts.containers.ContainerBase;

public class PacketGuiData {
    private final int containerId;
    private final int dataId;
    private final int data;

    public PacketGuiData(int containerId, int dataId, int data) {
        this.containerId = containerId;
        this.dataId = dataId;
        this.data = data;
    }

    public static void encode(PacketGuiData msg, FriendlyByteBuf buffer) {
        buffer.writeInt(msg.containerId);
        buffer.writeVarInt(msg.dataId);
        buffer.writeVarInt(msg.data);
    }

    public static PacketGuiData decode(FriendlyByteBuf buffer) {
        return new PacketGuiData(buffer.readInt(), buffer.readVarInt(), buffer.readVarInt());
    }

    public static class Handler {
        public static void handle(PacketGuiData msg, NetworkEvent.Context ctx) {
            if (ctx.getDirection() != PlayNetworkDirection.PLAY_TO_CLIENT) {
                return;
            }

            ctx.enqueueWork(() -> handleClientSide(msg, ctx));
            ctx.setPacketHandled(true);
        }
    }

    @OnlyIn(Dist.CLIENT)
    private static void handleClientSide(PacketGuiData msg, NetworkEvent.Context ctx) {
        Player player = Minecraft.getInstance().player;
        if (player != null && player.containerMenu instanceof ContainerBase menu && menu.containerId == msg.containerId) {
            menu.receiveGuiData(msg.dataId, msg.data);
        }
    }
}
