package vswe.stevescarts.arcade.tracks;

import net.minecraft.sounds.SoundEvents;
import vswe.stevescarts.arcade.ArcadeGame;

public class TrackEnderHandler extends Track {
	private boolean isSpawner;

	public TrackEnderHandler(final int x, final int y, final TrackOrientation orientation, final boolean isSpawner) {
		super(x, y, orientation);
		this.isSpawner = isSpawner;
	}

	@Override
	public void travel(final ArcadeTracks game, final Cart cart) {
		if (isSpawner) {
			game.getEnderman().setAlive(true);
			game.getEnderman().setDirection(TrackOrientation.DIRECTION.RIGHT);
			game.getEnderman().setX(cart.getX() + 5);
			game.getEnderman().setY(cart.getY());
		} else if (game.getEnderman().isAlive()) {
			game.getEnderman().setAlive(false);
		}
		ArcadeGame.playSound(SoundEvents.ENDERMAN_TELEPORT, 1.0f, 1.0f);
	}

	@Override
	public Track copy() {
		return new TrackEnderHandler(getX(), getY(), getOrientation(), isSpawner);
	}
}
