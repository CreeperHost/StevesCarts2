package vswe.stevescarts.network.packets;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.handling.IPayloadHandler;
import vswe.stevescarts.Constants;
import vswe.stevescarts.containers.ContainerMinecart;
import vswe.stevescarts.entities.ModularMinecart;

public class PacketOpenConnectedCart implements CustomPacketPayload {
    public static final Type<PacketOpenConnectedCart> TYPE = new Type<>(Identifier.fromNamespaceAndPath(Constants.MOD_ID, "open_connected_cart"));
    private final int sourceCartId;
    private final int targetCartId;

    public PacketOpenConnectedCart(int sourceCartId, int targetCartId) {
        this.sourceCartId = sourceCartId;
        this.targetCartId = targetCartId;
    }

    public static PacketOpenConnectedCart read(FriendlyByteBuf buffer) {
        return new PacketOpenConnectedCart(buffer.readInt(), buffer.readInt());
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public void write(FriendlyByteBuf buffer) {
        buffer.writeInt(sourceCartId);
        buffer.writeInt(targetCartId);
    }

    public static class Handler implements IPayloadHandler<PacketOpenConnectedCart> {
        @Override
        public void handle(PacketOpenConnectedCart message, IPayloadContext context) {
            if (context.flow() != PacketFlow.SERVERBOUND) return;

            context.enqueueWork(() -> {
                if (!(context.player() instanceof ServerPlayer player)
                        || !(player.containerMenu instanceof ContainerMinecart menu)
                        || menu.cart.getId() != message.sourceCartId) {
                    return;
                }

                Entity sourceEntity = player.level().getEntity(message.sourceCartId);
                Entity targetEntity = player.level().getEntity(message.targetCartId);
                if (!(sourceEntity instanceof ModularMinecart sourceCart)
                        || !(targetEntity instanceof ModularMinecart targetCart)
                        || !sourceCart.getConnectedCarts().contains(targetCart)) {
                    return;
                }

                player.openMenu(targetCart, buffer -> buffer.writeInt(targetCart.getId()));
            });
        }
    }
}
