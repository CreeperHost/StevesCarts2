package vswe.stevescarts.network.packets;

import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;
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

    public static void encode(PacketDistributor msg, PacketBuffer buffer)
    {
        buffer.writeBlockPos(msg.blockPos);
        buffer.writeInt(msg.id);
        buffer.writeByteArray(msg.array);
    }

    public static PacketDistributor decode(PacketBuffer buffer)
    {
        return new PacketDistributor(buffer.readBlockPos(), buffer.readInt(), buffer.readByteArray());
    }

    public static class Handler
    {
        public static void handle(final PacketDistributor msg, Supplier<NetworkEvent.Context> ctx)
        {
            ctx.get().enqueueWork(() ->
            {
                World world = ctx.get().getSender().getLevel();
                if (msg.blockPos != null)
                {
                    BlockPos blockPos = msg.blockPos;
                    if (world.getBlockEntity(blockPos) != null && world.getBlockEntity(blockPos) instanceof TileEntityDistributor)
                    {
                        TileEntityDistributor tileEntityDistributor = (TileEntityDistributor) world.getBlockEntity(blockPos);
                        tileEntityDistributor.receivePacket(msg.id, msg.array, ctx.get().getSender());
                    }
                }
            });
            ctx.get().setPacketHandled(true);
        }
    }
}
