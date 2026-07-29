package vswe.stevescarts.arcade.monopoly;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.resources.Identifier;
import vswe.stevescarts.client.guis.GuiMinecart;

import java.util.EnumSet;

public class Go extends CornerPlace {
    public Go(final ArcadeMonopoly game) {
        super(game, 0);
    }

    @Override
    public void draw(GuiGraphicsExtractor GuiGraphicsExtractor, Identifier texture, GuiMinecart gui, final EnumSet<PLACE_STATE> states) {
        super.draw(GuiGraphicsExtractor, texture, gui, states);
        Note.DIAMOND.draw(GuiGraphicsExtractor, game, gui, 45, 5, 2);
    }

    @Override
    public void drawText(GuiGraphicsExtractor GuiGraphicsExtractor, GuiMinecart gui, final EnumSet<PLACE_STATE> states) {
        game.getModule().drawString(GuiGraphicsExtractor, gui, "Collect", 5, 10, 4210752);
        game.getModule().drawString(GuiGraphicsExtractor, gui, "as you pass.", 5, 20, 4210752);
    }

    @Override
    public void onPiecePass(final Piece piece) {
        piece.addMoney(Note.DIAMOND, 2, true);
    }
}
