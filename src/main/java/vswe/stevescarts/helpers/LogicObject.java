package vswe.stevescarts.helpers;

import com.mojang.blaze3d.matrix.MatrixStack;
import vswe.stevescarts.blocks.tileentities.TileEntityDetector;
import vswe.stevescarts.client.guis.GuiDetector;
import vswe.stevescarts.entitys.EntityMinecartModular;
import vswe.stevescarts.modules.ModuleBase;
import vswe.stevescarts.modules.data.ModuleData;

import java.util.ArrayList;

public class LogicObject
{
    private byte id;
    private LogicObject parent;
    private byte type;
    private ArrayList<LogicObject> childs;
    private int x;
    private int y;
    private int level;
    private byte data;

    public LogicObject(final byte id, final byte type, final byte data)
    {
        this.id = id;
        this.type = type;
        this.data = data;
        childs = new ArrayList<>();
    }

    public LogicObject(final byte type, final byte data)
    {
        this((byte) 0, type, data);
    }

    public void setParent(final TileEntityDetector detector, final LogicObject parent)
    {
        if (parent != null)
        {
            //TODO Packet
            //			PacketStevesCarts.sendPacket(0, new byte[] { parent.id, getExtra(), data });
            for (final LogicObject child : childs)
            {
                child.setParent(detector, this);
            }
        }
        else
        {
            //TODO Packet
            //			PacketStevesCarts.sendPacket(1, new byte[] { id });
        }
    }

    public void setParent(final LogicObject parent)
    {
        if (this.parent != null)
        {
            this.parent.childs.remove(this);
        }
        this.parent = parent;
        if (this.parent != null && this.parent.hasRoomForChild())
        {
            this.parent.childs.add(this);
        }
    }

    public ArrayList<LogicObject> getChilds()
    {
        return childs;
    }

    public LogicObject getParent()
    {
        return parent;
    }

    public byte getId()
    {
        return id;
    }

    public byte getExtra()
    {
        return type;
    }

    public byte getData()
    {
        return data;
    }

    public void setX(final int val)
    {
        x = val;
    }

    public void setY(final int val)
    {
        y = val;
    }

    public void setXCenter(final int val)
    {
        setX(val + (isOperator() ? -10 : -8));
    }

    public void setYCenter(final int val)
    {
        setY(val + (isOperator() ? -5 : -8));
    }

    //TODO
    public void draw(MatrixStack matrixStack, GuiDetector gui, final int mouseX, final int mouseY, final int x, final int y)
    {
        generatePosition(x - 50, y, 100, 0);
        draw(matrixStack, gui, mouseX, mouseY);
    }

    public void draw(MatrixStack matrixStack, GuiDetector gui, final int mouseX, final int mouseY)
    {
        if (!isOperator())
        {
            ResourceHelper.bindResource(GuiDetector.texture);
            int yIndex = 0;
            if (gui.inRect(mouseX, mouseY, getRect()))
            {
                yIndex = 1;
            }
            gui.blit(matrixStack, gui.getGuiLeft() + x, gui.getGuiTop() + y, 0, 202 + yIndex * 16, 16, 16);
            if (isModule())
            {
                final ModuleData module = ModuleData.getList().get(data);
                if (module != null)
                {
                    //					gui.drawModuleIcon(module, gui.getGuiLeft() + x, gui.getGuiTop() + y, 1.0f, 1.0f, 0.0f, 0.0f);
                }
            }
            else
            {
                ResourceHelper.bindResource(GuiDetector.stateTexture);
                final int[] src = gui.getModuleTexture(data);
                gui.blit(matrixStack, gui.getGuiLeft() + x, gui.getGuiTop() + y, src[0], src[1], 16, 16);
            }
        }
        else
        {
            ResourceHelper.bindResource(GuiDetector.texture);
            final int[] src2 = gui.getOperatorTexture(data);
            gui.blit(matrixStack, gui.getGuiLeft() + x, gui.getGuiTop() + y, src2[0], src2[1], 20, 11);
            if (gui.inRect(mouseX, mouseY, getRect()))
            {
                int yIndex2;
                if (gui.currentObject == null)
                {
                    yIndex2 = 2;
                }
                else if (hasRoomForChild() && isChildValid(gui.currentObject))
                {
                    yIndex2 = 0;
                }
                else
                {
                    yIndex2 = 1;
                }
                gui.blit(matrixStack, gui.getGuiLeft() + x, gui.getGuiTop() + y, 16, 202 + yIndex2 * 11, 20, 11);
            }
        }
        if (parent != null && parent.maxChilds() > 1)
        {
            int px1 = gui.getGuiLeft() + x;
            final int py1 = gui.getGuiTop() + y;
            int px2 = gui.getGuiLeft() + parent.x;
            int py2 = gui.getGuiTop() + parent.y;
            py2 += 5;
            px1 += (isOperator() ? 10 : 8);
            boolean tooClose = false;
            if (x > parent.x)
            {
                px2 += 20;
                if (px1 < px2)
                {
                    tooClose = true;
                }
            }
            else if (px1 > px2)
            {
                tooClose = true;
            }
            if (!tooClose)
            {
                //				Gui.drawRect(px1, py2, px2, py2 + 1, -12566464);
                //				Gui.drawRect(px1, py1, px1 + 1, py2, -12566464);
                //				GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            }
        }
        for (final LogicObject child : childs)
        {
            child.draw(matrixStack, gui, mouseX, mouseY);
        }
    }

    public void generatePosition(final int x, final int y, final int w, final int level)
    {
        setXCenter(x + w / 2);
        setYCenter(y);
        this.level = level;
        final int max = maxChilds();
        for (int i = 0; i < childs.size(); ++i)
        {
            childs.get(i).generatePosition(x + w / max * i, y + (childs.get(i).isOperator() ? 11 : 16), w / max, level + ((childs.get(i).maxChilds() > 1) ? 1 : 0));
        }
    }

    private boolean isModule()
    {
        return type == 0;
    }

    private boolean isOperator()
    {
        return type == 1;
    }

    private boolean isState()
    {
        return type == 2;
    }

    private OperatorObject getOperator()
    {
        if (isOperator())
        {
            return OperatorObject.getAllOperators().get(data);
        }
        return null;
    }

    public boolean evaluateLogicTree(final TileEntityDetector detector, final EntityMinecartModular cart, final int depth)
    {
        if (depth >= 1000)
        {
            return false;
        }
        if (isState())
        {
            final ModuleState state = ModuleState.getStates().get(getData());
            return state != null && state.evaluate(cart);
        }
        if (isModule())
        {
            for (final ModuleBase module : cart.getModules())
            {
                if (getData() == module.getModuleId())
                {
                    return true;
                }
            }
            return false;
        }
        if (getChilds().size() != maxChilds())
        {
            return false;
        }
        final OperatorObject operator = getOperator();
        if (operator == null)
        {
            return false;
        }
        if (operator.getChildCount() == 2)
        {
            return operator.evaluate(detector, cart, depth + 1, getChilds().get(0), getChilds().get(1));
        }
        if (operator.getChildCount() == 1)
        {
            return operator.evaluate(detector, cart, depth + 1, getChilds().get(0), null);
        }
        return operator.evaluate(detector, cart, depth + 1, null, null);
    }

    private int maxChilds()
    {
        final OperatorObject operator = getOperator();
        if (operator != null)
        {
            return operator.getChildCount();
        }
        return 0;
    }

    public boolean isChildValid(final LogicObject child)
    {
        if (level >= 4 && child.isOperator())
        {
            return false;
        }
        if (level >= 5)
        {
            return false;
        }
        final OperatorObject operator = getOperator();
        final OperatorObject operatorchild = child.getOperator();
        return operator == null || operatorchild == null || operator.isChildValid(operatorchild);
    }

    public boolean canBeRemoved()
    {
        final OperatorObject operator = getOperator();
        return operator == null || operator.inTab();
    }

    public boolean hasRoomForChild()
    {
        return childs.size() < maxChilds();
    }

    public int[] getRect()
    {
        if (!isOperator())
        {
            return new int[]{x, y, 16, 16};
        }
        return new int[]{x, y, 20, 11};
    }

    @Override
    public boolean equals(final Object obj)
    {
        if (obj instanceof LogicObject)
        {
            final LogicObject logic = (LogicObject) obj;
            return logic.id == id && ((logic.parent == null && parent == null) || (logic.parent != null && parent != null && logic.parent.id == parent.id)) && logic.getExtra() == getExtra() && logic.getData() == getData();
        }
        return false;
    }

    public LogicObject copy(final LogicObject parent)
    {
        final LogicObject obj = new LogicObject(id, getExtra(), getData());
        obj.setParent(parent);
        return obj;
    }

    public String getName()
    {
        if (isState())
        {
            final ModuleState state = ModuleState.getStates().get(getData());
            if (state == null)
            {
                return "Undefined";
            }
            return state.getName();
        }
        else
        {
            if (!isModule())
            {
                String name = "Undefined";
                final OperatorObject operator = getOperator();
                if (operator != null)
                {
                    name = operator.getName();
                }
                return name + "\nChild nodes: " + getChilds().size() + "/" + maxChilds();
            }
            final ModuleData module = ModuleData.getList().get(getData());
            if (module == null)
            {
                return "Undefined";
            }
            return module.getName();
        }
    }
}
