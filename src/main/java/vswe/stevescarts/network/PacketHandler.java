package vswe.stevescarts.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import vswe.stevescarts.Constants;
import vswe.stevescarts.network.packets.*;

import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class PacketHandler
{
    private static final String PROTOCOL_VERSION = Integer.toString(1);
    private static final SimpleChannel HANDLER = NetworkRegistry.ChannelBuilder.named(new ResourceLocation(Constants.MOD_ID, "main_channel")).clientAcceptedVersions(PROTOCOL_VERSION::equals).serverAcceptedVersions(PROTOCOL_VERSION::equals).networkProtocolVersion(() -> PROTOCOL_VERSION).simpleChannel();

    private static int index;

    public static void register()
    {
        registerMessage(PacketCreateCart.class, PacketCreateCart::encode, PacketCreateCart::decode, PacketCreateCart.Handler::handle);
        registerMessage(PacketMinecartButton.class, PacketMinecartButton::encode, PacketMinecartButton::decode, PacketMinecartButton.Handler::handle);
        registerMessage(PacketCargpManager.class, PacketCargpManager::encode, PacketCargpManager::decode, PacketCargpManager.Handler::handle);
        registerMessage(PacketDistributor.class, PacketDistributor::encode, PacketDistributor::decode, PacketDistributor.Handler::handle);
        registerMessage(PacketActivator.class, PacketActivator::encode, PacketActivator::decode, PacketActivator.Handler::handle);
        registerMessage(PacketFluidSync.class, PacketFluidSync::encode, PacketFluidSync::decode, PacketFluidSync.Handler::handle);
        registerMessage(PacketMinecartTurn.class, PacketMinecartTurn::encode, PacketMinecartTurn::decode, PacketMinecartTurn.Handler::handle);
        registerMessage(PacketGuiData.class, PacketGuiData::encode, PacketGuiData::decode, PacketGuiData.Handler::handle);
    }

    private static <MSG> void registerMessage(Class<MSG> type, BiConsumer<MSG, FriendlyByteBuf> encoder, Function<FriendlyByteBuf, MSG> decoder, BiConsumer<MSG, Supplier<NetworkEvent.Context>> consumer)
    {
        HANDLER.registerMessage(index++, type, encoder, decoder, consumer);
    }

    public static void sendToServer(Object msg)
    {
        HANDLER.sendToServer(msg);
    }

    public static void send(net.minecraftforge.network.PacketDistributor.PacketTarget target, Object message)
    {
        HANDLER.send(target, message);
    }

    public static void sendTo(Object message, ServerPlayer player)
    {
        //TODO
//        HANDLER.sendTo(message, player.connection.getConnection(), NetworkDirection.PLAY_TO_CLIENT);
    }
}
