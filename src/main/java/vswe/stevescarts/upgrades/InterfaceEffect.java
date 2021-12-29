package vswe.stevescarts.upgrades;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.inventory.container.IContainerListener;
import net.minecraft.util.text.StringTextComponent;
import vswe.stevescarts.blocks.tileentities.TileEntityUpgrade;
import vswe.stevescarts.client.guis.GuiUpgrade;
import vswe.stevescarts.containers.ContainerUpgrade;

public abstract class InterfaceEffect extends BaseEffect
{
    public void drawForeground(final TileEntityUpgrade upgrade, final GuiUpgrade gui)
    {
    }

    public void drawBackground(MatrixStack matrixStack, TileEntityUpgrade upgrade, final GuiUpgrade gui, final int x, final int y)
    {
    }

    public void drawMouseOver(MatrixStack matrixStack, TileEntityUpgrade upgrade, final GuiUpgrade gui, final int x, final int y)
    {
    }

    public void checkGuiData(final TileEntityUpgrade upgrade, final ContainerUpgrade con, final IContainerListener crafting, final boolean isNew)
    {
    }

    public void receiveGuiData(final TileEntityUpgrade upgrade, final int id, final short data)
    {
    }

    protected void drawMouseOver(MatrixStack matrixStack, GuiUpgrade gui, final String str, final int x, final int y, final int[] rect)
    {
        if (gui.inRect(x - gui.getGuiLeft(), y - gui.getGuiTop(), rect))
        {
            gui.renderTooltip(matrixStack, new StringTextComponent(str), x - gui.getGuiLeft(), y - gui.getGuiTop());
        }
    }
}
