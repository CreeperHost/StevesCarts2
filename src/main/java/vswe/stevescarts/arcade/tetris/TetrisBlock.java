package vswe.stevescarts.arcade.tetris;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
import vswe.stevescarts.client.guis.GuiMinecart;

public class TetrisBlock
{
    private int u;
    private int v;
    private GuiMinecart.RENDER_ROTATION r;

    public TetrisBlock(final int u, final int v)
    {
        this.u = u;
        this.v = v;
        r = GuiMinecart.RENDER_ROTATION.NORMAL;
    }

    public void render(GuiGraphics guiGraphics, ArcadeTetris game, final GuiMinecart gui, final int x, final int y)
    {
        if (y >= 0)
        {
            game.getModule().drawImage(guiGraphics, gui, 189 + x * 10, 9 + y * 10, u, v, 10, 10, r);
        }
    }

    public void rotate()
    {
        r = r.getNextRotation();
    }
}
