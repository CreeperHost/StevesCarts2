package vswe.stevescarts.arcade.invaders;

import com.mojang.blaze3d.vertex.PoseStack;
import vswe.stevescarts.client.guis.GuiMinecart;

public class Player extends Unit
{
    protected boolean ready;
    private int targetX;
    private int targetY;

    public Player(final ArcadeInvaders game, final int x, final int y)
    {
        super(game, x, y);
    }

    public Player(final ArcadeInvaders game)
    {
        this(game, 200, 150);
        ready = true;
    }

    @Override
    public void draw(PoseStack matrixStack, GuiMinecart gui)
    {
        if (ready || targetY == y)
        {
            game.drawImageInArea(matrixStack, gui, x, y, 16, 16, 16, 16);
        }
        else
        {
            game.drawImageInArea(matrixStack, gui, x, y, 16, 16, 16, 16, 3, 0, 1000, 1000);
        }
    }

    protected void setTarget(final int x, final int y)
    {
        targetX = x;
        targetY = y;
    }

    @Override
    public UPDATE_RESULT update()
    {
        if (!ready)
        {
            if (targetY == y && targetX == x)
            {
                ready = true;
            }
            else if (targetY == y)
            {
                x = Math.min(targetX, x + 8);
            }
            else if (x == -15)
            {
                y = Math.max(targetY, y - 8);
            }
            else
            {
                x = Math.max(-15, x - 8);
            }
        }
        else if (super.update() == UPDATE_RESULT.DEAD)
        {
            return UPDATE_RESULT.DEAD;
        }
        return UPDATE_RESULT.DONE;
    }

    public void move(final int dir)
    {
        x += dir * 5;
        if (x < 10)
        {
            x = 10;
        }
        else if (x > 417)
        {
            x = 417;
        }
    }

    @Override
    protected boolean isPlayer()
    {
        return true;
    }

    @Override
    protected int getHitboxWidth()
    {
        return 16;
    }

    @Override
    protected int getHitboxHeight()
    {
        return 16;
    }
}
