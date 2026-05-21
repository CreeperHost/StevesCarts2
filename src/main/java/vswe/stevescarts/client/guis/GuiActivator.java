package vswe.stevescarts.client.guis;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import vswe.stevescarts.StevesCartsClient;
import vswe.stevescarts.blocks.tileentities.TileEntityActivator;
import vswe.stevescarts.containers.ContainerActivator;
import vswe.stevescarts.helpers.ActivatorOption;
import vswe.stevescarts.helpers.Localization;
import vswe.stevescarts.helpers.ResourceHelper;
import vswe.stevescarts.network.PacketHandler;
import vswe.stevescarts.network.packets.PacketActivator;

public class GuiActivator extends AbstractContainerScreen<ContainerActivator>
{
    private static Identifier texture;
    TileEntityActivator activator;
    Inventory invPlayer;

    public GuiActivator(ContainerActivator containerActivator, Inventory playerInventory, Component iTextComponent)
    {
        super(containerActivator, playerInventory, iTextComponent);
        this.invPlayer = playerInventory;
        imageWidth = 255;
        imageHeight = 222;
        this.activator = containerActivator.getActivator();
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float p_230450_2_, int mouseX, int mouseY)
    {
        final int j = getGuiLeft();
        final int k = getGuiTop();
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, GuiActivator.texture, j, k, 0, 0, imageWidth, imageHeight, 256, 256);
        mouseX -= getGuiLeft();
        mouseY -= getGuiTop();
        for (int i = 0; i < activator.getOptions().size(); ++i)
        {
            final ActivatorOption option = activator.getOptions().get(i);
            final int[] box = getBoxRect(i);
            int srcX = 0;
            if (inRect(mouseX, mouseY, box))
            {
                srcX = 16;
            }
            guiGraphics.blit(RenderPipelines.GUI_TEXTURED, GuiActivator.texture, j + box[0], k + box[1], srcX, imageHeight, box[2], box[3], 256, 256);
            guiGraphics.blit(RenderPipelines.GUI_TEXTURED, GuiActivator.texture, j + box[0] + 1, k + box[1] + 1, (box[2] - 2) * option.getOption(), imageHeight + box[3], box[2] - 2, box[3] - 2, 256, 256);
        }
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int p_230451_2_, int p_230451_3_)
    {
    }

    public boolean inRect(final int x, final int y, final int[] coords)
    {
        return coords != null && x >= coords[0] && x < coords[0] + coords[2] && y >= coords[1] && y < coords[1] + coords[3];
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float p_230430_4_)
    {
        this.renderBackground(guiGraphics, mouseX, mouseY, p_230430_4_);
        super.render(guiGraphics, mouseX, mouseY, p_230430_4_);
        renderTooltip(guiGraphics, mouseX, mouseY);
        mouseX -= getGuiLeft();
        mouseY -= getGuiTop();
        guiGraphics.drawString(Minecraft.getInstance().font, Localization.GUI.TOGGLER.TITLE.translate(), getGuiLeft() + 8, getGuiTop() + 6, 0xFF404040, false);
        for (int i = 0; i < activator.getOptions().size(); ++i)
        {
            final ActivatorOption option = activator.getOptions().get(i);
            final int[] box = getBoxRect(i);
            guiGraphics.drawString(Minecraft.getInstance().font, option.getName(), getGuiLeft() + box[0] + box[2] + 6, getGuiTop() + box[1] + 4, 0xFF404040, false);
        }
        for (int i = 0; i < activator.getOptions().size(); ++i)
        {
            final ActivatorOption option = activator.getOptions().get(i);
            final int[] box = getBoxRect(i);
            drawMouseMover(guiGraphics, option.getInfo(), mouseX, mouseY, box);
        }
    }

    private void drawMouseMover(GuiGraphics guiGraphics, String str, final int x, final int y, final int[] rect)
    {
        if (inRect(x, y, rect))
        {
            drawMouseOver(guiGraphics, str, x, y, rect);
        }
    }

    public void drawMouseOver(GuiGraphics guiGraphics, final String str, final int x, final int y, final int[] rect)
    {
        if (inRect(x, y, rect))
        {
            guiGraphics.setTooltipForNextFrame(Minecraft.getInstance().font, Component.literal(str), getGuiLeft() + x, getGuiTop() + y);
        }
    }

    private int[] getBoxRect(final int i)
    {
        return new int[]{20, 22 + i * 20, 16, 16};
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean isDoubleClick) {
        super.mouseClicked(event, isDoubleClick);
        double mouseX = event.x() - getGuiLeft();
        double mouseY = event.y() - getGuiTop();
        for (int i = 0; i < activator.getOptions().size(); ++i)
        {
            final int[] box = getBoxRect(i);
            if (inRect((int) mouseX, (int) mouseY, box))
            {
                byte data = (byte) ((event.button() != 0) ? 1 : 0);
                data |= (byte) (i << 1);
                activator.getOptions().get(i).changeOption(event.button() == 0);
                StevesCartsClient.sendToServer(new PacketActivator(activator.getBlockPos(), 0, new byte[]{data}));
            }
        }
        return true;
    }

    static
    {
        GuiActivator.texture = ResourceHelper.getResource("/gui/activator.png");
    }
}
