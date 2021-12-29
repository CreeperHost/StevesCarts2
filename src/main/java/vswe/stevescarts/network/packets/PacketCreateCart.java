package vswe.stevescarts.network.packets;

import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;
import vswe.stevescarts.blocks.tileentities.TileEntityCartAssembler;

import java.util.function.Supplier;

public class PacketCreateCart
{
    private final BlockPos blockPos;
    private final int id;
    private final byte[] data;

    public PacketCreateCart(BlockPos blockPos, int id, byte[] data)
    {
        this.blockPos = blockPos;
        this.id = id;
        this.data = data;
    }

    public static void encode(PacketCreateCart msg, PacketBuffer buffer)
    {
        buffer.writeBlockPos(msg.blockPos);
        buffer.writeInt(msg.id);
        buffer.writeByteArray(msg.data);
    }

    public static PacketCreateCart decode(PacketBuffer buffer)
    {
        return new PacketCreateCart(buffer.readBlockPos(), buffer.readInt(), buffer.readByteArray());
    }

    public static class Handler
    {
        public static void handle(final PacketCreateCart msg, Supplier<NetworkEvent.Context> ctx)
        {
            ctx.get().enqueueWork(() ->
            {
                World world = ctx.get().getSender().getLevel();
                if (msg.blockPos != null && world.getBlockEntity(msg.blockPos) != null)
                {
                    BlockPos blockPos = msg.blockPos;
                    if (world.isLoaded(blockPos) && world.getBlockEntity(blockPos) instanceof TileEntityCartAssembler)
                    {
                        TileEntityCartAssembler tileEntityCartAssembler = (TileEntityCartAssembler) world.getBlockEntity(blockPos);
                        tileEntityCartAssembler.receivePacket(msg.id, msg.data, ctx.get().getSender());
                    }
                }
            });
            ctx.get().setPacketHandled(true);
        }
    }
}
