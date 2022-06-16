package vswe.stevescarts.arcade.tetris;

import com.mojang.blaze3d.vertex.PoseStack;
import vswe.stevescarts.client.guis.GuiMinecart;

public class TetrisPiecePart
{
    private TetrisBlock block;
    private int offX;
    private int offY;

    public TetrisPiecePart(final TetrisBlock block, final int offX, final int offY)
    {
        this.block = block;
        this.offX = offX;
        this.offY = offY;
    }

    public void render(PoseStack matrixStack, ArcadeTetris game, final GuiMinecart gui, final int x, final int y)
    {
        block.render(matrixStack, game, gui, x + offX, y + offY);
    }

    public void rotate(final int offSet)
    {
        block.rotate();
        final int temp = offX;
        offX = -offY + offSet;
        offY = temp;
    }

    public void placeInBoard(final TetrisBlock[][] board, final int x, final int y)
    {
        board[x + offX][y + offY] = block;
    }

    public boolean canMoveTo(final TetrisBlock[][] board, final int x, final int y)
    {
        return isValidAt(board, x + offX, y + offY);
    }

    public boolean isValidAt(final TetrisBlock[][] board, final int x, final int y)
    {
        return x >= 0 && x < board.length && y < board[0].length && (y < 0 || board[x][y] == null);
    }

    public boolean canRotate(final TetrisBlock[][] board, final int x, final int y, final int offSet)
    {
        return isValidAt(board, x - offY + offSet, y + offX);
    }

    public boolean canPlaceInBoard(final int y)
    {
        return y + offY >= 0;
    }
}
