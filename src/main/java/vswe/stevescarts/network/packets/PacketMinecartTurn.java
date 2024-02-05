package vswe.stevescarts.network.packets;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;
import vswe.stevescarts.Constants;
import vswe.stevescarts.entities.EntityMinecartModular;

public class PacketMinecartTurn implements CustomPacketPayload {
    public static final ResourceLocation ID = new ResourceLocation(Constants.MOD_ID, "cart_turn");
    private final int cartID;

    public PacketMinecartTurn(int cartID) {
        this.cartID = cartID;
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeInt(cartID);
    }

    @Override
    public ResourceLocation id() {
        return ID;
    }

    public static PacketMinecartTurn read(FriendlyByteBuf buffer) {
        return new PacketMinecartTurn(buffer.readInt());
    }

    public static void handle(final PacketMinecartTurn msg, PlayPayloadContext ctx) {
        if (ctx.flow() != PacketFlow.SERVERBOUND) return;

        ctx.workHandler().execute(() -> {
            if (ctx.player().orElse(null) instanceof ServerPlayer player) {
                Level level = player.level();
                if (level.getEntity(msg.cartID) == null) return;
                if (level.getEntity(msg.cartID) instanceof EntityMinecartModular entityMinecartModular) {
                    entityMinecartModular.turnback();
                }
            }
        });
    }
}
