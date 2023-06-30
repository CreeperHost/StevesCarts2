package vswe.stevescarts.client.guis;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Deprecated //THis is not used anywhere
public abstract class GuiBase extends AbstractContainerScreen
{
    public GuiBase(AbstractContainerMenu abstractContainerMenu, Inventory inventory, Component component)
    {
        super(abstractContainerMenu, inventory, component);
    }

    public void drawMouseOver(GuiGraphics guiGraphics, final String str, final int x, final int y)
    {
        final String[] split = str.split("\n");
        final List<String> text = new ArrayList<>(Arrays.asList(split));
        List<FormattedCharSequence> list = new ArrayList<>();
        for (String s : text)
        {
            list.add(FormattedCharSequence.forward(s, Style.EMPTY));
        }
        guiGraphics.renderTooltip(Minecraft.getInstance().font, list, x, y);
    }

    public boolean inRect(final int x, final int y, final int[] coords)
    {
        return coords != null && x >= coords[0] && x < coords[0] + coords[2] && y >= coords[1] && y < coords[1] + coords[3];
    }


    @Override
    public void renderBackground(GuiGraphics guiGraphics)
    {
        super.renderBackground(guiGraphics);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int x, int y, float f)
    {
        super.render(guiGraphics, x, y, f);
    }
}
