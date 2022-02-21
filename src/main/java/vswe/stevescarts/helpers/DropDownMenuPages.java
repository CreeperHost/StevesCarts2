package vswe.stevescarts.helpers;

import com.mojang.blaze3d.vertex.PoseStack;
import vswe.stevescarts.client.guis.GuiDetector;

public class DropDownMenuPages extends DropDownMenu
{
    private int page;
    private int maxPages;
    private int[] leftArrow;
    private int[] rightArrow;

    public DropDownMenuPages(final int index, final int max)
    {
        super(index);
        leftArrow = new int[]{20, 20, 5, 7};
        rightArrow = new int[]{70, 20, 5, 7};
        page = 0;
        maxPages = max;
    }

    @Override
    protected int getCurrentId(final int index, final int objects)
    {
        return index - objects * page;
    }

    @Override
    public void drawMain(PoseStack matrixStack, GuiDetector gui, final int x, final int y)
    {
        super.drawMain(matrixStack, gui, x, y);
        drawObject(matrixStack, gui, x, y, new int[]{30, 20, 23, 7}, 0, 170, 0, 0);
        drawObject(matrixStack, gui, x, y, new int[]{60, 20, 5, 7}, 24 + 6 * page, 170, 0, 0);
        drawObject(matrixStack, gui, x, y, leftArrow, 0, 177, 5, 0);
        drawObject(matrixStack, gui, x, y, rightArrow, 0, 184, 5, 0);
    }

    private void drawObject(PoseStack matrixStack, GuiDetector gui, final int x, final int y, int[] rect, int srcX, int srcY, final int hoverDifX, final int hoverDifY)
    {
        final int[] array;
        rect = (array = new int[]{rect[0], rect[1], rect[2], rect[3]});
        final int n = 1;
        array[n] += 20 + getScroll() - 170;
        final int gap = rect[1] - getMainRect()[1] + rect[3];
        if (gap > 0)
        {
            final int height = Math.min(rect[3], gap);
            final int offset = rect[3] - height;
            rect[3] = height;
            if (gui.inRect(x, y, rect))
            {
                srcX += hoverDifX;
                srcY += hoverDifY;
            }
            gui.blit(matrixStack, gui.getGuiLeft() + rect[0], gui.getGuiTop() + rect[1] + offset, srcX, srcY + offset, rect[2], rect[3]);
        }
    }

    @Override
    public void onClick(final GuiDetector gui, final int x, final int y)
    {
        if (clicked(gui, x, y, leftArrow))
        {
            --page;
            if (page < 0)
            {
                page = maxPages - 1;
            }
        }
        else if (clicked(gui, x, y, rightArrow))
        {
            ++page;
            if (page >= maxPages)
            {
                page = 0;
            }
        }
    }

    private boolean clicked(final GuiDetector gui, final int x, final int y, int[] rect)
    {
        final int[] array;
        rect = (array = new int[]{rect[0], rect[1], rect[2], rect[3]});
        final int n = 1;
        array[n] += 20 + getScroll() - 170;
        final int gap = rect[1] - getMainRect()[1] + rect[3];
        if (gap > 0)
        {
            rect[3] = Math.min(rect[3], gap);
            return gui.inRect(x, y, rect);
        }
        return false;
    }
}
