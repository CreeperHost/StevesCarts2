package vswe.stevescarts.arcade.monopoly;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import vswe.stevescarts.client.guis.GuiMinecart;

public class NoteAnimation {
    private final Note note;
    private int animation;
    private final boolean isNew;

    public NoteAnimation(final Note note, final int start, final boolean isNew) {
        this.note = note;
        animation = start;
        this.isNew = isNew;
    }

    public boolean draw(GuiGraphicsExtractor GuiGraphicsExtractor, ArcadeMonopoly game, final GuiMinecart gui, final int x, final int y) {
        if (animation >= 0) {
            if (isNew) {
                note.draw(GuiGraphicsExtractor, game, gui, x, y - 10 + animation / 2);
            } else {
                note.draw(GuiGraphicsExtractor, game, gui, x, y + animation);
            }
        }
        return ++animation > 20;
    }

    public Note getNote() {
        return note;
    }

    public int getAnimation() {
        return animation;
    }

    public boolean isNew() {
        return isNew;
    }
}
