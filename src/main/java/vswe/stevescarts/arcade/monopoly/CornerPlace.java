package vswe.stevescarts.arcade.monopoly;

import com.mojang.blaze3d.vertex.PoseStack;
import vswe.stevescarts.client.guis.GuiMinecart;

import java.util.EnumSet;

public class CornerPlace extends Place {
	private int texture;

	public CornerPlace(final ArcadeMonopoly game, final int texture) {
		super(game);
		this.texture = texture;
	}

	@Override
	public void draw(PoseStack matrixStack, GuiMinecart gui, final EnumSet<PLACE_STATE> states) {
		game.loadTexture(gui, 2);
		applyColorFilter(gui, states);
		game.getModule().drawImage(matrixStack, gui, 0, 0, 122 * (texture % 2), 122 * (texture / 2), 122, 122);
	}
}
