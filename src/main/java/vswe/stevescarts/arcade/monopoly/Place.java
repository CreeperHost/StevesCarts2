package vswe.stevescarts.arcade.monopoly;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.resources.Identifier;
import vswe.stevescarts.client.guis.GuiMinecart;

import java.util.EnumSet;

public class Place {
    protected ArcadeMonopoly game;

    public Place(final ArcadeMonopoly game) {
        this.game = game;
    }

    protected int getTextureId() {
        return -1;
    }

    public void draw(GuiGraphicsExtractor GuiGraphicsExtractor, Identifier texture, GuiMinecart gui, final EnumSet<PLACE_STATE> states) {
        int t;
        int u;
        int v;
        if (getTextureId() == -1) {
            t = 1;
            u = 0;
            v = 0;
        } else {
            t = 3 + getTextureId() / 6;
            u = getTextureId() % 3;
            v = getTextureId() % 6 / 3;
        }
        game.getModule().drawImage(GuiGraphicsExtractor, game.getTexture(gui, t), gui, new int[]{0, 0, 76, 122}, 76 * u, 122 * v, getColorFilter(states));
    }

    protected int getColorFilter(final EnumSet<PLACE_STATE> states) {
        if (states.contains(PLACE_STATE.SELECTED)) {
            if (states.contains(PLACE_STATE.HOVER)) {
                return 0xFFFFCC80;
            } else {
                return 0xFFFFFFBF;
            }
        } else if (states.contains(PLACE_STATE.MARKED)) {
            if (states.contains(PLACE_STATE.HOVER)) {
                return 0xFFFFBFFF;
            } else {
                return 0xFFFFD9D9;
            }
        } else if (states.contains(PLACE_STATE.HOVER)) {
            return 0xFFE6E6FF;
        }
        return 0xFFFFFFFF;
    }

    public void drawText(GuiGraphicsExtractor GuiGraphicsExtractor, GuiMinecart gui, final EnumSet<PLACE_STATE> states) {
    }

    public void drawPiece(GuiGraphicsExtractor GuiGraphicsExtractor, Identifier texture, GuiMinecart gui, final Piece piece, final int total, final int pos, final int area, final EnumSet<PLACE_STATE> states) {
        final int SIZE = 24;
        final int PADDING = 5;
        final int MARGIN = 2;
        final int allowedWidth = getAllowedWidth(area) - 10;
        final int fullWidth = total * 26 - 2;
        int startX;
        int offSet;
        if (allowedWidth < fullWidth && total > 1) {
            startX = 5;
            offSet = (allowedWidth - 24) / (total - 1);
        } else {
            startX = 5 + (allowedWidth - fullWidth) / 2;
            offSet = 26;
        }
        game.getModule().drawImage(GuiGraphicsExtractor, texture, gui, startX + offSet * pos, getPieceYPosition(area), 232, piece.getV() * 24, 24, 24);
    }

    protected int getPieceYPosition(final int area) {
        return 70;
    }

    protected int getAllowedWidth(final int area) {
        return 76;
    }

    public void onPiecePass(final Piece piece) {
    }

    public boolean onPieceStop(final Piece piece) {
        return true;
    }

    public void onClick() {
    }

    public int getPieceAreaCount() {
        return 1;
    }

    public int getPieceAreaForPiece(final Piece piece) {
        return 0;
    }

    public enum PLACE_STATE {
        HOVER, SELECTED, MARKED, ZOOMED
    }
}
