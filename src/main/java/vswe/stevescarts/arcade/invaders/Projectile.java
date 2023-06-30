package vswe.stevescarts.arcade.invaders;

import net.minecraft.client.gui.GuiGraphics;
import vswe.stevescarts.client.guis.GuiMinecart;

public class Projectile extends Unit
{
    protected boolean playerProjectile;

    public Projectile(final ArcadeInvaders game, final int x, final int y, final boolean playerProjectile)
    {
        super(game, x, y);
        this.playerProjectile = playerProjectile;
    }

    @Override
    public void draw(GuiGraphics guiGraphics, GuiMinecart gui)
    {
        if (playerProjectile)
        {
            game.getModule().drawImage(guiGraphics, gui, x, y, 38, 0, 5, 16);
        }
        else
        {
            game.getModule().drawImage(guiGraphics, gui, x, y, 32, 0, 6, 6);
        }
    }

    @Override
    protected void hitCalculation()
    {
    }

    @Override
    public UPDATE_RESULT update()
    {
        if (super.update() == UPDATE_RESULT.DEAD)
        {
            return UPDATE_RESULT.DEAD;
        }
        y += (playerProjectile ? -5 : 5);
        if (y < 0 || y > 168)
        {
            dead = true;
            return UPDATE_RESULT.DEAD;
        }
        return UPDATE_RESULT.DONE;
    }

    @Override
    protected int getHitboxWidth()
    {
        return playerProjectile ? 5 : 6;
    }

    @Override
    protected int getHitboxHeight()
    {
        return playerProjectile ? 16 : 6;
    }
}
