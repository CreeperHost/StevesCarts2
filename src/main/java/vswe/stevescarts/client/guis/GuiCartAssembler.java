package vswe.stevescarts.client.guis;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import vswe.stevescarts.blocks.tileentities.TileEntityCartAssembler;
import vswe.stevescarts.containers.ContainerCartAssembler;
import vswe.stevescarts.containers.slots.SlotAssembler;
import vswe.stevescarts.entitys.EntityMinecartModular;
import vswe.stevescarts.helpers.DropDownMenuItem;
import vswe.stevescarts.helpers.Localization;
import vswe.stevescarts.helpers.ResourceHelper;
import vswe.stevescarts.helpers.TitleBox;
import vswe.stevescarts.items.ItemCartModule;
import vswe.stevescarts.modules.data.ModuleData;
import vswe.stevescarts.modules.data.ModuleDataHull;
import vswe.stevescarts.network.PacketHandler;
import vswe.stevescarts.network.packets.PacketCreateCart;

import java.util.ArrayList;
import java.util.List;

public class GuiCartAssembler extends AbstractContainerScreen<ContainerCartAssembler>
{
    private ArrayList<TextWithColor> statusLog;
    private boolean hasErrors;
    private boolean firstLoad;
    private static ResourceLocation[] backgrounds;
    private static final ResourceLocation textureLeft;
    private static final ResourceLocation textureRight;
    private static final ResourceLocation textureExtra;
    private int[] assembleRect;
    private String validChars;
    private int dropdownX;
    private int dropdownY;
    private int scrollingX;
    private int scrollingY;
    private boolean isScrolling;
    private int[] blackBackground;
    private ContainerCartAssembler containerCartAssembler;
    private TileEntityCartAssembler assembler;

    private final int[] assemblingProgRect = {375, 180, 115, 11};
    private final int[] fuelProgRect = {375, 200, 115, 11};
    private boolean spin = true;

    public GuiCartAssembler(ContainerCartAssembler containerCartAssembler, Inventory playerInventory, Component iTextComponent)
    {
        super(containerCartAssembler, playerInventory, iTextComponent);
        firstLoad = true;
        assembleRect = new int[]{390, 160, 80, 11};
        validChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        dropdownX = -1;
        dropdownY = -1;
        blackBackground = new int[]{145, 15, 222, 148};
        this.containerCartAssembler = containerCartAssembler;
        this.assembler = containerCartAssembler.getAssembler();
        this.imageWidth = 512;
        this.imageHeight = 256;
    }

    private void updateErrorList()
    {
        final ArrayList<TextWithColor> lines = new ArrayList<>();
        if (this.assembler.getItem(0).isEmpty())
        {
            this.addText(lines, Localization.GUI.ASSEMBLER.ASSEMBLE_INSTRUCTION.translate());
            this.hasErrors = true;
        }
        else
        {
            ItemCartModule cartModule = (ItemCartModule) this.assembler.getItem(0).getItem();
            final ModuleData hulldata = cartModule.getModuleData();
            if (hulldata == null || !(hulldata instanceof ModuleDataHull))
            {
                this.addText(lines, Localization.GUI.ASSEMBLER.INVALID_HULL.translate(), 10357518);
                this.hasErrors = true;
            }
            else
            {
                final ModuleDataHull hull = (ModuleDataHull) hulldata;
                this.addText(lines, Localization.GUI.ASSEMBLER.HULL_CAPACITY.translate() + ": " + hull.getCapacity());
                this.addText(lines, Localization.GUI.ASSEMBLER.COMPLEXITY_CAP.translate() + ": " + hull.getComplexityMax());
                this.addText(lines, Localization.GUI.ASSEMBLER.TOTAL_COST.translate() + ": " + this.assembler.getTotalCost());
                this.addText(lines, Localization.GUI.ASSEMBLER.TOTAl_TIME.translate() + ": " + this.formatTime((int) (this.assembler.generateAssemblingTime() / this.assembler.getEfficiency())));
                this.addNewLine(lines);
                final ArrayList<String> errors = this.assembler.getErrors();
                this.hasErrors = (errors.size() > 0);
                if (errors.size() == 0)
                {
                    this.addText(lines, Localization.GUI.ASSEMBLER.NO_ERROR.translate(), 22566);
                }
                else
                {
                    for (final String error : errors)
                    {
                        this.addText(lines, error, 10357518);
                    }
                }
            }
        }
        this.statusLog = lines;
    }

    private void addText(final ArrayList<TextWithColor> lines, final String text)
    {
        addText(lines, text, 4210752);
    }

    private void addText(final ArrayList<TextWithColor> lines, final String text, final int color)
    {
        List<IReorderingProcessor> newlines = RenderComponentsUtil.wrapComponents(new StringTextComponent(text), 130, font);
        for (final IReorderingProcessor line : newlines)
        {
            lines.add(new TextWithColor(line, color));
        }
    }

    private void addNewLine(final ArrayList<TextWithColor> lines)
    {
        lines.add(null);
    }

    @Override
    protected void renderBg(PoseStack matrixStack, float p_230450_2_, int mouseX, int mouseY)
    {
        if (firstLoad)
        {
            updateErrorList();
            firstLoad = false;
        }
//        GlStateManager._color4f(1.0f, 1.0f, 1.0f, 1.0f);
        final int j = getGuiLeft();
        final int k = getGuiTop();
        ResourceHelper.bindResource(GuiCartAssembler.backgrounds[assembler.getSimulationInfo().getBackground()]);
        blit(matrixStack, j + 143, k + 15, 0, 0, 220, 148);
        ResourceHelper.bindResource(GuiCartAssembler.textureLeft);
        blit(matrixStack, j, k, 0, 0, 256, imageHeight);
        ResourceHelper.bindResource(GuiCartAssembler.textureRight);
        blit(matrixStack, j + 256, k, 0, 0, imageWidth - 256, imageHeight);
        blit(matrixStack, j + 256, k, 0, 0, imageWidth - 256, imageHeight);
        ResourceHelper.bindResource(GuiCartAssembler.textureExtra);
        final ArrayList<SlotAssembler> slots = assembler.getSlots();
        for (final SlotAssembler slot : slots)
        {
            int targetX = slot.getX() - 1;
            int targetY = slot.getY() - 1;
            int size;
            int srcX;
            int srcY;
            if (slot.useLargeInterface())
            {
                targetX -= 3;
                targetY -= 3;
                size = 24;
                srcX = 0;
                srcY = 0;
            }
            else
            {
                size = 18;
                if (!slot.getItem().isEmpty() && TileEntityCartAssembler.getSlotStatus(slot.getItem()) <= 0)
                {
                    if (TileEntityCartAssembler.getSlotStatus(slot.getItem()) == TileEntityCartAssembler.getRemovedSize())
                    {
                        srcX = 140;
                    }
                    else
                    {
                        srcX = 122;
                    }
                    srcY = 40;
                }
                else
                {
                    srcX = 24;
                    srcY = 0;
                }
            }
            //Slots
            blit(matrixStack, j + targetX, k + targetY, srcX, srcY, size, size);
            int animationTick = slot.getAnimationTick();
            if (animationTick < 0)
            {
                animationTick = 0;
            }
            if (animationTick < 8 && !slot.useLargeInterface())
            {
                blit(matrixStack, j + targetX + 1, k + targetY + 1, 0, 24 + animationTick, 16, 8 - animationTick);
                blit(matrixStack, j + targetX + 1, k + targetY + 1 + 8 + animationTick, 0, 32, 16, 8 - animationTick);
            }
            slot.update();
        }
        for (final TitleBox box : assembler.getTitleBoxes())
        {
            final int targetY2 = box.getY() - 12;
            final int targetX2 = box.getX();
            blit(matrixStack, j + targetX2, k + targetY2, 0, 40, 115, 11);
//            GlStateManager._color4f((box.getColor() >> 16) / 255.0f, (box.getColor() >> 8 & 0xFF) / 255.0f, (box.getColor() & 0xFF) / 255.0f, 1.0f);
            blit(matrixStack, j + targetX2 + 8, k + targetY2 + 2, 0, 51 + box.getID() * 7, 115, 7);
//            GlStateManager._color4f(1.0f, 1.0f, 1.0f, 1.0f);
        }
        int srcX2 = 42;
        int srcY2 = 0;
        if (assembler.getIsDisassembling())
        {
            srcX2 = 158;
            srcY2 = 40;
        }
        if (hasErrors)
        {
            srcY2 += 22;
        }
        else if (inRect(mouseX - j, mouseY - k, assembleRect))
        {
            srcY2 += 11;
        }
        blit(matrixStack, j + assembleRect[0], k + assembleRect[1], srcX2, srcY2, assembleRect[2], assembleRect[3]);
        float assemblingProgress = 0.0f;
        if (containerCartAssembler.getIsAssembling())
        {
            assemblingProgress = (float) containerCartAssembler.getAssemblingTime() / (float) containerCartAssembler.getMaxAssemblingTime();
        }
        drawProgressBar(matrixStack, assemblingProgRect, assemblingProgress, 22, mouseX, mouseY);
        drawProgressBar(matrixStack, fuelProgRect, (float) containerCartAssembler.getFuel() / (float) assembler.getMaxFuelLevel(), 31, mouseX, mouseY);
        renderDropDownMenu(matrixStack, mouseX, mouseY);
        renderEntityInInventory(leftPos + 256, topPos + 100, 50, (float) (leftPos + 51) - mouseX, (float) (j + 75 - 50) - mouseY);
    }

    @Override
    public void renderLabels(PoseStack matrixStack, int mouseX, int mouseY)
    {
        font.draw(matrixStack, Localization.GUI.ASSEMBLER.TITLE.translate(), 18, 6, 4210752);
    }

    @Override
    public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks)
    {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(matrixStack, mouseX, mouseY);
        if (assembler.isErrorListOutdated)
        {
            updateErrorList();
            assembler.isErrorListOutdated = false;
        }
        final ArrayList<TextWithColor> lines = statusLog;
        if (lines != null)
        {
            int lineCount = lines.size();
            boolean dotdotdot = false;
            if (lineCount > 11)
            {
                lineCount = 10;
                dotdotdot = true;
            }
            for (int i = 0; i < lineCount; ++i)
            {
                final TextWithColor info = lines.get(i);
                if (info != null)
                {
                    font.draw(matrixStack, info.getText(), leftPos + 370, topPos + 40 + i * 10, info.getColor());
                }
            }
            if (dotdotdot)
            {
                font.draw(matrixStack, "...", leftPos + 370, topPos + 40 + lineCount * 10, 4210752);
            }
        }
        float assemblingProgress;
        String assemblingInfo;
        if (containerCartAssembler.getIsAssembling())
        {
            assemblingProgress = (float) containerCartAssembler.getAssemblingTime() / (float) containerCartAssembler.getMaxAssemblingTime();
            assemblingInfo = Localization.GUI.ASSEMBLER.ASSEMBLE_PROGRESS.translate() + ": " + formatProgress(assemblingProgress);
            assemblingInfo = assemblingInfo + ": " + Localization.GUI.ASSEMBLER.TIME_LEFT.translate() + ": " + formatTime((int) ((containerCartAssembler.getMaxAssemblingTime() - containerCartAssembler.getAssemblingTime()) / assembler.getEfficiency()));
        }
        else
        {
            assemblingInfo = Localization.GUI.ASSEMBLER.IDLE_MESSAGE.translate();
        }
        if (!hasErrors)
        {
            if (assembler.getIsDisassembling())
            {
                drawProgressBarInfo(matrixStack, assembleRect, mouseX, mouseY, Localization.GUI.ASSEMBLER.MODIFY_CART.translate());
            }
            else
            {
                drawProgressBarInfo(matrixStack, assembleRect, mouseX, mouseY, Localization.GUI.ASSEMBLER.ASSEMBLE_CART.translate());
            }
        }
        drawProgressBarInfo(matrixStack, assemblingProgRect, mouseX, mouseY, assemblingInfo);
        drawProgressBarInfo(matrixStack, fuelProgRect, mouseX, mouseY, Localization.GUI.ASSEMBLER.FUEL_LEVEL.translate() + ": " + containerCartAssembler.getFuel() + "/" + assembler.getMaxFuelLevel());
    }

    private String formatProgress(final float progress)
    {
        final float percentage = (int) (progress * 10000.0f) / 100.0f;
        return String.format("%05.2f%%", percentage);
    }

    private String formatTime(int ticks)
    {
        int seconds = ticks / 20;
        ticks -= seconds * 20;
        int minutes = seconds / 60;
        seconds -= minutes * 60;
        final int hours = minutes / 60;
        minutes -= hours * 60;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    private void drawProgressBarInfo(PoseStack matrixStack, final int[] rect, final int x, final int y, final String str)
    {
        if (inRect(x - getGuiLeft(), y - getGuiTop(), rect))
        {
            renderTooltip(matrixStack, new TranslatableComponent(str), x, y);
        }
    }

    private void drawProgressBar(PoseStack matrixStack, int[] rect, float progress, int barSrcY, int x, int y)
    {
        final int j = getGuiLeft();
        final int k = getGuiTop();
        int boxSrcY = 0;
        if (inRect(x - j, y - k, rect))
        {
            boxSrcY = 11;
        }
        blit(matrixStack, j + rect[0], k + rect[1], 122, boxSrcY, rect[2], rect[3]);
        if (progress != 0.0f)
        {
            if (progress > 1.0f)
            {
                progress = 1.0f;
            }
            blit(matrixStack, j + rect[0] + 1, k + rect[1] + 1, 122, barSrcY, (int) (rect[2] * progress), rect[3] - 2);
        }
    }

    public void renderEntityInInventory(int p_228187_0_, int p_228187_1_, int p_228187_2_, float p_228187_3_, float p_228187_4_)
    {
        assembler.createPlaceholder();
        //		float f = (float)Math.atan((double)(p_228187_3_ / 40.0F));
//        		float f1 = (float)Math.atan((double)(p_228187_4_ / 40.0F));
        RenderSystem.pushMatrix();
        RenderSystem.translatef((float) p_228187_0_, (float) p_228187_1_, 1050.0F);
        RenderSystem.scalef(1.0F, 1.0F, -1.0F);
        PoseStack matrixstack = new PoseStack();
        matrixstack.translate(0.0D, 0.0D, 1000.0D);
        matrixstack.scale((float) p_228187_2_, (float) p_228187_2_, (float) p_228187_2_);
        Quaternion quaternion = Vector3f.YN.rotationDegrees(assembler.getRoll() * 10F);
        Quaternion quaternion1 = Vector3f.XP.rotationDegrees(180);
        if(spin)
        {
            matrixstack.mulPose(quaternion);
        }

        matrixstack.mulPose(quaternion1);
        EntityMinecartModular p_228187_5_ = assembler.getPlaceholder();
        float f3 = p_228187_5_.yRot;
        float f4 = p_228187_5_.xRot;
        //		p_228187_5_.yRot = 180.0F + f * 40.0F;
        //		p_228187_5_.xRot = -f1 * 20.0F;
        EntityRendererManager entityrenderermanager = Minecraft.getInstance().getEntityRenderDispatcher();
        //		quaternion1.conj();
        //		entityrenderermanager.overrideCameraOrientation(quaternion1);
        entityrenderermanager.setRenderShadow(false);
        IRenderTypeBuffer.Impl irendertypebuffer$impl = Minecraft.getInstance().renderBuffers().bufferSource();
        RenderSystem.runAsFancy(() ->
        {
            if (p_228187_5_ != null)
                entityrenderermanager.render(p_228187_5_, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, matrixstack, irendertypebuffer$impl, 15728880);
        });
        irendertypebuffer$impl.endBatch();
        entityrenderermanager.setRenderShadow(true);
        p_228187_5_.yRot = f3;
        p_228187_5_.xRot = f4;
        RenderSystem.popMatrix();
    }

    private void renderDropDownMenu(PoseStack matrixStack, final int x, final int y)
    {
        GlStateManager._pushMatrix();
        GlStateManager._translatef(0.0f, 0.0f, 200.0f);
        final int j = getGuiLeft();
        final int k = getGuiTop();
        if (dropdownX != -1 && dropdownY != -1)
        {
            final ArrayList<DropDownMenuItem> items = assembler.getDropDown();
            for (int i = 0; i < items.size(); ++i)
            {
                final DropDownMenuItem item = items.get(i);
                final int[] rect = item.getRect(dropdownX, dropdownY, i);
                int[] subrect = new int[0];
                int srcX = 0;
                int srcY = item.getIsLarge() ? 113 : 93;
                blit(matrixStack, j + rect[0], k + rect[1], srcX, srcY, rect[2], rect[3]);
                if (item.getIsLarge())
                {
                    drawString(matrixStack, item.getName(), j + rect[0] + 55, k + rect[1] + 7);
                }
                blit(matrixStack, j + rect[0] + 34, k + rect[1] + 2, item.getImageID() % 16 * 16, 179 + item.getImageID() / 16 * 16, 16, 16);
                if (item.hasSubmenu())
                {
                    subrect = item.getSubRect(dropdownX, dropdownY, i);
                    srcX = (item.getIsSubMenuOpen() ? 0 : 43);
                    srcY = 133;
                    blit(matrixStack, j + subrect[0], k + subrect[1], srcX, srcY, subrect[2], subrect[3]);
                }
                switch (item.getType())
                {
                    case BOOL:
                    {
                        drawBooleanBox(matrixStack, x, y, 5 + rect[0], 5 + rect[1], item.getBOOL());
                        break;
                    }
                    case INT:
                    {
                        if (item.getIsSubMenuOpen())
                        {
                            drawIncreamentBox(matrixStack, x, y, getOffSetXForSubMenuBox(0, 2) + subrect[0], 3 + subrect[1]);
                            drawDecreamentBox(matrixStack, x, y, getOffSetXForSubMenuBox(1, 2) + subrect[0], 3 + subrect[1]);
                        }
                        final int targetX = rect[0] + 16;
                        final int targetY = rect[1] + 7;
                        final int valueToWrite = item.getINT();
                        if (valueToWrite >= 10)
                        {
                            drawDigit(matrixStack, valueToWrite / 10, -1, targetX, targetY);
                            drawDigit(matrixStack, valueToWrite % 10, 1, targetX, targetY);
                            break;
                        }
                        drawDigit(matrixStack, valueToWrite, 0, targetX, targetY);
                        break;
                    }
                    case MULTIBOOL:
                    {
                        if (item.getIsSubMenuOpen())
                        {
                            for (int count = item.getMULTIBOOLCount(), bool = 0; bool < count; ++bool)
                            {
                                drawBooleanBox(matrixStack, x, y, subrect[0] + getOffSetXForSubMenuBox(bool, count), subrect[1] + 3, item.getMULTIBOOL(bool));
                            }
                            break;
                        }
                        break;
                    }
                }
            }
        }
        GlStateManager._popMatrix();
    }

    private void drawString(PoseStack matrixStack, String str, final int x, final int y)
    {
        str = str.toUpperCase();
        for (int i = 0; i < str.length(); ++i)
        {
            final char c = str.charAt(i);
            final int index = validChars.indexOf(c);
            if (index != -1)
            {
                blit(matrixStack, x + 7 * i, y, 8 * index, 165, 6, 7);
            }
        }
    }

    private int getOffSetXForSubMenuBox(final int id, final int count)
    {
        return 2 + (int) (20.0f + (id - count / 2.0f) * 10.0f);
    }

    private void drawDigit(PoseStack matrixStack, int digit, int offset, int targetX, int targetY)
    {
        final int srcX = digit * 8;
        final int srcY = 172;
        targetX += offset * 4;
        blit(matrixStack, getGuiLeft() + targetX, getGuiTop() + targetY, srcX, srcY, 6, 7);
    }

    private void drawIncreamentBox(PoseStack matrixStack, int mouseX, int mouseY, int x, int y)
    {
        drawStandardBox(matrixStack, mouseX, mouseY, x, y, 10);
    }

    private void drawDecreamentBox(PoseStack matrixStack, int mouseX, int mouseY, int x, int y)
    {
        drawStandardBox(matrixStack, mouseX, mouseY, x, y, 20);
    }

    private void drawBooleanBox(PoseStack matrixStack, int mouseX, int mouseY, int x, int y, boolean itemvalue)
    {
        drawStandardBox(matrixStack, mouseX, mouseY, x, y, 0);
        if (itemvalue)
        {
            blit(matrixStack, getGuiLeft() + x + 2, getGuiTop() + y + 2, 0, 159, 6, 6);
        }
    }

    public boolean inRect(final int x, final int y, final int[] coords)
    {
        return coords != null && x >= coords[0] && x < coords[0] + coords[2] && y >= coords[1] && y < coords[1] + coords[3];
    }

    private void drawStandardBox(PoseStack matrixStack, int mouseX, int mouseY, int x, int y, int srcX)
    {
        final int targetX = getGuiLeft() + x;
        final int targetY = getGuiTop() + y;
        final int srcY = 149;
        blit(matrixStack, targetX, targetY, srcX, srcY, 10, 10);
        if (inRect(mouseX, mouseY, new int[]{targetX, targetY, 10, 10}))
        {
            blit(matrixStack, targetX, targetY, 30, srcY, 10, 10);
        }
    }

    private boolean clickBox(final int mouseX, final int mouseY, final int x, final int y)
    {
        return inRect(mouseX, mouseY, new int[]{x, y, 10, 10});
    }

    @Override
    public void mouseMoved(final double x0, final double y0) {

    }

    @Override
    public boolean mouseDragged(double x0, double y0, int button, double p_231045_6_, double p_231045_8_)
    {
        super.mouseMoved(x0, y0);
        final int x = (int) (x0 - getGuiLeft());
        final int y = (int) (y0 - getGuiTop());
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
        if (isScrolling)
        {
            if (button != -1)
            {
                isScrolling = false;
                assembler.setSpinning(true);
            }
            else
            {
                assembler.setYaw(assembler.getYaw() + x - scrollingX);
                assembler.setRoll(assembler.getRoll() + y - scrollingY);
                scrollingX = x;
                scrollingY = y;
            }
        }
        return true;
    }

    @Override
    public boolean mouseClicked(double x0, double y0, int button)
    {
        super.mouseClicked(x0, y0, button);
        int x = (int) (x0 - getGuiLeft());
        int y = (int) (y0 - getGuiTop());
        if (inRect(x, y, assembleRect))
        {
            PacketHandler.sendToServer(new PacketCreateCart(this.assembler.getBlockPos(), 0, new byte[0]));
            return true;
        }
        else if (inRect(x, y, blackBackground))
        {
            if (button == 0)
            {
                if (!isScrolling)
                {
                    scrollingX = x;
                    scrollingY = y;
                    isScrolling = true;
                    if(spin)
                    {
                        spin = false;
                        return true;
                    }
                    else
                    {
                        spin = true;
                        return true;
                    }
                }
            }
            else if (button == 1)
            {
                dropdownX = x;
                dropdownY = y;
                if (dropdownY + assembler.getDropDown().size() * 20 > 164)
                {
                    dropdownY = 164 - assembler.getDropDown().size() * 20;
                    return true;
                }
            }
        }
        else
        {
            final ArrayList<SlotAssembler> slots = assembler.getSlots();
            for (int i = 1; i < slots.size(); ++i)
            {
                final SlotAssembler slot = slots.get(i);
                final int targetX = slot.getX() - 1;
                final int targetY = slot.getY() - 1;
                final int size = 18;
                if (inRect(x, y, new int[]{targetX, targetY, size, size}) && !slot.getItem().isEmpty() && TileEntityCartAssembler.getSlotStatus(slot.getItem()) <= 0)
                {
                    //					PacketStevesCarts.sendPacket(1, new byte[] { (byte) i });
                    return true;
                }
            }
        }
        if (button == 0 && dropdownX != -1 && dropdownY != -1)
        {
            boolean anyLargeItem = false;
            final ArrayList<DropDownMenuItem> items = assembler.getDropDown();
            for (int j = 0; j < items.size(); ++j)
            {
                final DropDownMenuItem item = items.get(j);
                if (item.getIsLarge())
                {
                    anyLargeItem = true;
                    final int[] rect = item.getRect(dropdownX, dropdownY, j);
                    int[] subrect = new int[0];
                    if (item.hasSubmenu() && item.getIsSubMenuOpen())
                    {
                        subrect = item.getSubRect(dropdownX, dropdownY, j);
                    }
                    switch (item.getType())
                    {
                        case BOOL:
                        {
                            if (clickBox(x, y, 5 + rect[0], 5 + rect[1]))
                            {
                                item.setBOOL(!item.getBOOL());
                                break;
                            }
                            break;
                        }
                        case INT:
                        {
                            if (!item.getIsSubMenuOpen())
                            {
                                break;
                            }
                            if (clickBox(x, y, getOffSetXForSubMenuBox(0, 2) + subrect[0], 3 + subrect[1]))
                            {
                                item.setINT(item.getINT() + 1);
                            }
                            if (clickBox(x, y, getOffSetXForSubMenuBox(1, 2) + subrect[0], 3 + subrect[1]))
                            {
                                item.setINT(item.getINT() - 1);
                                break;
                            }
                            break;
                        }
                        case MULTIBOOL:
                        {
                            if (item.getIsSubMenuOpen())
                            {
                                for (int count = item.getMULTIBOOLCount(), bool = 0; bool < count; ++bool)
                                {
                                    if (clickBox(x, y, subrect[0] + getOffSetXForSubMenuBox(bool, count), subrect[1] + 3))
                                    {
                                        item.setMULTIBOOL(bool, !item.getMULTIBOOL(bool));
                                        break;
                                    }
                                }
                                break;
                            }
                            break;
                        }
                    }
                }
            }
            if (!anyLargeItem)
            {
                final int n = -1;
                dropdownY = n;
                dropdownX = n;
            }
        }
        return true;
    }

    static
    {
        GuiCartAssembler.backgrounds = new ResourceLocation[4];
        for (int i = 0; i < GuiCartAssembler.backgrounds.length; ++i)
        {
            GuiCartAssembler.backgrounds[i] = ResourceHelper.getResource("/gui/garageBackground" + i + ".png");
        }
        textureLeft = ResourceHelper.getResource("/gui/garagePart1.png");
        textureRight = ResourceHelper.getResource("/gui/garagePart2.png");
        textureExtra = ResourceHelper.getResource("/gui/garageExtra.png");
    }

    private class TextWithColor
    {
        private IReorderingProcessor text;
        private int color;

        public TextWithColor(final IReorderingProcessor text, final int color)
        {
            this.text = text;
            this.color = color;
        }

        public IReorderingProcessor getText()
        {
            return text;
        }

        public int getColor()
        {
            return color;
        }
    }
}
