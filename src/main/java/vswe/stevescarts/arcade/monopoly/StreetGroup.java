package vswe.stevescarts.arcade.monopoly;

public class StreetGroup extends PropertyGroup {
	private float[] color;
	private int houseCost;

	public StreetGroup(final int houseCost, final int[] color) {
		this.houseCost = houseCost;
		this.color = new float[] { color[0] / 256.0f, color[1] / 256.0f, color[2] / 256.0f };
	}

	public float[] getColor() {
		return color;
	}

	public int getStructureCost() {
		return houseCost;
	}
}
