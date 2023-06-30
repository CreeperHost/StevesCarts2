package vswe.stevescarts.arcade.monopoly;

import net.minecraft.client.gui.GuiGraphics;
import vswe.stevescarts.client.guis.GuiMinecart;

import java.util.EnumSet;

public class Go extends CornerPlace
{
    public Go(final ArcadeMonopoly game)
    {
        super(game, 0);
    }

    @Override
    public void draw(GuiGraphics guiGraphics, GuiMinecart gui, final EnumSet<PLACE_STATE> states)
    {
        super.draw(guiGraphics, gui, states);
        Note.DIAMOND.draw(guiGraphics, game, gui, 45, 5, 2);
    }

    @Override
    public void drawText(GuiGraphics guiGraphics, GuiMinecart gui, final EnumSet<PLACE_STATE> states)
    {
        game.getModule().drawString(guiGraphics, gui, "Collect", 5, 10, 4210752);
        game.getModule().drawString(guiGraphics, gui, "as you pass.", 5, 20, 4210752);
    }

    @Override
    public void onPiecePass(final Piece piece)
    {
        piece.addMoney(Note.DIAMOND, 2, true);
    }
}
