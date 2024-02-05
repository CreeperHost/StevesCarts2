package vswe.stevescarts.network.packets;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;
import vswe.stevescarts.Constants;
import vswe.stevescarts.blocks.tileentities.TileEntityDistributor;

public class PacketDistributorTile implements CustomPacketPayload {
    public static final ResourceLocation ID = new ResourceLocation(Constants.MOD_ID, "distributor");
    private final BlockPos blockPos;
    private final int id;
    private final byte[] array;

    public PacketDistributorTile(BlockPos blockPos, int id, byte[] array) {
        this.blockPos = blockPos;
        this.id = id;
        this.array = array;
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeBlockPos(blockPos);
        buf.writeInt(id);
        buf.writeByteArray(array);
    }

    @Override
    public ResourceLocation id() {
        return ID;
    }

    public static PacketDistributorTile read(FriendlyByteBuf buffer) {
        return new PacketDistributorTile(buffer.readBlockPos(), buffer.readInt(), buffer.readByteArray());
    }

    public static void handle(final PacketDistributorTile msg, PlayPayloadContext ctx) {
        if (ctx.flow() != PacketFlow.SERVERBOUND) return;

        ctx.workHandler().execute(() -> {
            if (msg.blockPos != null && ctx.player().orElse(null) instanceof ServerPlayer player) {
                BlockPos blockPos = msg.blockPos;
                if (player.level().getBlockEntity(blockPos) != null && player.level().getBlockEntity(blockPos) instanceof TileEntityDistributor tileEntityDistributor) {
                    tileEntityDistributor.receivePacket(msg.id, msg.array, player);
                }
            }
        });
    }
}
