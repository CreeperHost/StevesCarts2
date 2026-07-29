package vswe.stevescarts.network.packets;

import io.netty.buffer.ByteBuf;
import net.creeperhost.polylib.data.serializable.AbstractDataStore;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.handling.IPayloadHandler;
import vswe.stevescarts.Constants;
import vswe.stevescarts.client.network.ClientPacketHandlers;


public class PacketEntityData implements CustomPacketPayload {
    public static final Type<PacketEntityData> TYPE = new Type<>(Identifier.fromNamespaceAndPath(Constants.MOD_ID, "entity_data"));
    private final int entityId;
    private final int index;
    private final AbstractDataStore<?> data;
    private RegistryFriendlyByteBuf buffer = null;

    public PacketEntityData(int entityId, int index, AbstractDataStore<?> data) {
        this.entityId = entityId;
        this.index = index;
        this.data = data;
    }

    public PacketEntityData(int entityId, int index, RegistryFriendlyByteBuf buffer) {
        this.entityId = entityId;
        this.index = index;
        this.buffer = buffer;
        this.data = null;
    }

    public static PacketEntityData read(RegistryFriendlyByteBuf buffer) {
        int entityId = buffer.readVarInt();
        int index = buffer.readVarInt();
        ByteBuf copy = buffer.copy();
        //TODO, Need to find a better way to handle this packet.
        while (buffer.readableBytes() > 0) buffer.readByte();
        return new PacketEntityData(entityId, index, new RegistryFriendlyByteBuf(copy, buffer.registryAccess()));
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public void write(RegistryFriendlyByteBuf buf) {
        buf.writeVarInt(entityId);
        buf.writeVarInt(index);
        data.toBytes(buf);
    }

    public int getEntityId() {
        return entityId;
    }

    public int getIndex() {
        return index;
    }

    public RegistryFriendlyByteBuf getBuffer() {
        return buffer;
    }

    public AbstractDataStore<?> getData() {
        return data;
    }

    public static class Handler implements IPayloadHandler<PacketEntityData> {
        @Override
        public void handle(PacketEntityData msg, IPayloadContext ctx) {
            if (ctx.flow() != PacketFlow.CLIENTBOUND) return;
            ctx.enqueueWork(() -> ClientPacketHandlers.handleEntityData(msg, ctx));
        }
    }
}
