package vswe.stevescarts.arcade.tracks;

import com.mojang.blaze3d.matrix.MatrixStack;
import vswe.stevescarts.client.guis.GuiMinecart;

public class Cart {
	private int x;
	private int y;
	private TrackOrientation.DIRECTION dir;
	private int imageIndex;
	private boolean enabled;

	public Cart(final int imageIndex) {
		this.imageIndex = imageIndex;
		enabled = true;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public TrackOrientation.DIRECTION getDireciotn() {
		return dir;
	}

	public void setX(final int val) {
		x = val;
	}

	public void setY(final int val) {
		y = val;
	}

	public void setDirection(final TrackOrientation.DIRECTION val) {
		dir = val;
	}

	public void setAlive(final boolean val) {
		enabled = val;
	}

	public void move(final ArcadeTracks game) {
		if (!enabled) {
			return;
		}
		x += dir.getX();
		y += dir.getY();
		if (x < 0 || y < 0 || x >= game.getTrackMap().length || y >= game.getTrackMap()[0].length || game.getTrackMap()[x][y] == null) {
			if (dir != TrackOrientation.DIRECTION.STILL) {
				onCrash();
			}
			dir = TrackOrientation.DIRECTION.STILL;
		} else {
			game.getTrackMap()[x][y].travel(game, this);
			dir = game.getTrackMap()[x][y].getOrientation().travel(dir.getOpposite());
		}
		if (game.isItemOnGround() && x == game.getItemX() && y == game.getItemY()) {
			onItemPickUp();
			game.pickItemUp();
		}
	}

	public void onItemPickUp() {
	}

	public void onCrash() {
	}

	public void render(MatrixStack matrixStack, ArcadeTracks game, final GuiMinecart gui, final int tick) {
		if (!enabled) {
			return;
		}
		final int x = 7 + (int) (16.0f * (this.x + dir.getX() * (tick / 4.0f)));
		final int y = 7 + (int) (16.0f * (this.y + dir.getY() * (tick / 4.0f)));
		final int u = 256 - 12 * (imageIndex + 1);
		final int v = 244;
		final int w = 12;
		final int h = 12;
		game.drawImageInArea(matrixStack, gui, x, y, u, v, w, h);
	}

	public boolean isAlive() {
		return enabled;
	}
}
