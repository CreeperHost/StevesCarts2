package vswe.stevescarts.arcade;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import vswe.stevescarts.SCConfig;
import vswe.stevescarts.client.guis.GuiMinecart;
import vswe.stevescarts.helpers.Localization;
import vswe.stevescarts.modules.realtimers.ModuleArcade;

public abstract class ArcadeGame {
	private ModuleArcade module;
	private Localization.ARCADE name;

	public ArcadeGame(final ModuleArcade module, final Localization.ARCADE name) {
		this.name = name;
		this.module = module;
	}

	public String getName() {
		return name.translate();
	}

	public ModuleArcade getModule() {
		return module;
	}

	@OnlyIn(Dist.CLIENT)
	public void update() {
		if (SCConfig.useArcadeSounds.get()) {
			getModule().getCart().silent();
		}
	}

	@OnlyIn(Dist.CLIENT)
	public void drawForeground(PoseStack matrixStack, GuiMinecart gui) {
	}

	@OnlyIn(Dist.CLIENT)
	public void drawBackground(PoseStack matrixStack, GuiMinecart gui, final int x, final int y) {
	}

	@OnlyIn(Dist.CLIENT)
	public void drawMouseOver(PoseStack matrixStack, GuiMinecart gui, final int x, final int y) {
	}

	@OnlyIn(Dist.CLIENT)
	public void mouseClicked(final GuiMinecart gui, final int x, final int y, final int button) {
	}

	@OnlyIn(Dist.CLIENT)
	public void mouseMovedOrUp(final GuiMinecart gui, final int x, final int y, final int button) {
	}

	@OnlyIn(Dist.CLIENT)
	public void keyPress(final GuiMinecart gui, final int id, final int extraInformation) {
	}

	public void Save(final CompoundTag tagCompound, final int id) {
	}

	public void Load(final CompoundTag tagCompound, final int id) {
	}

	public void receivePacket(final int id, final byte[] data, final Player player) {
	}

	public void checkGuiData(final Object[] info) {
	}

	public void receiveGuiData(final int id, final short data) {
	}

	public boolean disableStandardKeyFunctionality() {
		return false;
	}

	@OnlyIn(Dist.CLIENT)
	public static void playSound(SoundEvent sound, float volume, float pitch) {
		if (SCConfig.useArcadeSounds.get() && sound != null) {
//			SoundHandler.playSound(sound, SoundCategory.BLOCKS, volume, pitch);
		}
	}

	public boolean allowKeyRepeat() {
		return false;
	}

	public void load(final GuiMinecart gui) {
		//TODO
//		gui.enableKeyRepeat(allowKeyRepeat());
	}

	public void unload(final GuiMinecart gui) {
		if (allowKeyRepeat()) {
//			gui.enableKeyRepeat(false);
		}
	}

	public void drawImageInArea(PoseStack matrixStack, GuiMinecart gui, final int x, final int y, final int u, final int v, final int w, final int h) {
		drawImageInArea(matrixStack,gui, x, y, u, v, w, h, 5, 4, 443, 168);
	}

	public void drawImageInArea(PoseStack matrixStack, GuiMinecart gui, int x, int y, int u, int v, int w, int h, final int x1, final int y1, final int x2, final int y2) {
		if (x < x1) {
			w -= x1 - x;
			u += x1 - x;
			x = x1;
		} else if (x + w > x2) {
			w = x2 - x;
		}
		if (y < y1) {
			h -= y1 - y;
			v += y1 - y;
			y = y1;
		} else if (y + h > y2) {
			h = y2 - y;
		}
		if (w > 0 && h > 0) {
			getModule().drawImage(matrixStack, gui, x, y, u, v, w, h);
		}
	}
}
