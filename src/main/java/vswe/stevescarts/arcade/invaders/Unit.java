package vswe.stevescarts.arcade.invaders;

import com.mojang.blaze3d.vertex.PoseStack;
import vswe.stevescarts.client.guis.GuiMinecart;

public abstract class Unit {
	protected int x;
	protected int y;
	protected ArcadeInvaders game;
	protected boolean dead;
	protected int health;

	public Unit(final ArcadeInvaders game, final int x, final int y) {
		this.x = x;
		this.y = y;
		this.game = game;
		health = 1;
	}

	public abstract void draw(PoseStack matrixStack, GuiMinecart p0);

	public UPDATE_RESULT update() {
		if (!dead) {
			hitCalculation();
		}
		return dead ? UPDATE_RESULT.DEAD : UPDATE_RESULT.DONE;
	}

	protected void hitCalculation() {
		for (final Projectile projectile : game.projectiles) {
			if (!projectile.dead && (isObstacle() || projectile.playerProjectile != isPlayer()) && collidesWith(projectile)) {
				--health;
				if (health == 0) {
					dead = true;
				}
				projectile.dead = true;
			}
		}
	}

	protected boolean collidesWith(final Unit unit) {
		return isUnitAinUnitB(this, unit) || isUnitAinUnitB(unit, this);
	}

	private boolean isUnitAinUnitB(final Unit a, final Unit b) {
		return ((a.x >= b.x && a.x <= b.x + b.getHitboxWidth()) || (a.x + a.getHitboxWidth() >= b.x && a.x + a.getHitboxWidth() <= b.x + b.getHitboxWidth())) && ((a.y >= b.y && a.y <= b.y + b.getHitboxHeight()) || (a.y + a.getHitboxHeight() >= b.y && a.y + a.getHitboxHeight() <= b.y + b.getHitboxHeight()));
	}

	protected boolean isPlayer() {
		return false;
	}

	protected boolean isObstacle() {
		return false;
	}

	protected abstract int getHitboxWidth();

	protected abstract int getHitboxHeight();

	public enum UPDATE_RESULT {
		DONE,
		TURN_BACK,
		DEAD,
		GAME_OVER,
		TARGET
	}
}
