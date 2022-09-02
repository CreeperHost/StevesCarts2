package vswe.stevescarts.network.packets;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;
import vswe.stevescarts.blocks.tileentities.TileEntityManager;

import java.util.function.Supplier;

public class PacketCargpManager
{
    private final BlockPos blockPos;
    private final int id;
    private final byte[] array;

    public PacketCargpManager(BlockPos blockPos, int id, byte[] array)
    {
        this.blockPos = blockPos;
        this.id = id;
        this.array = array;
    }

    public static void encode(PacketCargpManager msg, FriendlyByteBuf buffer)
    {
        buffer.writeBlockPos(msg.blockPos);
        buffer.writeInt(msg.id);
        buffer.writeByteArray(msg.array);
    }

    public static PacketCargpManager decode(FriendlyByteBuf buffer)
    {
        return new PacketCargpManager(buffer.readBlockPos(), buffer.readInt(), buffer.readByteArray());
    }

    public static class Handler
    {
        public static void handle(final PacketCargpManager msg, Supplier<NetworkEvent.Context> ctx)
        {
            ctx.get().enqueueWork(() ->
            {
                Level world = ctx.get().getSender().getLevel();
                if (msg.blockPos != null)
                {
                    BlockPos blockPos = msg.blockPos;
                    if (world.getBlockEntity(blockPos) != null && world.getBlockEntity(blockPos) instanceof TileEntityManager manager)
                    {
                        manager.receivePacket(msg.id, msg.array, ctx.get().getSender());
                    }
                }
            });
            ctx.get().setPacketHandled(true);
        }
    }
}
