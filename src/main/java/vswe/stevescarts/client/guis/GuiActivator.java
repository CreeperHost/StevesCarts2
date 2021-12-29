package vswe.stevescarts.client.guis;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import vswe.stevescarts.blocks.tileentities.TileEntityActivator;
import vswe.stevescarts.containers.ContainerActivator;
import vswe.stevescarts.helpers.ActivatorOption;
import vswe.stevescarts.helpers.Localization;
import vswe.stevescarts.helpers.ResourceHelper;
import vswe.stevescarts.network.PacketHandler;
import vswe.stevescarts.network.packets.PacketActivator;

public class GuiActivator extends ContainerScreen<ContainerActivator>
{
    private static ResourceLocation texture;
    TileEntityActivator activator;
    PlayerInventory invPlayer;

    public GuiActivator(ContainerActivator containerActivator, PlayerInventory playerInventory, ITextComponent iTextComponent)
    {
        super(containerActivator, playerInventory, iTextComponent);
        this.invPlayer = playerInventory;
        imageWidth = 255;
        imageHeight = 222;
        this.activator = containerActivator.getActivator();
    }

    @Override
    protected void renderBg(MatrixStack matrixStack, float p_230450_2_, int mouseX, int mouseY)
    {
        final int j = getGuiLeft();
        final int k = getGuiTop();
        ResourceHelper.bindResource(GuiActivator.texture);
        blit(matrixStack, j, k, 0, 0, imageWidth, imageHeight);
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
            blit(matrixStack, j + box[0], k + box[1], srcX, imageHeight, box[2], box[3]);
            blit(matrixStack, j + box[0] + 1, k + box[1] + 1, (box[2] - 2) * option.getOption(), imageHeight + box[3], box[2] - 2, box[3] - 2);
        }
    }

    @Override
    protected void renderLabels(MatrixStack p_230451_1_, int p_230451_2_, int p_230451_3_)
    {
    }

    public boolean inRect(final int x, final int y, final int[] coords)
    {
        return coords != null && x >= coords[0] && x < coords[0] + coords[2] && y >= coords[1] && y < coords[1] + coords[3];
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float p_230430_4_)
    {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, p_230430_4_);
        renderTooltip(matrixStack, mouseX, mouseY);
        mouseX -= getGuiLeft();
        mouseY -= getGuiTop();
        font.draw(matrixStack, Localization.GUI.TOGGLER.TITLE.translate(), getGuiLeft() + 8, getGuiTop() + 6, 4210752);
        for (int i = 0; i < activator.getOptions().size(); ++i)
        {
            final ActivatorOption option = activator.getOptions().get(i);
            final int[] box = getBoxRect(i);
            font.draw(matrixStack, option.getName(), getGuiLeft() + box[0] + box[2] + 6, getGuiTop() + box[1] + 4, 4210752);
        }
        for (int i = 0; i < activator.getOptions().size(); ++i)
        {
            final ActivatorOption option = activator.getOptions().get(i);
            final int[] box = getBoxRect(i);
            drawMouseMover(matrixStack, option.getInfo(), mouseX, mouseY, box);
        }
    }

    private void drawMouseMover(MatrixStack matrixStack, String str, final int x, final int y, final int[] rect)
    {
        if (inRect(x, y, rect))
        {
            drawMouseOver(matrixStack, str, x, y, rect);
        }
    }

    public void drawMouseOver(MatrixStack matrixStack, final String str, final int x, final int y, final int[] rect)
    {
        if (inRect(x, y, rect))
        {
            renderTooltip(matrixStack, new StringTextComponent(str), getGuiLeft() + x, getGuiTop() + y);
        }
    }

    private int[] getBoxRect(final int i)
    {
        return new int[]{20, 22 + i * 20, 16, 16};
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button)
    {
        super.mouseClicked(mouseX, mouseY, button);
        mouseX -= getGuiLeft();
        mouseY -= getGuiTop();
        for (int i = 0; i < activator.getOptions().size(); ++i)
        {
            final int[] box = getBoxRect(i);
            if (inRect((int) mouseX, (int) mouseY, box))
            {
                byte data = (byte) ((button != 0) ? 1 : 0);
                data |= (byte) (i << 1);
                activator.getOptions().get(i).changeOption(button == 0);
                PacketHandler.sendToServer(new PacketActivator(activator.getBlockPos(), 0, new byte[]{data}));
            }
        }
        return true;
    }

    static
    {
        GuiActivator.texture = ResourceHelper.getResource("/gui/activator.png");
    }
}
