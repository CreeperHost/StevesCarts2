package vswe.stevescarts.upgrades;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.ContainerListener;
import vswe.stevescarts.blocks.tileentities.TileEntityUpgrade;
import vswe.stevescarts.client.guis.GuiUpgrade;
import vswe.stevescarts.containers.ContainerUpgrade;

public abstract class InterfaceEffect extends BaseEffect
{
    public void drawForeground(final TileEntityUpgrade upgrade, final GuiUpgrade gui)
    {
    }

    public void drawBackground(PoseStack matrixStack, TileEntityUpgrade upgrade, final GuiUpgrade gui, final int x, final int y)
    {
    }

    public void drawMouseOver(PoseStack matrixStack, TileEntityUpgrade upgrade, final GuiUpgrade gui, final int x, final int y)
    {
    }

    public void checkGuiData(final TileEntityUpgrade upgrade, final ContainerUpgrade con, final ContainerListener crafting, final boolean isNew)
    {
    }

    public void receiveGuiData(final TileEntityUpgrade upgrade, final int id, final short data)
    {
    }

    protected void drawMouseOver(PoseStack matrixStack, GuiUpgrade gui, final String str, final int x, final int y, final int[] rect)
    {
        if (gui.inRect(x - gui.getGuiLeft(), y - gui.getGuiTop(), rect))
        {
            gui.renderTooltip(matrixStack, Component.literal(str), x, y);
        }
    }
}
