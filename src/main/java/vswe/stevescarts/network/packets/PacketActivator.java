package vswe.stevescarts.network.packets;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.handling.IPayloadHandler;
import vswe.stevescarts.Constants;
import vswe.stevescarts.blocks.tileentities.TileEntityActivator;

public class PacketActivator implements CustomPacketPayload {
    public static final Type<PacketActivator> TYPE = new Type<>(Identifier.fromNamespaceAndPath(Constants.MOD_ID, "activator"));
    private final BlockPos blockPos;
    private final int id;
    private final byte[] array;

    public PacketActivator(BlockPos blockPos, int id, byte[] array) {
        this.blockPos = blockPos;
        this.id = id;
        this.array = array;
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeBlockPos(blockPos);
        buf.writeInt(id);
        buf.writeByteArray(array);
    }

    public static PacketActivator read(FriendlyByteBuf buffer) {
        return new PacketActivator(buffer.readBlockPos(), buffer.readInt(), buffer.readByteArray());
    }

    public static class Handler implements IPayloadHandler<PacketActivator> {
        @Override
        public void handle(PacketActivator msg, IPayloadContext ctx) {
            if (ctx.flow() != PacketFlow.SERVERBOUND) return;

            ctx.enqueueWork(() -> {
                if (msg.blockPos != null && ctx.player() instanceof ServerPlayer player) {
                    BlockPos blockPos = msg.blockPos;
                    if (player.level().getBlockEntity(blockPos) != null && player.level().getBlockEntity(blockPos) instanceof TileEntityActivator tileEntityActivator) {
                        tileEntityActivator.receivePacket(msg.id, msg.array, player);
                    }
                }
            });
        }
    }
}
