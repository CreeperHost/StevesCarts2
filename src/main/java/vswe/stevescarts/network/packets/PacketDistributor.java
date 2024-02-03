package vswe.stevescarts.network.packets;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.NetworkEvent;
import vswe.stevescarts.blocks.tileentities.TileEntityDistributor;

import java.util.function.Supplier;

public class PacketDistributor
{
    private final BlockPos blockPos;
    private final int id;
    private final byte[] array;

    public PacketDistributor(BlockPos blockPos, int id, byte[] array)
    {
        this.blockPos = blockPos;
        this.id = id;
        this.array = array;
    }

    public static void encode(PacketDistributor msg, FriendlyByteBuf buffer)
    {
        buffer.writeBlockPos(msg.blockPos);
        buffer.writeInt(msg.id);
        buffer.writeByteArray(msg.array);
    }

    public static PacketDistributor decode(FriendlyByteBuf buffer)
    {
        return new PacketDistributor(buffer.readBlockPos(), buffer.readInt(), buffer.readByteArray());
    }

    public static class Handler
    {
        public static void handle(final PacketDistributor msg, NetworkEvent.Context ctx)
        {
            ctx.enqueueWork(() ->
            {
                Level world = ctx.getSender().level();
                if (msg.blockPos != null)
                {
                    BlockPos blockPos = msg.blockPos;
                    if (world.getBlockEntity(blockPos) != null && world.getBlockEntity(blockPos) instanceof TileEntityDistributor tileEntityDistributor)
                    {
                        tileEntityDistributor.receivePacket(msg.id, msg.array, ctx.getSender());
                    }
                }
            });
            ctx.setPacketHandled(true);
        }
    }
}
