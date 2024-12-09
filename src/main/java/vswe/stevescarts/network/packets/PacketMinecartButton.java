package vswe.stevescarts.network.packets;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.handling.IPayloadHandler;
import vswe.stevescarts.Constants;
import vswe.stevescarts.api.modules.ModuleBase;
import vswe.stevescarts.entities.ModularMinecart;

public class PacketMinecartButton implements CustomPacketPayload {
    public static final Type<PacketMinecartButton> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "cart_button"));
    private final int cartID;
    private final int id;
    private final byte[] array;

    public PacketMinecartButton(int cartID, int id, byte[] array) {
        this.cartID = cartID;
        this.id = id;
        this.array = array;
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeInt(cartID);
        buf.writeInt(id);
        buf.writeByteArray(array);
    }

    public static PacketMinecartButton read(FriendlyByteBuf buffer) {
        return new PacketMinecartButton(buffer.readInt(), buffer.readInt(), buffer.readByteArray());
    }

    public static class Handler implements IPayloadHandler<PacketMinecartButton> {
        @Override
        public void handle(PacketMinecartButton msg, IPayloadContext ctx) {
            ctx.enqueueWork(() -> {
                if (ctx.flow() == PacketFlow.CLIENTBOUND) {
                    handleClientSide(msg, ctx);
                } else {
                    handleServerSide(msg, ctx);
                }
            });
        }
    }

    @OnlyIn (Dist.CLIENT)
    private static void handleClientSide(PacketMinecartButton msg, IPayloadContext ctx) {
        Minecraft mc = Minecraft.getInstance();
        Level level = mc.level;
        Player player = mc.player;
        handle(msg, level, player);
    }

    private static void handleServerSide(PacketMinecartButton msg, IPayloadContext ctx) {
        Player player = ctx.player();
        handle(msg, player.level(), player);
    }

    private static void handle(PacketMinecartButton msg, Level level, Player player) {
        if (level.getEntity(msg.cartID) == null) return;
        if (level.getEntity(msg.cartID) instanceof ModularMinecart ModularMinecart) {
            int id = msg.id;
            for (final ModuleBase module : ModularMinecart.modules()) {
                if (id >= module.getPacketStart() && id < module.getPacketStart() + module.totalNumberOfPackets()) {
                    module.delegateReceivedPacket(id - module.getPacketStart(), msg.array, player);
                    break;
                }
            }
        }
    }
}
