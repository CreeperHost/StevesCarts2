package vswe.stevescarts.arcade.monopoly;

import com.mojang.blaze3d.vertex.PoseStack;
import vswe.stevescarts.client.guis.GuiMinecart;

public class NoteAnimation
{
    private Note note;
    private int animation;
    private boolean isNew;

    public NoteAnimation(final Note note, final int start, final boolean isNew)
    {
        this.note = note;
        animation = start;
        this.isNew = isNew;
    }

    public boolean draw(PoseStack matrixStack, ArcadeMonopoly game, final GuiMinecart gui, final int x, final int y)
    {
        if (animation >= 0)
        {
            if (isNew)
            {
                note.draw(matrixStack, game, gui, x, y - 10 + animation / 2);
            }
            else
            {
                note.draw(matrixStack, game, gui, x, y + animation);
            }
        }
        return ++animation > 20;
    }

    public Note getNote()
    {
        return note;
    }

    public int getAnimation()
    {
        return animation;
    }

    public boolean isNew()
    {
        return isNew;
    }
}
