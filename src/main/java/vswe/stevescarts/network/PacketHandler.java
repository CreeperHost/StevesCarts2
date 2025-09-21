package vswe.stevescarts.network;

import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import vswe.stevescarts.Constants;
import vswe.stevescarts.network.packets.*;

public class PacketHandler {
    private static final String PROTOCOL_VERSION = Integer.toString(1);

    public static void init(IEventBus bus) {
        bus.addListener(PacketHandler::registerEvent);
    }

    public static void registerEvent(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar(Constants.MOD_ID)
                .versioned(PROTOCOL_VERSION);

        //@formatter:off
        registrar.playToServer(PacketCreateCart.TYPE,               StreamCodec.of((buff, packet) -> packet.write(buff), PacketCreateCart::read),       new PacketCreateCart.Handler());
        registrar.playBidirectional(PacketMinecartButton.TYPE,      StreamCodec.of((buff, packet) -> packet.write(buff), PacketMinecartButton::read),   new PacketMinecartButton.ServerHandler(), new PacketMinecartButton.ClientHandler());
        registrar.playToServer(PacketCargpManager.TYPE,             StreamCodec.of((buff, packet) -> packet.write(buff), PacketCargpManager::read),     new PacketCargpManager.Handler());
        registrar.playToServer(PacketDistributorTile.TYPE,          StreamCodec.of((buff, packet) -> packet.write(buff), PacketDistributorTile::read),  new PacketDistributorTile.Handler());
        registrar.playToServer(PacketActivator.TYPE,                StreamCodec.of((buff, packet) -> packet.write(buff), PacketActivator::read),        new PacketActivator.Handler());
        registrar.playToClient(PacketFluidSync.TYPE,                StreamCodec.of((buff, packet) -> packet.write(buff), PacketFluidSync::read),        new PacketFluidSync.Handler());
        registrar.playToServer(PacketMinecartTurn.TYPE,             StreamCodec.of((buff, packet) -> packet.write(buff), PacketMinecartTurn::read),     new PacketMinecartTurn.Handler());
        registrar.playToClient(PacketGuiData.TYPE,                  StreamCodec.of((buff, packet) -> packet.write(buff), PacketGuiData::read),          new PacketGuiData.Handler());
        registrar.playToClient(PacketEntityData.TYPE,               StreamCodec.of((buff, packet) -> packet.write(buff), PacketEntityData::read),       new PacketEntityData.Handler());
        //@formatter:on
    }

    @Deprecated
    public static void sendTo(CustomPacketPayload message, ServerPlayer player) {
        PacketDistributor.sendToPlayer(player, message);
//        PacketDistributor.PLAYER.with(player).send(message);
    }
}
