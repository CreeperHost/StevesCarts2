package vswe.stevescarts.network.packets;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;
import vswe.stevescarts.Constants;
import vswe.stevescarts.containers.ContainerBase;

public class PacketGuiData implements CustomPacketPayload {
    public static final ResourceLocation ID = new ResourceLocation(Constants.MOD_ID, "gui_data");
    private final int containerId;
    private final int dataId;
    private final int data;

    public PacketGuiData(int containerId, int dataId, int data) {
        this.containerId = containerId;
        this.dataId = dataId;
        this.data = data;
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeInt(containerId);
        buf.writeVarInt(dataId);
        buf.writeVarInt(data);
    }

    @Override
    public ResourceLocation id() {
        return ID;
    }

    public static PacketGuiData read(FriendlyByteBuf buffer) {
        return new PacketGuiData(buffer.readInt(), buffer.readVarInt(), buffer.readVarInt());
    }

    public static void handle(PacketGuiData msg, PlayPayloadContext ctx) {
        if (ctx.flow() != PacketFlow.CLIENTBOUND) return;
        ctx.workHandler().execute(() -> handleClientSide(msg, ctx));
    }

    @OnlyIn (Dist.CLIENT)
    private static void handleClientSide(PacketGuiData msg, PlayPayloadContext ctx) {
        Player player = Minecraft.getInstance().player;
        if (player != null && player.containerMenu instanceof ContainerBase menu && menu.containerId == msg.containerId) {
            menu.receiveGuiData(msg.dataId, msg.data);
        }
    }
}
