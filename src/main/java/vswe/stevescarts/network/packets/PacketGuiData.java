package vswe.stevescarts.network.packets;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import vswe.stevescarts.containers.ContainerBase;

import java.util.function.Supplier;

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
        public static void handle(PacketGuiData msg, Supplier<NetworkEvent.Context> ctx) {
            if (ctx.get().getDirection() != NetworkDirection.PLAY_TO_CLIENT) {
                return;
            }

            ctx.get().enqueueWork(() -> handleClientSide(msg, ctx));
            ctx.get().setPacketHandled(true);
        }
    }

    @OnlyIn(Dist.CLIENT)
    private static void handleClientSide(PacketGuiData msg, Supplier<NetworkEvent.Context> ctx) {
        Player player = Minecraft.getInstance().player;
        if (player != null && player.containerMenu instanceof ContainerBase menu && menu.containerId == msg.containerId) {
            menu.receiveGuiData(msg.dataId, msg.data);
        }
    }
}
