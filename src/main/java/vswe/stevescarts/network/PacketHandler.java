package vswe.stevescarts.network;

import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
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
    }

    private static <MSG> void registerMessage(Class<MSG> type, BiConsumer<MSG, PacketBuffer> encoder, Function<PacketBuffer, MSG> decoder, BiConsumer<MSG, Supplier<NetworkEvent.Context>> consumer)
    {
        HANDLER.registerMessage(index++, type, encoder, decoder, consumer);
    }

    public static void sendToServer(Object msg)
    {
        HANDLER.sendToServer(msg);
    }

    public static void send(net.minecraftforge.fml.network.PacketDistributor.PacketTarget target, Object message)
    {
        HANDLER.send(target, message);
    }
}
