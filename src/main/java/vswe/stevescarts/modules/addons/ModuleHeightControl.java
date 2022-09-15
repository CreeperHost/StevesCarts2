package vswe.stevescarts.modules.addons;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.world.entity.player.Player;
import vswe.stevescarts.client.guis.GuiMinecart;
import vswe.stevescarts.entities.EntityMinecartModular;
import vswe.stevescarts.helpers.HeightControlOre;
import vswe.stevescarts.helpers.ResourceHelper;

public class ModuleHeightControl extends ModuleAddon
{
    private int levelNumberBoxX;
    private int levelNumberBoxY;
    private int[] arrowUp;
    private int[] arrowMiddle;
    private int[] arrowDown;
    private int oreMapX;
    private int oreMapY;
    private EntityDataAccessor<Integer> Y_TARGET;

    public ModuleHeightControl(EntityMinecartModular cart)
    {
        super(cart);
        levelNumberBoxX = 8;
        levelNumberBoxY = 18;
        arrowUp = new int[]{9, 36, 17, 9};
        arrowMiddle = new int[]{9, 46, 17, 6};
        arrowDown = new int[]{9, 53, 17, 9};
        oreMapX = 40;
        oreMapY = 18;
    }

    @Override
    public boolean hasSlots()
    {
        return false;
    }

    @Override
    public boolean hasGui()
    {
        return true;
    }

    @Override
    public int guiWidth()
    {
        return Math.max(100, oreMapX + 5 + HeightControlOre.ores.size() * 4);
    }

    @Override
    public int guiHeight()
    {
        return 65;
    }

    @Override
    public void drawForeground(PoseStack matrixStack, GuiMinecart gui)
    {
        drawString(matrixStack, gui, getModuleName(), 8, 6, 4210752);
        final String s = String.valueOf(getYTarget());
        int x = levelNumberBoxX + 6;
        int color = 16777215;
        if (getYTarget() >= 100)
        {
            x -= 4;
        }
        else if (getYTarget() < 10)
        {
            x += 3;
            if (getYTarget() < 5)
            {
                color = 16711680;
            }
        }
        drawString(matrixStack, gui, s, x, levelNumberBoxY + 5, color);
    }

    @Override
    public void drawBackground(PoseStack matrixStack, GuiMinecart gui, int x, int y)
    {
        ResourceHelper.bindResource("/gui/heightcontrol.png");
        drawImage(matrixStack, gui, levelNumberBoxX, levelNumberBoxY, 4, 36, 21, 15);
        drawImage(matrixStack, gui, arrowUp, 4, 12);
        drawImage(matrixStack, gui, arrowMiddle, 4, 21);
        drawImage(matrixStack, gui, arrowDown, 4, 27);
        for (int i = 0; i < HeightControlOre.ores.size(); ++i)
        {
            final HeightControlOre ore = HeightControlOre.ores.get(i);
            for (int j = 0; j < 11; ++j)
            {
                int altitude = getYTarget() - j + 5;
                boolean empty = ore.spanLowest > altitude || altitude > ore.spanHighest;
                boolean high = ore.bestLowest <= altitude && altitude <= ore.bestHighest;
                int srcY;
                int srcX;
                if (empty)
                {
                    srcY = 0;
                    srcX = 0;
                }
                else
                {
                    srcX = ore.srcX;
                    srcY = ore.srcY;
                    if (high)
                    {
                        srcY += 4;
                    }
                }
                drawImage(matrixStack, gui, oreMapX + i * 4, oreMapY + j * 4, srcX, srcY, 4, 4);
            }
        }
        if (getYTarget() != (int) getCart().y())
        {
            drawMarker(matrixStack, gui, 5, false);
        }
        int pos = getYTarget() + 5 - (int) getCart().y();
        if (pos >= 0 && pos < 11)
        {
            drawMarker(matrixStack, gui, pos, true);
        }
    }

    private void drawMarker(PoseStack matrixStack, GuiMinecart gui, int pos, boolean isTargetLevel)
    {
        int srcX = 4;
        int srcY = isTargetLevel ? 6 : 0;
        drawImage(matrixStack, gui, oreMapX - 1, oreMapY + pos * 4 - 1, srcX, srcY, 1, 6);
        for (int i = 0; i < HeightControlOre.ores.size(); ++i)
        {
            drawImage(matrixStack, gui, oreMapX + i * 4, oreMapY + pos * 4 - 1, srcX + 1, srcY, 4, 6);
        }
        drawImage(matrixStack, gui, oreMapX + HeightControlOre.ores.size() * 4, oreMapY + pos * 4 - 1, srcX + 5, srcY, 1, 6);
    }

    @Override
    public void mouseClicked(GuiMinecart gui, int x, int y, int button)
    {
        if (button == 0)
        {
            byte packetData = 0;
            if (inRect(x, y, arrowMiddle))
            {
                packetData |= 0x1;
            }
            else
            {
                if (!inRect(x, y, arrowUp))
                {
                    if (!inRect(x, y, arrowDown))
                    {
                        return;
                    }
                    packetData |= 0x2;
                }
                if (Screen.hasShiftDown())
                {
                    packetData |= 0x4;
                }
            }
            sendPacket(0, packetData);
        }
    }

    @Override
    protected void receivePacket(int id, byte[] data, Player player)
    {
        if (id == 0)
        {
            byte info = data[0];
            if ((info & 0x1) != 0x0)
            {
                setYTarget((int) getCart().y());
            }
            else
            {
                int mult;
                if ((info & 0x2) == 0x0)
                {
                    mult = 1;
                }
                else
                {
                    mult = -1;
                }
                int dif;
                if ((info & 0x4) == 0x0)
                {
                    dif = 1;
                }
                else
                {
                    dif = 10;
                }
                int targetY = getYTarget();
                targetY += mult * dif;
                if (targetY < 0)
                {
                    targetY = 0;
                }
                else if (targetY > 255)
                {
                    targetY = 255;
                }
                setYTarget(targetY);
            }
        }
    }

    @Override
    public int numberOfPackets()
    {
        return 1;
    }

    @Override
    public int numberOfDataWatchers()
    {
        return 1;
    }

    @Override
    public void initDw()
    {
        Y_TARGET = createDw(EntityDataSerializers.INT);
        registerDw(Y_TARGET, (int) getCart().y());
    }

    public void setYTarget(int val)
    {
        updateDw(Y_TARGET, val);
    }

    @Override
    public int getYTarget()
    {
        if (isPlaceholder())
        {
            return 64;
        }
        int data = getDw(Y_TARGET);
        if (data < 0)
        {
            data += 256;
        }
        return data;
    }

    @Override
    protected void Save(CompoundTag tagCompound, int id)
    {
        tagCompound.putShort(generateNBTName("Height", id), (short) getYTarget());
    }

    @Override
    protected void Load(CompoundTag tagCompound, int id)
    {
        setYTarget(tagCompound.getShort(generateNBTName("Height", id)));
    }
}
