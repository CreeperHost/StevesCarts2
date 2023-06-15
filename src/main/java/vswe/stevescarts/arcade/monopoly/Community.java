package vswe.stevescarts.arcade.monopoly;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
import vswe.stevescarts.client.guis.GuiMinecart;

import java.util.EnumSet;

public class Community extends CardPlace
{
    public Community(final ArcadeMonopoly game)
    {
        super(game);
    }

    @Override
    protected int getTextureId()
    {
        return 5;
    }

    @Override
    public void drawText(GuiGraphics guiGraphics, GuiMinecart gui, final EnumSet<PLACE_STATE> states)
    {
        game.getModule().drawSplitString(guiGraphics, gui, "Dungeon Chest", 3 + gui.getGuiLeft(), 10 + gui.getGuiTop(), 70, true, 4210752);
    }

    @Override
    public Card getCard()
    {
        return CardCommunity.cards.get(game.getModule().getCart().random.nextInt(CardCommunity.cards.size()));
    }
}
