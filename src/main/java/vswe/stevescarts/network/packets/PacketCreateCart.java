package vswe.stevescarts.network.packets;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.network.NetworkEvent;
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

    public static void encode(PacketCreateCart msg, FriendlyByteBuf buffer)
    {
        buffer.writeBlockPos(msg.blockPos);
        buffer.writeInt(msg.id);
        buffer.writeByteArray(msg.data);
    }

    public static PacketCreateCart decode(FriendlyByteBuf buffer)
    {
        return new PacketCreateCart(buffer.readBlockPos(), buffer.readInt(), buffer.readByteArray());
    }

    public static class Handler
    {
        public static void handle(final PacketCreateCart msg, Supplier<NetworkEvent.Context> ctx)
        {
            ctx.get().enqueueWork(() ->
            {
                ServerLevel world = ctx.get().getSender().getLevel();
                if (msg.blockPos != null && world.getBlockEntity(msg.blockPos) != null)
                {
                    BlockPos blockPos = msg.blockPos;
                    if (world.isLoaded(blockPos) && world.getBlockEntity(blockPos) instanceof TileEntityCartAssembler tileEntityCartAssembler)
                    {
                        tileEntityCartAssembler.receivePacket(msg.id, msg.data, ctx.get().getSender());
                    }
                }
            });
            ctx.get().setPacketHandled(true);
        }
    }
}
