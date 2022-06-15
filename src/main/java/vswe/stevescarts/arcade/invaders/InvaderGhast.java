package vswe.stevescarts.arcade.invaders;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import vswe.stevescarts.arcade.ArcadeGame;
import vswe.stevescarts.client.guis.GuiMinecart;

import java.util.Random;

public class InvaderGhast extends Unit {
	private int tentacleTextureId;
	private int shooting;
	protected boolean isPahighast;
	private boolean hasTarget;
	private int targetX;
	private int targetY;

	public InvaderGhast(final ArcadeInvaders game, final int x, final int y) {
		super(game, x, y);
		tentacleTextureId = game.getModule().getCart().random.nextInt(4);
		shooting = -10;
		if (game.canSpawnPahighast && !game.hasPahighast && game.getModule().getCart().random.nextInt(1000) == 0) {
			isPahighast = true;
			game.hasPahighast = true;
		}
	}

	@Override
	public void draw(PoseStack matrixStack, GuiMinecart gui) {
		if (isPahighast) {
			game.drawImageInArea(matrixStack, gui, x, y, 32, 32, 16, 16);
		} else {
			game.drawImageInArea(matrixStack, gui, x, y, (shooting > -10) ? 16 : 0, 0, 16, 16);
		}
		game.drawImageInArea(matrixStack, gui, x, y + 16, 0, 16 + 8 * tentacleTextureId, 16, 8);
	}

	@Override
	public UPDATE_RESULT update() {
		if (hasTarget) {
			boolean flag = false;
			if (x != targetX) {
				if (x > targetX) {
					x = Math.max(targetX, x - 4);
				} else {
					x = Math.min(targetX, x + 4);
				}
				flag = true;
			}
			if (y != targetY) {
				if (y > targetY) {
					y = Math.max(targetY, y - 4);
				} else {
					y = Math.min(targetY, y + 4);
				}
				flag = true;
			}
			return flag ? UPDATE_RESULT.TARGET : UPDATE_RESULT.DONE;
		}
		if (super.update() == UPDATE_RESULT.DEAD) {
			return UPDATE_RESULT.DEAD;
		}
		if (shooting > -10) {
			if (shooting == 0) {
				RandomSource random = game.getModule().getCart().random;
				ArcadeGame.playSound(SoundEvents.GHAST_HURT, 0.1f, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F);
				this.game.projectiles.add(new Projectile(this.game, x + 8 - 3, y + 8 - 3, false));
			}
			--shooting;
		}
		if (game.moveDown > 0) {
			++y;
		} else {
			x += game.moveDirection * game.moveSpeed;
			if (y > 130) {
				return UPDATE_RESULT.GAME_OVER;
			}
			if (x > 417 || x < 10) {
				return UPDATE_RESULT.TURN_BACK;
			}
		}
		if (!isPahighast && shooting == -10 && game.getModule().getCart().random.nextInt(300) == 0) {
			shooting = 10;
		}
		return UPDATE_RESULT.DONE;
	}

	@Override
	protected int getHitboxWidth() {
		return 16;
	}

	@Override
	protected int getHitboxHeight() {
		return 24;
	}

	public void setTarget(final int x, final int y) {
		hasTarget = true;
		targetX = x;
		targetY = y;
	}
}
