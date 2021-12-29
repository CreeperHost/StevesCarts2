package vswe.stevescarts.arcade.tracks;

import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import vswe.stevescarts.helpers.Localization;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class TrackLevel {
	public static final TrackLevel editor;
	private static String MAP_FOLDER_PATH;
	private String name;
	private int playerX;
	private int playerY;
	private TrackOrientation.DIRECTION playerDir;
	private int itemX;
	private int itemY;
	private ArrayList<Track> tracks;
	private ArrayList<LevelMessage> messages;

	private static byte getFileVersion() {
		return 0;
	}

	@OnlyIn(Dist.CLIENT)
	public static ArrayList<TrackLevel> loadMapsFromFolder() {
		final ArrayList<TrackLevel> maps = new ArrayList<>();
		try {
			File dir = new File(Minecraft.getInstance().gameDirectory, TrackLevel.MAP_FOLDER_PATH);
			File[] children = dir.listFiles();
			if (children != null) {
				for (final File child : children) {
					if (child.isFile()) {
						final String name = child.getName();
						final TrackLevel map = loadMap(name);
						if (map != null) {
							maps.add(map);
						}
					}
				}
			}
		} catch (Exception exception) {
			System.out.println("Failed to load the maps");
		}
		return maps;
	}

	@OnlyIn(Dist.CLIENT)
	public static TrackLevel loadMap(final String filename) {
		try {
			final byte[] bytes = readFromFile(new File(Minecraft.getInstance().gameDirectory, TrackLevel.MAP_FOLDER_PATH + filename));
			return loadMapData(bytes);
		} catch (Exception exception) {
			exception.printStackTrace();
			return null;
		}
	}

	public static TrackLevel loadMap(final byte[] bytes) {
		try {
			return loadMapData(bytes);
		} catch (Exception exception) {
			exception.printStackTrace();
			return null;
		}
	}

	public static TrackLevel loadMapData(final byte[] bytes) throws IOException {
		final ByteArrayInputStream data = new ByteArrayInputStream(bytes);
		final int version = data.read();
		final int namelength = data.read();
		final byte[] namebytes = new byte[namelength];
		data.read(namebytes, 0, namelength);
		final String name = new String(namebytes, Charset.forName("UTF-8"));
		final int header = data.read() << 24 | data.read() << 16 | data.read() << 8 | data.read() << 0;
		final int playerX = header & 0x1F;
		final int playerY = header >> 5 & 0xF;
		final TrackOrientation.DIRECTION playerDir = TrackOrientation.DIRECTION.fromInteger(header >> 9 & 0x3);
		final int itemX = header >> 11 & 0x1F;
		final int itemY = header >> 16 & 0xF;
		final int tracksize = header >> 20 & 0x1FF;
		final TrackLevel map = new TrackLevel(null, playerX, playerY, playerDir, itemX, itemY);
		if (!name.isEmpty())
			map.setName(name);
		for (int i = 0; i < tracksize; ++i) {
			final int trackdata = data.read() << 16 | data.read() << 8 | data.read() << 0;
			final int trackX = trackdata & 0x1F;
			final int trackY = trackdata >> 5 & 0xF;
			final int type = trackdata >> 9 & 0x7;
			final TrackOrientation orientation = TrackOrientation.ALL.get(trackdata >> 12 & 0x3F);
			final int extraLength = trackdata >> 18 & 0x3F;
			final Track track = TrackEditor.getRealTrack(trackX, trackY, type, orientation);
			final byte[] extraData = new byte[extraLength];
			data.read(extraData);
			track.setExtraInfo(extraData);
			map.getTracks().add(track);
		}
		return map;
	}

	@OnlyIn(Dist.CLIENT)
	public static boolean saveMap(String name, int playerX, int playerY, TrackOrientation.DIRECTION playerDir, int itemX, int itemY, ArrayList<Track> tracks) {
		try {
			final byte[] bytes = saveMapData(name, playerX, playerY, playerDir, itemX, itemY, tracks);
			writeToFile(new File(Minecraft.getInstance().gameDirectory, "sc2/arcade/trackoperator/" + name.replace(" ", "_") + ".dat"), bytes);
			return true;
		} catch (IOException ex) {
			return false;
		}
	}

	@OnlyIn(Dist.CLIENT)
	public static String saveMapToString(String name, int playerX, int playerY, TrackOrientation.DIRECTION playerDir, int itemX, int itemY, ArrayList<Track> tracks) {
		try {
			final byte[] bytes = saveMapData(name, playerX, playerY, playerDir, itemX, itemY, tracks);
			String str = "TrackLevel.loadMap(new byte[] {";
			for (int i = 0; i < bytes.length; ++i) {
				if (i != 0) {
					str += ",";
				}
				str += bytes[i];
			}
			str += "});";
			return str;
		} catch (IOException ex) {
			return "";
		}
	}

	@OnlyIn(Dist.CLIENT)
	public static byte[] saveMapData(final String name,
	                                 final int playerX,
	                                 final int playerY,
	                                 final TrackOrientation.DIRECTION playerDir,
	                                 final int itemX,
	                                 final int itemY,
	                                 final ArrayList<Track> tracks) throws IOException {
		final ByteArrayOutputStream stream = new ByteArrayOutputStream();
		final DataOutputStream data = new DataOutputStream(stream);
		data.writeByte(getFileVersion());
		data.writeByte(name.length());
		data.writeBytes(name);
		int header = 0;
		header |= playerX;
		header |= playerY << 5;
		header |= playerDir.toInteger() << 9;
		header |= itemX << 11;
		header |= itemY << 16;
		header |= tracks.size() << 20;
		data.writeInt(header);
		for (final Track track : tracks) {
			int trackdata = 0;
			final byte[] extraData = track.getExtraInfo();
			trackdata |= track.getX();
			trackdata |= track.getY() << 5;
			trackdata |= track.getU() << 9;
			trackdata |= track.getOrientation().toInteger() << 12;
			trackdata |= extraData.length << 18;
			data.write((trackdata & 0xFF0000) >> 16);
			data.write((trackdata & 0xFF00) >> 8);
			data.write(trackdata & 0xFF);
			data.write(extraData);
		}
		return stream.toByteArray();
	}

	@OnlyIn(Dist.CLIENT)
	private static void writeToFile(final File file, final byte[] bytes) throws IOException {
		createFolder(file.getParentFile());
		final FileOutputStream writer = new FileOutputStream(file);
		writer.write(bytes);
		writer.close();
	}

	@OnlyIn(Dist.CLIENT)
	private static byte[] readFromFile(final File file) throws IOException {
		createFolder(file.getParentFile());
		final FileInputStream reader = new FileInputStream(file);
		final byte[] bytes = new byte[(int) file.length()];
		reader.read(bytes);
		reader.close();
		return bytes;
	}

	@OnlyIn(Dist.CLIENT)
	private static void createFolder(final File dir) throws IOException {
		if (dir == null) {
			return;
		}
		final File parent = dir.getParentFile();
		createFolder(parent);
		if (!dir.isDirectory()) {
			dir.mkdirs();
		}
	}

	public TrackLevel(Localization.STORIES.THE_BEGINNING name, final int playerX, final int playerY, final TrackOrientation.DIRECTION playerDir, final int itemX, final int itemY) {
		if (name != null)
			this.name = name.translate();
		this.playerX = playerX;
		this.playerY = playerY;
		this.playerDir = playerDir;
		this.itemX = itemX;
		this.itemY = itemY;
		tracks = new ArrayList<>();
		messages = new ArrayList<>();
	}

	public String getName() {
		return name;
	}

	public void setName(Localization.STORIES.THE_BEGINNING name) {
		this.name = name.translate();
	}
	public void setName(String name) {
		this.name = name;
	}

	public int getPlayerStartX() {
		return playerX;
	}

	public int getPlayerStartY() {
		return playerY;
	}

	public TrackOrientation.DIRECTION getPlayerStartDirection() {
		return playerDir;
	}

	public int getItemX() {
		return itemX;
	}

	public int getItemY() {
		return itemY;
	}

	public ArrayList<Track> getTracks() {
		return tracks;
	}

	public ArrayList<LevelMessage> getMessages() {
		return messages;
	}

	public void addMessage(final LevelMessage levelMessage) {
		messages.add(levelMessage);
	}

	static {
		editor = new TrackLevel(Localization.STORIES.THE_BEGINNING.MAP_EDITOR, 0, 0, TrackOrientation.DIRECTION.RIGHT, 26, 9);
		TrackLevel.MAP_FOLDER_PATH = "sc2/arcade/trackoperator/";
	}
}
