package vswe.stevescarts.client.guis;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.ComponentRenderUtils;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.jspecify.annotations.NonNull;
import vswe.stevescarts.StevesCartsClient;
import vswe.stevescarts.api.IModuleItem;
import vswe.stevescarts.api.modules.data.ModuleData;
import vswe.stevescarts.api.modules.data.ModuleDataHull;
import vswe.stevescarts.blocks.tileentities.TileEntityCartAssembler;
import vswe.stevescarts.containers.ContainerCartAssembler;
import vswe.stevescarts.containers.slots.SlotAssembler;
import vswe.stevescarts.helpers.DropDownMenuItem;
import vswe.stevescarts.helpers.Localization;
import vswe.stevescarts.helpers.ResourceHelper;
import vswe.stevescarts.helpers.TitleBox;
import vswe.stevescarts.network.packets.PacketCreateCart;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class GuiCartAssembler extends AbstractContainerScreen<ContainerCartAssembler> {
    private static final Identifier textureLeft;
    private static final Identifier textureRight;
    private static final Identifier textureExtra;
    private static Identifier[] backgrounds;

    static {
        GuiCartAssembler.backgrounds = new Identifier[4];
        for (int i = 0; i < GuiCartAssembler.backgrounds.length; ++i) {
            GuiCartAssembler.backgrounds[i] = ResourceHelper.getResource("/gui/garageBackground" + i + ".png");
        }
        textureLeft = ResourceHelper.getResource("/gui/garagePart1.png");
        textureRight = ResourceHelper.getResource("/gui/garagePart2.png");
        textureExtra = ResourceHelper.getResource("/gui/garageExtra.png");
    }

    private final int[] assembleRect;
    private final String validChars;
    private final int[] blackBackground;
    private final ContainerCartAssembler containerCartAssembler;
    private final TileEntityCartAssembler assembler;
    private final int[] assemblingProgRect = {375, 180, 115, 11};
    private final int[] fuelProgRect = {375, 200, 115, 11};
    private ArrayList<TextWithColor> statusLog;
    private boolean hasErrors;
    private boolean firstLoad;
    private int dropdownX;
    private int dropdownY;
    private int scrollingX;
    private int scrollingY;
    private boolean isScrolling;
    private boolean spin = true;

    public GuiCartAssembler(ContainerCartAssembler containerCartAssembler, Inventory playerInventory, Component iTextComponent) {
        super(containerCartAssembler, playerInventory, iTextComponent, 512, 256);
        firstLoad = true;
        assembleRect = new int[]{390, 160, 80, 11};
        validChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        dropdownX = -1;
        dropdownY = -1;
        blackBackground = new int[]{145, 15, 222, 148};
        this.containerCartAssembler = containerCartAssembler;
        this.assembler = containerCartAssembler.getAssembler();
    }

    public static void renderEntityInInventory(GuiGraphicsExtractor graphics, int x1, int y1, int x2, int y2, float scale, Vector3f translation, Quaternionf rotation, @Nullable Quaternionf overrideCameraAngle, Entity entity) {
        EntityRenderDispatcher entityrenderdispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
        EntityRenderer<? super Entity, ?> entityrenderer = entityrenderdispatcher.getRenderer(entity);
        EntityRenderState entityrenderstate = entityrenderer.createRenderState(entity, 1.0F);
        //TODO
//        entityrenderstate.hitboxesRenderState = null;
        graphics.entity(entityrenderstate, scale, translation, rotation, overrideCameraAngle, x1, y1, x2, y2);
    }

    private void updateErrorList() {
        final ArrayList<TextWithColor> lines = new ArrayList<>();
        if (this.assembler.getItem(0).isEmpty()) {
            this.addText(lines, Localization.GUI.ASSEMBLER.ASSEMBLE_INSTRUCTION.translate());
            this.hasErrors = true;
        } else {
            ItemStack stack = this.assembler.getItem(0);
            if (!(stack.getItem() instanceof IModuleItem cartModule)) {
                this.hasErrors = false;
                this.statusLog = lines;
                return;
            }
            ModuleData hulldata = cartModule.getModuleData();
            if (hulldata == null || !(hulldata instanceof ModuleDataHull hull)) {
                this.addText(lines, Localization.GUI.ASSEMBLER.INVALID_HULL.translate(), 10357518);
                this.hasErrors = true;
            } else {
                this.addText(lines, Localization.GUI.ASSEMBLER.HULL_CAPACITY.translate() + ": " + hull.getCapacity());
                this.addText(lines, Localization.GUI.ASSEMBLER.COMPLEXITY_CAP.translate() + ": " + hull.getComplexityMax());
                this.addText(lines, Localization.GUI.ASSEMBLER.TOTAL_COST.translate() + ": " + this.assembler.getTotalCost());
                this.addText(lines, Localization.GUI.ASSEMBLER.TOTAl_TIME.translate() + ": " + this.formatTime((int) (this.assembler.generateAssemblingTime() / this.assembler.getEfficiency())));
                this.addNewLine(lines);
                final ArrayList<String> errors = this.assembler.getErrors();
                this.hasErrors = (errors.size() > 0);
                if (errors.size() == 0) {
                    this.addText(lines, Localization.GUI.ASSEMBLER.NO_ERROR.translate(), 22566);
                } else {
                    for (final String error : errors) {
                        this.addText(lines, error, 10357518);
                    }
                }
            }
        }
        this.statusLog = lines;
    }

    private void addText(final ArrayList<TextWithColor> lines, final String text) {
        addText(lines, text, 0x404040);
    }

    private void addText(final ArrayList<TextWithColor> lines, final String text, final int color) {
        List<FormattedCharSequence> newlines = ComponentRenderUtils.wrapComponents(Component.literal(text), 130, font);
        for (final FormattedCharSequence line : newlines) {
            lines.add(new TextWithColor(line, 0xFF000000 | color));
        }
    }

    private void addNewLine(final ArrayList<TextWithColor> lines) {
        lines.add(null);
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTicks) {
        if (firstLoad) {
            updateErrorList();
            firstLoad = false;
        }
        final int left = getLeftPos();
        final int top = getTopPos();
        graphics.blit(RenderPipelines.GUI_TEXTURED, GuiCartAssembler.backgrounds[assembler.getSimulationInfo().getBackground()], left + 143, top + 15, 0, 0, 220, 148, 256, 256);
        graphics.blit(RenderPipelines.GUI_TEXTURED, GuiCartAssembler.textureLeft, left, top, 0, 0, 256, imageHeight, 256, 256);
        graphics.blit(RenderPipelines.GUI_TEXTURED, GuiCartAssembler.textureRight, left + 256, top, 0, 0, imageWidth - 256, imageHeight, 256, 256);
        graphics.blit(RenderPipelines.GUI_TEXTURED, GuiCartAssembler.textureRight, left + 256, top, 0, 0, imageWidth - 256, imageHeight, 256, 256);
//        ResourceHelper.bindResource(GuiCartAssembler.textureExtra);
        final ArrayList<SlotAssembler> slots = assembler.getSlots();
        for (final SlotAssembler slot : slots) {
            int targetX = slot.getX() - 1;
            int targetY = slot.getY() - 1;
            int size;
            int srcX;
            int srcY;
            if (slot.useLargeInterface()) {
                targetX -= 3;
                targetY -= 3;
                size = 24;
                srcX = 0;
                srcY = 0;
            } else {
                size = 18;
                if (!slot.getItem().isEmpty() && TileEntityCartAssembler.getSlotStatus(slot.getItem()) <= 0) {
                    if (TileEntityCartAssembler.getSlotStatus(slot.getItem()) == TileEntityCartAssembler.getRemovedSize()) {
                        srcX = 140;
                    } else {
                        srcX = 122;
                    }
                    srcY = 40;
                } else {
                    srcX = 24;
                    srcY = 0;
                }
            }
            //Slots
            graphics.blit(RenderPipelines.GUI_TEXTURED, GuiCartAssembler.textureExtra, left + targetX, top + targetY, srcX, srcY, size, size, 256, 256);
            int animationTick = slot.getAnimationTick();
            if (animationTick < 0) {
                animationTick = 0;
            }
            if (animationTick < 8 && !slot.useLargeInterface()) {
                graphics.blit(RenderPipelines.GUI_TEXTURED, GuiCartAssembler.textureExtra, left + targetX + 1, top + targetY + 1, 0, 24 + animationTick, 16, 8 - animationTick, 256, 256);
                graphics.blit(RenderPipelines.GUI_TEXTURED, GuiCartAssembler.textureExtra, left + targetX + 1, top + targetY + 1 + 8 + animationTick, 0, 32, 16, 8 - animationTick, 256, 256);
            }
            slot.update();
        }
        for (final TitleBox box : assembler.getTitleBoxes()) {
            final int targetY2 = box.getY() - 12;
            final int targetX2 = box.getX();
            graphics.blit(RenderPipelines.GUI_TEXTURED, GuiCartAssembler.textureExtra, left + targetX2, top + targetY2, 0, 40, 115, 11, 256, 256);
            graphics.blit(RenderPipelines.GUI_TEXTURED, GuiCartAssembler.textureExtra, left + targetX2 + 8, top + targetY2 + 2, 0, 51 + box.getID() * 7, 115, 7, 256, 256);
        }
        int srcX2 = 42;
        int srcY2 = 0;
        if (assembler.getIsDisassembling()) {
            srcX2 = 158;
            srcY2 = 40;
        }
        if (hasErrors) {
            srcY2 += 22;
        } else if (inRect(mouseX - left, mouseY - top, assembleRect)) {
            srcY2 += 11;
        }
        graphics.blit(RenderPipelines.GUI_TEXTURED, GuiCartAssembler.textureExtra, left + assembleRect[0], top + assembleRect[1], srcX2, srcY2, assembleRect[2], assembleRect[3], 256, 256);
        float assemblingProgress = 0.0f;
        if (containerCartAssembler.getIsAssembling()) {
            assemblingProgress = (float) containerCartAssembler.getAssemblingTime() / (float) containerCartAssembler.getMaxAssemblingTime();
        }
        drawProgressBar(graphics, assemblingProgRect, assemblingProgress, 22, mouseX, mouseY);
        drawProgressBar(graphics, fuelProgRect, (float) containerCartAssembler.getFuel() / (float) assembler.getMaxFuelLevel(), 31, mouseX, mouseY);
        renderDropDownMenu(graphics, GuiCartAssembler.textureExtra, mouseX, mouseY);
        renderEntityInInventory(graphics, leftPos + 145, topPos + 15, leftPos + 363, topPos + 163, 50);
    }

    @Override
    public void extractLabels(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
        graphics.text(Minecraft.getInstance().font, Localization.GUI.ASSEMBLER.TITLE.translate(), 18, 6, 0xFFffffff);
    }

    @Override
    public void extractContents(@NonNull GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTicks) {
        this.extractBackground(graphics, mouseX, mouseY, partialTicks);
        super.extractContents(graphics, mouseX, mouseY, partialTicks);
        this.extractTooltip(graphics, mouseX, mouseY);
        if (assembler.isErrorListOutdated) {
            updateErrorList();
            assembler.isErrorListOutdated = false;
        }
        final ArrayList<TextWithColor> lines = statusLog;
        if (lines != null) {
            int lineCount = lines.size();
            boolean dotdotdot = false;
            if (lineCount > 11) {
                lineCount = 10;
                dotdotdot = true;
            }
            for (int i = 0; i < lineCount; ++i) {
                final TextWithColor info = lines.get(i);
                if (info != null) {
                    graphics.text(Minecraft.getInstance().font, info.getText(), leftPos + 370, topPos + 40 + i * 10, info.getColor(), false);
                }
            }
            if (dotdotdot) {
                graphics.text(Minecraft.getInstance().font, "...", leftPos + 370, topPos + 40 + lineCount * 10, 0xFF404040, false);
            }
        }
        float assemblingProgress;
        String assemblingInfo;
        if (containerCartAssembler.getIsAssembling()) {
            assemblingProgress = (float) containerCartAssembler.getAssemblingTime() / (float) containerCartAssembler.getMaxAssemblingTime();
            assemblingInfo = Localization.GUI.ASSEMBLER.ASSEMBLE_PROGRESS.translate() + ": " + formatProgress(assemblingProgress);
            assemblingInfo = assemblingInfo + ": " + Localization.GUI.ASSEMBLER.TIME_LEFT.translate() + ": " + formatTime((int) ((containerCartAssembler.getMaxAssemblingTime() - containerCartAssembler.getAssemblingTime()) / assembler.getEfficiency()));
        } else {
            assemblingInfo = Localization.GUI.ASSEMBLER.IDLE_MESSAGE.translate();
        }
        if (!hasErrors) {
            if (assembler.getIsDisassembling()) {
                drawProgressBarInfo(graphics, assembleRect, mouseX, mouseY, Localization.GUI.ASSEMBLER.MODIFY_CART.translate());
            } else {
                drawProgressBarInfo(graphics, assembleRect, mouseX, mouseY, Localization.GUI.ASSEMBLER.ASSEMBLE_CART.translate());
            }
        }
        drawProgressBarInfo(graphics, assemblingProgRect, mouseX, mouseY, assemblingInfo);
        drawProgressBarInfo(graphics, fuelProgRect, mouseX, mouseY, Localization.GUI.ASSEMBLER.FUEL_LEVEL.translate() + ": " + containerCartAssembler.getFuel() + "/" + assembler.getMaxFuelLevel());
    }

    private String formatProgress(final float progress) {
        final float percentage = (int) (progress * 10000.0f) / 100.0f;
        return String.format("%05.2f%%", percentage);
    }

    private String formatTime(int ticks) {
        int seconds = ticks / 20;
        ticks -= seconds * 20;
        int minutes = seconds / 60;
        seconds -= minutes * 60;
        final int hours = minutes / 60;
        minutes -= hours * 60;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    private void drawProgressBarInfo(GuiGraphicsExtractor graphics, final int[] rect, final int x, final int y, final String str) {
        if (inRect(x - getLeftPos(), y - getTopPos(), rect)) {
            graphics.setTooltipForNextFrame(Minecraft.getInstance().font, Component.literal(str), x, y);
        }
    }

    private void drawProgressBar(GuiGraphicsExtractor graphics, int[] rect, float progress, int barSrcY, int x, int y) {
        final int j = getLeftPos();
        final int k = getTopPos();
        int boxSrcY = 0;
        if (inRect(x - j, y - k, rect)) {
            boxSrcY = 11;
        }
        graphics.blit(RenderPipelines.GUI_TEXTURED, GuiCartAssembler.textureExtra, j + rect[0], k + rect[1], 122, boxSrcY, rect[2], rect[3], 256, 256);
        if (progress != 0.0f) {
            if (progress > 1.0f) {
                progress = 1.0f;
            }
            graphics.blit(RenderPipelines.GUI_TEXTURED, GuiCartAssembler.textureExtra, j + rect[0] + 1, k + rect[1] + 1, 122, barSrcY, (int) (rect[2] * progress), rect[3] - 2, 256, 256);
        }
    }

    public void renderEntityInInventory(GuiGraphicsExtractor graphics, int xPos, int yPos, int xMax, int yMax, int scale) {
        if (assembler.getHullModule() == null) {
            return;
        }
        assembler.createPlaceholder();

        Quaternionf angle = new Quaternionf().rotationXYZ((float) Math.toRadians(assembler.getRoll()), (float) Math.toRadians(assembler.getYaw()), (float) Math.PI);
        renderEntityInInventory(graphics, xPos, yPos, xMax, yMax, scale, new Vector3f(0, 0.5F, 0), angle, null, assembler.getPlaceholder());
    }

    private void renderDropDownMenu(GuiGraphicsExtractor graphics, Identifier identifier, final int x, final int y) {
        final int left = getLeftPos();
        final int top = getTopPos();
        if (dropdownX != -1 && dropdownY != -1) {
            final ArrayList<DropDownMenuItem> items = assembler.getDropDown();
            for (int i = 0; i < items.size(); ++i) {
                final DropDownMenuItem item = items.get(i);
                final int[] rect = item.getRect(dropdownX, dropdownY, i);
                int[] subrect = new int[0];
                int srcX = 0;
                int srcY = item.getIsLarge() ? 113 : 93;
                graphics.blit(RenderPipelines.GUI_TEXTURED, identifier, left + rect[0], top + rect[1], srcX, srcY, rect[2], rect[3], 256, 256);
                if (item.getIsLarge()) {
                    drawString(graphics, identifier, item.getName(), left + rect[0] + 55, top + rect[1] + 7);
                }
                graphics.blit(RenderPipelines.GUI_TEXTURED, identifier, left + rect[0] + 34, top + rect[1] + 2, item.getImageID() % 16 * 16, 179 + item.getImageID() / 16 * 16, 16, 16, 256, 256);
                if (item.hasSubmenu()) {
                    subrect = item.getSubRect(dropdownX, dropdownY, i);
                    srcX = (item.getIsSubMenuOpen() ? 0 : 43);
                    srcY = 133;
                    graphics.blit(RenderPipelines.GUI_TEXTURED, identifier, left + subrect[0], top + subrect[1], srcX, srcY, subrect[2], subrect[3], 256, 256);
                }
                switch (item.getType()) {
                    case BOOL ->
                            drawBooleanBox(graphics, identifier, x, y, 5 + rect[0], 5 + rect[1], item.getBOOL());
                    case INT -> {
                        if (item.getIsSubMenuOpen()) {
                            drawIncreamentBox(graphics, identifier, x, y, getOffSetXForSubMenuBox(0, 2) + subrect[0], 3 + subrect[1]);
                            drawDecreamentBox(graphics, identifier, x, y, getOffSetXForSubMenuBox(1, 2) + subrect[0], 3 + subrect[1]);
                        }
                        final int targetX = rect[0] + 16;
                        final int targetY = rect[1] + 7;
                        final int valueToWrite = item.getINT();
                        if (valueToWrite >= 10) {
                            drawDigit(graphics, identifier, valueToWrite / 10, -1, targetX, targetY);
                            drawDigit(graphics, identifier, valueToWrite % 10, 1, targetX, targetY);
                            break;
                        }
                        drawDigit(graphics, identifier, valueToWrite, 0, targetX, targetY);
                    }
                    case MULTIBOOL -> {
                        if (item.getIsSubMenuOpen()) {
                            for (int count = item.getMULTIBOOLCount(), bool = 0; bool < count; ++bool) {
                                drawBooleanBox(graphics, identifier, x, y, subrect[0] + getOffSetXForSubMenuBox(bool, count), subrect[1] + 3, item.getMULTIBOOL(bool));
                            }
                        }
                    }
                }
            }
        }
    }

    private void drawString(GuiGraphicsExtractor GuiGraphicsExtractor, Identifier identifier, String str, final int x, final int y) {
        str = str.toUpperCase();
        for (int i = 0; i < str.length(); ++i) {
            final char c = str.charAt(i);
            final int index = validChars.indexOf(c);
            if (index != -1) {
                GuiGraphicsExtractor.blit(RenderPipelines.GUI_TEXTURED, identifier, x + 7 * i, y, 8 * index, 165, 6, 7, 256, 256);
            }
        }
    }

    private int getOffSetXForSubMenuBox(final int id, final int count) {
        return 2 + (int) (20.0f + (id - count / 2.0f) * 10.0f);
    }

    private void drawDigit(GuiGraphicsExtractor graphics, Identifier identifier, int digit, int offset, int targetX, int targetY) {
        final int srcX = digit * 8;
        final int srcY = 172;
        targetX += offset * 4;
        graphics.blit(RenderPipelines.GUI_TEXTURED, identifier, getLeftPos() + targetX, getTopPos() + targetY, srcX, srcY, 6, 7, 256, 256);
    }

    private void drawIncreamentBox(GuiGraphicsExtractor graphics, Identifier identifier, int mouseX, int mouseY, int x, int y) {
        drawStandardBox(graphics, identifier, mouseX, mouseY, x, y, 10);
    }

    private void drawDecreamentBox(GuiGraphicsExtractor graphics, Identifier identifier, int mouseX, int mouseY, int x, int y) {
        drawStandardBox(graphics, identifier, mouseX, mouseY, x, y, 20);
    }

    private void drawBooleanBox(GuiGraphicsExtractor graphics, Identifier identifier, int mouseX, int mouseY, int x, int y, boolean itemvalue) {
        drawStandardBox(graphics, identifier, mouseX, mouseY, x, y, 0);
        if (itemvalue) {
            graphics.blit(RenderPipelines.GUI_TEXTURED, identifier, getLeftPos() + x + 2, getTopPos() + y + 2, 0, 159, 6, 6, 256, 256);
        }
    }

    public boolean inRect(final int x, final int y, final int[] coords) {
        return coords != null && x >= coords[0] && x < coords[0] + coords[2] && y >= coords[1] && y < coords[1] + coords[3];
    }

    private void drawStandardBox(GuiGraphicsExtractor graphics, Identifier identifier, int mouseX, int mouseY, int x, int y, int srcX) {
        final int targetX = getLeftPos() + x;
        final int targetY = getTopPos() + y;
        final int srcY = 149;
        graphics.blit(RenderPipelines.GUI_TEXTURED, identifier, targetX, targetY, srcX, srcY, 10, 10, 256, 256);
        if (inRect(mouseX, mouseY, new int[]{targetX, targetY, 10, 10})) {
            graphics.blit(RenderPipelines.GUI_TEXTURED, identifier, targetX, targetY, 30, srcY, 10, 10, 256, 256);
        }
    }

    private boolean clickBox(final int mouseX, final int mouseY, final int x, final int y) {
        return inRect(mouseX, mouseY, new int[]{x, y, 10, 10});
    }

    @Override
    public void mouseMoved(final double x0, final double y0) {

    }

    @Override
    public boolean mouseDragged(MouseButtonEvent event, double mouseX, double mouseY) {
        super.mouseMoved(event.x(), event.y());
        final int x = (int) (event.x() - getLeftPos());
        final int y = (int) (event.y() - getTopPos());
        if (dropdownX != -1 && dropdownY != -1) {
            final ArrayList<DropDownMenuItem> items = assembler.getDropDown();
            for (int i = 0; i < items.size(); ++i) {
                final DropDownMenuItem item = items.get(i);
                boolean insideSubRect = false;
                if (item.hasSubmenu()) {
                    insideSubRect = inRect(x, y, item.getSubRect(dropdownX, dropdownY, i));
                    if (!insideSubRect && item.getIsSubMenuOpen()) {
                        item.setIsSubMenuOpen(false);
                    } else if (insideSubRect && !item.getIsSubMenuOpen()) {
                        item.setIsSubMenuOpen(true);
                    }
                }
                final boolean insideRect = insideSubRect || inRect(x, y, item.getRect(dropdownX, dropdownY, i));
                if (!insideRect && item.getIsLarge()) {
                    item.setIsLarge(false);
                } else if (insideRect && !item.getIsLarge()) {
                    item.setIsLarge(true);
                }
            }
        }
        if (isScrolling) {
            if (event.button() != -1) {
                isScrolling = false;
                assembler.setSpinning(true);
            } else {
                assembler.setYaw(assembler.getYaw() + x - scrollingX);
                assembler.setRoll(assembler.getRoll() + y - scrollingY);
                scrollingX = x;
                scrollingY = y;
            }
        }
        return true;
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean isDoubleClick) {
        super.mouseClicked(event, isDoubleClick);
        int x = (int) (event.x() - getLeftPos());
        int y = (int) (event.y() - getTopPos());
        if (inRect(x, y, assembleRect)) {
            StevesCartsClient.sendToServer(new PacketCreateCart(this.assembler.getBlockPos(), 0, new byte[0]));
            return true;
        } else if (inRect(x, y, blackBackground)) {
            if (event.button() == 0) {
                if (!isScrolling) {
                    scrollingX = x;
                    scrollingY = y;
                    isScrolling = true;
                    spin = !spin;
                    return true;
                }
            } else if (event.button() == 1) {
                dropdownX = x;
                dropdownY = y;
                if (dropdownY + assembler.getDropDown().size() * 20 > 164) {
                    dropdownY = 164 - assembler.getDropDown().size() * 20;
                    return true;
                }
            }
        } else {
            final ArrayList<SlotAssembler> slots = assembler.getSlots();
            for (int i = 1; i < slots.size(); ++i) {
                final SlotAssembler slot = slots.get(i);
                final int targetX = slot.getX() - 1;
                final int targetY = slot.getY() - 1;
                final int size = 18;
                if (inRect(x, y, new int[]{targetX, targetY, size, size}) && !slot.getItem().isEmpty() && TileEntityCartAssembler.getSlotStatus(slot.getItem()) <= 0) {
                    StevesCartsClient.sendToServer(new PacketCreateCart(this.assembler.getBlockPos(), 1, new byte[]{(byte) i}));
                    return true;
                }
            }
        }
        if (event.button() == 0 && dropdownX != -1 && dropdownY != -1) {
            boolean anyLargeItem = false;
            final ArrayList<DropDownMenuItem> items = assembler.getDropDown();
            for (int j = 0; j < items.size(); ++j) {
                final DropDownMenuItem item = items.get(j);
                if (item.getIsLarge()) {
                    anyLargeItem = true;
                    final int[] rect = item.getRect(dropdownX, dropdownY, j);
                    int[] subrect = new int[0];
                    if (item.hasSubmenu() && item.getIsSubMenuOpen()) {
                        subrect = item.getSubRect(dropdownX, dropdownY, j);
                    }
                    switch (item.getType()) {
                        case BOOL -> {
                            if (clickBox(x, y, 5 + rect[0], 5 + rect[1])) {
                                item.setBOOL(!item.getBOOL());
                            }
                        }
                        case INT -> {
                            if (!item.getIsSubMenuOpen()) {
                                break;
                            }
                            if (clickBox(x, y, getOffSetXForSubMenuBox(0, 2) + subrect[0], 3 + subrect[1])) {
                                item.setINT(item.getINT() + 1);
                            }
                            if (clickBox(x, y, getOffSetXForSubMenuBox(1, 2) + subrect[0], 3 + subrect[1])) {
                                item.setINT(item.getINT() - 1);
                            }
                        }
                        case MULTIBOOL -> {
                            if (item.getIsSubMenuOpen()) {
                                for (int count = item.getMULTIBOOLCount(), bool = 0; bool < count; ++bool) {
                                    if (clickBox(x, y, subrect[0] + getOffSetXForSubMenuBox(bool, count), subrect[1] + 3)) {
                                        item.setMULTIBOOL(bool, !item.getMULTIBOOL(bool));
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }
            if (!anyLargeItem) {
                final int n = -1;
                dropdownY = n;
                dropdownX = n;
            }
        }
        return true;
    }

    private record TextWithColor(FormattedCharSequence text, int color) {

        public FormattedCharSequence getText() {
            return text;
        }

        public int getColor() {
            return color;
        }
    }
}
