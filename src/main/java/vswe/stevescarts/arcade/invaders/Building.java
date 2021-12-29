package vswe.stevescarts.arcade.invaders;

import com.mojang.blaze3d.matrix.MatrixStack;
import vswe.stevescarts.client.guis.GuiMinecart;

public class Building extends Unit {
	public Building(final ArcadeInvaders game, final int x, final int y) {
		super(game, x, y);
		health = 10;
	}

	@Override
	public void draw(MatrixStack matrixStack, GuiMinecart gui) {
		game.getModule().drawImage(matrixStack, gui, x, y, 32 + (10 - health) * 16, 16, 16, 16);
	}

	@Override
	protected int getHitboxWidth() {
		return 16;
	}

	@Override
	protected int getHitboxHeight() {
		return 16;
	}

	@Override
	protected boolean isObstacle() {
		return true;
	}

	@Override
	public UPDATE_RESULT update() {
		if (super.update() == UPDATE_RESULT.DEAD) {
			return UPDATE_RESULT.DEAD;
		}
		for (final Unit invader : game.invaders) {
			if (!invader.dead && collidesWith(invader)) {
				dead = true;
				health = 0;
				return UPDATE_RESULT.DEAD;
			}
		}
		return UPDATE_RESULT.DONE;
	}
}
