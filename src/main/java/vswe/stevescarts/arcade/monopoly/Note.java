package vswe.stevescarts.arcade.monopoly;

import com.mojang.blaze3d.vertex.PoseStack;
import vswe.stevescarts.client.guis.GuiMinecart;

import java.util.ArrayList;

public class Note
{
    public static final ArrayList<Note> notes;
    public static final Note COAL;
    public static final Note IRON;
    public static final Note REDSTONE;
    public static final Note GOLD;
    public static final Note LAPIZ;
    public static final Note DIAMOND;
    public static final Note EMERALD;
    private int units;
    private int u;
    private int v;
    private int id;

    public Note(final int id, final int units, final int u, final int v)
    {
        this.id = id;
        this.units = units;
        this.u = u;
        this.v = v;
        Note.notes.add(this);
    }

    public int getId()
    {
        return id;
    }

    public void draw(PoseStack matrixStack, ArcadeMonopoly game, final GuiMinecart gui, final int x, final int y)
    {
        game.loadTexture(gui, 1);
        game.getModule().drawImage(matrixStack, gui, x, y, 76 + u * 16, 38 + v * 16, 16, 16);
    }

    public void draw(PoseStack matrixStack, ArcadeMonopoly game, final GuiMinecart gui, final int x, final int y, final int amount)
    {
        draw(matrixStack, game, gui, x, y, amount, 4210752);
    }

    public void draw(PoseStack matrixStack, ArcadeMonopoly game, final GuiMinecart gui, final int x, final int y, final int amount, final int color)
    {
        draw(matrixStack, game, gui, x + 10, y);
        game.getModule().drawString(matrixStack, gui, amount + "x ", new int[]{x + gui.getGuiLeft(), y + gui.getGuiTop(), 10, 16}, color);
    }

    public void drawPlayer(PoseStack matrixStack, ArcadeMonopoly game, final GuiMinecart gui, final int x, final int y, final int amount)
    {
        game.loadTexture(gui, 1);
        game.drawImageInArea(matrixStack, gui, x, y, 76 + u * 16, 38 + v * 16, 16, 16);
        if (x + 16 < 443)
        {
            game.getModule().drawString(matrixStack, gui, String.valueOf(amount), x + gui.getGuiLeft(), y + 17 + gui.getGuiTop(), 16, true, 4210752);
        }
    }

    public static int drawValue(PoseStack matrixStack, ArcadeMonopoly game, final GuiMinecart gui, final int x, final int y, int maxNoteCount, int value)
    {
        int id = 0;
        for (int i = Note.notes.size() - 1; i >= 0; --i)
        {
            if (value >= Note.notes.get(i).units && (maxNoteCount != 1 || value % Note.notes.get(i).units == 0))
            {
                final int amount = value / Note.notes.get(i).units;
                value -= amount * Note.notes.get(i).units;
                Note.notes.get(i).draw(matrixStack, game, gui, x + id * 34, y, amount);
                ++id;
                --maxNoteCount;
            }
        }
        return id;
    }

    public static void drawPlayerValue(PoseStack matrixStack, ArcadeMonopoly game, final GuiMinecart gui, final int x, final int y, final int[] values)
    {
        for (int i = 0; i < Note.notes.size(); ++i)
        {
            Note.notes.get(i).drawPlayer(matrixStack, game, gui, x + (6 - i) * 20, y, values[i]);
        }
    }

    public int getUnits()
    {
        return units;
    }

    static
    {
        notes = new ArrayList<>();
        COAL = new Note(0, 1, 0, 0);
        IRON = new Note(1, 5, 1, 0);
        REDSTONE = new Note(2, 10, 2, 0);
        GOLD = new Note(3, 20, 3, 0);
        LAPIZ = new Note(4, 50, 0, 1);
        DIAMOND = new Note(5, 100, 1, 1);
        EMERALD = new Note(6, 500, 2, 1);
    }
}
