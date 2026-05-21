package vswe.stevescarts.client.guis;

import net.creeperhost.polylib.client.modulargui.ModularGui;
import net.creeperhost.polylib.client.modulargui.ModularGuiContainer;
import net.creeperhost.polylib.client.modulargui.elements.GuiElement;
import net.creeperhost.polylib.client.modulargui.lib.BackgroundRender;
import net.creeperhost.polylib.client.modulargui.lib.Constraints;
import net.creeperhost.polylib.client.modulargui.lib.ForegroundRender;
import net.creeperhost.polylib.client.modulargui.lib.GuiRender;
import net.creeperhost.polylib.client.modulargui.lib.container.ContainerGuiProvider;
import net.creeperhost.polylib.client.modulargui.lib.container.ContainerScreenAccess;
import net.creeperhost.polylib.client.modulargui.lib.geometry.GuiParent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import vswe.stevescarts.blocks.tileentities.TileEntityCargo;
import vswe.stevescarts.containers.ContainerCargo;
import vswe.stevescarts.helpers.CargoItemSelection;
import vswe.stevescarts.helpers.Localization;
import vswe.stevescarts.helpers.ResourceHelper;
import vswe.stevescarts.init.ModBlocks;

public class GuiCargo extends ModularGuiContainer<ContainerCargo> {
    private static Identifier[] texturesLeft;
    private static Identifier[] texturesRight;

    public GuiCargo(ContainerCargo containerCargo, Inventory playerInventory, Component iTextComponent) {
        super(containerCargo, playerInventory, createProvider(containerCargo));
    }

    private static ContainerGuiProvider<ContainerCargo> createProvider(ContainerCargo containerCargo) {
        return new ContainerGuiProvider<>() {
            @Override
            public void buildGui(ModularGui gui, ContainerScreenAccess<ContainerCargo> access) {
                gui.initStandardGui(305, 222);
                CargoElement element = new CargoElement(gui.getRoot(), containerCargo);
                Constraints.bind(element, gui.getRoot());
            }
        };
    }

    static {
        GuiCargo.texturesLeft = new Identifier[]{ResourceHelper.getResource("/gui/cargoVersion0Part1.png"), ResourceHelper.getResource("/gui/cargoVersion1Part1.png")};
        GuiCargo.texturesRight = new Identifier[]{ResourceHelper.getResource("/gui/cargoVersion0Part2.png"), ResourceHelper.getResource("/gui/cargoVersion1Part2.png")};
    }

    private static class CargoElement extends GuiElement<CargoElement> implements BackgroundRender, ForegroundRender {
        protected final ContainerCargo containerCargo;
        protected final TileEntityCargo manager;

        CargoElement(GuiParent<?> parent, ContainerCargo containerCargo) {
            super(parent);
            this.containerCargo = containerCargo;
            this.manager = containerCargo.tileEntityCargo;
        }

        @Override
        public void renderBehind(GuiRender render, double mouseX, double mouseY, float partialTicks) {
            int version = containerCargo.getLayoutType() == 0 ? 0 : 1;
            int left = (int) xMin();
            int top = (int) yMin();

            render.graphics().blit(RenderPipelines.GUI_TEXTURED, GuiCargo.texturesLeft[version], left, top, 0, 0, 256, 222, 256, 256);
            render.graphics().blit(RenderPipelines.GUI_TEXTURED, GuiCargo.texturesRight[version], left + 256, top, 0, 0, 49, 222, 256, 256);

            for (int i = 0; i < 4; ++i) {
                drawArrow(render, GuiCargo.texturesRight[version], i, left, top);
                final int color = containerCargo.getColor()[i] - 1;
                if (color != 4) {
                    drawColors(render, GuiCargo.texturesRight[version], i, color, left, top);
                }
            }

            int[] coords = getMiddleCoords();
            render.renderItem(new ItemStack(getBlock(), 1), left + coords[0], top + coords[1]);

            for (int j = 0; j < 4; ++j) {
                drawItems(j, render, left, top);
            }
        }

        @Override
        public void renderInFront(GuiRender render, double mouseX, double mouseY, float partialTicks) {
            int left = (int) xMin();
            int top = (int) yMin();
            int[] coords = getMiddleCoords();

            render.graphics().drawString(font(), getManagerName(), left + coords[0] - 34, top + 4, 0xFFffffff);
            render.graphics().drawString(font(), Localization.GUI.MANAGER.TITLE.translate(), left + coords[0] + coords[2], top + 4, 0xFFffffff);

            for (int i = 0; i < 4; ++i) {
                coords = getTextCoords(i);
                render.graphics().drawString(font(), getMaxSizeText(i), left + coords[0], top + coords[1], 0xFFffffff);
            }
        }

        @Override
        public boolean renderOverlay(GuiRender render, double mouseX, double mouseY, float partialTicks, boolean consumed) {
            super.renderOverlay(render, mouseX, mouseY, partialTicks, consumed);
            for (int i = 0; i < 4; ++i) {
                try {
                    drawExtraOverlay(render, i, mouseX, mouseY);
                    drawMouseOver(render, Localization.GUI.MANAGER.CHANGE_TRANSFER_DIRECTION.translate() + "\n" + Localization.GUI.MANAGER.CURRENT_SETTING.translate() + ": " + (containerCargo.toCart()[i] ? Localization.GUI.MANAGER.DIRECTION_TO_CART.translate() : Localization.GUI.MANAGER.DIRECTION_FROM_CART.translate()), mouseX, mouseY, getArrowCoords(i));
                    drawMouseOver(render, Localization.GUI.MANAGER.CHANGE_TURN_BACK_SETTING.translate() + "\n" + Localization.GUI.MANAGER.CURRENT_SETTING.translate() + ": " + ((containerCargo.getColor()[i] == 5) ? Localization.GUI.MANAGER.TURN_BACK_NOT_SELECTED.translate() : (containerCargo.doReturn()[containerCargo.getColor()[i] - 1] ? Localization.GUI.MANAGER.TURN_BACK_DO.translate() : Localization.GUI.MANAGER.TURN_BACK_DO_NOT.translate())), mouseX, mouseY, getReturnCoords(i));
                    drawMouseOver(render, Localization.GUI.MANAGER.CHANGE_TRANSFER_SIZE.translate() + ": " + Localization.GUI.MANAGER.CURRENT_SETTING.translate() + ": " + getMaxSizeOverlay(i), mouseX, mouseY, getTextCoords(i));
                    drawMouseOver(render, Localization.GUI.MANAGER.CHANGE_SIDE.translate() + ": " + Localization.GUI.MANAGER.CURRENT_SIDE.translate() + ": " + (new String[]{Localization.GUI.MANAGER.SIDE_RED.translate(), Localization.GUI.MANAGER.SIDE_BLUE.translate(), Localization.GUI.MANAGER.SIDE_YELLOW.translate(), Localization.GUI.MANAGER.SIDE_GREEN.translate(), Localization.GUI.MANAGER.SIDE_DISABLED.translate()})[containerCargo.getColor()[i] - 1], mouseX, mouseY, getColorpickerCoords(i));
                } catch (Exception ignored) {}
            }
            drawMouseOver(render, getLayoutString() + "\n" + Localization.GUI.MANAGER.CURRENT_SETTING.translate() + ": " + getLayoutOption(containerCargo.getLayoutType()), mouseX, mouseY, getMiddleCoords());
            return consumed;
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            int x = (int) (mouseX - xMin());
            int y = (int) (mouseY - yMin());
            if (inRect(x, y, getMiddleCoords())) {
                manager.sendPacket(5, (byte) ((button == 0) ? 1 : -1));
            } else {
                for (int i = 0; i < 4; ++i) {
                    byte data = (byte) i;
                    data |= (byte) (button << 2);
                    if (inRect(x, y, getArrowCoords(i))) {
                        manager.sendPacket(0, (byte) i);
                        break;
                    }
                    if (inRect(x, y, getTextCoords(i))) {
                        manager.sendPacket(2, data);
                        break;
                    }
                    if (inRect(x, y, getColorpickerCoords(i))) {
                        manager.sendPacket(3, data);
                        break;
                    }
                    if (inRect(x, y, getReturnCoords(i))) {
                        manager.sendPacket(4, (byte) i);
                        break;
                    }
                    if (sendOnClick(i, x, y, data)) {
                        break;
                    }
                }
            }
            return true;
        }

        protected void drawColors(GuiRender render, Identifier resourceLocation, final int id, final int color, final int left, final int top) {
            try {
                int[] coords = getReturnCoords(id);
                render.graphics().blit(RenderPipelines.GUI_TEXTURED, resourceLocation, left + coords[0], top + coords[1], getColorSourceX() + (containerCargo.doReturn()[containerCargo.getColor()[id] - 1] ? 8 : 0), 80 + 8 * color, 8, 8, 256, 256);
                coords = getBoxCoords(id);
                render.graphics().blit(RenderPipelines.GUI_TEXTURED, resourceLocation, left + coords[0] - 2, top + coords[1] - 2, getColorSourceX(), 20 * color, 20, 20, 256, 256);
                if (containerCargo.getLayoutType() == 2) {
                    final int[] coords1 = getInvCoords(id);
                    render.graphics().blit(RenderPipelines.GUI_TEXTURED, resourceLocation, left + coords1[0] - 2, top + coords1[1] - 2, 125, 56 * color, 92, 56, 256, 256);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        protected int[] getReturnCoords(final int id) {
            final int x = id % 2;
            final int y = id / 2;
            final int xCoord = getCenterTargetX() + 14 + x * 70;
            int yCoord = 49 + y * 32;
            yCoord += offsetObjectY(containerCargo.getLayoutType(), x, y);
            return new int[]{xCoord, yCoord, 8, 8};
        }

        private void drawArrow(GuiRender render, Identifier resourceLocation, final int id, final int left, final int top) {
            int sourceX = getArrowSourceX();
            int sourceY = 28;
            sourceY += 56 * id;
            if (!containerCargo.toCart()[id]) {
                sourceX += 28;
            }
            final int targetX = getArrowCoords(id)[0];
            final int targetY = getArrowCoords(id)[1];
            int sizeX = 28;
            int sizeY = 28;
            render.graphics().blit(RenderPipelines.GUI_TEXTURED, resourceLocation, left + targetX, top + targetY, sourceX, sourceY, sizeX, sizeY, 256, 256);
            if (id == manager.getLastSetting() && containerCargo.getColor()[id] != 5) {
                sourceY -= 28;
                int scaledProgress = manager.moveProgressScaled(42);
                int offsetX = 0;
                int offsetY = 0;
                if (containerCargo.toCart()[id]) {
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
                render.graphics().blit(RenderPipelines.GUI_TEXTURED, resourceLocation, left + targetX + offsetX, top + targetY + offsetY, sourceX + offsetX, sourceY + offsetY, sizeX, sizeY, 256, 256);
                offsetY = (offsetX = 0);
                sizeY = (sizeX = 28);
                if (scaledProgress > 19) {
                    scaledProgress -= 19;
                    if (containerCargo.toCart()[id]) {
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
                    render.graphics().blit(RenderPipelines.GUI_TEXTURED, resourceLocation, left + targetX + offsetX, top + targetY + offsetY, sourceX + offsetX, sourceY + offsetY, sizeX, sizeY, 256, 256);
                }
            }
        }

        protected int[] getMiddleCoords() {
            return new int[]{getCenterTargetX() + 45, 61, 20, 20};
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

        protected int[] getArrowCoords(final int id) {
            final int x = id % 2;
            final int y = id / 2;
            final int xCoord = getCenterTargetX() + 25 + x * 28;
            int yCoord = 17 + y * 76;
            yCoord += offsetObjectY(containerCargo.getLayoutType(), x, y);
            return new int[]{xCoord, yCoord, 28, 28};
        }

        public void drawMouseOver(GuiRender render, final String str, final double mouseX, final double mouseY, final int[] rect) {
            if (inRect((int) (mouseX - xMin()), (int) (mouseY - yMin()), rect)) {
                render.renderTooltip(Component.literal(str), mouseX, mouseY);
            }
        }

        public boolean inRect(final int x, final int y, final int[] coords) {
            return coords != null && x >= coords[0] && x < coords[0] + coords[2] && y >= coords[1] && y < coords[1] + coords[3];
        }

        protected int[] getColorpickerCoords(final int id) {
            final int x = id % 2;
            final int y = id / 2;
            final int xCoord = getCenterTargetX() + 3 + x * 92;
            int yCoord = 49 + y * 32;
            yCoord += offsetObjectY(containerCargo.getLayoutType(), x, y);
            return new int[]{xCoord, yCoord, 8, 8};
        }

        protected String getMaxSizeOverlay(final int id) {
            final int amount = containerCargo.getAmount(id);
            final int type = containerCargo.getAmountType(id);
            if (type == 0) {
                return Localization.GUI.CARGO.TRANSFER_ALL.translate();
            }
            if (type == 1) {
                return Localization.GUI.CARGO.TRANSFER_ITEMS.translate(String.valueOf(amount), String.valueOf(amount));
            }
            return Localization.GUI.CARGO.TRANSFER_STACKS.translate(String.valueOf(amount), String.valueOf(amount));
        }

        protected String getMaxSizeText(final int id) {
            final int type = containerCargo.getAmountType(id);
            String s;
            if (type == 0) {
                s = Localization.GUI.CARGO.TRANSFER_ALL_SHORT.translate();
            } else {
                final int amount = containerCargo.getAmount(id);
                s = String.valueOf(amount);
                if (type == 1) {
                    s = s + " " + Localization.GUI.CARGO.TRANSFER_ITEMS_SHORT.translate();
                } else {
                    s = s + " " + Localization.GUI.CARGO.TRANSFER_STACKS_SHORT.translate();
                }
            }
            return s;
        }

        protected int getArrowSourceX() {
            return 49;
        }

        protected int getColorSourceX() {
            return 105;
        }

        protected int getCenterTargetX() {
            return 98;
        }

        protected void drawItems(final int id, final GuiRender render, final int left, final int top) {
            ItemStack cartIcon;
            if (containerCargo.getTarget()[id] < 0 || containerCargo.getTarget()[id] >= TileEntityCargo.itemSelections.size() || TileEntityCargo.itemSelections.get(containerCargo.getTarget()[id]).getIcon().isEmpty()) {
                cartIcon = new ItemStack(Items.MINECART, 1);
            } else {
                cartIcon = TileEntityCargo.itemSelections.get(containerCargo.getTarget()[id]).getIcon();
            }
            final int[] coords = getBoxCoords(id);
            render.renderItem(cartIcon, left + coords[0], top + coords[1]);
        }

        protected int[] getBoxCoords(final int id) {
            final int x = id % 2;
            final int y = id / 2;
            final int xCoord = getCenterTargetX() + 4 + x * 82;
            int yCoord = 17 + y * 88;
            yCoord += offsetObjectY(containerCargo.getLayoutType(), x, y);
            return new int[]{xCoord, yCoord, 20, 20};
        }

        protected int offsetObjectY(final int layout, final int x, final int y) {
            if (layout != 0) {
                return -5 + y * 10;
            }
            return 0;
        }

        protected boolean sendOnClick(final int id, final int x, final int y, final byte data) {
            if (inRect(x, y, getBoxCoords(id))) {
                manager.sendPacket(1, data);
                return true;
            }
            return false;
        }

        protected void drawExtraOverlay(GuiRender render, final int id, final double mouseX, final double mouseY) {
            if (containerCargo.getTarget()[id] >= 0) {
                final int n = containerCargo.getTarget()[id];
                if (n < TileEntityCargo.itemSelections.size()) {
                    final CargoItemSelection item = TileEntityCargo.itemSelections.get(containerCargo.getTarget()[id]);
                    if (item.getName() != null) {
                        drawMouseOver(render, Localization.GUI.CARGO.CHANGE_STORAGE_AREA.translate() + ": " + Localization.GUI.MANAGER.CURRENT_SETTING.translate() + ": " + item.getName(), mouseX, mouseY, getBoxCoords(id));
                    } else {
                        drawMouseOver(render, Localization.GUI.CARGO.CHANGE_STORAGE_AREA.translate() + ": " + Localization.GUI.MANAGER.CURRENT_SETTING.translate() + ": " + Localization.GUI.CARGO.UNKNOWN_AREA.translate(), mouseX, mouseY, getBoxCoords(id));
                    }
                    return;
                }
            }
            drawMouseOver(render, Localization.GUI.CARGO.CHANGE_STORAGE_AREA.translate() + ": " + Localization.GUI.MANAGER.CURRENT_SETTING.translate() + ": " + Localization.GUI.CARGO.UNKNOWN_AREA.translate(), mouseX, mouseY, getBoxCoords(id));
        }

        protected Block getBlock() {
            return ModBlocks.CARGO_MANAGER.get();
        }

        protected String getManagerName() {
            return Localization.GUI.CARGO.TITLE.translate();
        }

        private int[] getInvCoords(final int id) {
            final int x = id % 2;
            final int y = id / 2;
            final int xCoord = 8 + x * 198;
            final int yCoord = 11 + y * 64;
            return new int[]{xCoord, yCoord};
        }

        protected String getLayoutString() {
            return Localization.GUI.CARGO.CHANGE_SLOT_LAYOUT.translate();
        }

        protected String getLayoutOption(final int id) {
            switch (id) {
                default -> {
                    return Localization.GUI.CARGO.LAYOUT_SHARED.translate();
                }
                case 1 -> {
                    return Localization.GUI.CARGO.LAYOUT_SIDE.translate();
                }
                case 2 -> {
                    return Localization.GUI.CARGO.LAYOUT_COLOR.translate();
                }
            }
        }
    }
}
