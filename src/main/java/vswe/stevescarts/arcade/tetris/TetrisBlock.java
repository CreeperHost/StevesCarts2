package vswe.stevescarts.arcade.tetris;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.resources.Identifier;
import vswe.stevescarts.client.guis.GuiMinecart;

public class TetrisBlock {
    private final int u;
    private final int v;
    private GuiMinecart.RENDER_ROTATION r;

    public TetrisBlock(final int u, final int v) {
        this.u = u;
        this.v = v;
        r = GuiMinecart.RENDER_ROTATION.NORMAL;
    }

    public void render(GuiGraphicsExtractor GuiGraphicsExtractor, Identifier texture, ArcadeTetris game, final GuiMinecart gui, final int x, final int y) {
        if (y >= 0) {
//            game.getModule().drawImage(GuiGraphicsExtractor, texture, gui, 189 + x * 10, 9 + y * 10, u, v, 10, 10, r);
        }
    }

    public void rotate() {
        r = r.getNextRotation();
    }
}
