package vswe.stevescarts.network.packets;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.NetworkEvent;
import net.neoforged.neoforge.network.PlayNetworkDirection;
import vswe.stevescarts.api.modules.ModuleBase;
import vswe.stevescarts.entities.EntityMinecartModular;

public class PacketMinecartButton {
    private final int cartID;
    private final int id;
    private final byte[] array;

    public PacketMinecartButton(int cartID, int id, byte[] array) {
        this.cartID = cartID;
        this.id = id;
        this.array = array;
    }

    public static void encode(PacketMinecartButton msg, FriendlyByteBuf buffer) {
        buffer.writeInt(msg.cartID);
        buffer.writeInt(msg.id);
        buffer.writeByteArray(msg.array);
    }

    public static PacketMinecartButton decode(FriendlyByteBuf buffer) {
        return new PacketMinecartButton(buffer.readInt(), buffer.readInt(), buffer.readByteArray());
    }

    public static class Handler {
        public static void handle(PacketMinecartButton msg, NetworkEvent.Context ctx) {
            ctx.enqueueWork(() ->
            {
                if (ctx.getDirection() == PlayNetworkDirection.PLAY_TO_SERVER) {
                    handleServerSide(msg, ctx);
                } else if (ctx.getDirection() == PlayNetworkDirection.PLAY_TO_CLIENT) {
                    handleClientSide(msg, ctx);
                }
            });
            ctx.setPacketHandled(true);
        }

        @OnlyIn(Dist.CLIENT)
        private static void handleClientSide(PacketMinecartButton msg, NetworkEvent.Context ctx) {
            Minecraft mc = Minecraft.getInstance();
            Level level = mc.level;
            Player player = mc.player;
            handle(msg, level, player);
        }

        private static void handleServerSide(PacketMinecartButton msg, NetworkEvent.Context ctx) {
            Level level = ctx.getSender().level();
            Player player = ctx.getSender();
            handle(msg, level, player);
        }

        private static void handle(PacketMinecartButton msg, Level level, Player player) {
            if (level.getEntity(msg.cartID) == null) return;
            if (level.getEntity(msg.cartID) instanceof EntityMinecartModular entityMinecartModular) {
                int id = msg.id;
                for (final ModuleBase module : entityMinecartModular.getModules()) {
                    if (id >= module.getPacketStart() && id < module.getPacketStart() + module.totalNumberOfPackets()) {
                        module.delegateReceivedPacket(id - module.getPacketStart(), msg.array, player);
                        break;
                    }
                }
            }
        }
    }
}
