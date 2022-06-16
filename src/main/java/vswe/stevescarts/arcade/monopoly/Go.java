package vswe.stevescarts.arcade.monopoly;

import com.mojang.blaze3d.vertex.PoseStack;
import vswe.stevescarts.client.guis.GuiMinecart;

import java.util.EnumSet;

public class Go extends CornerPlace
{
    public Go(final ArcadeMonopoly game)
    {
        super(game, 0);
    }

    @Override
    public void draw(PoseStack matrixStack, GuiMinecart gui, final EnumSet<PLACE_STATE> states)
    {
        super.draw(matrixStack, gui, states);
        Note.DIAMOND.draw(matrixStack, game, gui, 45, 5, 2);
    }

    @Override
    public void drawText(PoseStack matrixStack, GuiMinecart gui, final EnumSet<PLACE_STATE> states)
    {
        game.getModule().drawString(matrixStack, gui, "Collect", 5, 10, 4210752);
        game.getModule().drawString(matrixStack, gui, "as you pass.", 5, 20, 4210752);
    }

    @Override
    public void onPiecePass(final Piece piece)
    {
        piece.addMoney(Note.DIAMOND, 2, true);
    }
}
