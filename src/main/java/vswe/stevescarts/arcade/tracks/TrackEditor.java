package vswe.stevescarts.arcade.tracks;

public class TrackEditor extends Track {
	private int type;

	public TrackEditor(final TrackOrientation orientation) {
		super(0, 0, orientation);
		type = 0;
	}

	@Override
	public Track copy() {
		final TrackEditor newTrack = new TrackEditor(getOrientation());
		newTrack.type = type;
		return newTrack;
	}

	public Track getRealTrack(final int x, final int y) {
		return getRealTrack(x, y, type, getOrientation());
	}

	public static Track getRealTrack(final int x, final int y, final int type, final TrackOrientation orientation) {
		switch (type) {
			case 1: {
				return new TrackDetector(x, y, orientation);
			}
			case 2: {
				return new TrackHeavy(x, y, orientation);
			}
			default: {
				return new Track(x, y, orientation);
			}
		}
	}

	@Override
	public int getU() {
		return type;
	}

	public int getType() {
		return type;
	}

	public void setType(final int val) {
		type = val;
	}

	public void nextType() {
		type = (type + 1) % 3;
	}
}
