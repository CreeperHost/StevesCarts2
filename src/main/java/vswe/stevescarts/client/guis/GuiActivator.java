package vswe.stevescarts.client.guis;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import org.jspecify.annotations.NonNull;
import vswe.stevescarts.StevesCartsClient;
import vswe.stevescarts.blocks.tileentities.TileEntityActivator;
import vswe.stevescarts.containers.ContainerActivator;
import vswe.stevescarts.helpers.ActivatorOption;
import vswe.stevescarts.helpers.Localization;
import vswe.stevescarts.helpers.ResourceHelper;
import vswe.stevescarts.network.packets.PacketActivator;

public class GuiActivator extends AbstractContainerScreen<ContainerActivator> {
    private static Identifier texture;

    static {
        GuiActivator.texture = ResourceHelper.getResource("/gui/activator.png");
    }

    TileEntityActivator activator;
    Inventory invPlayer;

    public GuiActivator(ContainerActivator containerActivator, Inventory playerInventory, Component iTextComponent) {
        super(containerActivator, playerInventory, iTextComponent, 255, 222);
        this.invPlayer = playerInventory;
        this.activator = containerActivator.getActivator();
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTicks) {
        final int left = getLeftPos();
        final int top = getTopPos();
        graphics.blit(RenderPipelines.GUI_TEXTURED, GuiActivator.texture, left, top, 0, 0, imageWidth, imageHeight, 256, 256);
        mouseX -= left;
        mouseY -= top;
        for (int i = 0; i < activator.getOptions().size(); ++i) {
            final ActivatorOption option = activator.getOptions().get(i);
            final int[] box = getBoxRect(i);
            int srcX = 0;
            if (inRect(mouseX, mouseY, box)) {
                srcX = 16;
            }
            graphics.blit(RenderPipelines.GUI_TEXTURED, GuiActivator.texture, left + box[0], top + box[1], srcX, imageHeight, box[2], box[3], 256, 256);
            graphics.blit(RenderPipelines.GUI_TEXTURED, GuiActivator.texture, left + box[0] + 1, top + box[1] + 1, (box[2] - 2) * option.getOption(), imageHeight + box[3], box[2] - 2, box[3] - 2, 256, 256);
        }
    }

    public boolean inRect(final int x, final int y, final int[] coords) {
        return coords != null && x >= coords[0] && x < coords[0] + coords[2] && y >= coords[1] && y < coords[1] + coords[3];
    }

    @Override
    public void extractContents(@NonNull GuiGraphicsExtractor graphics, int mouseX, int mouseY, float p_230430_4_) {
        this.extractBackground(graphics, mouseX, mouseY, p_230430_4_);
        super.extractContents(graphics, mouseX, mouseY, p_230430_4_);
        extractTooltip(graphics, mouseX, mouseY);
        mouseX -= getLeftPos();
        mouseY -= getTopPos();
        graphics.text(Minecraft.getInstance().font, Localization.GUI.TOGGLER.TITLE.translate(), getLeftPos() + 8, getTopPos() + 6, 0xFF404040, false);
        for (int i = 0; i < activator.getOptions().size(); ++i) {
            final ActivatorOption option = activator.getOptions().get(i);
            final int[] box = getBoxRect(i);
            graphics.text(Minecraft.getInstance().font, option.getName(), getLeftPos() + box[0] + box[2] + 6, getTopPos() + box[1] + 4, 0xFF404040, false);
        }
        for (int i = 0; i < activator.getOptions().size(); ++i) {
            final ActivatorOption option = activator.getOptions().get(i);
            final int[] box = getBoxRect(i);
            drawMouseMover(graphics, option.getInfo(), mouseX, mouseY, box);
        }
    }

    private void drawMouseMover(GuiGraphicsExtractor graphics, String str, final int x, final int y, final int[] rect) {
        if (inRect(x, y, rect)) {
            drawMouseOver(graphics, str, x, y, rect);
        }
    }

    public void drawMouseOver(GuiGraphicsExtractor graphics, final String str, final int x, final int y, final int[] rect) {
        if (inRect(x, y, rect)) {
            graphics.setTooltipForNextFrame(Minecraft.getInstance().font, Component.literal(str), getLeftPos() + x, getTopPos() + y);
        }
    }

    private int[] getBoxRect(final int i) {
        return new int[]{20, 22 + i * 20, 16, 16};
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean isDoubleClick) {
        super.mouseClicked(event, isDoubleClick);
        double mouseX = event.x() - getLeftPos();
        double mouseY = event.y() - getTopPos();
        for (int i = 0; i < activator.getOptions().size(); ++i) {
            final int[] box = getBoxRect(i);
            if (inRect((int) mouseX, (int) mouseY, box)) {
                byte data = (byte) ((event.button() != 0) ? 1 : 0);
                data |= (byte) (i << 1);
                activator.getOptions().get(i).changeOption(event.button() == 0);
                StevesCartsClient.sendToServer(new PacketActivator(activator.getBlockPos(), 0, new byte[]{data}));
            }
        }
        return true;
    }
}
