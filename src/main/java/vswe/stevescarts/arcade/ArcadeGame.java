package vswe.stevescarts.arcade;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import vswe.stevescarts.SCConfig;
import vswe.stevescarts.client.guis.GuiMinecart;
import vswe.stevescarts.helpers.Localization;
import vswe.stevescarts.modules.realtimers.ModuleArcade;

public abstract class ArcadeGame {
    private final ModuleArcade module;
    private final Localization.ARCADE name;

    public ArcadeGame(final ModuleArcade module, final Localization.ARCADE name) {
        this.name = name;
        this.module = module;
    }

    public static void playSound(SoundEvent sound, float volume, float pitch) {
        if (SCConfig.CLIENT.useArcadeSounds.get() && sound != null) {
            //			SoundHandler.playSound(sound, SoundCategory.BLOCKS, volume, pitch);
        }
    }

    public String getName() {
        return name.translate();
    }

    public ModuleArcade getModule() {
        return module;
    }

    public void update() {
        if (SCConfig.CLIENT.useArcadeSounds.get()) {
//            getModule().getCart().silent();//Sound stuff needs to be re-implemented at some point
        }
    }

    public void drawForeground(GuiGraphicsExtractor GuiGraphicsExtractor, GuiMinecart gui) {
    }

    public void drawBackground(GuiGraphicsExtractor GuiGraphicsExtractor, GuiMinecart gui, final int x, final int y) {
    }

    public void drawMouseOver(GuiGraphicsExtractor GuiGraphicsExtractor, GuiMinecart gui, final int x, final int y) {
    }

    public void mouseClicked(final GuiMinecart gui, final int x, final int y, final int button) {
    }

    public void mouseMovedOrUp(final GuiMinecart gui, final int x, final int y, final int button) {
    }

    public void keyPress(final GuiMinecart gui, final int id, final int extraInformation) {
    }

    public void Save(ValueOutput output, int id) {
    }

    public void Load(ValueInput input, int id) {
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

    public boolean allowKeyRepeat() {
        return false;
    }

    public void load(final GuiMinecart gui) {
        //TODO
//        		gui.enableKeyRepeat(allowKeyRepeat());
    }

    public void unload(final GuiMinecart gui) {
        if (allowKeyRepeat()) {
            //			gui.enableKeyRepeat(false);
        }
    }

    public void drawImageInArea(GuiGraphicsExtractor GuiGraphicsExtractor, Identifier texture, GuiMinecart gui, final int x, final int y, final int u, final int v, final int w, final int h) {
        drawImageInArea(GuiGraphicsExtractor, texture, gui, x, y, u, v, w, h, 5, 4, 443, 168);
    }

    public void drawImageInArea(GuiGraphicsExtractor GuiGraphicsExtractor, Identifier texture, GuiMinecart gui, int x, int y, int u, int v, int w, int h, final int x1, final int y1, final int x2, final int y2) {
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
            getModule().drawImage(GuiGraphicsExtractor, texture, gui, x, y, u, v, w, h);
        }
    }
}
