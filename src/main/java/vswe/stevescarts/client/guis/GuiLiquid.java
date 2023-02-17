package vswe.stevescarts.client.guis;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import vswe.stevescarts.blocks.tileentities.TileEntityLiquid;
import vswe.stevescarts.containers.ContainerLiquid;
import vswe.stevescarts.helpers.Localization;
import vswe.stevescarts.helpers.ResourceHelper;
import vswe.stevescarts.init.ModBlocks;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class GuiLiquid extends ContainerScreen<ContainerLiquid>
{
    private static ResourceLocation texture;
    private static ResourceLocation textureExtra;
    private ContainerLiquid containerLiquid;

    public GuiLiquid(ContainerLiquid containerLiquid, PlayerInventory playerInventory, ITextComponent iTextComponent)
    {
        super(containerLiquid, playerInventory, iTextComponent);
        this.containerLiquid = containerLiquid;
        imageWidth = 230;
        imageHeight = 222;
    }

    @Override
    protected void renderBg(MatrixStack matrixStack, float p_230450_2_, int mouseX, int mouseY)
    {
        ResourceHelper.bindResource(GuiLiquid.texture);
        blit(matrixStack, leftPos, topPos, 0, 0, imageWidth, imageHeight);
        if (getLiquid().getTanks() != null)
        {
            for (int i = 0; i < 4; ++i)
            {
                final int[] coords = getTankCoords(i);
                getLiquid().getTanks()[i].drawFluid(matrixStack, this, getGuiLeft() + coords[0], getGuiTop() + coords[1]);
            }
        }
        ResourceHelper.bindResource(GuiLiquid.textureExtra);
        int version;
        if (containerLiquid.getLayoutType() == 0)
        {
            version = 0;
        }
        else
        {
            version = 1;
        }
        for (int j = 0; j < 2; ++j)
        {
            blit(matrixStack, leftPos + ((j == 0) ? 27 : 171), topPos + 63, 0, 102 + version * 12, 32, 12);
        }
        for (int j = 0; j < 4; ++j)
        {
            final int[] coords2 = getTankCoords(j);
            final int type = j % 2;
            blit(matrixStack, leftPos + coords2[0], topPos + coords2[1], 0, 51 * type, 36, 51);
        }

        GlStateManager._color4f(1.0f, 1.0f, 1.0f, 1.0f);
        final int left = getGuiLeft();
        final int top = getGuiTop();
        for (int i = 0; i < 4; ++i)
        {
            drawArrow(matrixStack, i, left, top);
            final int color = containerLiquid.getColor()[i] - 1;
            if (color != 4)
            {
                drawColors(matrixStack, i, color, left, top);
            }
        }
        final ItemRenderer renderitem = Minecraft.getInstance().getItemRenderer();
        final int[] coords = getMiddleCoords();
        renderitem.renderGuiItem(new ItemStack(getBlock(), 1), left + coords[0], top + coords[1]);
        for (int j = 0; j < 4; ++j)
        {
            drawItems(j, renderitem, left, top);
        }
        GlStateManager._color4f(1.0f, 1.0f, 1.0f, 1.0f);
    }

    @Override
    protected void renderLabels(MatrixStack p_230451_1_, int p_230451_2_, int p_230451_3_)
    {
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float p_230430_4_)
    {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, p_230430_4_);
        renderTooltip(matrixStack, mouseX, mouseY);
        int[] coords = getMiddleCoords();

        font.draw(matrixStack, getManagerName(), leftPos + coords[0] - 34, topPos + 4, 4210752);
        font.draw(matrixStack, Localization.GUI.MANAGER.TITLE.translate(), leftPos + coords[0] + coords[2], topPos + 4, 4210752);
        for (int i = 0; i < 4; ++i)
        {
            coords = getTextCoords(i);
            final String str = getMaxSizeText(i);
            font.draw(matrixStack, str, leftPos + coords[0], topPos + coords[1], 4210752);
        }
        for (int i = 0; i < 4; ++i)
        {
            try {
                drawExtraOverlay(matrixStack, i, mouseX, mouseY);
                drawMouseOver(matrixStack, Localization.GUI.MANAGER.CHANGE_TRANSFER_DIRECTION.translate() + ": " + Localization.GUI.MANAGER.CURRENT_SETTING.translate() + ": " + (containerLiquid.toCart()[i] ? Localization.GUI.MANAGER.DIRECTION_TO_CART.translate() : Localization.GUI.MANAGER.DIRECTION_FROM_CART.translate()), mouseX, mouseY, getArrowCoords(i));
                drawMouseOver(matrixStack, Localization.GUI.MANAGER.CHANGE_TURN_BACK_SETTING.translate() + "\n" + Localization.GUI.MANAGER.CURRENT_SETTING.translate() + ": " + ((containerLiquid.getColor()[i] == 5) ? Localization.GUI.MANAGER.TURN_BACK_NOT_SELECTED.translate() : (containerLiquid.doReturn()[containerLiquid.getColor()[i] - 1] ? Localization.GUI.MANAGER.TURN_BACK_DO.translate() : Localization.GUI.MANAGER.TURN_BACK_DO_NOT.translate())), mouseX, mouseY, getReturnCoords(i));
                drawMouseOver(matrixStack, Localization.GUI.MANAGER.CHANGE_TRANSFER_SIZE.translate() + ": " + Localization.GUI.MANAGER.CURRENT_SETTING.translate() + ": " + getMaxSizeOverlay(i), mouseX, mouseY, getTextCoords(i));
                drawMouseOver(matrixStack, Localization.GUI.MANAGER.CHANGE_SIDE.translate() + " " + Localization.GUI.MANAGER.CURRENT_SIDE.translate() + ": " + (new String[]{Localization.GUI.MANAGER.SIDE_RED.translate(), Localization.GUI.MANAGER.SIDE_BLUE.translate(), Localization.GUI.MANAGER.SIDE_YELLOW.translate(), Localization.GUI.MANAGER.SIDE_GREEN.translate(), Localization.GUI.MANAGER.SIDE_DISABLED.translate()})[containerLiquid.getColor()[i] - 1], mouseX, mouseY, getColorpickerCoords(i));
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        drawMouseOver(matrixStack, getLayoutString() + "\n" + Localization.GUI.MANAGER.CURRENT_SETTING.translate() + ": " + getLayoutOption(containerLiquid.getLayoutType()), mouseX, mouseY, getMiddleCoords());
    }

    public void drawMouseOver(MatrixStack matrixStack, final String str, final int x, final int y, final int[] rect)
    {
        if (inRect(x - getGuiLeft(), y - getGuiTop(), rect))
        {
            List<ITextComponent> toolTip = Arrays.stream(str.split("\n")).map(StringTextComponent::new).collect(Collectors.toList());
            renderComponentTooltip(matrixStack, toolTip, x, y);
        }
    }

    public boolean inRect(final int x, final int y, final int[] coords)
    {
        return coords != null && x >= coords[0] && x < coords[0] + coords[2] && y >= coords[1] && y < coords[1] + coords[3];
    }

    protected int[] getMiddleCoords()
    {
        return new int[]{getCenterTargetX() + 45, 61, 20, 20};
    }

    protected String getManagerName()
    {
        return Localization.GUI.LIQUID.TITLE.translate();
    }

    protected int[] getTextCoords(final int id)
    {
        final int[] coords = getBoxCoords(id);
        final int xCoord = coords[0];
        int yCoord = coords[1];
        if (id >= 2)
        {
            yCoord -= 12;
        }
        else
        {
            yCoord += 20;
        }
        return new int[]{xCoord, yCoord, 20, 10};
    }

    protected int[] getBoxCoords(final int id)
    {
        final int x = id % 2;
        final int y = id / 2;
        final int xCoord = getCenterTargetX() + 4 + x * 82;
        int yCoord = 17 + y * 88;
        yCoord += offsetObjectY(containerLiquid.getLayoutType(), x, y);
        return new int[]{xCoord, yCoord, 20, 20};
    }

    protected int[] getColorpickerCoords(final int id)
    {
        final int x = id % 2;
        final int y = id / 2;
        final int xCoord = getCenterTargetX() + 3 + x * 92;
        int yCoord = 49 + y * 32;
        yCoord += offsetObjectY(containerLiquid.getLayoutType(), x, y);
        return new int[]{xCoord, yCoord, 8, 8};
    }

    protected int[] getReturnCoords(final int id)
    {
        final int x = id % 2;
        final int y = id / 2;
        final int xCoord = getCenterTargetX() + 14 + x * 70;
        int yCoord = 49 + y * 32;
        yCoord += offsetObjectY(containerLiquid.getLayoutType(), x, y);
        return new int[]{xCoord, yCoord, 8, 8};
    }

    private void drawArrow(MatrixStack matrixStack, final int id, final int left, final int top)
    {
        int sourceX = getArrowSourceX();
        int sourceY = 28;
        sourceY += 56 * id;
        if (!containerLiquid.toCart()[id])
        {
            sourceX += 28;
        }
        final int targetX = getArrowCoords(id)[0];
        final int targetY = getArrowCoords(id)[1];
        int sizeX = 28;
        int sizeY = 28;
        blit(matrixStack, left + targetX, top + targetY, sourceX, sourceY, sizeX, sizeY);
        if (id == getLiquid().getLastSetting() && containerLiquid.getColor()[id] != 5)
        {
            sourceY -= 28;
            int scaledProgress = getLiquid().moveProgressScaled(42);
            int offsetX = 0;
            int offsetY = 0;
            if (containerLiquid.toCart()[id])
            {
                sizeX = 14;
                if (id % 2 == 0)
                {
                    offsetX = 14;
                }
                sizeY = scaledProgress;
                if (sizeY > 19)
                {
                    sizeY = 19;
                }
                if (id < 2)
                {
                    offsetY = 28 - sizeY;
                }
            }
            else
            {
                sizeY = 14;
                if (id >= 2)
                {
                    offsetY = 14;
                }
                sizeX = scaledProgress;
                if (sizeX > 19)
                {
                    sizeX = 19;
                }
                if (id % 2 == 1)
                {
                    offsetX = 28 - sizeX;
                }
            }
            blit(matrixStack, left + targetX + offsetX, top + targetY + offsetY, sourceX + offsetX, sourceY + offsetY, sizeX, sizeY);
            offsetY = (offsetX = 0);
            sizeY = (sizeX = 28);
            if (scaledProgress > 19)
            {
                scaledProgress -= 19;
                if (containerLiquid.toCart()[id])
                {
                    sizeX = scaledProgress;
                    if (sizeX > 23)
                    {
                        sizeX = 23;
                    }
                    if (id % 2 == 0)
                    {
                        offsetX = 22 - sizeX;
                    }
                    else
                    {
                        offsetX = 6;
                    }
                }
                else
                {
                    sizeY = scaledProgress;
                    if (sizeY > 23)
                    {
                        sizeY = 23;
                    }
                    if (id >= 2)
                    {
                        offsetY = 22 - sizeY;
                    }
                    else
                    {
                        offsetY = 6;
                    }
                }
                blit(matrixStack, left + targetX + offsetX, top + targetY + offsetY, sourceX + offsetX, sourceY + offsetY, sizeX, sizeY);
            }
        }
    }

    protected void drawItems(final int id, final ItemRenderer renderitem, final int left, final int top)
    {
        //		ItemStack cartIcon;
        //		if (containerLiquid.getTarget()[id] < 0 || containerCargo.getTarget()[id] >= TileEntityCargo.itemSelections.size()
        //				|| TileEntityCargo.itemSelections.get(containerCargo.getTarget()[id]).getIcon().isEmpty())
        //		{
        //			cartIcon = new ItemStack(Items.MINECART, 1);
        //		} else {
        //			cartIcon = TileEntityCargo.itemSelections.get(containerCargo.getTarget()[id]).getIcon();
        //		}
        //
        //		final int[] coords = getBoxCoords(id);
        //		renderitem.renderGuiItem(cartIcon, left + coords[0], top + coords[1]);
    }


    protected String getMaxSizeOverlay(final int id)
    {
        return Localization.GUI.LIQUID.TRANSFER_BUCKETS.translate(getMaxSizeText(id));
    }

    protected String getMaxSizeText(final int id)
    {
        return Localization.GUI.LIQUID.TRANSFER_BUCKET_SHORT.translate();
    }

    protected int getArrowSourceX()
    {
        return 72;
    }

    protected int getColorSourceX()
    {
        return 128;
    }

    protected int getCenterTargetX()
    {
        return 62;
    }

    protected int[] getArrowCoords(final int id)
    {
        final int x = id % 2;
        final int y = id / 2;
        final int xCoord = getCenterTargetX() + 25 + x * 28;
        int yCoord = 17 + y * 76;
        yCoord += offsetObjectY(containerLiquid.getLayoutType(), x, y);
        return new int[]{xCoord, yCoord, 28, 28};
    }

    protected void drawColors(MatrixStack matrixStack, int id, final int color, final int left, final int top)
    {
        try
        {
            int[] coords = getReturnCoords(id);
            blit(matrixStack, left + coords[0], top + coords[1], getColorSourceX() + (containerLiquid.doReturn()[containerLiquid.getColor()[id] - 1] ? 8 : 0), 80 + 8 * color, 8, 8);
            coords = getBoxCoords(id);
            blit(matrixStack, left + coords[0] - 2, top + coords[1] - 2, getColorSourceX(), 20 * color, 20, 20);
            if (containerLiquid.getLayoutType() == 2)
            {
                final int[] coords2 = getTankCoords(id);
                blit(matrixStack, left + coords2[0], top + coords2[1], 36, 51 * color, 36, 51);
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    protected int offsetObjectY(final int layout, final int x, final int y)
    {
        return -5 + y * 10;
    }

    protected void drawExtraOverlay(MatrixStack matrixStack, int id, final int x, final int y)
    {
        drawMouseOver(matrixStack, getLiquid().getTanks()[id].getMouseOver(), x, y, getTankCoords(id));
    }

    protected Block getBlock()
    {
        return ModBlocks.LIQUID_MANAGER.get();
    }

    private int[] getTankCoords(final int id)
    {
        final int x = id % 2;
        final int y = id / 2;
        final int xCoord = 25 + x * 144;
        final int yCoord = 12 + y * 63;
        return new int[]{xCoord, yCoord, 36, 51};
    }

    private TileEntityLiquid getLiquid()
    {
        return containerLiquid.getTileEntityLiquid();
    }

    protected String getLayoutString()
    {
        return Localization.GUI.LIQUID.CHANGE_LAYOUT.translate();
    }

    protected String getLayoutOption(final int id)
    {
        switch (id)
        {
            default:
            {
                return Localization.GUI.LIQUID.LAYOUT_ALL.translate();
            }
            case 1:
            {
                return Localization.GUI.LIQUID.LAYOUT_SIDE.translate();
            }
            case 2:
            {
                return Localization.GUI.LIQUID.LAYOUT_COLOR.translate();
            }
        }
    }

    protected boolean sendOnClick(final int id, final int x, final int y, final byte data)
    {
        if (inRect(x, y, getBoxCoords(id)))
        {
            getLiquid().sendPacket(1, data);
            return true;
        }
        return false;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button)
    {
        super.mouseClicked(mouseX, mouseY, button);
        int x = (int) mouseX;
        int y = (int) mouseY;
        x -= getGuiLeft();
        y -= getGuiTop();
        if (inRect(x, y, getMiddleCoords()))
        {
            getLiquid().sendPacket(5, (byte) ((button == 0) ? 1 : -1));
        }
        else
        {
            for (int i = 0; i < 4; ++i)
            {
                byte data = (byte) i;
                data |= (byte) (button << 2);
                if (inRect(x, y, getArrowCoords(i)))
                {
                    getLiquid().sendPacket(0, (byte) i);
                    break;
                }
                if (inRect(x, y, getTextCoords(i)))
                {
                    getLiquid().sendPacket(2, data);
                    break;
                }
                if (inRect(x, y, getColorpickerCoords(i)))
                {
                    getLiquid().sendPacket(3, data);
                    break;
                }
                if (inRect(x, y, getReturnCoords(i)))
                {
                    getLiquid().sendPacket(4, (byte) i);
                    break;
                }
                if (sendOnClick(i, x, y, data))
                {
                    break;
                }
            }
        }
        return true;
    }

    static
    {
        GuiLiquid.texture = ResourceHelper.getResource("/gui/liquidmanager.png");
        GuiLiquid.textureExtra = ResourceHelper.getResource("/gui/liquidmanagerExtra.png");
    }
}
