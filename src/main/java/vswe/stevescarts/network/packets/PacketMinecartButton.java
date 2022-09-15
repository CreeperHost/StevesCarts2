package vswe.stevescarts.network.packets;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;
import vswe.stevescarts.entities.EntityMinecartModular;
import vswe.stevescarts.api.modules.ModuleBase;

import java.util.function.Supplier;

public class PacketMinecartButton
{
    private final int cartID;
    private final int id;
    private final byte[] array;

    public PacketMinecartButton(int cartID, int id, byte[] array)
    {
        this.cartID = cartID;
        this.id = id;
        this.array = array;
    }

    public static void encode(PacketMinecartButton msg, FriendlyByteBuf buffer)
    {
        buffer.writeInt(msg.cartID);
        buffer.writeInt(msg.id);
        buffer.writeByteArray(msg.array);
    }

    public static PacketMinecartButton decode(FriendlyByteBuf buffer)
    {
        return new PacketMinecartButton(buffer.readInt(), buffer.readInt(), buffer.readByteArray());
    }

    public static class Handler
    {
        public static void handle(final PacketMinecartButton msg, Supplier<NetworkEvent.Context> ctx)
        {
            ctx.get().enqueueWork(() ->
            {
                Level world = ctx.get().getSender().getLevel();
                if (world.getEntity(msg.cartID) == null) return;
                if (world.getEntity(msg.cartID) instanceof EntityMinecartModular entityMinecartModular)
                {
                    Player player = ctx.get().getSender();
                    int id = msg.id;

                    for (final ModuleBase module : entityMinecartModular.getModules())
                    {
                        if (id >= module.getPacketStart() && id < module.getPacketStart() + module.totalNumberOfPackets())
                        {
                            module.delegateReceivedPacket(id - module.getPacketStart(), msg.array, player);
                            break;
                        }
                    }
                }
            });
            ctx.get().setPacketHandled(true);
        }
    }
}
