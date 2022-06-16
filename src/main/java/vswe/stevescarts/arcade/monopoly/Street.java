package vswe.stevescarts.arcade.monopoly;

import com.mojang.blaze3d.vertex.PoseStack;
import vswe.stevescarts.client.guis.GuiMinecart;

import java.util.EnumSet;

public class Street extends Property {
	private float[] color;
	private int structures;
	private int baseRent;

	public Street(final ArcadeMonopoly game, final StreetGroup group, final String name, final int cost, final int baseRent) {
		super(game, group, name, cost);
		color = group.getColor();
		this.baseRent = baseRent;
	}

	@Override
	public void draw(PoseStack matrixStack, GuiMinecart gui, final EnumSet<PLACE_STATE> states) {
		super.draw(matrixStack, gui, states);
		//TODO
//		GlStateManager._color4f(color[0], color[1], color[2], 1.0f);
		game.getModule().drawImage(matrixStack, gui, 0, 0, 76, 0, 76, 22);
//		GlStateManager._color4f(1.0f, 1.0f, 1.0f, 1.0f);
		if (structures > 0 && structures < 5) {
			for (int i = 0; i < structures; ++i) {
				game.getModule().drawImage(matrixStack, gui, 3 + i * 18, 3, 76, 22, 16, 16);
			}
		} else if (structures == 5) {
			game.getModule().drawImage(matrixStack, gui, 3, 3, 92, 22, 16, 16);
		}
		drawValue(matrixStack, gui);
	}

	public void increaseStructure() {
		++structures;
	}

	@Override
	protected int getTextY() {
		return 30;
	}

	public int getRentCost(final int structureCount) {
		switch (structureCount) {
			default: {
				return baseRent;
			}
			case 1: {
				return baseRent * 5;
			}
			case 2: {
				return baseRent * 15;
			}
			case 3: {
				return baseRent * 40;
			}
			case 4: {
				return baseRent * 70;
			}
			case 5: {
				return baseRent * 100;
			}
		}
	}

	public int getRentCost(final boolean ownsAll) {
		if (ownsAll) {
			return baseRent * 2;
		}
		return baseRent;
	}

	@Override
	public int getRentCost() {
		if (structures == 0) {
			return getRentCost(ownsAllInGroup(getOwner()));
		}
		return getRentCost(structures);
	}

	public int getStructureCount() {
		return structures;
	}

	public int getStructureCost() {
		return ((StreetGroup) getGroup()).getStructureCost();
	}

	public boolean ownsAllInGroup(final Piece currentPiece) {
		for (final Property property : getGroup().getProperties()) {
			if (property.getOwner() != currentPiece || property.isMortgaged()) {
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean canMortgage() {
		return super.canMortgage() && structures == 0;
	}

	public int getStructureSellPrice() {
		return getStructureCost() / 2;
	}

	public void decreaseStructures() {
		--structures;
	}
}
