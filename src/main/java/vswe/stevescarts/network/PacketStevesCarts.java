//package vswe.stevescarts.packet;
//
//import com.google.common.io.ByteArrayDataInput;
//import com.google.common.io.ByteStreams;
//import net.minecraft.entity.Entity;
//import net.minecraft.entity.player.EntityPlayer;
//import net.minecraft.entity.player.EntityPlayerMP;
//import net.minecraft.inventory.Container;
//import net.minecraft.inventory.ContainerPlayer;
//import net.minecraft.server.management.PlayerChunkMap;
//import net.minecraft.util.math.BlockPos;
//import net.minecraft.world.World;
//import net.minecraft.world.WorldServer;
//import net.minecraftforge.fml.client.FMLClientHandler;
//import net.minecraftforge.fml.common.FMLCommonHandler;
//import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
//import net.minecraftforge.fml.relauncher.Side;
//import net.minecraftforge.fml.relauncher.SideOnly;
//import reborncore.common.network.ExtendedPacketBuffer;
//import reborncore.common.network.INetworkPacket;
//import reborncore.common.network.NetworkManager;
//import vswe.stevescarts.blocks.BlockCartAssembler;
//import vswe.stevescarts.init.ModBlocks;
//import vswe.stevescarts.blocks.tileentities.TileEntityBase;
//import vswe.stevescarts.containers.ContainerBase;
//import vswe.stevescarts.containers.ContainerMinecart;
//import vswe.stevescarts.entitys.EntityMinecartModular;
//import vswe.stevescarts.modules.ModuleBase;
//
//import java.io.ByteArrayOutputStream;
//import java.io.DataOutputStream;
//import java.io.IOException;
//
///**
// * A hack around the old packet system
// */
//public class PacketStevesCarts implements INetworkPacket<PacketStevesCarts> {
//
//	private byte[] bytes;
//
//	public PacketStevesCarts(byte[] bytes) {
//		this.bytes = bytes;
//	}
//
//	public PacketStevesCarts() {}
//
//	@Override
//	public void writeData(ExtendedPacketBuffer buffer) {
//		buffer.writeByteArray(bytes);
//	}
//
//	@Override
//	public void readData(ExtendedPacketBuffer buffer) {
//		bytes = buffer.readByteArray();
//	}
//
//	@Override
//	public void processData(PacketStevesCarts message, MessageContext context) {
//		if (context.side == Side.CLIENT) {
//			FMLClientHandler.instance().getClient().addScheduledTask(() -> processDataClient(message, context));
//		} else { //Server
//			FMLCommonHandler.instance().getMinecraftServerInstance().callFromMainThread(() -> processDataServer(message, context));
//		}
//	}
//
//	private boolean processDataServer(PacketStevesCarts message, MessageContext context) {
//		EntityPlayer player = context.getServerHandler().player;
//		World world = player.world;
//		final ByteArrayDataInput reader = ByteStreams.newDataInput(message.bytes);
//		int id = reader.readByte();
//
//		if (player.openContainer instanceof ContainerPlayer) {
//			final int entityid = reader.readInt();
//			final int len = bytes.length - 5;
//			final byte[] data = new byte[len];
//			for (int i = 0; i < len; ++i) {
//				data[i] = reader.readByte();
//			}
//			final EntityMinecartModular cart = getCart(entityid, world);
//			if (cart != null) {
//				receivePacketAtCart(cart, id, data, player);
//			}
//		} else {
//			final int len = bytes.length - 1;
//			final byte[] data2 = new byte[len];
//			for (int j = 0; j < len; ++j) {
//				data2[j] = reader.readByte();
//			}
//
//			final Container con = player.openContainer;
//
//			if (con instanceof ContainerMinecart) {
//				final ContainerMinecart conMC = (ContainerMinecart) con;
//				final EntityMinecartModular cart2 = conMC.cart;
//
//				receivePacketAtCart(cart2, id, data2, player);
//			} else if (con instanceof ContainerBase) {
//				final ContainerBase conBase = (ContainerBase) con;
//				final TileEntityBase base = conBase.getTileEntity();
//				if (base != null) {
//					base.receivePacket(id, data2, player);
//				}
//			}
//		}
//		return true;
//	}
//
//	@SideOnly(Side.CLIENT)
//	private void processDataClient(PacketStevesCarts message, MessageContext context) {
//		EntityPlayer player = FMLClientHandler.instance().getClient().player;
//		final ByteArrayDataInput reader = ByteStreams.newDataInput(message.bytes);
//		final World world = player.world;
//		int id = reader.readByte();
//		if (id == -1) {
//			final int x = reader.readInt();
//			final int y = reader.readInt();
//			final int z = reader.readInt();
//
//			final int len = bytes.length - 13;
//			final byte[] data = new byte[len];
//			for (int i = 0; i < len; ++i) {
//				data[i] = reader.readByte();
//			}
//
//			((BlockCartAssembler) ModBlocks.CART_ASSEMBLER.getBlock()).updateMultiBlock(world, new BlockPos(x, y, z));
//		} else {
//			final int entityid = reader.readInt();
//			final int len = bytes.length - 5;
//			final byte[] data = new byte[len];
//			for (int j = 0; j < len; ++j) {
//				data[j] = reader.readByte();
//			}
//			final EntityMinecartModular cart = getCart(entityid, world);
//			if (cart != null) {
//				receivePacketAtCart(cart, id, data, player);
//			}
//		}
//	}
//
//	private void receivePacketAtCart(final EntityMinecartModular cart, final int id, final byte[] data, final EntityPlayer player) {
//		for (final ModuleBase module : cart.getModules()) {
//			if (id >= module.getPacketStart() && id < module.getPacketStart() + module.totalNumberOfPackets()) {
//				module.delegateReceivedPacket(id - module.getPacketStart(), data, player);
//				break;
//			}
//		}
//	}
//
//	private EntityMinecartModular getCart(final int ID, final World world) {
//		for (final Object e : world.loadedEntityList) {
//			if (e instanceof Entity && ((Entity) e).getEntityId() == ID && e instanceof EntityMinecartModular) {
//				return (EntityMinecartModular) e;
//			}
//		}
//		return null;
//	}
//
//	public static void sendPacket(final int id, final byte[] extraData) {
//		final ByteArrayOutputStream bs = new ByteArrayOutputStream();
//		final DataOutputStream ds = new DataOutputStream(bs);
//		try {
//			ds.writeByte((byte) id);
//			for (final byte b : extraData) {
//				ds.writeByte(b);
//			}
//		} catch (IOException ex) {}
//		NetworkManager.sendToServer(new PacketStevesCarts(bs.toByteArray()));
//	}
//
//	private static byte[] makeCartPacket(EntityMinecartModular cart, int id, byte[] data) {
//		final ByteArrayOutputStream bs = new ByteArrayOutputStream();
//		final DataOutputStream ds = new DataOutputStream(bs);
//		try {
//			ds.writeByte((byte) id);
//			ds.writeInt(cart.getEntityId());
//			for (final byte b : data) {
//				ds.writeByte(b);
//			}
//		} catch (IOException ignored) {}
//		return bs.toByteArray();
//	}
//
//	public static void sendPacket(final EntityMinecartModular cart, final int id, final byte[] extraData) {
//		NetworkManager.sendToServer(new PacketStevesCarts(makeCartPacket(cart, id, extraData)));
//	}
//
//	public static void sendPacketToPlayer(final int id, final byte[] data, final EntityPlayer player, final EntityMinecartModular cart) {
//		NetworkManager.sendToPlayer(new PacketStevesCarts(makeCartPacket(cart, id, data)), (EntityPlayerMP) player);
//	}
//
//	public static void sendPacketToAllAround(final int id, final byte[] data, final EntityMinecartModular cart) {
//		sendToAllAround(new PacketStevesCarts(makeCartPacket(cart, id, data)), cart.getExactPosition(), cart.getEntityWorld());
//	}
//
//	public static void sendBlockInfoToClients(final World world, final byte[] data, final BlockPos pos) {
//		final ByteArrayOutputStream bs = new ByteArrayOutputStream();
//		final DataOutputStream ds = new DataOutputStream(bs);
//		try {
//			ds.writeByte(-1);
//			ds.writeInt(pos.getX());
//			ds.writeInt(pos.getY());
//			ds.writeInt(pos.getZ());
//			for (final byte b : data) {
//				ds.writeByte(b);
//			}
//		} catch (IOException ex) {}
//		sendToAllAround(new PacketStevesCarts(bs.toByteArray()), pos, world);
//	}
//
//	// see https://github.com/MinecraftForge/MinecraftForge/issues/3677
//	private static void sendToAllAround(INetworkPacket packet, BlockPos pos, World world) {
//		if (!(world instanceof WorldServer)) {
//			return;
//		}
//
//		WorldServer worldServer = (WorldServer) world;
//		PlayerChunkMap playerManager = worldServer.getPlayerChunkMap();
//
//		int chunkX = pos.getX() >> 4;
//		int chunkZ = pos.getZ() >> 4;
//
//		for (Object playerObj : world.playerEntities) {
//			if (playerObj instanceof EntityPlayerMP) {
//				EntityPlayerMP player = (EntityPlayerMP) playerObj;
//
//				if (playerManager.isPlayerWatchingChunk(player, chunkX, chunkZ)) {
//					NetworkManager.sendToPlayer(packet, player);
//				}
//			}
//		}
//	}
//}
