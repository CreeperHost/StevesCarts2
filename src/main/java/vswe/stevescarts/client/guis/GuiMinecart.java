package vswe.stevescarts.client.guis;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import vswe.stevescarts.containers.ContainerMinecart;
import vswe.stevescarts.containers.slots.SlotBase;
import vswe.stevescarts.entities.EntityMinecartModular;
import vswe.stevescarts.helpers.Localization;
import vswe.stevescarts.helpers.ModuleCountPair;
import vswe.stevescarts.helpers.ResourceHelper;
import vswe.stevescarts.api.modules.ModuleBase;
import vswe.stevescarts.api.modules.data.ModuleData;
import vswe.stevescarts.network.PacketHandler;
import vswe.stevescarts.network.packets.PacketMinecartTurn;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class GuiMinecart extends AbstractContainerScreen<ContainerMinecart>
{
    private static ResourceLocation textureLeft;
    private static ResourceLocation textureRight;
    private static ResourceLocation textureReturn;
    private boolean isScrolling;
    private final int[] scrollBox;
    private EntityMinecartModular cart;
    private final int[] returnButton;

    public GuiMinecart(ContainerMinecart containerMinecart, final Inventory invPlayer, final Component iTextComponent)
    {
        super(containerMinecart, invPlayer, iTextComponent);
        scrollBox = new int[]{450, 15, 18, 225};
        returnButton = new int[]{324, 173, 24, 12};
        setup(containerMinecart.cart);
    }

    protected void setup(final EntityMinecartModular cart)
    {
        this.cart = cart;
        this.imageWidth = 478;
        this.imageHeight = 256;
    }

    @Override
    public void render(@NotNull PoseStack poseStack, int mouseX, int mouseY, float partialTicks)
    {
        this.renderBackground(poseStack);
        super.render(poseStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(poseStack, mouseX, mouseY);
        if (cart == null) return;
        if (cart.getModules() != null)
        {
            final ModuleBase thief = cart.getInterfaceThief();
            if (thief != null)
            {
                drawModuleForeground(poseStack, thief);
                drawModuleMouseOver(poseStack, thief, mouseX, mouseY);
            }
            else
            {
                for (final ModuleBase module : cart.getModules())
                {
                    drawModuleForeground(poseStack, module);
                }
                renderModuleListText(poseStack, mouseX, mouseY);
                for (final ModuleBase module : cart.getModules())
                {
                    drawModuleMouseOver(poseStack, module, mouseX, mouseY);
                }
                renderModuleListMouseOver(poseStack, mouseX, mouseY);
                renderReturnMouseOver(poseStack, mouseX, mouseY);
            }
        }
    }


    @Override
    protected void renderBg(@NotNull PoseStack poseStack, float p_230450_2_, int mouseX, int mouseY)
    {
        final int left = getGuiLeft();
        final int top = getGuiTop();
        ResourceHelper.bindResource(GuiMinecart.textureLeft);
        blit(poseStack, left, top, 0, 0, 256, 256);
        ResourceHelper.bindResource(GuiMinecart.textureRight);
        blit(poseStack, left + 256, top, 0, 0, imageWidth - 256, imageHeight);
        if (cart != null)
        {
            final ModuleBase thief = cart.getInterfaceThief();
            if (thief != null)
            {
                drawModuleSlots(poseStack, thief);
                drawModuleBackground(poseStack, thief, mouseX, mouseY);
                drawModuleBackgroundItems(thief, mouseX, mouseY);
                for (final ModuleBase module : cart.getModules())
                {
                    if (module.hasGui() && module.hasSlots())
                    {
                        final ArrayList<SlotBase> slotsList = module.getSlots();
                        for (final SlotBase slot : slotsList)
                        {
                            resetSlot(slot);
                        }
                    }
                }
            }
            else if (cart.getModules() != null)
            {
                //Draw Scroll Bar
                blit(poseStack, left + scrollBox[0], top + scrollBox[1], 222, 24, scrollBox[2], scrollBox[3]);
                blit(poseStack, left + scrollBox[0] + 2, top + scrollBox[1] + 2 + cart.getScrollY(), 240, 26 + (cart.canScrollModules ? 0 : 25), 14, 25);

                for (final ModuleBase module : cart.getModules())
                {
                    drawModuleSlots(poseStack, module);
                }

                for (final ModuleBase module : cart.getModules())
                {
                    drawModuleBackground(poseStack, module, mouseX, mouseY);
                }

                renderModuleList(mouseX, mouseY);
                renderReturnButton(poseStack, mouseX, mouseY);
                for (final ModuleBase module : cart.getModules())
                {
                    drawModuleBackgroundItems(module, mouseX, mouseY);
                }
            }
        }
    }

    @Override
    protected void renderLabels(@NotNull PoseStack poseStack, int p_230451_2_, int p_230451_3_)
    {
    }

    private void renderModuleList(int x, int y)
    {
        ArrayList<ModuleCountPair> moduleCounts = cart.getModuleCounts();

        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        for (int i = 0; i < moduleCounts.size(); ++i)
        {
            ModuleCountPair count = moduleCounts.get(i);
            drawModuleIcon(count.getData(), getGuiLeft() + getModuleDisplayX(i), getGuiTop() + getModuleDisplayY(i), 1.0f, 1.0f, 0.0f, 0.0f);
        }
        GlStateManager._disableBlend();
    }

    private void renderReturnButton(PoseStack matrixStack, int x, int y)
    {
        x -= getGuiLeft();
        y -= getGuiTop();
        ResourceHelper.bindResource(GuiMinecart.textureReturn);
        int uy = inRect(x, y, returnButton) ? 12 : 0;
        blit(matrixStack, returnButton[0] + getGuiLeft(), returnButton[1] + getGuiTop(), 0, uy, returnButton[2], returnButton[3]);
    }

    public void drawModuleIcon(ModuleData icon, final int targetX, final int targetY, final float sizeX, final float sizeY, final float offsetX, final float offsetY)
    {
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        itemRenderer.renderGuiItem(icon.getItemStack(), targetX, targetY);
    }

    private void renderModuleListText(PoseStack matrixStack, int x, int y)
    {
        x -= getGuiLeft();
        y -= getGuiTop();
        ArrayList<ModuleCountPair> moduleCounts = cart.getModuleCounts();
        font.draw(matrixStack, cart.getName(), getGuiLeft() + 5, getGuiTop() + 172, 4210752);
        GlStateManager._enableBlend();
        for (int i = 0; i < moduleCounts.size(); ++i)
        {
            ModuleCountPair count = moduleCounts.get(i);
            if (count.getCount() != 1)
            {
                int alpha = (int) ((inRect(x, y, getModuleDisplayX(i), getModuleDisplayY(i), 16, 16) ? 1.0f : 0.75f) * 256.0f);
                String str = String.valueOf(count.getCount());
                font.drawShadow(matrixStack, str, getModuleDisplayX(i) + 16 - font.width(str), getModuleDisplayY(i) + 8, 0xFFFFFF | alpha << 24);
            }
        }
        GlStateManager._disableBlend();
    }

    private void renderModuleListMouseOver(PoseStack matrixStack, int x, int y)
    {
        x -= getGuiLeft();
        y -= getGuiTop();
        ArrayList<ModuleCountPair> moduleCounts = cart.getModuleCounts();
        //        GlStateManager._color4f(1.0f, 1.0f, 1.0f, 1.0f);
        for (int i = 0; i < moduleCounts.size(); ++i)
        {
            final ModuleCountPair count = moduleCounts.get(i);
            if (inRect(x, y, getModuleDisplayX(i), getModuleDisplayY(i), 16, 16))
            {
                for (ModuleBase module : cart.getModules())
                {
                    if (module.getClass() == count.getData().getModuleClass())
                    {
                        if (module.hasExtraData())
                        {
                            count.setExtraData(module.getExtraData());
                        }
                        break;
                    }
                }
                drawMouseOver(matrixStack, count.toString(), x, y);
            }
        }
    }

    public void drawMouseOver(PoseStack matrixStack, final String str, final int x, final int y)
    {
        final String[] split = str.split("\n");
        final List<String> text = new ArrayList<>(Arrays.asList(split));
        List<Component> list = new ArrayList<>();
        for (String s : text)
        {
            list.add(Component.literal(s));
        }
        renderTooltip(matrixStack, list, Optional.empty(), getGuiLeft() + x, getGuiTop() + y);
    }

    private void renderReturnMouseOver(PoseStack matrixStack, int x, int y)
    {
        x -= getGuiLeft();
        y -= getGuiTop();

        if (inRect(x, y, returnButton))
        {
            drawMouseOver(matrixStack, Localization.GUI.CART.RETURN.translate(), x, y);
        }
    }

    private int getModuleDisplayX(int id)
    {
        return id % 8 * 18 + 7;
    }

    private int getModuleDisplayY(int id)
    {
        return id / 8 * 18 + 182;
    }

    @Override
    public boolean mouseClicked(double x, double y, int button)
    {
        ModuleBase thief = cart.getInterfaceThief();
        if (thief != null)
        {
            handleModuleMouseClicked(thief, (int) x, (int) y, button);
        }
        else if (cart.getModules() != null)
        {
            if (inRect((int) x - getGuiLeft(), (int) y - getGuiTop(), scrollBox[0], scrollBox[1], scrollBox[2], scrollBox[3]))
            {
                scrollToMouse(y);
                isScrolling = true;
                return true;
            }
            for (ModuleBase module : cart.getModules())
            {
                handleModuleMouseClicked(module, (int) x, (int) y, button);
            }
            if (inRect((int) x - getGuiLeft(), (int) y - getGuiTop(), returnButton))
            {
                PacketHandler.sendToServer(new PacketMinecartTurn(cart.getId()));
            }
        }
        return super.mouseClicked(x, y, button);
    }

    @Override
    public boolean mouseReleased(double p_97812_, double p_97813_, int p_97814_) {
        isScrolling = false;
        return super.mouseReleased(p_97812_, p_97813_, p_97814_);
    }

    protected boolean inRect(final int x, final int y, final int x1, final int y1, final int sizeX, final int sizeY)
    {
        return x >= x1 && x <= x1 + sizeX && y >= y1 && y <= y1 + sizeY;
    }

    public boolean inRect(final int x, final int y, final int[] coords)
    {
        return coords != null && x >= coords[0] && x < coords[0] + coords[2] && y >= coords[1] && y < coords[1] + coords[3];
    }

    @Override
    public boolean mouseDragged(double x, double y, int button, double moveX, double moveY) {
        if (super.mouseDragged(x, y, button, moveX, moveY)) return true;
        if (isScrolling)
        {
            scrollToMouse(y);
            return true;
        }
        return false;
    }

    @Override
    public void mouseMoved(final double x, final double y)
    {
        super.mouseMoved(x, y);
        int button = 0;
        if (isScrolling)
        {
            scrollToMouse(y);
        }
        if (cart.getModules() != null)
        {
            final ModuleBase thief = cart.getInterfaceThief();
            if (thief != null)
            {
                handleModuleMouseMoved(thief, (int) x, (int) y, button);
            }
            else
            {
                for (final ModuleBase module : cart.getModules())
                {
                    handleModuleMouseMoved(module, (int) x, (int) y, button);
                }
            }
        }
    }

    private void scrollToMouse(double mouseY) {
        int temp = (int) mouseY - getGuiTop() - 12 - (scrollBox[1] + 2);
        if (temp < 0)
        {
            temp = 0;
        }
        else if (temp > 198)
        {
            temp = 198;
        }
        cart.setScrollY(temp);
    }

    @Override
    public boolean keyPressed(int p_231046_1_, int id, int p_231046_3_)
    {
        super.keyPressed(p_231046_1_, id, p_231046_3_);
        if (cart.getModules() != null)
        {
            final ModuleBase thief = cart.getInterfaceThief();
            if (thief != null)
            {
                handleModuleKeyPress(thief, id, p_231046_3_);
            }
            else
            {
                for (final ModuleBase module : cart.getModules())
                {
                    handleModuleKeyPress(module, id, p_231046_3_);
                }
            }
        }
        return true;
    }

    @OnlyIn(Dist.CLIENT)
    private void drawModuleForeground(PoseStack matrixStack, ModuleBase module)
    {
        if (module.hasGui())
        {
            module.drawForeground(matrixStack, this);
            if (module.useButtons())
            {
                module.drawButtonText(matrixStack, this);
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    private void drawModuleMouseOver(PoseStack matrixStack, ModuleBase module, final int x, final int y)
    {
        if (module.hasGui())
        {
            module.drawMouseOver(matrixStack, this, x - getGuiLeft() - module.getX(), y - getGuiTop() - module.getY());
            if (module.useButtons())
            {
                module.drawButtonOverlays(matrixStack, this, x - getGuiLeft() - module.getX(), y - getGuiTop() - module.getY());
            }
        }
    }

    private void drawModuleSlots(PoseStack matrixStack, final ModuleBase module)
    {
        if (module.hasGui() && module.hasSlots())
        {
            final ArrayList<SlotBase> slotsList = module.getSlots();
            for (final SlotBase slot : slotsList)
            {
                final int[] rect = {slot.getX() + 1, slot.getY() + 1, 16, 16};
                module.handleScroll(rect);
                final boolean drawAll = rect[3] == 16;
                if (drawAll)
                {
                    slot.x = slot.getX() + module.getX() + 1;
                    slot.y = slot.getY() + module.getY() - cart.getRealScrollY();
                }
                else
                {
                    resetSlot(slot);
                }
                module.drawImage(matrixStack, this, slot.getX(), slot.getY() - 1, getXSize() - 256, 0, 18, 18);
                if (!drawAll)
                {
                    module.drawImage(matrixStack, this, slot.getX() + 1, slot.getY(), getXSize() - 256 + 18, 1, 16, 16);
                }
            }
        }
    }

    private void resetSlot(final SlotBase slot)
    {
        slot.x = -9001;
        slot.y = -9001;
    }

    private void drawModuleBackground(PoseStack matrixStack, final ModuleBase module, final int x, final int y)
    {
        if (module.hasGui())
        {
            module.drawBackground(matrixStack, this, x - getGuiLeft() - module.getX(), y - getGuiTop() - module.getY());
            if (module.useButtons())
            {
                module.drawButtons(matrixStack, this, x - getGuiLeft() - module.getX(), y - getGuiTop() - module.getY());
            }
        }
    }

    private void drawModuleBackgroundItems(final ModuleBase module, final int x, final int y)
    {
        if (module.hasGui())
        {
            module.drawBackgroundItems(this, x - getGuiLeft() - module.getX(), y - getGuiTop() - module.getY());
        }
    }

    private void handleModuleMouseClicked(final ModuleBase module, final int x, final int y, final int button)
    {
        module.mouseClicked(this, x - getGuiLeft() - module.getX(), y - getGuiTop() - module.getY(), button);
        if (module.useButtons())
        {
            module.mouseClickedButton(this, x - getGuiLeft() - module.getX(), y - getGuiTop() - module.getY(), button);
        }
    }

    private void handleModuleMouseMoved(final ModuleBase module, final int x, final int y, final int button)
    {
        module.mouseMovedOrUp(this, x - getGuiLeft() - module.getX(), y - getGuiTop() - module.getY(), button);
    }

    private void handleModuleKeyPress(final ModuleBase module, final int id, final int extraInformation)
    {
        module.keyPress(this, id, extraInformation);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount)
    {
        super.mouseScrolled(mouseX, mouseY, amount);
        if (amount < 0)
        {
            amount = -1;
        }
        if (amount > 0)
        {
            amount = 1;
        }
        if (this.inRect((int) mouseX - getGuiLeft(), (int) mouseY - getGuiTop(), 0, 0, imageWidth, imageHeight))
        {
            int moduleSize = this.cart.modularSpaceHeight;
            int scroll = cart.getScrollY() + ((int) -amount * 4000) / (moduleSize - EntityMinecartModular.MODULAR_SPACE_HEIGHT);
            scroll = Mth.clamp(scroll, 0, 198);
            cart.setScrollY(scroll);
        }
        return true;
    }

    public void pushScissor() {
        GuiHelper.pushGuiScissor(minecraft, getGuiLeft() + 5, getGuiTop() + 4, 438, 164, width, height);
    }

    public void popScissor() {
        GuiHelper.popScissor();
    }

    public void drawTexturedModalRect(PoseStack matrixStack, final int x, final int y, final int u, final int v, final int w, final int h, final RENDER_ROTATION rotation)
    {
        final float fw = 0.00390625f;
        final float fy = 0.00390625f;

        final double a = (u) * fw;
        final double b = (u + w) * fw;
        final double c = (v + h) * fy;
        final double d = (v) * fy;

        final double[] ptA = {a, c};
        final double[] ptB = {b, c};
        final double[] ptC = {b, d};
        final double[] ptD = {a, d};

        double[] pt1, pt2, pt3, pt4;

        switch (rotation)
        {
            default -> {
                pt1 = ptA;
                pt2 = ptB;
                pt3 = ptC;
                pt4 = ptD;
            }
            case ROTATE_90 -> {
                pt1 = ptB;
                pt2 = ptC;
                pt3 = ptD;
                pt4 = ptA;
            }
            case ROTATE_180 -> {
                pt1 = ptC;
                pt2 = ptD;
                pt3 = ptA;
                pt4 = ptB;
            }
            case ROTATE_270 -> {
                pt1 = ptD;
                pt2 = ptA;
                pt3 = ptB;
                pt4 = ptC;
            }
            case FLIP_HORIZONTAL -> {
                pt1 = ptB;
                pt2 = ptA;
                pt3 = ptD;
                pt4 = ptC;
            }
            case ROTATE_90_FLIP -> {
                pt1 = ptA;
                pt2 = ptD;
                pt3 = ptC;
                pt4 = ptB;
            }
            case FLIP_VERTICAL -> {
                pt1 = ptD;
                pt2 = ptC;
                pt3 = ptB;
                pt4 = ptA;
            }
            case ROTATE_270_FLIP -> {
                pt1 = ptC;
                pt2 = ptB;
                pt3 = ptA;
                pt4 = ptD;
            }
        }

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        Tesselator tessellator = Tesselator.getInstance();
        BufferBuilder buff = tessellator.getBuilder();
        buff.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        float zLevel = 1F;

        buff.vertex((x), y + h, zLevel).uv((float) pt1[0], (float) pt1[1]).endVertex();
        buff.vertex((x + w), y + h, zLevel).uv((float) pt2[0], (float) pt2[1]).endVertex();
        buff.vertex((x + w), y, zLevel).uv((float) pt3[0], (float) pt3[1]).endVertex();
        buff.vertex((x), y, zLevel).uv((float) pt4[0], (float) pt4[1]).endVertex();
        tessellator.end();
    }

    public enum RENDER_ROTATION
    {
        NORMAL, ROTATE_90, ROTATE_180, ROTATE_270, FLIP_HORIZONTAL, ROTATE_90_FLIP, FLIP_VERTICAL, ROTATE_270_FLIP;

        public RENDER_ROTATION getNextRotation()
        {
            switch (this)
            {
                default -> {
                    return RENDER_ROTATION.ROTATE_90;
                }
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
            }
        }

        public RENDER_ROTATION getFlippedRotation()
        {
            switch (this)
            {
                default -> {
                    return RENDER_ROTATION.FLIP_HORIZONTAL;
                }
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
            }
        }
    }

    static
    {
        GuiMinecart.textureLeft = ResourceHelper.getResource("/gui/guiBase1.png");
        GuiMinecart.textureRight = ResourceHelper.getResource("/gui/guiBase2.png");
        GuiMinecart.textureReturn = ResourceHelper.getResource("/gui/return.png");
    }
}
