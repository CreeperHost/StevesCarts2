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
import net.neoforged.neoforge.network.handling.PlayPayloadContext;
import vswe.stevescarts.Constants;
import vswe.stevescarts.api.modules.ModuleBase;
import vswe.stevescarts.entities.EntityMinecartModular;

public class PacketMinecartButton implements CustomPacketPayload {
    public static final ResourceLocation ID = new ResourceLocation(Constants.MOD_ID, "cart_button");
    private final int cartID;
    private final int id;
    private final byte[] array;

    public PacketMinecartButton(int cartID, int id, byte[] array) {
        this.cartID = cartID;
        this.id = id;
        this.array = array;
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeInt(cartID);
        buf.writeInt(id);
        buf.writeByteArray(array);
    }

    @Override
    public ResourceLocation id() {
        return ID;
    }

    public static PacketMinecartButton read(FriendlyByteBuf buffer) {
        return new PacketMinecartButton(buffer.readInt(), buffer.readInt(), buffer.readByteArray());
    }

    public static void handle(PacketMinecartButton msg, PlayPayloadContext ctx) {

        ctx.workHandler().execute(() -> {
            if (ctx.flow() == PacketFlow.CLIENTBOUND) {
                handleClientSide(msg, ctx);
            } else {
                handleServerSide(msg, ctx);
            }
        });
    }

    @OnlyIn (Dist.CLIENT)
    private static void handleClientSide(PacketMinecartButton msg, PlayPayloadContext ctx) {
        Minecraft mc = Minecraft.getInstance();
        Level level = mc.level;
        Player player = mc.player;
        handle(msg, level, player);
    }

    private static void handleServerSide(PacketMinecartButton msg, PlayPayloadContext ctx) {
        Player player = ctx.player().orElse(null);
        if (player == null) return;
        handle(msg, player.level(), player);
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
