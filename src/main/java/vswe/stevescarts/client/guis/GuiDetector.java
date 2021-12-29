package vswe.stevescarts.client.guis;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import vswe.stevescarts.blocks.tileentities.TileEntityDetector;
import vswe.stevescarts.containers.ContainerDetector;
import vswe.stevescarts.helpers.*;
import vswe.stevescarts.modules.data.ModuleData;

import java.util.ArrayList;
import java.util.Iterator;

public class GuiDetector extends ContainerScreen<ContainerDetector>
{
    private ArrayList<DropDownMenu> menus;
    private DropDownMenuPages modulesMenu;
    private DropDownMenu statesMenu;
    private DropDownMenu flowMenu;
    public static ResourceLocation texture;
    public static ResourceLocation moduleTexture;
    public static ResourceLocation stateTexture;
    public static ResourceLocation dropdownTexture;
    public LogicObject currentObject;
    TileEntityDetector detector;
    PlayerInventory invPlayer;

    public GuiDetector(ContainerDetector containerDetector, PlayerInventory playerInventory, ITextComponent iTextComponent)
    {
        super(containerDetector, playerInventory, iTextComponent);
        this.invPlayer = playerInventory;
        this.detector = containerDetector.getDetector();

        imageWidth = 255;
        imageHeight = 202;
        final Iterator<LogicObject> i$ = detector.mainObj.getChilds().iterator();
        if (i$.hasNext())
        {
            final LogicObject child = i$.next();
            child.setParent(null);
        }
        detector.recalculateTree();
        (menus = new ArrayList<>()).add(modulesMenu = new DropDownMenuPages(0, 2));
        menus.add(statesMenu = new DropDownMenu(1));
        menus.add(flowMenu = new DropDownMenu(2));
    }

    @Override
    protected void renderBg(MatrixStack matrixStack, float x, int y, int p_230450_4_)
    {
        final int j = getGuiLeft();
        final int k = getGuiTop();
        ResourceHelper.bindResource(GuiDetector.texture);
        blit(matrixStack, j, k, 0, 0, imageWidth, imageHeight);
        x -= getGuiLeft();
        y -= getGuiTop();
        detector.mainObj.draw(matrixStack, this, (int) x, (int) y);
        DropDownMenu.update(this, (int) x, y, menus);
        flowMenu.drawMain(matrixStack, this, (int) x, y);
        ResourceHelper.bindResource(GuiDetector.texture);
        int flowPosId = 0;
        //TODO
        for (final OperatorObject operator : OperatorObject.getOperatorList(detector.getDetectorType()))
        {
            if (operator.inTab())
            {
                final int[] src = getOperatorTexture(operator.getID());
                flowMenu.drawContent(matrixStack, this, flowPosId, src[0], src[1]);
                ++flowPosId;
            }
        }
        statesMenu.drawMain(matrixStack, this, (int) x, y);
        ResourceHelper.bindResource(GuiDetector.stateTexture);
        int statePosId = 0;
        for (final ModuleState state : ModuleState.getStateList())
        {
            final int[] src2 = getModuleTexture(state.getID());
            statesMenu.drawContent(matrixStack, this, statePosId, src2[0], src2[1]);
            ++statePosId;
        }
        modulesMenu.drawMain(matrixStack, this, (int) x, y);
        int modulePosId = 0;
        for (final ModuleData module : ModuleData.getModules())
        {
            if (module.getIsValid())
            {
                modulesMenu.drawContent(matrixStack, this, modulePosId, module);
                ++modulePosId;
            }
        }
        flowMenu.drawHeader(matrixStack, this);
        statesMenu.drawHeader(matrixStack, this);
        modulesMenu.drawHeader(matrixStack, this);
        if (currentObject != null)
        {
            currentObject.draw(matrixStack, this, -500, -500, (int) x, y);
        }

    }

    public boolean inRect(final int x, final int y, final int[] coords)
    {
        return coords != null && x >= coords[0] && x < coords[0] + coords[2] && y >= coords[1] && y < coords[1] + coords[3];
    }

    @Override
    protected void renderLabels(MatrixStack p_230451_1_, int p_230451_2_, int p_230451_3_)
    {
    }

    @Override
    public void render(MatrixStack matrixStack, int x, int y, float p_230430_4_)
    {
        this.renderBackground(matrixStack);
        super.render(matrixStack, x, y, p_230430_4_);
        font.draw(matrixStack, DetectorType.getTypeFromSate(detector.getLevel().getBlockState(detector.getBlockPos())).getTranslatedName(), getGuiLeft() + 8, getGuiTop() + 6, 4210752);
        if (modulesMenu.getScroll() != 0)
        {
            int modulePosId = 0;
            for (final ModuleData module : ModuleData.getModules())
            {
                if (module.getIsValid())
                {
                    final int[] target = modulesMenu.getContentRect(modulePosId);
                    if (drawMouseOver(matrixStack, module.getName(), x, y, target))
                    {
                        break;
                    }
                    ++modulePosId;
                }
            }
        }
        else if (statesMenu.getScroll() != 0)
        {
            int statesPosId = 0;
            for (final ModuleState state : ModuleState.getStateList())
            {
                final int[] target = statesMenu.getContentRect(statesPosId);
                if (drawMouseOver(matrixStack, state.getName(), x, y, target))
                {
                    break;
                }
                ++statesPosId;
            }
        }
        else if (flowMenu.getScroll() != 0)
        {
            int flowPosId = 0;
            for (final OperatorObject operator : OperatorObject.getOperatorList(detector.getDetectorType()))
            {
                if (operator.inTab())
                {
                    final int[] target = flowMenu.getContentRect(flowPosId);
                    if (drawMouseOver(matrixStack, operator.getName(), x, y, target))
                    {
                        break;
                    }
                    ++flowPosId;
                }
            }
        }
        else
        {
            drawMouseOverFromObject(matrixStack, detector.mainObj, x, y);
        }
    }


    private boolean drawMouseOverFromObject(MatrixStack matrixStack, LogicObject obj, final int x, final int y)
    {
        if (drawMouseOver(matrixStack, obj.getName(), x, y, obj.getRect()))
        {
            return true;
        }
        for (final LogicObject child : obj.getChilds())
        {
            if (drawMouseOverFromObject(matrixStack, child, x, y))
            {
                return true;
            }
        }
        return false;
    }

    private boolean drawMouseOver(MatrixStack matrixStack, String str, final int x, final int y, final int[] rect)
    {
        if (rect != null && inRect(x - getGuiLeft(), y - getGuiTop(), rect))
        {
            renderTooltip(matrixStack, new StringTextComponent(str), x, y);
            return true;
        }
        return false;
    }

    public int[] getOperatorTexture(final byte operatorId)
    {
        final int x = operatorId % 11;
        final int y = operatorId / 11;
        return new int[]{36 + x * 20, imageHeight + y * 11};
    }

    public int[] getModuleTexture(final byte moduleId)
    {
        final int srcX = moduleId % 16 * 16;
        final int srcY = moduleId / 16 * 16;
        return new int[]{srcX, srcY};
    }

    private int[] getOperatorRect(final int posId)
    {
        return new int[]{20 + posId * 30, 20, 20, 11};
    }

    public void drawModuleIcon(ModuleData icon, final int targetX, final int targetY, final float sizeX, final float sizeY, final float offsetX, final float offsetY)
    {
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        itemRenderer.renderGuiItem(icon.getItemStack(), targetX, targetY);
    }

    @Override
    public boolean mouseClicked(double x, double y, int button)
    {
        super.mouseClicked(x, y, button);
        x -= getGuiLeft();
        y -= getGuiTop();
        if (button == 0)
        {
            if (hasShiftDown())
            {
                if (currentObject == null)
                {
                    pickupObject((int) x, (int) y, detector.mainObj);
                }
            }
            else
            {
                int modulePosId = 0;
                for (final ModuleData module : ModuleData.getModules())
                {
                    if (module.getIsValid())
                    {
                        final int[] target = modulesMenu.getContentRect(modulePosId);
                        if (inRect((int) x, (int) y, target))
                        {
                            currentObject = new LogicObject((byte) 0, module.getID());
                            return true;
                        }
                        ++modulePosId;
                    }
                }
                int statePosId = 0;
                for (final ModuleState state : ModuleState.getStateList())
                {
                    final int[] target2 = statesMenu.getContentRect(statePosId);
                    if (inRect((int) x, (int) y, target2))
                    {
                        currentObject = new LogicObject((byte) 2, state.getID());
                        return true;
                    }
                    ++statePosId;
                }
                int flowPosId = 0;
                for (final OperatorObject operator : OperatorObject.getOperatorList(detector.getDetectorType()))
                {
                    if (operator.inTab())
                    {
                        final int[] target3 = flowMenu.getContentRect(flowPosId);
                        if (inRect((int) x, (int) y, target3))
                        {
                            currentObject = new LogicObject((byte) 1, operator.getID());
                            return true;
                        }
                        ++flowPosId;
                    }
                }
                for (final DropDownMenu menu : menus)
                {
                    menu.onClick(this, (int) x, (int) y);
                }
            }
        }
        else if (button == 1 && currentObject == null)
        {
            removeObject((int) x, (int) y, detector.mainObj);
        }
        return true;
    }

    @Override
    public void mouseMoved(double x, double y)
    {
        super.mouseMoved(x, y);
        //		x -= getGuiLeft();
        //		y -= getGuiTop();
        //		if (currentObject != null) {
        //			dropOnObject((int)x, (int)y, detector.mainObj, currentObject);
        //			currentObject = null;
        //		}
    }

    private boolean removeObject(final int x, final int y, final LogicObject object)
    {
        if (inRect(x, y, object.getRect()) && object.canBeRemoved())
        {
            object.setParent(detector, null);
            return true;
        }
        for (final LogicObject child : object.getChilds())
        {
            if (removeObject(x, y, child))
            {
                return true;
            }
        }
        return false;
    }

    private boolean pickupObject(final int x, final int y, final LogicObject object)
    {
        if (inRect(x, y, object.getRect()) && object.canBeRemoved())
        {
            (currentObject = object).setParent(detector, null);
            return true;
        }
        for (final LogicObject child : object.getChilds())
        {
            if (pickupObject(x, y, child))
            {
                return true;
            }
        }
        return false;
    }

    private boolean dropOnObject(final int x, final int y, final LogicObject object, final LogicObject drop)
    {
        if (inRect(x, y, object.getRect()))
        {
            if (object.hasRoomForChild() && object.isChildValid(drop))
            {
                drop.setParent(detector, object);
            }
            return true;
        }
        for (final LogicObject child : object.getChilds())
        {
            if (dropOnObject(x, y, child, drop))
            {
                return true;
            }
        }
        return false;
    }

    static
    {
        GuiDetector.texture = ResourceHelper.getResource("/gui/detector.png");
        GuiDetector.stateTexture = ResourceHelper.getResource("/gui/states.png");
        GuiDetector.dropdownTexture = ResourceHelper.getResource("/gui/detector2.png");
    }
}
