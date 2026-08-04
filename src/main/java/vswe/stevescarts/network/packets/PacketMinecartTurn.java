package vswe.stevescarts.network.packets;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.handling.IPayloadHandler;
import vswe.stevescarts.Constants;
import vswe.stevescarts.entities.ModularMinecart;

public class PacketMinecartTurn implements CustomPacketPayload {
    public static final Type<PacketMinecartTurn> TYPE = new Type<>(Identifier.fromNamespaceAndPath(Constants.MOD_ID, "cart_turn"));
    private final int cartID;

    public PacketMinecartTurn(int cartID) {
        this.cartID = cartID;
    }

    public static PacketMinecartTurn read(FriendlyByteBuf buffer) {
        return new PacketMinecartTurn(buffer.readInt());
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeInt(cartID);
    }

    public static class Handler implements IPayloadHandler<PacketMinecartTurn> {
        @Override
        public void handle(PacketMinecartTurn msg, IPayloadContext ctx) {
            if (ctx.flow() != PacketFlow.SERVERBOUND) return;

            ctx.enqueueWork(() -> {
                if (ctx.player() instanceof ServerPlayer player) {
                    Level level = player.level();
                    if (level.getEntity(msg.cartID) == null) return;
                    if (level.getEntity(msg.cartID) instanceof ModularMinecart ModularMinecart) {
                        ModularMinecart.turnback();
                    }
                }
            });
        }
    }
}
