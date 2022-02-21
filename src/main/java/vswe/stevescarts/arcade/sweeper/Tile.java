package vswe.stevescarts.arcade.sweeper;

import com.mojang.blaze3d.vertex.PoseStack;
import vswe.stevescarts.client.guis.GuiMinecart;

public class Tile {
	private int nearbyCreepers;
	private TILE_STATE state;
	private ArcadeSweeper game;

	public Tile(final ArcadeSweeper game) {
		state = TILE_STATE.CLOSED;
		this.game = game;
	}

	public void setCreeper() {
		nearbyCreepers = 9;
	}

	public void setNearbyCreepers(final int val) {
		nearbyCreepers = val;
	}

	public boolean isCreeper() {
		return nearbyCreepers == 9;
	}

	public void draw(PoseStack matrixStack, ArcadeSweeper game, final GuiMinecart gui, final int x, final int y, final int mx, final int my) {
		final int[] rect = { x, y, 10, 10 };
		if (isCreeper() && game.hasFinished) {
			game.getModule().drawImage(matrixStack, gui, rect, 30, 0);
		} else {
			final int u = (isOpen() || (state == TILE_STATE.FLAGGED && !isCreeper() && !game.isPlaying && !game.hasFinished)) ? 0 : (game.getModule().inRect(mx, my, rect) ? 20 : 10);
			game.getModule().drawImage(matrixStack, gui, rect, u, 0);
			if (isOpen() && nearbyCreepers != 0) {
				game.getModule().drawImage(matrixStack, gui, x + 1, y + 1, (nearbyCreepers - 1) * 8, 11, 8, 8);
			}
			if (state == TILE_STATE.FLAGGED) {
				if (!game.isPlaying && !isCreeper()) {
					game.getModule().drawImage(matrixStack, gui, x + 1, y + 1, 16, 20, 8, 8);
				} else {
					game.getModule().drawImage(matrixStack, gui, x + 1, y + 1, 0, 20, 8, 8);
				}
			} else if (state == TILE_STATE.MARKED) {
				game.getModule().drawImage(matrixStack, gui, x + 1, y + 1, 8, 20, 8, 8);
			}
		}
	}

	private boolean isOpen() {
		return (isCreeper() && !game.isPlaying && !game.hasFinished) || state == TILE_STATE.OPENED;
	}

	public TILE_OPEN_RESULT open() {
		if (state == TILE_STATE.OPENED || state == TILE_STATE.FLAGGED) {
			return TILE_OPEN_RESULT.FAILED;
		}
		state = TILE_STATE.OPENED;
		if (nearbyCreepers == 0) {
			final ArcadeSweeper game = this.game;
			--game.emptyLeft;
			return TILE_OPEN_RESULT.BLOB;
		}
		if (isCreeper()) {
			return TILE_OPEN_RESULT.DEAD;
		}
		final ArcadeSweeper game2 = game;
		--game2.emptyLeft;
		return TILE_OPEN_RESULT.OK;
	}

	@SuppressWarnings("incomplete-switch")
	public void mark() {
		switch (state) {
			case CLOSED: {
				state = TILE_STATE.FLAGGED;
				final ArcadeSweeper game = this.game;
				--game.creepersLeft;
				break;
			}
			case FLAGGED: {
				state = TILE_STATE.MARKED;
				final ArcadeSweeper game2 = game;
				++game2.creepersLeft;
				break;
			}
			case MARKED: {
				state = TILE_STATE.CLOSED;
				break;
			}
		}
	}

	public TILE_STATE getState() {
		return state;
	}

	public void setState(final TILE_STATE state) {
		this.state = state;
	}

	public int getNearbyCreepers() {
		return nearbyCreepers;
	}

	public enum TILE_STATE {
		CLOSED,
		OPENED,
		FLAGGED,
		MARKED
	}

	public enum TILE_OPEN_RESULT {
		OK,
		BLOB,
		FAILED,
		DEAD
	}
}
