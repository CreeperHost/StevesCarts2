package vswe.stevescarts.modules.addons;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import vswe.stevescarts.client.guis.GuiMinecart;
import vswe.stevescarts.entitys.EntityMinecartModular;
import vswe.stevescarts.helpers.Localization;
import vswe.stevescarts.helpers.ResourceHelper;
import vswe.stevescarts.api.modules.template.ModuleEngine;

public class ModulePowerObserver extends ModuleAddon
{
    private short[] areaData;
    private short[] powerLevel;
    private int currentEngine;

    public ModulePowerObserver(final EntityMinecartModular cart)
    {
        super(cart);
        areaData = new short[4];
        powerLevel = new short[4];
        currentEngine = -1;
    }

    @Override
    public boolean hasGui()
    {
        return true;
    }

    @Override
    public boolean hasSlots()
    {
        return false;
    }

    @Override
    public int guiWidth()
    {
        return 190;
    }

    @Override
    public int guiHeight()
    {
        return 150;
    }

    @Override
    public void drawForeground(PoseStack matrixStack, GuiMinecart gui)
    {
        drawString(matrixStack, gui, getModuleName(), 8, 6, 4210752);
        for (int i = 0; i < 4; ++i)
        {
            final int[] rect = getPowerRect(i);
            drawString(matrixStack, gui, powerLevel[i] + Localization.MODULES.ADDONS.K.translate(new String[0]), rect, 4210752);
        }
    }

    private boolean removeOnPickup()
    {
        return true;
    }

    @Override
    public void drawBackground(PoseStack matrixStack, GuiMinecart gui, final int x, final int y)
    {
        for (int i = 0; i < getCart().getEngines().size(); ++i)
        {
            if (!removeOnPickup() || currentEngine != i)
            {
                drawEngine(gui, i, getEngineRect(i));
            }
        }
        ResourceHelper.bindResource("/gui/observer.png");
        for (int i = 0; i < 4; ++i)
        {
            int[] rect = getAreaRect(i);
            drawImage(matrixStack, gui, rect, 18, 22 * i);
            if (inRect(x, y, rect))
            {
                drawImage(matrixStack, gui, rect, 18, 22 * (i + 4));
            }
            int count = 0;
            for (int j = 0; j < getCart().getEngines().size(); ++j)
            {
                if ((areaData[i] & 1 << j) != 0x0)
                {
                    drawEngine(gui, j, getEngineRectInArea(i, count));
                    ++count;
                }
            }
            ResourceHelper.bindResource("/gui/observer.png");
            rect = getPowerRect(i);
            if (isAreaActive(i))
            {
                drawImage(matrixStack, gui, rect, 122, 0);
            }
            else
            {
                drawImage(matrixStack, gui, rect, 122 + rect[2], 0);
            }
            if (inRect(x, y, rect))
            {
                drawImage(matrixStack, gui, rect, 122 + rect[2] * 2, 0);
            }
        }
        if (currentEngine != -1)
        {
            drawEngine(gui, currentEngine, getEngineRectMouse(x, y + getCart().getRealScrollY()));
        }
    }

    private void drawEngine(GuiMinecart gui, int id, int[] rect)
    {
        ModuleEngine engine = getCart().getEngines().get(id);
        rect = cloneRect(rect);
        int srcY = 0;
        if (!doStealInterface())
        {
            srcY -= handleScroll(rect);
        }
        gui.drawModuleIcon(engine.getData(), gui.getGuiLeft() + getX() + rect[0], gui.getGuiTop() + getY() + rect[1], rect[2] / 16, rect[3] / 16, 0, srcY / 16);
    }

    private int[] getAreaRect(final int id)
    {
        return new int[]{10, 40 + 25 * id, 104, 22};
    }

    private int[] getEngineRect(final int id)
    {
        return new int[]{11 + id * 20, 21, 16, 16};
    }

    private int[] getEngineRectMouse(final int x, final int y)
    {
        return new int[]{x - 8, y - 8, 16, 16};
    }

    private int[] getEngineRectInArea(final int areaid, final int number)
    {
        final int[] area = getAreaRect(areaid);
        return new int[]{area[0] + 4 + number * 20, area[1] + 3, 16, 16};
    }

    private int[] getPowerRect(final int areaid)
    {
        final int[] area = getAreaRect(areaid);
        return new int[]{area[0] + area[2] + 10, area[1] + 2, 35, 18};
    }

    @Override
    public void drawMouseOver(PoseStack matrixStack, GuiMinecart gui, final int x, final int y)
    {
        for (int i = 0; i < getCart().getEngines().size(); ++i)
        {
            if (!removeOnPickup() || currentEngine != i)
            {
                final ModuleEngine engine = getCart().getEngines().get(i);
                drawStringOnMouseOver(matrixStack, gui, engine.getData().getName() + "\n" + Localization.MODULES.ADDONS.OBSERVER_INSTRUCTION.translate(), x, y, getEngineRect(i));
            }
        }
        for (int i = 0; i < 4; ++i)
        {
            int count = 0;
            for (int j = 0; j < getCart().getEngines().size(); ++j)
            {
                if ((areaData[i] & 1 << j) != 0x0)
                {
                    final ModuleEngine engine2 = getCart().getEngines().get(j);
                    drawStringOnMouseOver(matrixStack, gui, engine2.getData().getName() + "\n" + Localization.MODULES.ADDONS.OBSERVER_REMOVE.translate(), x, y, getEngineRectInArea(i, count));
                    ++count;
                }
            }
            if (currentEngine != -1)
            {
                drawStringOnMouseOver(matrixStack, gui, Localization.MODULES.ADDONS.OBSERVER_DROP.translate(), x, y, getAreaRect(i));
            }
            drawStringOnMouseOver(matrixStack, gui, Localization.MODULES.ADDONS.OBSERVER_CHANGE.translate() + "\n" + Localization.MODULES.ADDONS.OBSERVER_CHANGE_10.translate(), x, y, getPowerRect(i));
        }
    }

    @Override
    public int numberOfGuiData()
    {
        return 8;
    }

    @Override
    protected void checkGuiData(final Object[] info)
    {
        for (int i = 0; i < 4; ++i)
        {
            updateGuiData(info, i, areaData[i]);
        }
        for (int i = 0; i < 4; ++i)
        {
            updateGuiData(info, i + 4, powerLevel[i]);
        }
    }

    @Override
    public void receiveGuiData(final int id, final short data)
    {
        if (id >= 0 && id < 4)
        {
            areaData[id] = data;
        }
        else if (id >= 4 && id < 8)
        {
            powerLevel[id - 4] = data;
        }
    }

    @Override
    public int numberOfPackets()
    {
        return 3;
    }

    @Override
    protected void receivePacket(final int id, final byte[] data, final Player player)
    {
        if (id == 0)
        {
            final int area = data[0];
            final int engine = data[1];
            final short[] areaData = this.areaData;
            final int n = area;
            areaData[n] |= (short) (1 << engine);
        }
        else if (id == 1)
        {
            final int area = data[0];
            final int engine = data[1];
            final short[] areaData2 = areaData;
            final int n2 = area;
            areaData2[n2] &= (short) ~(1 << engine);
        }
        else if (id == 2)
        {
            final int area = data[0];
            final int button = data[1] & 0x1;
            final boolean shift = (data[1] & 0x2) != 0x0;
            int change = (button == 0) ? 1 : -1;
            if (shift)
            {
                change *= 10;
            }
            short value = powerLevel[area];
            value += (short) change;
            if (value < 0)
            {
                value = 0;
            }
            else if (value > 999)
            {
                value = 999;
            }
            powerLevel[area] = value;
        }
    }

    @Override
    public void mouseMovedOrUp(final GuiMinecart gui, final int x, final int y, final int button)
    {
        if (button != -1)
        {
            if (button == 0)
            {
                for (int i = 0; i < 4; ++i)
                {
                    final int[] rect = getAreaRect(i);
                    if (inRect(x, y, rect))
                    {
                        sendPacket(0, new byte[]{(byte) i, (byte) currentEngine});
                        break;
                    }
                }
            }
            currentEngine = -1;
        }
    }

    @Override
    public void mouseClicked(final GuiMinecart gui, final int x, final int y, final int button)
    {
        for (int i = 0; i < 4; ++i)
        {
            final int[] rect = getPowerRect(i);
            if (inRect(x, y, rect))
            {
                sendPacket(2, new byte[]{(byte) i, (byte) (button | (Screen.hasShiftDown() ? 2 : 0))});
                break;
            }
        }
        if (button == 0)
        {
            for (int i = 0; i < getCart().getEngines().size(); ++i)
            {
                final int[] rect = getEngineRect(i);
                if (inRect(x, y, rect))
                {
                    currentEngine = i;
                    break;
                }
            }
        }
        else if (button == 1)
        {
            for (int i = 0; i < 4; ++i)
            {
                int count = 0;
                for (int j = 0; j < getCart().getEngines().size(); ++j)
                {
                    if ((areaData[i] & 1 << j) != 0x0)
                    {
                        final int[] rect2 = getEngineRectInArea(i, count);
                        if (inRect(x, y, rect2))
                        {
                            sendPacket(1, new byte[]{(byte) i, (byte) j});
                            break;
                        }
                        ++count;
                    }
                }
            }
        }
    }

    public boolean isAreaActive(final int area)
    {
        int power = 0;
        for (int i = 0; i < getCart().getEngines().size(); ++i)
        {
            final ModuleEngine engine = getCart().getEngines().get(i);
            if ((areaData[area] & 1 << i) != 0x0)
            {
                power += engine.getTotalFuel();
            }
        }
        return power > powerLevel[area] * 1000;
    }

    @Override
    protected void Save(final CompoundTag tagCompound, final int id)
    {
        for (int i = 0; i < 4; ++i)
        {
            tagCompound.putShort(generateNBTName("AreaData" + i, id), areaData[i]);
            tagCompound.putShort(generateNBTName("PowerLevel" + i, id), powerLevel[i]);
        }
    }

    @Override
    protected void Load(final CompoundTag tagCompound, final int id)
    {
        for (int i = 0; i < 4; ++i)
        {
            areaData[i] = tagCompound.getShort(generateNBTName("AreaData" + i, id));
            powerLevel[i] = tagCompound.getShort(generateNBTName("PowerLevel" + i, id));
        }
    }
}
