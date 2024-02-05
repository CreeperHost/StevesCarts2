package vswe.stevescarts.network;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlerEvent;
import net.neoforged.neoforge.network.registration.IPayloadRegistrar;
import vswe.stevescarts.Constants;
import vswe.stevescarts.network.packets.*;

public class PacketHandler {
    private static final String PROTOCOL_VERSION = Integer.toString(1);

    public static void init(IEventBus bus) {
        bus.addListener(PacketHandler::registerEvent);
    }

    public static void registerEvent(RegisterPayloadHandlerEvent event) {
        IPayloadRegistrar registrar = event.registrar(Constants.MOD_ID)
                .versioned(PROTOCOL_VERSION);

        //@formatter:off
        registrar.play(PacketCreateCart.ID,         PacketCreateCart::read,         PacketCreateCart::handle);
        registrar.play(PacketMinecartButton.ID,     PacketMinecartButton::read,     PacketMinecartButton::handle);
        registrar.play(PacketCargpManager.ID,       PacketCargpManager::read,       PacketCargpManager::handle);
        registrar.play(PacketDistributorTile.ID,    PacketDistributorTile::read,    PacketDistributorTile::handle);
        registrar.play(PacketActivator.ID,          PacketActivator::read,          PacketActivator::handle);
        registrar.play(PacketFluidSync.ID,          PacketFluidSync::read,          PacketFluidSync::handle);
        registrar.play(PacketMinecartTurn.ID,       PacketMinecartTurn::read,       PacketMinecartTurn::handle);
        registrar.play(PacketGuiData.ID,            PacketGuiData::read,            PacketGuiData::handle);
        //@formatter:on
    }

    @Deprecated
    public static void sendToServer(CustomPacketPayload msg) {
        PacketDistributor.SERVER.noArg().send(msg);
    }

    @Deprecated
    public static void sendTo(CustomPacketPayload message, ServerPlayer player) {
        PacketDistributor.PLAYER.with(player).send(message);
    }
}
