package vswe.stevescarts.arcade.tracks;

import vswe.stevescarts.client.guis.GuiBase;
import vswe.stevescarts.client.guis.GuiMinecart;

import java.util.ArrayList;

public abstract class TrackOrientation {
	public static ArrayList<TrackOrientation> ALL;
	public static TrackOrientation JUNCTION_4WAY;
	public static TrackOrientation STRAIGHT_HORIZONTAL;
	public static TrackOrientation STRAIGHT_VERTICAL;
	public static TrackOrientation CORNER_DOWN_RIGHT;
	public static TrackOrientation CORNER_DOWN_LEFT;
	public static TrackOrientation CORNER_UP_LEFT;
	public static TrackOrientation CORNER_UP_RIGHT;
	public static TrackOrientation JUNCTION_3WAY_CORNER_RIGHT_ENTRANCE_DOWN;
	public static TrackOrientation JUNCTION_3WAY_CORNER_RIGHT_ENTRANCE_RIGHT;
	public static TrackOrientation JUNCTION_3WAY_CORNER_RIGHT_ENTRANCE_UP;
	public static TrackOrientation JUNCTION_3WAY_CORNER_RIGHT_ENTRANCE_LEFT;
	public static TrackOrientation JUNCTION_3WAY_CORNER_LEFT_ENTRANCE_DOWN;
	public static TrackOrientation JUNCTION_3WAY_CORNER_LEFT_ENTRANCE_RIGHT;
	public static TrackOrientation JUNCTION_3WAY_CORNER_LEFT_ENTRANCE_UP;
	public static TrackOrientation JUNCTION_3WAY_CORNER_LEFT_ENTRANCE_LEFT;
	public static TrackOrientation JUNCTION_3WAY_STRAIGHT_FORWARD_VERTICAL_CORNER_DOWN_RIGHT;
	public static TrackOrientation JUNCTION_3WAY_STRAIGHT_FORWARD_VERTICAL_CORNER_DOWN_LEFT;
	public static TrackOrientation JUNCTION_3WAY_STRAIGHT_FORWARD_VERTICAL_CORNER_UP_LEFT;
	public static TrackOrientation JUNCTION_3WAY_STRAIGHT_FORWARD_VERTICAL_CORNER_UP_RIGHT;
	public static TrackOrientation JUNCTION_3WAY_STRAIGHT_FORWARD_HORIZONTAL_CORNER_DOWN_RIGHT;
	public static TrackOrientation JUNCTION_3WAY_STRAIGHT_FORWARD_HORIZONTAL_CORNER_DOWN_LEFT;
	public static TrackOrientation JUNCTION_3WAY_STRAIGHT_FORWARD_HORIZONTAL_CORNER_UP_LEFT;
	public static TrackOrientation JUNCTION_3WAY_STRAIGHT_FORWARD_HORIZONTAL_CORNER_UP_RIGHT;
	public static TrackOrientation JUNCTION_3WAY_STRAIGHT_TURN_VERTICAL_CORNER_DOWN_RIGHT;
	public static TrackOrientation JUNCTION_3WAY_STRAIGHT_TURN_VERTICAL_CORNER_DOWN_LEFT;
	public static TrackOrientation JUNCTION_3WAY_STRAIGHT_TURN_VERTICAL_CORNER_UP_LEFT;
	public static TrackOrientation JUNCTION_3WAY_STRAIGHT_TURN_VERTICAL_CORNER_UP_RIGHT;
	public static TrackOrientation JUNCTION_3WAY_STRAIGHT_TURN_HORIZONTAL_CORNER_DOWN_RIGHT;
	public static TrackOrientation JUNCTION_3WAY_STRAIGHT_TURN_HORIZONTAL_CORNER_DOWN_LEFT;
	public static TrackOrientation JUNCTION_3WAY_STRAIGHT_TURN_HORIZONTAL_CORNER_UP_LEFT;
	public static TrackOrientation JUNCTION_3WAY_STRAIGHT_TURN_HORIZONTAL_CORNER_UP_RIGHT;
	private int v;
	private GuiMinecart.RENDER_ROTATION rotation;
	private TrackOrientation opposite;
	private int val;

	TrackOrientation(final int v, final GuiMinecart.RENDER_ROTATION rotation) {
		this.v = v;
		this.rotation = rotation;
		val = TrackOrientation.ALL.size();
		TrackOrientation.ALL.add(this);
	}

	protected TrackOrientation setOpposite(final TrackOrientation opposite) {
		this.opposite = opposite;
		if (this.opposite.opposite != null) {
			this.opposite.opposite.opposite = this;
		} else {
			this.opposite.opposite = this;
		}
		return this;
	}

	public int getV() {
		return v;
	}

	public GuiMinecart.RENDER_ROTATION getRotation() {
		return rotation;
	}

	public TrackOrientation getOpposite() {
		return opposite;
	}

	public int toInteger() {
		return val;
	}

	public abstract DIRECTION travel(final DIRECTION p0);

	static {
		TrackOrientation.ALL = new ArrayList<>();
		TrackOrientation.JUNCTION_4WAY = new TrackOrientation(5, GuiMinecart.RENDER_ROTATION.NORMAL) {
			@Override
			public DIRECTION travel(final DIRECTION in) {
				return in.getOpposite();
			}
		};
		TrackOrientation.STRAIGHT_HORIZONTAL = new TrackOrientationStraight(1, GuiMinecart.RENDER_ROTATION.ROTATE_90, DIRECTION.RIGHT);
		TrackOrientation.STRAIGHT_VERTICAL = new TrackOrientationStraight(1, GuiMinecart.RENDER_ROTATION.NORMAL, DIRECTION.DOWN);
		TrackOrientation.CORNER_DOWN_RIGHT = new TrackOrientationCorner(0, GuiMinecart.RENDER_ROTATION.NORMAL, DIRECTION.DOWN, DIRECTION.RIGHT);
		TrackOrientation.CORNER_DOWN_LEFT = new TrackOrientationCorner(0, GuiMinecart.RENDER_ROTATION.ROTATE_90, DIRECTION.DOWN, DIRECTION.LEFT);
		TrackOrientation.CORNER_UP_LEFT = new TrackOrientationCorner(0, GuiMinecart.RENDER_ROTATION.ROTATE_180, DIRECTION.UP, DIRECTION.LEFT);
		TrackOrientation.CORNER_UP_RIGHT = new TrackOrientationCorner(0, GuiMinecart.RENDER_ROTATION.ROTATE_270, DIRECTION.UP, DIRECTION.RIGHT);
		TrackOrientation.JUNCTION_3WAY_CORNER_RIGHT_ENTRANCE_DOWN = new TrackOrientation3Way(4, GuiMinecart.RENDER_ROTATION.NORMAL, DIRECTION.DOWN, DIRECTION.DOWN.getRight());
		TrackOrientation.JUNCTION_3WAY_CORNER_RIGHT_ENTRANCE_RIGHT = new TrackOrientation3Way(4, GuiMinecart.RENDER_ROTATION.ROTATE_270, DIRECTION.RIGHT, DIRECTION.RIGHT.getRight());
		TrackOrientation.JUNCTION_3WAY_CORNER_RIGHT_ENTRANCE_UP = new TrackOrientation3Way(4, GuiMinecart.RENDER_ROTATION.ROTATE_180, DIRECTION.UP, DIRECTION.UP.getRight());
		TrackOrientation.JUNCTION_3WAY_CORNER_RIGHT_ENTRANCE_LEFT = new TrackOrientation3Way(4, GuiMinecart.RENDER_ROTATION.ROTATE_90, DIRECTION.LEFT, DIRECTION.LEFT.getRight());
		TrackOrientation.JUNCTION_3WAY_CORNER_LEFT_ENTRANCE_DOWN = new TrackOrientation3Way(4, GuiMinecart.RENDER_ROTATION.FLIP_HORIZONTAL, DIRECTION.DOWN, DIRECTION.DOWN.getLeft()).setOpposite(TrackOrientation.JUNCTION_3WAY_CORNER_RIGHT_ENTRANCE_DOWN);
		TrackOrientation.JUNCTION_3WAY_CORNER_LEFT_ENTRANCE_RIGHT = new TrackOrientation3Way(4, GuiMinecart.RENDER_ROTATION.ROTATE_270_FLIP, DIRECTION.RIGHT, DIRECTION.RIGHT.getLeft()).setOpposite(TrackOrientation.JUNCTION_3WAY_CORNER_RIGHT_ENTRANCE_RIGHT);
		TrackOrientation.JUNCTION_3WAY_CORNER_LEFT_ENTRANCE_UP = new TrackOrientation3Way(4, GuiMinecart.RENDER_ROTATION.FLIP_VERTICAL, DIRECTION.UP, DIRECTION.UP.getLeft()).setOpposite(TrackOrientation.JUNCTION_3WAY_CORNER_RIGHT_ENTRANCE_UP);
		TrackOrientation.JUNCTION_3WAY_CORNER_LEFT_ENTRANCE_LEFT = new TrackOrientation3Way(4, GuiMinecart.RENDER_ROTATION.ROTATE_90_FLIP, DIRECTION.LEFT, DIRECTION.LEFT.getLeft()).setOpposite(TrackOrientation.JUNCTION_3WAY_CORNER_RIGHT_ENTRANCE_LEFT);
		TrackOrientation.JUNCTION_3WAY_STRAIGHT_FORWARD_VERTICAL_CORNER_DOWN_RIGHT = new TrackOrientation3Way(2, GuiMinecart.RENDER_ROTATION.NORMAL, DIRECTION.DOWN, DIRECTION.DOWN.getOpposite());
		TrackOrientation.JUNCTION_3WAY_STRAIGHT_FORWARD_VERTICAL_CORNER_DOWN_LEFT = new TrackOrientation3Way(2, GuiMinecart.RENDER_ROTATION.FLIP_HORIZONTAL, DIRECTION.DOWN, DIRECTION.DOWN.getOpposite());
		TrackOrientation.JUNCTION_3WAY_STRAIGHT_FORWARD_VERTICAL_CORNER_UP_LEFT = new TrackOrientation3Way(2, GuiMinecart.RENDER_ROTATION.ROTATE_180, DIRECTION.UP, DIRECTION.UP.getOpposite());
		TrackOrientation.JUNCTION_3WAY_STRAIGHT_FORWARD_VERTICAL_CORNER_UP_RIGHT = new TrackOrientation3Way(2, GuiMinecart.RENDER_ROTATION.FLIP_VERTICAL, DIRECTION.UP, DIRECTION.UP.getOpposite());
		TrackOrientation.JUNCTION_3WAY_STRAIGHT_FORWARD_HORIZONTAL_CORNER_DOWN_RIGHT = new TrackOrientation3Way(2, GuiMinecart.RENDER_ROTATION.ROTATE_270_FLIP, DIRECTION.RIGHT, DIRECTION.RIGHT.getOpposite());
		TrackOrientation.JUNCTION_3WAY_STRAIGHT_FORWARD_HORIZONTAL_CORNER_DOWN_LEFT = new TrackOrientation3Way(2, GuiMinecart.RENDER_ROTATION.ROTATE_90, DIRECTION.LEFT, DIRECTION.LEFT.getOpposite());
		TrackOrientation.JUNCTION_3WAY_STRAIGHT_FORWARD_HORIZONTAL_CORNER_UP_LEFT = new TrackOrientation3Way(2, GuiMinecart.RENDER_ROTATION.ROTATE_90_FLIP, DIRECTION.LEFT, DIRECTION.LEFT.getOpposite());
		TrackOrientation.JUNCTION_3WAY_STRAIGHT_FORWARD_HORIZONTAL_CORNER_UP_RIGHT = new TrackOrientation3Way(2, GuiMinecart.RENDER_ROTATION.ROTATE_270, DIRECTION.RIGHT, DIRECTION.RIGHT.getOpposite());
		TrackOrientation.JUNCTION_3WAY_STRAIGHT_TURN_VERTICAL_CORNER_DOWN_RIGHT = new TrackOrientation3Way(3, GuiMinecart.RENDER_ROTATION.NORMAL, DIRECTION.DOWN, DIRECTION.RIGHT).setOpposite(TrackOrientation.JUNCTION_3WAY_STRAIGHT_FORWARD_VERTICAL_CORNER_DOWN_RIGHT);
		TrackOrientation.JUNCTION_3WAY_STRAIGHT_TURN_VERTICAL_CORNER_DOWN_LEFT = new TrackOrientation3Way(3, GuiMinecart.RENDER_ROTATION.FLIP_HORIZONTAL, DIRECTION.DOWN, DIRECTION.LEFT).setOpposite(TrackOrientation.JUNCTION_3WAY_STRAIGHT_FORWARD_VERTICAL_CORNER_DOWN_LEFT);
		TrackOrientation.JUNCTION_3WAY_STRAIGHT_TURN_VERTICAL_CORNER_UP_LEFT = new TrackOrientation3Way(3, GuiMinecart.RENDER_ROTATION.ROTATE_180, DIRECTION.UP, DIRECTION.LEFT).setOpposite(TrackOrientation.JUNCTION_3WAY_STRAIGHT_FORWARD_VERTICAL_CORNER_UP_LEFT);
		TrackOrientation.JUNCTION_3WAY_STRAIGHT_TURN_VERTICAL_CORNER_UP_RIGHT = new TrackOrientation3Way(3, GuiMinecart.RENDER_ROTATION.FLIP_VERTICAL, DIRECTION.UP, DIRECTION.RIGHT).setOpposite(TrackOrientation.JUNCTION_3WAY_STRAIGHT_FORWARD_VERTICAL_CORNER_UP_RIGHT);
		TrackOrientation.JUNCTION_3WAY_STRAIGHT_TURN_HORIZONTAL_CORNER_DOWN_RIGHT = new TrackOrientation3Way(3, GuiMinecart.RENDER_ROTATION.ROTATE_270_FLIP, DIRECTION.RIGHT, DIRECTION.DOWN).setOpposite(TrackOrientation.JUNCTION_3WAY_STRAIGHT_FORWARD_HORIZONTAL_CORNER_DOWN_RIGHT);
		TrackOrientation.JUNCTION_3WAY_STRAIGHT_TURN_HORIZONTAL_CORNER_DOWN_LEFT = new TrackOrientation3Way(3, GuiMinecart.RENDER_ROTATION.ROTATE_90, DIRECTION.LEFT, DIRECTION.DOWN).setOpposite(TrackOrientation.JUNCTION_3WAY_STRAIGHT_FORWARD_HORIZONTAL_CORNER_DOWN_LEFT);
		TrackOrientation.JUNCTION_3WAY_STRAIGHT_TURN_HORIZONTAL_CORNER_UP_LEFT = new TrackOrientation3Way(3, GuiMinecart.RENDER_ROTATION.ROTATE_90_FLIP, DIRECTION.LEFT, DIRECTION.UP).setOpposite(TrackOrientation.JUNCTION_3WAY_STRAIGHT_FORWARD_HORIZONTAL_CORNER_UP_LEFT);
		TrackOrientation.JUNCTION_3WAY_STRAIGHT_TURN_HORIZONTAL_CORNER_UP_RIGHT = new TrackOrientation3Way(3, GuiMinecart.RENDER_ROTATION.ROTATE_270, DIRECTION.RIGHT, DIRECTION.UP).setOpposite(TrackOrientation.JUNCTION_3WAY_STRAIGHT_FORWARD_HORIZONTAL_CORNER_UP_RIGHT);
	}

	private static class TrackOrientationStraight extends TrackOrientation {
		private DIRECTION base;

		TrackOrientationStraight(final int v, final GuiMinecart.RENDER_ROTATION rotation, final DIRECTION base) {
			super(v, rotation);
			this.base = base;
		}

		@Override
		public DIRECTION travel(final DIRECTION in) {
			if (in.equals(base)) {
				return base.getOpposite();
			}
			return base;
		}
	}

	private static class TrackOrientationCorner extends TrackOrientation {
		private DIRECTION dir1;
		private DIRECTION dir2;

		TrackOrientationCorner(final int v, final GuiMinecart.RENDER_ROTATION rotation, final DIRECTION dir1, final DIRECTION dir2) {
			super(v, rotation);
			this.dir1 = dir1;
			this.dir2 = dir2;
		}

		@Override
		public DIRECTION travel(final DIRECTION in) {
			if (in.equals(dir1)) {
				return dir2;
			}
			if (in.equals(dir2)) {
				return dir1;
			}
			return in.getOpposite();
		}
	}

	private static class TrackOrientation3Way extends TrackOrientation {
		private DIRECTION entrance;
		private DIRECTION active;

		TrackOrientation3Way(final int v, final GuiMinecart.RENDER_ROTATION rotation, final DIRECTION entrance, final DIRECTION active) {
			super(v, rotation);
			this.entrance = entrance;
			this.active = active;
		}

		@Override
		public DIRECTION travel(final DIRECTION in) {
			if (in.equals(entrance)) {
				return active;
			}
			return entrance;
		}
	}

	public enum DIRECTION {
		UP(0, 0, -1),
		DOWN(1, 0, 1),
		LEFT(2, -1, 0),
		RIGHT(3, 1, 0),
		STILL(-1, 0, 0);

		ArrayList<DIRECTION> ALL;
		private int x;
		private int y;
		private int val;

		DIRECTION(final int val, final int x, final int y) {
			this.val = val;
			this.x = x;
			this.y = y;
		}

		public int getX() {
			return x;
		}

		public int getY() {
			return y;
		}

		public DIRECTION getOpposite() {
			switch (this) {
				case UP: {
					return DIRECTION.DOWN;
				}
				case DOWN: {
					return DIRECTION.UP;
				}
				case LEFT: {
					return DIRECTION.RIGHT;
				}
				case RIGHT: {
					return DIRECTION.LEFT;
				}
				default: {
					return DIRECTION.STILL;
				}
			}
		}

		public DIRECTION getLeft() {
			switch (this) {
				case UP: {
					return DIRECTION.RIGHT;
				}
				case DOWN: {
					return DIRECTION.LEFT;
				}
				case LEFT: {
					return DIRECTION.UP;
				}
				case RIGHT: {
					return DIRECTION.DOWN;
				}
				default: {
					return DIRECTION.STILL;
				}
			}
		}

		public DIRECTION getRight() {
			switch (this) {
				case UP: {
					return DIRECTION.LEFT;
				}
				case DOWN: {
					return DIRECTION.RIGHT;
				}
				case LEFT: {
					return DIRECTION.DOWN;
				}
				case RIGHT: {
					return DIRECTION.UP;
				}
				default: {
					return DIRECTION.STILL;
				}
			}
		}

		public GuiMinecart.RENDER_ROTATION getRenderRotation() {
			switch (this) {
				case UP: {
					return GuiMinecart.RENDER_ROTATION.NORMAL;
				}
				case RIGHT: {
					return GuiMinecart.RENDER_ROTATION.ROTATE_90;
				}
				case DOWN: {
					return GuiMinecart.RENDER_ROTATION.ROTATE_180;
				}
				case LEFT: {
					return GuiMinecart.RENDER_ROTATION.ROTATE_270;
				}
				default: {
					return GuiMinecart.RENDER_ROTATION.NORMAL;
				}
			}
		}

		public int toInteger() {
			return val;
		}

		public static DIRECTION fromInteger(final int i) {
			for (final DIRECTION dir : values()) {
				if (dir.val == i) {
					return dir;
				}
			}
			return null;
		}
	}
}
