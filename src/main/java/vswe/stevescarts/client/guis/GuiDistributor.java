package vswe.stevescarts.client.guis;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;
import vswe.stevescarts.blocks.tileentities.TileEntityDistributor;
import vswe.stevescarts.blocks.tileentities.TileEntityManager;
import vswe.stevescarts.containers.ContainerDistributor;
import vswe.stevescarts.helpers.DistributorSetting;
import vswe.stevescarts.helpers.DistributorSide;
import vswe.stevescarts.helpers.Localization;
import vswe.stevescarts.helpers.ResourceHelper;

import java.util.ArrayList;

public class GuiDistributor extends AbstractContainerScreen<ContainerDistributor>
{
    private String mouseOverText;
    private static ResourceLocation texture;
    private int activeId;
    private final TileEntityDistributor distributor;

    public GuiDistributor(ContainerDistributor containerDistributor, Inventory playerInventory, Component iTextComponent)
    {
        super(containerDistributor, playerInventory, iTextComponent);
        activeId = -1;
        imageWidth = 255;
        imageHeight = 186;
        this.distributor = containerDistributor.getDistributor();
    }

    @Override
    protected void renderBg(@NotNull PoseStack poseStack, float p_230450_2_, int x, int y)
    {
        final int j = getGuiLeft();
        final int k = getGuiTop();
        ResourceHelper.bindResource(GuiDistributor.texture);
        blit(poseStack, j, k, 0, 0, imageWidth, imageHeight);
        x -= getGuiLeft();
        y -= getGuiTop();
        final TileEntityManager[] invs = distributor.getInventories();
        final ArrayList<DistributorSide> sides = distributor.getSides();
        int id = 0;
        for (final DistributorSide side : sides)
        {
            if (side.isEnabled(distributor))
            {
                final int[] box = getSideBoxRect(id);
                int srcX = 0;
                if (inRect(x, y, box))
                {
                    srcX = box[2];
                }
                blit(poseStack, j + box[0], k + box[1], srcX, imageHeight, box[2], box[3]);
                blit(poseStack, j + box[0] + 2, k + box[1] + 2, box[2] * 2 + (box[2] - 4) * side.getId(), imageHeight, box[2] - 4, box[3] - 4);
                drawMouseMover(Localization.GUI.DISTRIBUTOR.SIDE.translate(side.getName()) + ((activeId != -1) ? (": [" + Localization.GUI.DISTRIBUTOR.DROP_INSTRUCTION.translate() + "]") : ""), x, y, box);
                int settingCount = 0;
                for (final DistributorSetting setting : DistributorSetting.settings)
                {
                    if (setting.isEnabled(distributor) && side.isSet(setting.getId()))
                    {
                        final int[] settingbox = getActiveSettingBoxRect(id, settingCount++);
                        drawSetting(poseStack, setting, settingbox, inRect(x, y, settingbox));
                        drawMouseMover(setting.getName(invs) + ": [" + Localization.GUI.DISTRIBUTOR.REMOVE_INSTRUCTION.translate() + "]", x, y, settingbox);
                    }
                }
                ++id;
            }
        }
        for (final DistributorSetting setting2 : DistributorSetting.settings)
        {
            if (setting2.isEnabled(distributor))
            {
                final int[] box = getSettingBoxRect(setting2.getImageId(), setting2.getIsTop());
                drawSetting(poseStack, setting2, box, inRect(x, y, box));
                drawMouseMover(setting2.getName(invs), x, y, box);
            }
        }
        if (activeId != -1)
        {
            final DistributorSetting setting3 = DistributorSetting.settings.get(activeId);
            drawSetting(poseStack, setting3, new int[]{x - 8, y - 8, 16, 16}, true);
        }
    }

    @Override
    protected void renderLabels(@NotNull PoseStack poseStack, int p_230451_2_, int p_230451_3_)
    {
    }

    @Override
    public void render(@NotNull PoseStack poseStack, int x, int y, float p_230430_4_)
    {
        this.renderBackground(poseStack);
        super.render(poseStack, x, y, p_230430_4_);
        font.draw(poseStack, Localization.GUI.DISTRIBUTOR.TITLE.translate(), leftPos + 8, topPos + 6, 4210752);
        final TileEntityManager[] invs = distributor.getInventories();
        if (invs.length == 0)
        {
            font.draw(poseStack, Localization.GUI.DISTRIBUTOR.NOT_CONNECTED.translate(), leftPos + 30, topPos + 40, 16728128);
        }
        if (mouseOverText != null && !mouseOverText.equals(""))
        {
            renderTooltip(poseStack, Component.literal(mouseOverText), x, y);
        }
        mouseOverText = null;
    }


    private void drawMouseMover(final String str, final int x, final int y, final int[] rect)
    {
        if (inRect(x, y, rect))
        {
            mouseOverText = str;
        }
    }

    public boolean inRect(final int x, final int y, final int[] coords)
    {
        return coords != null && x >= coords[0] && x < coords[0] + coords[2] && y >= coords[1] && y < coords[1] + coords[3];
    }

    private void drawSetting(PoseStack matrixStack, final DistributorSetting setting, final int[] box, final boolean hover)
    {
        final int j = getGuiLeft();
        final int k = getGuiTop();
        int srcX = 0;
        if (!setting.getIsTop())
        {
            srcX += box[2] * 2;
        }
        if (hover)
        {
            srcX += box[2];
        }
        blit(matrixStack, j + box[0], k + box[1], srcX, imageHeight + getSideBoxRect(0)[3], box[2], box[3]);
        blit(matrixStack, j + box[0] + 1, k + box[1] + 1, box[2] * 4 + (box[2] - 2) * setting.getImageId(), imageHeight + getSideBoxRect(0)[3], box[2] - 2, box[3] - 2);
    }

    private int[] getSideBoxRect(final int i)
    {
        return new int[]{20, 18 + i * 24, 22, 22};
    }

    private int[] getSettingBoxRect(final int i, final boolean topRow)
    {
        return new int[]{20 + i * 18, 143 + (topRow ? 0 : 18), 16, 16};
    }

    private int[] getActiveSettingBoxRect(final int side, final int setting)
    {
        final int[] sideCoords = getSideBoxRect(side);
        return new int[]{sideCoords[0] + sideCoords[2] + 5 + setting * 18, sideCoords[1] + (sideCoords[3] - 16) / 2, 16, 16};
    }

    @Override
    public boolean mouseClicked(double x, double y, int button)
    {
        x -= getGuiLeft();
        y -= getGuiTop();
        if (button == 0)
        {
            for (final DistributorSetting setting : DistributorSetting.settings)
            {
                if (setting.isEnabled(distributor))
                {
                    final int[] box = getSettingBoxRect(setting.getImageId(), setting.getIsTop());
                    if (!inRect((int) x, (int) y, box))
                    {
                        continue;
                    }
                    activeId = setting.getId();
                }
            }
            if (activeId != -1)
            {
                int id = 0;
                for (final DistributorSide side : distributor.getSides())
                {
                    if (side.isEnabled(distributor))
                    {
                        final int[] box = getSideBoxRect(id++);
                        if (inRect((int) x, (int) y, box))
                        {
                            //This is client-side and will need removing
                            distributor.getSides().get(side.getId()).set(activeId);
                            distributor.sendPacket(0, new byte[]{(byte) activeId, (byte) side.getId()});
                            //Remove from cursor
                            activeId = -1;
                            break;
                        }
                    }
                }
            }
        }
        else if (button == 1)
        {
            int id = 0;
            for (final DistributorSide side : distributor.getSides())
            {
                if (side.isEnabled(distributor))
                {
                    int settingCount = 0;
                    for (final DistributorSetting setting : DistributorSetting.settings)
                    {
                        if (setting.isEnabled(distributor) && side.isSet(setting.getId()))
                        {
                            final int[] settingbox = getActiveSettingBoxRect(id, settingCount++);
                            if (!inRect((int) x, (int) y, settingbox))
                            {
                                continue;
                            }
                            //This is client-side and will need removing
                            distributor.getSides().get(side.getId()).reset(setting.getId());
                            distributor.sendPacket(1, new byte[]{(byte) setting.getId(), (byte) side.getId()});
                            //Remove from cursor
                            activeId = -1;
                            break;
                        }
                    }
                    ++id;
                }
            }
        }
        return super.mouseClicked(x, y, button);
    }

    static
    {
        GuiDistributor.texture = ResourceHelper.getResource("/gui/distributor.png");
    }
}
