package vswe.stevescarts.arcade.monopoly;

import com.mojang.blaze3d.vertex.PoseStack;
import vswe.stevescarts.client.guis.GuiMinecart;

import java.util.EnumSet;

public class Station extends Property {
	private String name;
	private int stationId;

	public Station(final ArcadeMonopoly game, final PropertyGroup group, final int stationId, final String name) {
		super(game, group, name, 200);
		this.stationId = stationId;
		this.name = name;
	}

	@Override
	protected int getTextureId() {
		return 1 + stationId;
	}

	@Override
	public void draw(PoseStack matrixStack, GuiMinecart gui, final EnumSet<PLACE_STATE> states) {
		super.draw(matrixStack, gui, states);
		drawValue(matrixStack, gui);
	}

	@Override
	protected int getTextY() {
		return 10;
	}

	public int getRentCost(final int ownedStations) {
		return 25 * (int) Math.pow(2.0, ownedStations - 1);
	}

	@Override
	public int getRentCost() {
		return getRentCost(getOwnedInGroup());
	}
}
