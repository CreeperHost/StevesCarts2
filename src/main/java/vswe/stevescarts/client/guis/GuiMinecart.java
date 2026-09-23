package vswe.stevescarts.client.guis;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import org.joml.Matrix3x2fStack;
import org.jspecify.annotations.NonNull;
import vswe.stevescarts.StevesCartsClient;
import vswe.stevescarts.api.modules.ModuleBase;
import vswe.stevescarts.api.slots.SlotStevesCarts;
import vswe.stevescarts.containers.ContainerMinecart;
import vswe.stevescarts.entities.ModularMinecart;
import vswe.stevescarts.helpers.ModuleCountPair;
import vswe.stevescarts.helpers.ResourceHelper;
import vswe.stevescarts.network.packets.PacketMinecartTurn;

import java.util.*;

public class GuiMinecart extends AbstractContainerScreen<ContainerMinecart> {
    private static Identifier textureLeft;
    private static Identifier textureRight;
    private static Identifier textureReturn;

    static {
        GuiMinecart.textureLeft = ResourceHelper.getResource("/gui/guiBase1.png");
        GuiMinecart.textureRight = ResourceHelper.getResource("/gui/guiBase2.png");
        GuiMinecart.textureReturn = ResourceHelper.getResource("/gui/return.png");
    }

    private final int[] scrollBox;
    private final int[] returnButton;
    private boolean isScrolling;
    private ModularMinecart cart;

    public GuiMinecart(ContainerMinecart containerMinecart, final Inventory invPlayer, final Component iTextComponent) {
        super(containerMinecart, invPlayer, iTextComponent, 478, 256);
        scrollBox = new int[]{450, 15, 18, 225};
        returnButton = new int[]{324, 173, 24, 12};
        setup(containerMinecart.cart);
    }

    protected void setup(final ModularMinecart cart) {
        this.cart = cart;
    }

    public int getGuiLeft() {
        return getLeftPos();
    }

    public int getGuiTop() {
        return getTopPos();
    }

    @Override
    public void extractContents(@NonNull GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.extractBackground(guiGraphics, mouseX, mouseY, partialTicks);
        super.extractContents(guiGraphics, mouseX, mouseY, partialTicks);
        if (cart == null) return;

        final ModuleBase thief = cart.getInterfaceThief();
        if (thief != null) {
            drawModuleForeground(guiGraphics, thief);
            drawModuleMouseOver(guiGraphics, thief, mouseX, mouseY);
        } else {
            for (final ModuleBase module : cart.modules()) {
                drawModuleForeground(guiGraphics, module);
            }
            renderModuleListText(guiGraphics, mouseX, mouseY);
            for (final ModuleBase module : cart.modules()) {
                drawModuleMouseOver(guiGraphics, module, mouseX, mouseY);
            }
            renderModuleListMouseOver(guiGraphics, mouseX, mouseY);
            renderReturnMouseOver(guiGraphics, mouseX, mouseY);
        }
        this.extractTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTicks) {
        final int left = getLeftPos();
        final int top = getTopPos();
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, GuiMinecart.textureLeft, left, top, 0, 0, 256, 256, 256, 256);
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, GuiMinecart.textureRight, left + 256, top, 0, 0, imageWidth - 256, imageHeight, 256, 256);
        if (cart != null) {
            final ModuleBase thief = cart.getInterfaceThief();
            if (thief != null) {
                drawModuleSlots(guiGraphics, GuiMinecart.textureRight, thief);
                drawModuleBackground(guiGraphics, thief, mouseX, mouseY);
                drawModuleBackgroundItems(guiGraphics, thief, mouseX, mouseY);
                for (final ModuleBase module : cart.modules()) {
                    if (module.hasGui() && module.hasSlots()) {
                        final ArrayList<SlotStevesCarts> slotsList = module.getSlots();
                        for (final SlotStevesCarts slot : slotsList) {
                            resetSlot(slot);
                        }
                    }
                }
            } else {
                //Draw Scroll Bar
                guiGraphics.blit(RenderPipelines.GUI_TEXTURED, GuiMinecart.textureRight, left + scrollBox[0], top + scrollBox[1], 222, 24, scrollBox[2], scrollBox[3], 256, 256);
                guiGraphics.blit(RenderPipelines.GUI_TEXTURED, GuiMinecart.textureRight, left + scrollBox[0] + 2, top + scrollBox[1] + 2 + cart.getScrollY(), 240, 26 + (cart.canScrollModules ? 0 : 25), 14, 25, 256, 256);
                for (final ModuleBase module : cart.modules()) {
                    drawModuleSlots(guiGraphics, GuiMinecart.textureRight, module);
                }

                for (final ModuleBase module : cart.modules()) {
                    drawModuleBackground(guiGraphics, module, mouseX, mouseY);
                }

                renderModuleList(guiGraphics, mouseX, mouseY);
                renderReturnButton(guiGraphics, mouseX, mouseY);
                for (final ModuleBase module : cart.modules()) {
                    drawModuleBackgroundItems(guiGraphics, module, mouseX, mouseY);
                }
            }
        }
    }

    @Override
    protected void extractLabels(@NonNull GuiGraphicsExtractor guiGraphics, int p_230451_2_, int p_230451_3_) {
    }

    private void renderModuleList(GuiGraphicsExtractor guiGraphics, int x, int y) {
        for (int i = 0; i < cart.modules().size(); i++) {
            drawModuleIcon(guiGraphics, cart.modules().get(i).getItemStack(), getLeftPos() + getModuleDisplayX(i), getTopPos() + getModuleDisplayY(i), 1.0f, 1.0f, 0.0f, 0.0f);
        }
    }

    private void renderReturnButton(GuiGraphicsExtractor guiGraphics, int x, int y) {
        x -= getLeftPos();
        y -= getTopPos();
        int uy = inRect(x, y, returnButton) ? 12 : 0;
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, GuiMinecart.textureReturn, returnButton[0] + getLeftPos(), returnButton[1] + getTopPos(), 0, uy, returnButton[2], returnButton[3], 256, 256);
    }

    public void drawModuleIcon(GuiGraphicsExtractor guiGraphics, ItemStack icon, final int targetX, final int targetY, final float sizeX, final float sizeY, final float offsetX, final float offsetY) {
        guiGraphics.item(icon, targetX, targetY);
    }

    private void renderModuleListText(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY) {
        mouseX -= getLeftPos();
        mouseY -= getTopPos();
        ArrayList<ModuleCountPair> moduleCounts = cart.moduleCounts();
        guiGraphics.text(Minecraft.getInstance().font, cart.getName(), getLeftPos() + 5, getTopPos() + 172, 0xFFffffff);
        for (int i = 0; i < moduleCounts.size(); ++i) {
            ModuleCountPair count = moduleCounts.get(i);
            if (count.getCount() != 1) {
                int alpha = (int) ((inRect(mouseX, mouseY, getModuleDisplayX(i), getModuleDisplayY(i), 16, 16) ? 1.0f : 0.75f) * 256.0f);
                String str = String.valueOf(count.getCount());
                guiGraphics.text(Minecraft.getInstance().font, str, getLeftPos() + getModuleDisplayX(i) + 16 - font.width(str),
                        getModuleDisplayY(i) + 8, 0xFFFFFF | alpha << 24);
            }
        }
    }

    private void renderModuleListMouseOver(GuiGraphicsExtractor guiGraphics, int x, int y) {
        x -= getLeftPos();
        y -= getTopPos();
        ArrayList<ModuleCountPair> moduleCounts = cart.moduleCounts();
        for (int i = 0; i < moduleCounts.size(); ++i) {
            final ModuleCountPair count = moduleCounts.get(i);
            if (inRect(x, y, getModuleDisplayX(i), getModuleDisplayY(i), 16, 16)) {
                for (ModuleBase module : cart.modules()) {
                    if (module.getClass() == count.getData().getModuleClass()) {
                        if (module.hasExtraData()) {
                            count.setExtraData(module.writeExtraData());
                        }
                        break;
                    }
                }
                drawMouseOver(guiGraphics, count.toString(), x, y);
            }
        }
    }

    @Deprecated
    public void drawMouseOver(GuiGraphicsExtractor guiGraphics, final String str, final int x, final int y) {
        final String[] split = str.split("\n");
        final List<String> text = new ArrayList<>(Arrays.asList(split));
        List<Component> list = new ArrayList<>();
        for (String s : text) {
            list.add(Component.literal(s));
        }
        guiGraphics.setTooltipForNextFrame(Minecraft.getInstance().font, list, Optional.empty(), getLeftPos() + x, getTopPos() + y);
    }

    public void drawMouseOver(GuiGraphicsExtractor guiGraphics, List<Component> list, final int x, final int y) {
        guiGraphics.setTooltipForNextFrame(Minecraft.getInstance().font, list, Optional.empty(), getLeftPos() + x, getTopPos() + y);
    }

    public void drawMouseOver(GuiGraphicsExtractor guiGraphics, Component toolTip, final int x, final int y) {
        drawMouseOver(guiGraphics, Collections.singletonList(toolTip), x, y);
    }

    private void renderReturnMouseOver(GuiGraphicsExtractor guiGraphics, int x, int y) {
        x -= getLeftPos();
        y -= getTopPos();

        if (inRect(x, y, returnButton)) {
            drawMouseOver(guiGraphics, Component.translatable("gui.stevescarts.returnButton"), x, y);
        }
    }

    private int getModuleDisplayX(int id) {
        return id % 8 * 18 + 7;
    }

    private int getModuleDisplayY(int id) {
        return id / 8 * 18 + 182;
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean isDoubleClick) {
        ModuleBase thief = cart.getInterfaceThief();
        if (thief != null) {
            handleModuleMouseClicked(thief, (int) event.x(), (int) event.y(), event.button());
        } else if (cart.modules() != null) {
            if (inRect((int) event.x() - getLeftPos(), (int) event.y() - getTopPos(), scrollBox[0], scrollBox[1], scrollBox[2], scrollBox[3])) {
                scrollToMouse(event.y());
                isScrolling = true;
                return true;
            }
            for (ModuleBase module : cart.modules()) {
                handleModuleMouseClicked(module, (int) event.x(), (int) event.y(), event.button());
            }
            if (inRect((int) event.x() - getLeftPos(), (int) event.y() - getTopPos(), returnButton)) {
                StevesCartsClient.sendToServer(new PacketMinecartTurn(cart.getId()));
            }
        }
        return super.mouseClicked(event, isDoubleClick);
    }

    @Override
    public boolean mouseReleased(MouseButtonEvent event) {
        ModuleBase thief = cart.getInterfaceThief();
        if (thief != null) {
            handleModuleMouseReleased(thief, (int) event.x(), (int) event.y(), event.button());
        } else if (cart.modules() != null) {
            for (ModuleBase module : cart.modules()) {
                handleModuleMouseReleased(module, (int) event.x(), (int) event.y(), event.button());
            }
        }

        isScrolling = false;
        return super.mouseReleased(event);
    }

    protected boolean inRect(final int x, final int y, final int x1, final int y1, final int sizeX, final int sizeY) {
        return x >= x1 && x <= x1 + sizeX && y >= y1 && y <= y1 + sizeY;
    }

    public boolean inRect(final int x, final int y, final int[] coords) {
        return coords != null && x >= coords[0] && x < coords[0] + coords[2] && y >= coords[1] && y < coords[1] + coords[3];
    }

    @Override
    public boolean mouseDragged(MouseButtonEvent event, double mouseX, double mouseY) {
        if (super.mouseDragged(event, mouseX, mouseY)) return true;
        if (isScrolling) {
            scrollToMouse(event.y());
            return true;
        }
        return false;
    }

    @Override
    public void mouseMoved(final double x, final double y) {
        super.mouseMoved(x, y);
        if (isScrolling) {
            scrollToMouse(y);
        }
        if (cart.modules() != null) {
            final ModuleBase thief = cart.getInterfaceThief();
            if (thief != null) {
                handleModuleMouseMoved(thief, (int) x, (int) y);
            } else {
                for (final ModuleBase module : cart.modules()) {
                    handleModuleMouseMoved(module, (int) x, (int) y);
                }
            }
        }
    }

    private void scrollToMouse(double mouseY) {
        int temp = (int) mouseY - getTopPos() - 12 - (scrollBox[1] + 2);
        if (temp < 0) {
            temp = 0;
        } else if (temp > 198) {
            temp = 198;
        }
        cart.setScrollY(temp);
    }

    @Override
    public boolean keyPressed(KeyEvent event) {
        super.keyPressed(event);
        if (cart.modules() != null) {
            final ModuleBase thief = cart.getInterfaceThief();
            if (thief != null) {
                handleModuleKeyPress(thief, event.key(), event.modifiers());
            } else {
                for (final ModuleBase module : cart.modules()) {
                    handleModuleKeyPress(module, event.key(), event.modifiers());
                }
            }
        }
        return true;
    }

    private void drawModuleForeground(GuiGraphicsExtractor guiGraphics, ModuleBase module) {
        if (module.hasGui()) {
            module.drawForeground(guiGraphics, this);
            if (module.useButtons()) {
                module.drawButtonText(guiGraphics, this);
            }
        }
    }

    private void drawModuleMouseOver(GuiGraphicsExtractor guiGraphics, ModuleBase module, final int x, final int y) {
        if (module.hasGui()) {
            module.drawMouseOver(guiGraphics, this, x - getLeftPos() - module.getX(), y - getTopPos() - module.getY());
            if (module.useButtons()) {
                module.drawButtonOverlays(guiGraphics, this, x - getLeftPos() - module.getX(), y - getTopPos() - module.getY());
            }
        }
    }

    private void drawModuleSlots(GuiGraphicsExtractor guiGraphics, Identifier texture, final ModuleBase module) {
        if (module.hasGui() && module.hasSlots()) {
            final ArrayList<SlotStevesCarts> slotsList = module.getSlots();
            for (final SlotStevesCarts slot : slotsList) {
                final int[] rect = {slot.getX() + 1, slot.getY() + 1, 16, 16};
                module.handleScroll(rect);
                final boolean drawAll = rect[3] == 16;
                if (drawAll) {
                    slot.x = slot.getX() + module.getX() + 1;
                    slot.y = slot.getY() + module.getY() + 1 - cart.getRealScrollY();
                } else {
                    resetSlot(slot);
                }
                module.drawImage(guiGraphics, texture, this, slot.getX(), slot.getY(), getImageWidth() - 256, 0, 18, 18);
                if (!drawAll) {
                    module.drawImage(guiGraphics, texture, this, slot.getX() + 1, slot.getY() + 1, getImageWidth() - 256 + 18, 1, 16, 16);
                }
            }
        }
    }

    private void resetSlot(final SlotStevesCarts slot) {
        slot.x = -9001;
        slot.y = -9001;
    }

    private void drawModuleBackground(GuiGraphicsExtractor guiGraphics, final ModuleBase module, final int x, final int y) {
        if (module.hasGui()) {
            module.drawBackground(guiGraphics, this, x - getLeftPos() - module.getX(), y - getTopPos() - module.getY());
            if (module.useButtons()) {
                module.drawButtons(guiGraphics, this, x - getLeftPos() - module.getX(), y - getTopPos() - module.getY());
            }
        }
    }

    private void drawModuleBackgroundItems(GuiGraphicsExtractor guiGraphics, final ModuleBase module, final int x, final int y) {
        if (module.hasGui()) {
            module.drawBackgroundItems(guiGraphics, this, x - getLeftPos() - module.getX(), y - getTopPos() - module.getY());
        }
    }

    private void handleModuleMouseClicked(final ModuleBase module, final int x, final int y, final int button) {
        module.mouseClicked(this, x - getLeftPos() - module.getX(), y - getTopPos() - module.getY(), button);
        if (module.useButtons()) {
            module.mouseClickedButton(this, x - getLeftPos() - module.getX(), y - getTopPos() - module.getY(), button);
        }
    }

    private void handleModuleMouseReleased(final ModuleBase module, final int x, final int y, final int button) {
        module.mouseMovedOrUp(this, x - getLeftPos() - module.getX(), y - getTopPos() - module.getY(), button);
        module.mouseReleased(this, x - getLeftPos() - module.getX(), y - getTopPos() - module.getY(), button);
    }

    private void handleModuleMouseMoved(ModuleBase module, int x, int y) {
        module.mouseMovedOrUp(this, x - getLeftPos() - module.getX(), y - getTopPos() - module.getY(), -1);
        module.mouseMoved(this, x - getLeftPos() - module.getX(), y - getTopPos() - module.getY());
    }

    private void handleModuleKeyPress(final ModuleBase module, final int id, final int extraInformation) {
        module.keyPress(this, id, extraInformation);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        super.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
        if (scrollY < 0) {
            scrollY = -1;
        }
        if (scrollY > 0) {
            scrollY = 1;
        }
        if (this.inRect((int) mouseX - getLeftPos(), (int) mouseY - getTopPos(), 0, 0, imageWidth, imageHeight)) {
            int moduleSize = this.cart.modularSpaceHeight;
            int scroll = cart.getScrollY() + ((int) -scrollY * 4000) / (moduleSize - ModularMinecart.MODULAR_SPACE_HEIGHT);
            scroll = Mth.clamp(scroll, 0, 198);
            cart.setScrollY(scroll);
        }
        return true;
    }

    public void pushScissor(GuiGraphicsExtractor guiGraphics) {
        int left = getLeftPos() + 5;
        int top = getTopPos() + 4;
        guiGraphics.enableScissor(left, top, left + 438, top + 164);
    }

    public void popScissor(GuiGraphicsExtractor guiGraphics) {
        guiGraphics.disableScissor();
    }

    public void drawTexturedModalRect(GuiGraphicsExtractor guiGraphics, Identifier texture, int x, int y, int u, int v, int w, int h) {
        drawTexturedModalRect(guiGraphics, texture, x, y, u, v, w, h, RENDER_ROTATION.NORMAL);
    }

    public void drawTexturedModalRect(GuiGraphicsExtractor guiGraphics, Identifier texture, int x, int y, int u, int v, int w, int h, RENDER_ROTATION rotation) {
        if (rotation == RENDER_ROTATION.NORMAL) {
            guiGraphics.blit(RenderPipelines.GUI_TEXTURED, texture, x, y, (float) u, (float) v, w, h, 256, 256);
            return;
        }

        float angle = switch (rotation) {
            case ROTATE_90, ROTATE_90_FLIP -> (float) (Math.PI / 2.0);
            case ROTATE_180, FLIP_VERTICAL -> (float) Math.PI;
            case ROTATE_270, ROTATE_270_FLIP -> (float) (Math.PI * 1.5);
            default -> 0.0F;
        };
        boolean flipped = switch (rotation) {
            case FLIP_HORIZONTAL, ROTATE_90_FLIP, FLIP_VERTICAL, ROTATE_270_FLIP -> true;
            default -> false;
        };
        float centerX = x + w / 2.0F;
        float centerY = y + h / 2.0F;
        Matrix3x2fStack pose = guiGraphics.pose();
        pose.pushMatrix();
        try {
            pose.translate(centerX, centerY);
            pose.rotate(angle);
            if (flipped) {
                pose.scale(-1.0F, 1.0F);
            }
            pose.translate(-centerX, -centerY);
            guiGraphics.blit(RenderPipelines.GUI_TEXTURED, texture, x, y, (float) u, (float) v, w, h, 256, 256);
        } finally {
            pose.popMatrix();
        }
    }

    public enum RENDER_ROTATION {
        NORMAL, ROTATE_90, ROTATE_180, ROTATE_270, FLIP_HORIZONTAL, ROTATE_90_FLIP, FLIP_VERTICAL, ROTATE_270_FLIP;

        public RENDER_ROTATION getNextRotation() {
            switch (this) {
                case ROTATE_90 -> {
                    return RENDER_ROTATION.ROTATE_180;
                }
                case ROTATE_180 -> {
                    return RENDER_ROTATION.ROTATE_270;
                }
                case ROTATE_270 -> {
                    return RENDER_ROTATION.NORMAL;
                }
                case FLIP_HORIZONTAL -> {
                    return RENDER_ROTATION.ROTATE_90_FLIP;
                }
                case ROTATE_90_FLIP -> {
                    return RENDER_ROTATION.FLIP_VERTICAL;
                }
                case FLIP_VERTICAL -> {
                    return RENDER_ROTATION.ROTATE_270_FLIP;
                }
                case ROTATE_270_FLIP -> {
                    return RENDER_ROTATION.FLIP_HORIZONTAL;
                }
                default -> {
                    return RENDER_ROTATION.ROTATE_90;
                }
            }
        }

        public RENDER_ROTATION getFlippedRotation() {
            switch (this) {
                case ROTATE_90 -> {
                    return RENDER_ROTATION.ROTATE_90_FLIP;
                }
                case ROTATE_180 -> {
                    return RENDER_ROTATION.FLIP_VERTICAL;
                }
                case ROTATE_270 -> {
                    return RENDER_ROTATION.ROTATE_270_FLIP;
                }
                case FLIP_HORIZONTAL -> {
                    return RENDER_ROTATION.NORMAL;
                }
                case ROTATE_90_FLIP -> {
                    return RENDER_ROTATION.ROTATE_90;
                }
                case FLIP_VERTICAL -> {
                    return RENDER_ROTATION.ROTATE_180;
                }
                case ROTATE_270_FLIP -> {
                    return RENDER_ROTATION.ROTATE_270;
                }
                default -> {
                    return RENDER_ROTATION.FLIP_HORIZONTAL;
                }
            }
        }
    }
}
