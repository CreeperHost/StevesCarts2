package vswe.stevescarts.arcade.tracks;

import com.mojang.blaze3d.matrix.MatrixStack;
import vswe.stevescarts.client.guis.GuiMinecart;

import java.util.ArrayList;

public class ScrollableList {
	private int x;
	private int y;
	private ArcadeTracks game;
	private ArrayList<String> items;
	private int scrollPosition;
	private boolean isScrolling;
	private int selectedIndex;

	public ScrollableList(final ArcadeTracks game, final int x, final int y) {
		selectedIndex = -1;
		this.x = x;
		this.y = y;
		this.game = game;
		items = new ArrayList<>();
	}

	public void clearList() {
		items.clear();
	}

	public void clear() {
		selectedIndex = -1;
		scrollPosition = 0;
	}

	public void add(final String str) {
		items.add(str);
	}

	public boolean isVisible() {
		return true;
	}

	public int getSelectedIndex() {
		return selectedIndex;
	}

	public void onClick() {
	}

	public void drawBackground(MatrixStack matrixStack,  GuiMinecart gui, final int x, final int y) {
		if (!isVisible()) {
			return;
		}
		final int[] menu = game.getMenuArea();
		game.getModule().drawImage(matrixStack, gui, menu[0] + this.x, menu[1] + this.y, 0, 192, 132, 64);
		for (int i = 0; i < items.size(); ++i) {
			final int[] rect = getLevelButtonArea(i);
			if (rect[3] > 0) {
				int srcY = 188 + ((items.get(i) == null) ? 34 : (game.getModule().inRect(x, y, rect) ? 17 : 0));
				int borderSrcY = 239;
				if (rect[4] < 0) {
					srcY -= rect[4];
					borderSrcY -= rect[4];
				}
				game.getModule().drawImage(matrixStack, gui, rect, 146, srcY);
				if (i == selectedIndex) {
					game.getModule().drawImage(matrixStack, gui, rect, 146, borderSrcY);
				}
			}
		}
		final int[] area = getScrollArea();
		game.getModule().drawImage(matrixStack, gui, area[0], area[1] + scrollPosition, 132, 256 - ((items.size() >= 4) ? 32 : 16), 14, 16);
	}

	public void drawForeground(MatrixStack matrixStack, GuiMinecart gui) {
		if (!isVisible()) {
			return;
		}
		for (int i = 0; i < items.size(); ++i) {
			final int[] rect = getLevelButtonArea(i);
			final int x = rect[0] + 4;
			int y = rect[1] + 5;
			if (rect[4] < 0) {
				y += rect[4];
			}
			if (rect[4] >= -5) {
				if (rect[4] <= 48) {
					game.getModule().drawString(matrixStack, gui, (items.get(i) == null) ? "<???>" : items.get(i), x, y, 4210752);
				}
			}
		}
	}

	public void mouseMovedOrUp(final GuiMinecart gui, final int x, final int y, final int button) {
		if (!isVisible()) {
			return;
		}
		if (isScrolling) {
			if (button != -1) {
				isScrolling = false;
			} else {
				doScroll(y);
			}
		}
	}

	private void doScroll(final int y) {
		final int[] area = getScrollArea();
		scrollPosition = y - area[1] - 8;
		if (scrollPosition < 0) {
			scrollPosition = 0;
		} else if (scrollPosition > 42) {
			scrollPosition = 42;
		}
	}

	private int getScrollLevel() {
		final int totalSize = items.size() * 18;
		final int availableSpace = 60;
		final int canNotFit = totalSize - availableSpace;
		final int scrollLength = getScrollArea()[3] - 16;
		return canNotFit * (scrollPosition / scrollLength);
	}

	private int[] getLevelButtonArea(final int id) {
		final int[] menu = game.getMenuArea();
		final int offSetY = 18 * id - getScrollLevel();
		int height = 17;
		int y = menu[1] + this.y + 2 + offSetY;
		if (offSetY < 0) {
			height += offSetY;
			y -= offSetY;
		} else if (offSetY + height > 60) {
			height = 60 - offSetY;
		}
		return new int[] { menu[0] + 2 + x, y, 108, height, offSetY };
	}

	private int[] getScrollArea() {
		final int[] menu = game.getMenuArea();
		return new int[] { menu[0] + x + 116, menu[1] + y + 3, 14, 58 };
	}

	public void mouseClicked(final GuiMinecart gui, final int x, final int y, final int button) {
		if (!isVisible()) {
			return;
		}
		for (int i = 0; i < items.size(); ++i) {
			if (items.get(i) != null) {
				final int[] rect = getLevelButtonArea(i);
				if (rect[3] > 0 && game.getModule().inRect(x, y, rect)) {
					if (selectedIndex == i) {
						selectedIndex = -1;
					} else {
						selectedIndex = i;
					}
					onClick();
					break;
				}
			}
		}
		if (items.size() >= 4 && game.getModule().inRect(x, y, getScrollArea())) {
			doScroll(y);
			isScrolling = true;
		}
	}
}
