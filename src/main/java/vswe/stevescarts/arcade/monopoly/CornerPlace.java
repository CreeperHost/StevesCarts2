package vswe.stevescarts.arcade.monopoly;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import vswe.stevescarts.client.guis.GuiMinecart;

import java.util.EnumSet;

public class CornerPlace extends Place
{
    private int texture;

    public CornerPlace(final ArcadeMonopoly game, final int texture)
    {
        super(game);
        this.texture = texture;
    }

    @Override
    public void draw(GuiGraphics guiGraphics, ResourceLocation tex, GuiMinecart gui, final EnumSet<PLACE_STATE> states)
    {
        applyColorFilter(gui, states);
        game.getModule().drawImage(guiGraphics, game.getTexture(gui, 2), gui, 0, 0, 122 * (texture % 2), 122 * (texture / 2), 122, 122);
    }
}
