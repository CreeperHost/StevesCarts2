package vswe.stevescarts.client.guis;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import vswe.stevescarts.blocks.tileentities.TileEntityLiquid;
import vswe.stevescarts.containers.ContainerLiquid;
import vswe.stevescarts.helpers.ResourceHelper;
import vswe.stevescarts.init.ModBlocks;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class GuiLiquid extends AbstractContainerScreen<ContainerLiquid> {
    private static final int CHANNEL_COUNT = 4;
    private static final int DISABLED_COLOR = 5;
    private static Identifier texture;
    private static Identifier textureExtra;

    static {
        GuiLiquid.texture = ResourceHelper.getResource("/gui/liquidmanager.png");
        GuiLiquid.textureExtra = ResourceHelper.getResource("/gui/liquidmanagerExtra.png");
    }

    private final ContainerLiquid containerLiquid;

    public GuiLiquid(ContainerLiquid containerLiquid, Inventory playerInventory, Component iTextComponent) {
        super(containerLiquid, playerInventory, iTextComponent, 230, 222);
        this.containerLiquid = containerLiquid;
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTicks) {
        graphics.blit(RenderPipelines.GUI_TEXTURED, GuiLiquid.texture, leftPos, topPos, 0, 0, imageWidth, imageHeight, 256, 256);
        var tanks = getLiquid().getTanks();
        if (tanks != null) {
            for (int i = 0; i < Math.min(CHANNEL_COUNT, tanks.length); ++i) {
                if (tanks[i] == null) {
                    continue;
                }
                final int[] coords = getTankCoords(i);
                tanks[i].drawFluid(graphics, leftPos, topPos, leftPos + coords[0], topPos + coords[1]);
            }
        }
        final int left = leftPos;
        final int top = topPos;
        for (int i = 0; i < CHANNEL_COUNT; ++i) {
            drawArrow(graphics, i, left, top);
            final int color = containerLiquid.getColor(i);
            if (isActiveColor(color)) {
                drawColors(graphics, i, color, left, top);
            }
        }
        final int[] coords = getMiddleCoords();
        graphics.item(new ItemStack(getBlock(), 1), left + coords[0], top + coords[1]);
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float p_230430_4_) {
        super.extractRenderState(graphics, mouseX, mouseY, p_230430_4_);
        int[] coords = getMiddleCoords();

        graphics.text(Minecraft.getInstance().font, getManagerName(), leftPos + coords[0] - 34, topPos + 4, 0xFFffffff);
        graphics.text(Minecraft.getInstance().font, Component.translatable("gui.stevescarts.manager").getString(), leftPos + coords[0] + coords[2], topPos + 4, 0xFFffffff);
        for (int i = 0; i < CHANNEL_COUNT; ++i) {
            coords = getTextCoords(i);
            final String str = getMaxSizeText(i);
            graphics.text(Minecraft.getInstance().font, str, leftPos + coords[0], topPos + coords[1], 0xFFffffff);
        }
        for (int i = 0; i < CHANNEL_COUNT; ++i) {
            drawExtraOverlay(graphics, i, mouseX, mouseY);
            drawMouseOver(graphics, Component.translatable("gui.stevescarts.changeTransferDirection").getString() + ": " + Component.translatable("gui.stevescarts.currentSetting").getString() + ": " + (containerLiquid.isToCart(i) ? Component.translatable("gui.stevescarts.directionToCart").getString() : Component.translatable("gui.stevescarts.directionFromCart").getString()), mouseX, mouseY, getArrowCoords(i));
            drawMouseOver(graphics, Component.translatable("gui.stevescarts.changeTransferSize").getString() + ": " + Component.translatable("gui.stevescarts.currentSetting").getString() + ": " + getMaxSizeOverlay(i), mouseX, mouseY, getTextCoords(i));

            final int color = containerLiquid.getColor(i);
            if (!isValidColor(color)) {
                continue;
            }

            final String returnSetting = color == DISABLED_COLOR
                    ? Component.translatable("gui.stevescarts.turnBackDisabled").getString()
                    : containerLiquid.isReturning(color)
                    ? Component.translatable("gui.stevescarts.turnBack").getString()
                    : Component.translatable("gui.stevescarts.continueForward").getString();
            drawMouseOver(graphics, Component.translatable("gui.stevescarts.changeTurnBack").getString() + "\n" + Component.translatable("gui.stevescarts.currentSetting").getString() + ": " + returnSetting, mouseX, mouseY, getReturnCoords(i));

            final String[] colorNames = new String[]{Component.translatable("gui.stevescarts.sideRed").getString(), Component.translatable("gui.stevescarts.sideBlue").getString(), Component.translatable("gui.stevescarts.sideYellow").getString(), Component.translatable("gui.stevescarts.sideGreen").getString(), Component.translatable("gui.stevescarts.sideDisabled").getString()};
            drawMouseOver(graphics, Component.translatable("gui.stevescarts.changeSide").getString() + ": " + Component.translatable("gui.stevescarts.currentSide").getString() + ": " + colorNames[color - 1], mouseX, mouseY, getColorpickerCoords(i));
        }
        drawMouseOver(graphics, getLayoutString() + "\n" + Component.translatable("gui.stevescarts.currentSetting").getString() + ": " + getLayoutOption(containerLiquid.getLayoutType()), mouseX, mouseY, getMiddleCoords());
    }

    @Override
    protected void extractLabels(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
        //NO-OP
        graphics.text(font, playerInventoryTitle, inventoryLabelX, inventoryLabelY, -12566464, false);
    }

    public void drawMouseOver(GuiGraphicsExtractor graphics, final String str, final int x, final int y, final int[] rect) {
        if (inRect(x - leftPos, y - topPos, rect)) {
            List<Component> toolTip = Arrays.stream(str.split("\n")).map(Component::literal).collect(Collectors.toList());
            graphics.setTooltipForNextFrame(Minecraft.getInstance().font, toolTip, Optional.empty(), x, y);
        }
    }

    public boolean inRect(final int x, final int y, final int[] coords) {
        return coords != null && x >= coords[0] && x < coords[0] + coords[2] && y >= coords[1] && y < coords[1] + coords[3];
    }

    protected int[] getMiddleCoords() {
        return new int[]{getCenterTargetX() + 45, 61, 20, 20};
    }

    protected String getManagerName() {
        return Component.translatable("gui.stevescarts.liquidManager").getString();
    }

    protected int[] getTextCoords(final int id) {
        final int[] coords = getBoxCoords(id);
        final int xCoord = coords[0];
        int yCoord = coords[1];
        if (id >= 2) {
            yCoord -= 12;
        } else {
            yCoord += 20;
        }
        return new int[]{xCoord, yCoord, 20, 10};
    }

    protected int[] getBoxCoords(final int id) {
        final int x = id % 2;
        final int y = id / 2;
        final int xCoord = getCenterTargetX() + 4 + x * 82;
        int yCoord = 17 + y * 88;
        yCoord += offsetObjectY(containerLiquid.getLayoutType(), x, y);
        return new int[]{xCoord, yCoord, 20, 20};
    }

    protected int[] getColorpickerCoords(final int id) {
        final int x = id % 2;
        final int y = id / 2;
        final int xCoord = getCenterTargetX() + 3 + x * 92;
        int yCoord = 49 + y * 32;
        yCoord += offsetObjectY(containerLiquid.getLayoutType(), x, y);
        return new int[]{xCoord, yCoord, 8, 8};
    }

    protected int[] getReturnCoords(final int id) {
        final int x = id % 2;
        final int y = id / 2;
        final int xCoord = getCenterTargetX() + 14 + x * 70;
        int yCoord = 49 + y * 32;
        yCoord += offsetObjectY(containerLiquid.getLayoutType(), x, y);
        return new int[]{xCoord, yCoord, 8, 8};
    }

    private void drawArrow(GuiGraphicsExtractor GuiGraphicsExtractor, final int id, final int left, final int top) {
        int sourceX = getArrowSourceX();
        int sourceY = 28;
        sourceY += 56 * id;
        final boolean toCart = containerLiquid.isToCart(id);
        if (!toCart) {
            sourceX += 28;
        }
        final int targetX = getArrowCoords(id)[0];
        final int targetY = getArrowCoords(id)[1];
        int sizeX = 28;
        int sizeY = 28;
        GuiGraphicsExtractor.blit(RenderPipelines.GUI_TEXTURED, GuiLiquid.textureExtra, left + targetX, top + targetY, sourceX, sourceY, sizeX, sizeY, 256, 256);
        if (id == getLiquid().getLastSetting() && isActiveColor(containerLiquid.getColor(id))) {
            sourceY -= 28;
            int scaledProgress = getLiquid().moveProgressScaled(42);
            int offsetX = 0;
            int offsetY = 0;
            if (toCart) {
                sizeX = 14;
                if (id % 2 == 0) {
                    offsetX = 14;
                }
                sizeY = scaledProgress;
                if (sizeY > 19) {
                    sizeY = 19;
                }
                if (id < 2) {
                    offsetY = 28 - sizeY;
                }
            } else {
                sizeY = 14;
                if (id >= 2) {
                    offsetY = 14;
                }
                sizeX = scaledProgress;
                if (sizeX > 19) {
                    sizeX = 19;
                }
                if (id % 2 == 1) {
                    offsetX = 28 - sizeX;
                }
            }
            GuiGraphicsExtractor.blit(RenderPipelines.GUI_TEXTURED, GuiLiquid.textureExtra, left + targetX + offsetX, top + targetY + offsetY, sourceX + offsetX, sourceY + offsetY, sizeX, sizeY, 256, 256);
            offsetY = (offsetX = 0);
            sizeY = (sizeX = 28);
            if (scaledProgress > 19) {
                scaledProgress -= 19;
                if (toCart) {
                    sizeX = scaledProgress;
                    if (sizeX > 23) {
                        sizeX = 23;
                    }
                    if (id % 2 == 0) {
                        offsetX = 22 - sizeX;
                    } else {
                        offsetX = 6;
                    }
                } else {
                    sizeY = scaledProgress;
                    if (sizeY > 23) {
                        sizeY = 23;
                    }
                    if (id >= 2) {
                        offsetY = 22 - sizeY;
                    } else {
                        offsetY = 6;
                    }
                }
                GuiGraphicsExtractor.blit(RenderPipelines.GUI_TEXTURED, GuiLiquid.textureExtra, left + targetX + offsetX, top + targetY + offsetY, sourceX + offsetX, sourceY + offsetY, sizeX, sizeY, 256, 256);
            }
        }
    }

    protected String getMaxSizeOverlay(final int id) {
        float buckets = containerLiquid.getMaxAmountBuckets(id);
        if (containerLiquid.getMaxAmount(id) == 0) {
            return Component.translatable("gui.stevescarts.transferAllLiquid").getString();
        }
        return Component.translatable("gui.stevescarts.transferBuckets", String.valueOf(buckets)).getString() + Component.translatable("gui.stevescarts.transferBucketShort").getString();
    }

    protected String getMaxSizeText(final int id) {
        float buckets = containerLiquid.getMaxAmountBuckets(id);
        if (containerLiquid.getMaxAmount(id) == 0) {
            return Component.translatable("gui.stevescarts.transferAllLiquidShort").getString();
        }
        return buckets + Component.translatable("gui.stevescarts.transferBucketShort").getString();
    }

    protected int getArrowSourceX() {
        return 72;
    }

    protected int getColorSourceX() {
        return 128;
    }

    protected int getCenterTargetX() {
        return 62;
    }

    protected int[] getArrowCoords(final int id) {
        final int x = id % 2;
        final int y = id / 2;
        final int xCoord = getCenterTargetX() + 25 + x * 28;
        int yCoord = 17 + y * 76;
        yCoord += offsetObjectY(containerLiquid.getLayoutType(), x, y);
        return new int[]{xCoord, yCoord, 28, 28};
    }

    protected void drawColors(GuiGraphicsExtractor GuiGraphicsExtractor, int id, final int color, final int left, final int top) {
        if (!isActiveColor(color)) {
            return;
        }

        final int colorIndex = color - 1;
        int[] coords = getReturnCoords(id);
        GuiGraphicsExtractor.blit(RenderPipelines.GUI_TEXTURED, GuiLiquid.textureExtra, left + coords[0], top + coords[1], getColorSourceX() + (containerLiquid.isReturning(color) ? 8 : 0), 80 + 8 * colorIndex, 8, 8, 256, 256);
        coords = getBoxCoords(id);
        GuiGraphicsExtractor.blit(RenderPipelines.GUI_TEXTURED, GuiLiquid.textureExtra, left + coords[0] - 2, top + coords[1] - 2, getColorSourceX(), 20 * colorIndex, 20, 20, 256, 256);
        if (containerLiquid.getLayoutType() == 2) {
            final int[] coords2 = getTankCoords(id);
            GuiGraphicsExtractor.blit(RenderPipelines.GUI_TEXTURED, GuiLiquid.textureExtra, left + coords2[0], top + coords2[1], 36, 51 * colorIndex, 36, 51, 256, 256);
        }
    }

    private boolean isActiveColor(int color) {
        return color >= 1 && color < DISABLED_COLOR;
    }

    private boolean isValidColor(int color) {
        return color >= 1 && color <= DISABLED_COLOR;
    }

    protected int offsetObjectY(final int layout, final int x, final int y) {
        return -5 + y * 10;
    }

    protected void drawExtraOverlay(GuiGraphicsExtractor GuiGraphicsExtractor, int id, final int x, final int y) {
        var tanks = getLiquid().getTanks();
        if (tanks != null && id >= 0 && id < tanks.length && tanks[id] != null) {
            drawMouseOver(GuiGraphicsExtractor, tanks[id].getMouseOver(), x, y, getTankCoords(id));
        }
    }

    protected Block getBlock() {
        return ModBlocks.LIQUID_MANAGER.get();
    }

    private int[] getTankCoords(final int id) {
        final int x = id % 2;
        final int y = id / 2;
        final int xCoord = 25 + x * 144;
        final int yCoord = 12 + y * 63;
        return new int[]{xCoord, yCoord, 36, 51};
    }

    private TileEntityLiquid getLiquid() {
        return containerLiquid.getTileEntityLiquid();
    }

    protected String getLayoutString() {
        return Component.translatable("gui.stevescarts.changeTankLayout").getString();
    }

    protected String getLayoutOption(final int id) {
        switch (id) {
            case 1 -> {
                return Component.translatable("gui.stevescarts.layoutSidedTanks").getString();
            }
            case 2 -> {
                return Component.translatable("gui.stevescarts.layoutColorTanks").getString();
            }
            default -> {
                return Component.translatable("gui.stevescarts.layoutSharedTanks").getString();
            }
        }
    }

    protected boolean sendOnClick(final int id, final int x, final int y, final byte data) {
        if (inRect(x, y, getBoxCoords(id))) {
            getLiquid().sendPacket(1, data);
            return true;
        }
        return false;
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean isDoubleClick) {
        super.mouseClicked(event, isDoubleClick);
        int x = (int) event.x();
        int y = (int) event.y();
        int button = event.button();
        if (button != InputConstants.MOUSE_BUTTON_LEFT && button != InputConstants.MOUSE_BUTTON_RIGHT) {
            return false;
        }
        int encodedButton = button == InputConstants.MOUSE_BUTTON_RIGHT ? 1 : 0;
        x -= leftPos;
        y -= topPos;
        if (inRect(x, y, getMiddleCoords())) {
            getLiquid().sendPacket(5, (byte) ((button == InputConstants.MOUSE_BUTTON_LEFT) ? 1 : -1));
        } else {
            for (int i = 0; i < CHANNEL_COUNT; ++i) {
                byte data = (byte) i;
                data |= (byte) (encodedButton << 2);
                if (inRect(x, y, getArrowCoords(i))) {
                    getLiquid().sendPacket(0, (byte) i);
                    break;
                }
                if (inRect(x, y, getTextCoords(i))) {
                    getLiquid().sendPacket(2, data);
                    break;
                }
                if (inRect(x, y, getColorpickerCoords(i))) {
                    getLiquid().sendPacket(3, data);
                    break;
                }
                if (inRect(x, y, getReturnCoords(i))) {
                    getLiquid().sendPacket(4, (byte) i);
                    break;
                }
                if (sendOnClick(i, x, y, data)) {
                    break;
                }
            }
        }
        return true;
    }
}
