package vswe.stevescarts.client.guis;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import vswe.stevescarts.blocks.tileentities.TileEntityUpgrade;
import vswe.stevescarts.containers.ContainerUpgrade;
import vswe.stevescarts.helpers.ResourceHelper;
import vswe.stevescarts.upgrades.InterfaceEffect;
import vswe.stevescarts.upgrades.InventoryEffect;

public class GuiUpgrade extends AbstractContainerScreen<ContainerUpgrade>
{
    private static ResourceLocation texture;
    private TileEntityUpgrade upgrade;

    public GuiUpgrade(ContainerUpgrade containerUpgrade, Inventory playerInventory, Component iTextComponent)
    {
        super(containerUpgrade, playerInventory, iTextComponent);
        this.upgrade = containerUpgrade.getUpgrade();
        imageWidth = 256;
        imageHeight = 190;
    }

    @Override
    public void render(PoseStack matrixStack, int mouseX, int mouseY, float p_230430_4_)
    {
        this.renderBackground(matrixStack);
        if (upgrade.getUpgrade() != null)
        {
            final InterfaceEffect gui = upgrade.getUpgrade().getInterfaceEffect();
            if (gui != null)
            {
                gui.drawForeground(upgrade, this);
                gui.drawMouseOver(matrixStack, upgrade, this, mouseX, mouseY);
            }
        }
        super.render(matrixStack, mouseX, mouseY, p_230430_4_);
        renderTooltip(matrixStack, mouseX, mouseY);
    }

    public boolean inRect(final int x, final int y, final int[] coords)
    {
        return coords != null && x >= coords[0] && x < coords[0] + coords[2] && y >= coords[1] && y < coords[1] + coords[3];
    }

    @Override
    protected void renderBg(PoseStack matrixStack, float p_230450_2_, int mouseX, int mouseY)
    {
        final int j = getGuiLeft();
        final int k = getGuiTop();
        ResourceHelper.bindResource(GuiUpgrade.texture);
        blit(matrixStack, j, k, 0, 0, imageWidth, imageHeight);
        if (upgrade.getUpgrade() != null)
        {
            final InventoryEffect inventory = upgrade.getUpgrade().getInventoryEffect();
            if (inventory != null)
            {
                for (int i = 0; i < inventory.getInventorySize(); ++i)
                {
                    blit(matrixStack, j + inventory.getSlotX(i) - 1, k + inventory.getSlotY(i) - 1, 0, imageHeight, 18, 18);
                }
            }
            final InterfaceEffect gui = upgrade.getUpgrade().getInterfaceEffect();
            if (gui != null)
            {
                gui.drawBackground(matrixStack, upgrade, this, mouseX, mouseY);
            }
        }
    }

    @Override
    protected void renderLabels(PoseStack matrixStack, int p_230451_2_, int p_230451_3_)
    {
        font.draw(matrixStack, upgrade.getUpgrade().getName(), 8, 6, 4210752);
    }

    static
    {
        GuiUpgrade.texture = ResourceHelper.getResource("/gui/upgrade.png");
    }
}
