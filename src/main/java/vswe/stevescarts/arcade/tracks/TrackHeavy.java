package vswe.stevescarts.arcade.tracks;

public class TrackHeavy extends Track {
	public TrackHeavy(final int x, final int y, final TrackOrientation orientation) {
		super(x, y, orientation);
	}

	@Override
	public void onClick(final ArcadeTracks game) {
	}

	@Override
	public Track copy() {
		return new TrackHeavy(getX(), getY(), getOrientation());
	}

	@Override
	public int getU() {
		return 2;
	}
}
