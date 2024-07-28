package vswe.stevescarts.network.packets;

import io.netty.buffer.ByteBuf;
import net.creeperhost.polylib.data.serializable.AbstractDataStore;
import net.minecraft.client.Minecraft;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.handling.IPayloadHandler;
import vswe.stevescarts.Constants;
import vswe.stevescarts.polylib.DataEntity;
import vswe.stevescarts.polylib.EntityData;

import java.util.List;

public class PacketEntityData implements CustomPacketPayload {
    public static final Type<PacketEntityData> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "entity_data"));
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

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public void write(RegistryFriendlyByteBuf buf) {
        buf.writeVarInt(entityId);
        buf.writeVarInt(index);
        data.toBytes(buf);
    }

    public static PacketEntityData read(RegistryFriendlyByteBuf buffer) {
        int entityId = buffer.readVarInt();
        int index = buffer.readVarInt();
        ByteBuf copy = buffer.copy();
        return new PacketEntityData(entityId, index, new RegistryFriendlyByteBuf(copy, buffer.registryAccess()));
    }

    public static class Handler implements IPayloadHandler<PacketEntityData> {
        @Override
        public void handle(PacketEntityData msg, IPayloadContext ctx) {
            if (ctx.flow() != PacketFlow.CLIENTBOUND) return;
            ctx.enqueueWork(() -> handleClientSide(msg, ctx));
        }
    }

    @OnlyIn (Dist.CLIENT)
    private static void handleClientSide(PacketEntityData msg, IPayloadContext ctx) {
        Level level = Minecraft.getInstance().level;
        if (level == null) return;

        if (!(level.getEntity(msg.entityId) instanceof DataEntity dataEntity)) return;

        List<EntityData<?, ?>> list = dataEntity.getEntityDataList();
        if (msg.index < 0 || msg.index >= list.size()) return;

        EntityData<?, ?> data = list.get(msg.index);
        if (msg.buffer != null){
            data.fromBytes(msg.buffer);
        } else {
            data.set(cast(msg.data.get()));
        }
    }

    private static <T> T cast(Object object) {
        return (T) object;
    }
}
