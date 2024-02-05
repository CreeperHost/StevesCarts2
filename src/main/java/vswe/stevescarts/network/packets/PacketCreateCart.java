package vswe.stevescarts.network.packets;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;
import vswe.stevescarts.Constants;
import vswe.stevescarts.blocks.tileentities.TileEntityCartAssembler;

public class PacketCreateCart implements CustomPacketPayload {
    public static final ResourceLocation ID = new ResourceLocation(Constants.MOD_ID, "create_cart");
    private final BlockPos blockPos;
    private final int id;
    private final byte[] data;

    public PacketCreateCart(BlockPos blockPos, int id, byte[] data) {
        this.blockPos = blockPos;
        this.id = id;
        this.data = data;
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeBlockPos(blockPos);
        buf.writeInt(id);
        buf.writeByteArray(data);
    }

    @Override
    public ResourceLocation id() {
        return ID;
    }

    public static PacketCreateCart read(FriendlyByteBuf buffer) {
        return new PacketCreateCart(buffer.readBlockPos(), buffer.readInt(), buffer.readByteArray());
    }

    public static void handle(final PacketCreateCart msg, PlayPayloadContext ctx) {
        if (ctx.flow() != PacketFlow.SERVERBOUND) return;

        ctx.workHandler().execute(() -> {
            if (msg.blockPos != null && ctx.player().orElse(null) instanceof ServerPlayer player) {
                BlockPos blockPos = msg.blockPos;
                if (player.level().isLoaded(blockPos) && player.level().getBlockEntity(blockPos) instanceof TileEntityCartAssembler tileEntityCartAssembler) {
                    tileEntityCartAssembler.receivePacket(msg.id, msg.data, player);
                }
            }
        });
    }
}
