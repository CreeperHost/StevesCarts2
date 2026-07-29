package vswe.stevescarts.client.guis;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import org.jspecify.annotations.NonNull;
import vswe.stevescarts.blocks.tileentities.TileEntityUpgrade;
import vswe.stevescarts.containers.ContainerUpgrade;
import vswe.stevescarts.helpers.ResourceHelper;
import vswe.stevescarts.upgrades.InterfaceUpgradeEffect;
import vswe.stevescarts.upgrades.InventoryUpgradeEffect;

public class GuiUpgrade extends AbstractContainerScreen<ContainerUpgrade> {
    private static Identifier texture;

    static {
        GuiUpgrade.texture = ResourceHelper.getResource("/gui/upgrade.png");
    }

    private final TileEntityUpgrade upgrade;

    public GuiUpgrade(ContainerUpgrade containerUpgrade, Inventory playerInventory, Component iTextComponent) {
        super(containerUpgrade, playerInventory, iTextComponent, 256, 190);
        this.upgrade = containerUpgrade.getUpgrade();
    }

    @Override
    public void extractContents(@NonNull GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float p_230430_4_) {
        this.extractBackground(guiGraphics, mouseX, mouseY, p_230430_4_);
        super.extractContents(guiGraphics, mouseX, mouseY, p_230430_4_);
        if (upgrade.getUpgrade() != null) {
            final InterfaceUpgradeEffect gui = upgrade.getUpgrade().getInterfaceEffect();
            if (gui != null) {
                gui.drawForeground(upgrade, this);
                gui.drawMouseOver(guiGraphics, upgrade, this, mouseX, mouseY);
            }
        }
        extractTooltip(guiGraphics, mouseX, mouseY);
    }

    public boolean inRect(final int x, final int y, final int[] coords) {
        return coords != null && x >= coords[0] && x < coords[0] + coords[2] && y >= coords[1] && y < coords[1] + coords[3];
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTicks) {
        final int j = getLeftPos();
        final int k = getTopPos();
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, GuiUpgrade.texture, j, k, 0, 0, imageWidth, imageHeight, 256, 256);
        if (upgrade.getUpgrade() != null) {
            final InventoryUpgradeEffect inventory = upgrade.getUpgrade().getInventoryEffect();
            if (inventory != null) {
                for (int i = 0; i < inventory.getInventorySize(); ++i) {
                    guiGraphics.blit(RenderPipelines.GUI_TEXTURED, GuiUpgrade.texture, j + inventory.getSlotX(i) - 1, k + inventory.getSlotY(i) - 1, 0, imageHeight, 18, 18, 256, 256);
                }
            }
            final InterfaceUpgradeEffect gui = upgrade.getUpgrade().getInterfaceEffect();
            if (gui != null) {
                gui.drawBackground(guiGraphics, upgrade, this, mouseX, mouseY);
            }
        }
    }

    @Override
    protected void extractLabels(GuiGraphicsExtractor graphics, int xm, int ym) {
        graphics.text(Minecraft.getInstance().font, Component.translatable(upgrade.getUpgrade().getName()), 8, 6, 0xFFffffff);
    }
}
