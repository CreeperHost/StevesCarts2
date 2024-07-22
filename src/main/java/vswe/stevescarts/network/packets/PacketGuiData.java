package vswe.stevescarts.network.packets;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.handling.IPayloadHandler;
import vswe.stevescarts.Constants;
import vswe.stevescarts.containers.ContainerBase;

public class PacketGuiData implements CustomPacketPayload {
    public static final Type<PacketGuiData> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "gui_data"));
    private final int containerId;
    private final int dataId;
    private final int data;

    public PacketGuiData(int containerId, int dataId, int data) {
        this.containerId = containerId;
        this.dataId = dataId;
        this.data = data;
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeInt(containerId);
        buf.writeVarInt(dataId);
        buf.writeVarInt(data);
    }

    public static PacketGuiData read(FriendlyByteBuf buffer) {
        return new PacketGuiData(buffer.readInt(), buffer.readVarInt(), buffer.readVarInt());
    }

    public static class Handler implements IPayloadHandler<PacketGuiData> {
        @Override
        public void handle(PacketGuiData msg, IPayloadContext ctx) {
            if (ctx.flow() != PacketFlow.CLIENTBOUND) return;
            ctx.enqueueWork(() -> handleClientSide(msg, ctx));
        }
    }

    @OnlyIn (Dist.CLIENT)
    private static void handleClientSide(PacketGuiData msg, IPayloadContext ctx) {
        Player player = Minecraft.getInstance().player;
        if (player != null && player.containerMenu instanceof ContainerBase menu && menu.containerId == msg.containerId) {
            menu.receiveGuiData(msg.dataId, msg.data);
        }
    }
}
