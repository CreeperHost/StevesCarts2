package vswe.stevescarts.arcade.monopoly;

import com.mojang.blaze3d.matrix.MatrixStack;
import vswe.stevescarts.client.guis.GuiMinecart;

public class Die {
	private ArcadeMonopoly game;
	private int number;
	private int graphicalId;

	public Die(final ArcadeMonopoly game, final int graphicalId) {
		this.game = game;
		this.graphicalId = graphicalId;
		randomize();
	}

	public void draw(MatrixStack matrixStack, GuiMinecart gui, final int x, final int y) {
		game.getModule().drawImage(matrixStack, gui, x, y, 256 - 24 * (graphicalId + 1), 232, 24, 24);
		switch (number) {
			case 5: {
				drawEye(matrixStack, gui, x + 15, y + 3);
				drawEye(matrixStack, gui, x + 3, y + 15);
			}
			case 3: {
				drawEye(matrixStack, gui, x + 3, y + 3);
				drawEye(matrixStack, gui, x + 15, y + 15);
			}
			case 1: {
				drawEye(matrixStack, gui, x + 9, y + 9);
				break;
			}
			case 4: {
				drawEye(matrixStack, gui, x + 3, y + 3);
				drawEye(matrixStack, gui, x + 15, y + 15);
			}
			case 2: {
				drawEye(matrixStack, gui, x + 15, y + 3);
				drawEye(matrixStack, gui, x + 3, y + 15);
				break;
			}
			case 6: {
				drawEye(matrixStack, gui, x + 3, y + 2);
				drawEye(matrixStack, gui, x + 3, y + 9);
				drawEye(matrixStack, gui, x + 3, y + 16);
				drawEye(matrixStack, gui, x + 15, y + 2);
				drawEye(matrixStack, gui, x + 15, y + 9);
				drawEye(matrixStack, gui, x + 15, y + 16);
				break;
			}
		}
	}

	private void drawEye(MatrixStack matrixStack, GuiMinecart gui, final int x, final int y) {
		game.getModule().drawImage(matrixStack, gui, x, y, 256 - 6 * (graphicalId + 1), 226, 6, 6);
	}

	public int getNumber() {
		return number;
	}

	public void randomize() {
		number = game.getModule().getCart().random.nextInt(6) + 1;
	}
}
