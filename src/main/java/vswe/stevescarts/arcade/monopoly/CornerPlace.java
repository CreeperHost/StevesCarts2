package vswe.stevescarts.arcade.monopoly;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.resources.Identifier;
import vswe.stevescarts.client.guis.GuiMinecart;

import java.util.EnumSet;

public class CornerPlace extends Place {
    private final int texture;

    public CornerPlace(final ArcadeMonopoly game, final int texture) {
        super(game);
        this.texture = texture;
    }

    @Override
    public void draw(GuiGraphicsExtractor GuiGraphicsExtractor, Identifier tex, GuiMinecart gui, final EnumSet<PLACE_STATE> states) {
        applyColorFilter(gui, states);
        game.getModule().drawImage(GuiGraphicsExtractor, game.getTexture(gui, 2), gui, 0, 0, 122 * (texture % 2), 122 * (texture / 2), 122, 122);
    }
}
