package vswe.stevescarts.client.guis;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;
import vswe.stevescarts.blocks.tileentities.TileEntityUpgrade;
import vswe.stevescarts.containers.ContainerUpgrade;
import vswe.stevescarts.helpers.ResourceHelper;
import vswe.stevescarts.upgrades.InterfaceEffect;
import vswe.stevescarts.upgrades.InventoryEffect;

public class GuiUpgrade extends AbstractContainerScreen<ContainerUpgrade>
{
    private static ResourceLocation texture;
    private final TileEntityUpgrade upgrade;

    public GuiUpgrade(ContainerUpgrade containerUpgrade, Inventory playerInventory, Component iTextComponent)
    {
        super(containerUpgrade, playerInventory, iTextComponent);
        this.upgrade = containerUpgrade.getUpgrade();
        imageWidth = 256;
        imageHeight = 190;
    }

    @Override
    public void render(@NotNull PoseStack poseStack, int mouseX, int mouseY, float p_230430_4_)
    {
        this.renderBackground(poseStack);
        super.render(poseStack, mouseX, mouseY, p_230430_4_);
        if (upgrade.getUpgrade() != null)
        {
            final InterfaceEffect gui = upgrade.getUpgrade().getInterfaceEffect();
            if (gui != null)
            {
                gui.drawForeground(upgrade, this);
                gui.drawMouseOver(poseStack, upgrade, this, mouseX, mouseY);
            }
        }
        renderTooltip(poseStack, mouseX, mouseY);
    }

    public boolean inRect(final int x, final int y, final int[] coords)
    {
        return coords != null && x >= coords[0] && x < coords[0] + coords[2] && y >= coords[1] && y < coords[1] + coords[3];
    }

    @Override
    protected void renderBg(@NotNull PoseStack poseStack, float p_230450_2_, int mouseX, int mouseY)
    {
        final int j = getGuiLeft();
        final int k = getGuiTop();
        ResourceHelper.bindResource(GuiUpgrade.texture);
        blit(poseStack, j, k, 0, 0, imageWidth, imageHeight);
        if (upgrade.getUpgrade() != null)
        {
            final InventoryEffect inventory = upgrade.getUpgrade().getInventoryEffect();
            if (inventory != null)
            {
                for (int i = 0; i < inventory.getInventorySize(); ++i)
                {
                    blit(poseStack, j + inventory.getSlotX(i) - 1, k + inventory.getSlotY(i) - 1, 0, imageHeight, 18, 18);
                }
            }
            final InterfaceEffect gui = upgrade.getUpgrade().getInterfaceEffect();
            if (gui != null)
            {
                gui.drawBackground(poseStack, upgrade, this, mouseX, mouseY);
            }
        }
    }

    @Override
    protected void renderLabels(@NotNull PoseStack poseStack, int p_230451_2_, int p_230451_3_)
    {
        font.draw(poseStack, upgrade.getUpgrade().getName(), 8, 6, 4210752);
    }

    static
    {
        GuiUpgrade.texture = ResourceHelper.getResource("/gui/upgrade.png");
    }
}
