package vswe.stevescarts.network.packets;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;
import vswe.stevescarts.entitys.EntityMinecartModular;

import java.util.function.Supplier;

public class PacketMinecartTurn
{
    private final int cartID;

    public PacketMinecartTurn(int cartID)
    {
        this.cartID = cartID;
    }

    public static void encode(PacketMinecartTurn msg, FriendlyByteBuf buffer)
    {
        buffer.writeInt(msg.cartID);
    }

    public static PacketMinecartTurn decode(FriendlyByteBuf buffer)
    {
        return new PacketMinecartTurn(buffer.readInt());
    }

    public static class Handler
    {
        public static void handle(final PacketMinecartTurn msg, Supplier<NetworkEvent.Context> ctx)
        {
            ctx.get().enqueueWork(() ->
            {
                Level world = ctx.get().getSender().getLevel();
                if (world.getEntity(msg.cartID) == null) return;
                if (world.getEntity(msg.cartID) instanceof EntityMinecartModular)
                {
                    EntityMinecartModular entityMinecartModular = (EntityMinecartModular) world.getEntity(msg.cartID);
                    entityMinecartModular.turnback();
                }
            });
            ctx.get().setPacketHandled(true);
        }
    }
}
