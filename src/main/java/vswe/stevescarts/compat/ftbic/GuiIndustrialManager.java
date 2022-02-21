//package vswe.stevescarts.compat.ftbic;
//
//import com.mojang.blaze3d.matrix.MatrixStack;
//import com.mojang.blaze3d.platform.GlStateManager;
//import dev.ftb.mods.ftbic.util.FTBICUtils;
//import net.minecraft.client.Minecraft;
//import net.minecraft.client.gui.screen.inventory.ContainerScreen;
//import net.minecraft.client.renderer.ItemRenderer;
//import net.minecraft.entity.player.PlayerInventory;
//import net.minecraft.item.ItemStack;
//import net.minecraft.util.ResourceLocation;
//import net.minecraft.util.text.ITextComponent;
//import net.minecraft.util.text.StringTextComponent;
//import net.minecraftforge.fml.client.gui.GuiUtils;
//import vswe.stevescarts.helpers.Localization;
//import vswe.stevescarts.helpers.ResourceHelper;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class GuiIndustrialManager extends ContainerScreen<ContainerIndustrialManager>
//{
//    private static ResourceLocation texturesLeft;
//    private static ResourceLocation texturesRight;
//
//    private ContainerIndustrialManager containerIndustrialManager;
//
//    public GuiIndustrialManager(ContainerIndustrialManager containerIndustrialManager, PlayerInventory playerInventory, ITextComponent iTextComponent)
//    {
//        super(containerIndustrialManager, playerInventory, iTextComponent);
//        this.containerIndustrialManager = containerIndustrialManager;
//        imageWidth = 305;
//        imageHeight = 222;
//    }
//
//    @Override
//    protected void renderBg(MatrixStack matrixStack, float mouseX, int mouseY, int partial)
//    {
//
//        ResourceHelper.bindResource(texturesLeft);
//        blit(matrixStack, leftPos, topPos, 0, 0, 256, imageHeight);
//        ResourceHelper.bindResource(texturesRight);
//        blit(matrixStack, leftPos + 256, topPos, 0, 0, imageWidth - 256, imageHeight);
//
//        GlStateManager._color4f(1.0f, 1.0f, 1.0f, 1.0f);
//        final int left = getGuiLeft();
//        final int top = getGuiTop();
//        final ItemRenderer renderitem = Minecraft.getInstance().getItemRenderer();
//        final int[] coords = getMiddleCoords();
//        renderitem.renderGuiItem(new ItemStack(CompatFtbic.INDUSTRIAL_MANAGER.get(), 1), left + coords[0], top + coords[1]);
//        GlStateManager._color4f(1.0f, 1.0f, 1.0f, 1.0f);
//    }
//
//    protected int[] getMiddleCoords()
//    {
//        return new int[]{getCenterTargetX() + 45, 61, 20, 20};
//    }
//
//    protected int getCenterTargetX()
//    {
//        return 98;
//    }
//
//    //Override this to stop labels from rendering
//    @Override
//    protected void renderLabels(MatrixStack p_230451_1_, int p_230451_2_, int p_230451_3_) {}
//
//    @Override
//    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float p_230430_4_)
//    {
//        this.renderBackground(matrixStack);
//        super.render(matrixStack, mouseX, mouseY, p_230430_4_);
//        renderTooltip(matrixStack, mouseX, mouseY);
//        int[] coords = getMiddleCoords();
//
//        font.draw(matrixStack, new StringTextComponent("Industrial Manager"), leftPos + coords[0] - 34, topPos + 4, 4210752);
//        font.draw(matrixStack, Localization.GUI.MANAGER.TITLE.translate(), leftPos + coords[0] + coords[2], topPos + 4, 4210752);
//
//        drawEnergyBar(matrixStack, getGuiLeft() + 10, getGuiTop() + 10, 100, containerIndustrialManager.getEnergy(), 50000, mouseX, mouseY, "⚡");
//        drawEnergyBar(matrixStack, getGuiLeft() + imageWidth - 25, getGuiTop() + 10, 100, containerIndustrialManager.getEnergy(), 50000, mouseX, mouseY, "⚡");
//    }
//
//    protected int[] getTextCoords(final int id)
//    {
//        final int[] coords = getBoxCoords(id);
//        final int xCoord = coords[0];
//        int yCoord = coords[1];
//        if (id >= 2)
//        {
//            yCoord -= 12;
//        }
//        else
//        {
//            yCoord += 20;
//        }
//        return new int[]{xCoord, yCoord, 20, 10};
//    }
//
//    protected int[] getBoxCoords(final int id)
//    {
//        final int x = id % 2;
//        final int y = id / 2;
//        final int xCoord = getCenterTargetX() + 4 + x * 82;
//        int yCoord = 17 + y * 88;
//        yCoord += offsetObjectY(x, y);
//        return new int[]{xCoord, yCoord, 20, 20};
//    }
//
//    protected int offsetObjectY(final int x, final int y)
//    {
//        return -5 + y * 10;
//    }
//
//    static
//    {
//        texturesLeft = ResourceHelper.getResource("/gui/industrial_manager0.png");
//        texturesRight = ResourceHelper.getResource("/gui/industrial_manager1.png");
//    }
//
//    public void drawEnergyBar(MatrixStack matrixStack, int x, int y, int height, int energyStored, int maxEnergyStored, int mouseX, int mouseY, String powerType)
//    {
//        ResourceHelper.bindResource(ResourceHelper.getResource("/gui/gui_sheet.png"));
//
//        blit(matrixStack, x, y, 0, 150, 14, height, 256, 256);
//        blit(matrixStack, x, y + height - 1, 0, 255, 14, 1, 256, 256);
//        int draw = (int) ((double) energyStored / (double) maxEnergyStored * (height - 2));
//        blit(matrixStack, x + 1, y + height - draw - 1, 14, height + 150 - draw, 12, draw, 256, 256);
//
//        if (isInRect(x, y, 14, height, mouseX, mouseY)) {
//            List<ITextComponent> list = new ArrayList<>();
//            list.add(FTBICUtils.formatEnergy(energyStored).append(" / ").append(FTBICUtils.formatEnergy(50000)));
//            GuiUtils.drawHoveringText(matrixStack, list, mouseX, mouseY, width, height, -1, Minecraft.getInstance().font);
//        }
//    }
//
//    public boolean isInRect(int x, int y, int xSize, int ySize, int mouseX, int mouseY)
//    {
//        return ((mouseX >= x && mouseX <= x + xSize) && (mouseY >= y && mouseY <= y + ySize));
//    }
//}
