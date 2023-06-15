package vswe.stevescarts.modules.addons;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.world.entity.player.Player;
import vswe.stevescarts.client.guis.GuiMinecart;
import vswe.stevescarts.entities.CartDataSerializers;
import vswe.stevescarts.entities.EntityMinecartModular;
import vswe.stevescarts.helpers.Localization;
import vswe.stevescarts.helpers.ResourceHelper;

public class ModuleColorizer extends ModuleAddon
{
    private int markerOffsetX;
    private int scrollWidth;
    private int markerMoving;
    private EntityDataAccessor<int[]> COLORS;

    public ModuleColorizer(final EntityMinecartModular cart)
    {
        super(cart);
        markerOffsetX = 10;
        scrollWidth = 64;
        markerMoving = -1;
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
    public void drawForeground(GuiGraphics guiGraphics, GuiMinecart gui)
    {
        drawString(guiGraphics, gui, getModuleName(), 8, 6, 4210752);
    }

    @Override
    public int guiWidth()
    {
        return 125;
    }

    @Override
    public int guiHeight()
    {
        return 75;
    }

    private int[] getMovableMarker(final int i)
    {
        return new int[]{markerOffsetX + (int) (scrollWidth * (getColorVal(i) / 255.0f)) - 2, 17 + i * 20, 4, 13};
    }

    private int[] getArea(final int i)
    {
        return new int[]{markerOffsetX, 20 + i * 20, scrollWidth, 7};
    }

    @Override
    public void drawBackground(GuiGraphics guiGraphics, GuiMinecart gui, final int x, final int y)
    {
        ResourceHelper.bindResource("/gui/color.png");
        for (int i = 0; i < 3; ++i)
        {
            drawMarker(guiGraphics, gui, x, y, i);
        }
        drawImage(guiGraphics, gui, scrollWidth + 25, 29, 4, 7, 28, 28);
    }

    @Override
    public void drawMouseOver(GuiGraphics guiGraphics, GuiMinecart gui, final int x, final int y)
    {
        final String[] colorNames = {Localization.MODULES.ADDONS.COLOR_RED.translate(), Localization.MODULES.ADDONS.COLOR_GREEN.translate(), Localization.MODULES.ADDONS.COLOR_BLUE.translate()};
        for (int i = 0; i < 3; ++i)
        {
            drawStringOnMouseOver(guiGraphics, gui, colorNames[i] + ": " + getColorVal(i), x, y, getArea(i));
        }
    }

    private void drawMarker(GuiGraphics guiGraphics, GuiMinecart gui, final int x, final int y, final int id)
    {
        final float[] colorArea = new float[3];
        final float[] colorMarker = new float[3];
        for (int i = 0; i < 3; ++i)
        {
            if (i == id)
            {
                colorArea[i] = 0.7f;
                colorMarker[i] = 1.0f;
            }
            else
            {
                colorArea[i] = 0.2f;
                colorMarker[i] = 0.0f;
            }
        }
        drawImage(guiGraphics, gui, getArea(id), 0, 0);
        drawImage(guiGraphics, gui, getMovableMarker(id), 0, 7);
    }

    @Override
    public void mouseClicked(final GuiMinecart gui, final int x, final int y, final int button)
    {
        if (button == 0)
        {
            for (int i = 0; i < 3; ++i)
            {
                if (inRect(x, y, getMovableMarker(i)))
                {
                    markerMoving = i;
                }
            }
        }
    }

    @Override
    public void mouseMovedOrUp(final GuiMinecart gui, final int x, final int y, final int button)
    {
        if (markerMoving != -1)
        {
            int tempColor = (int) ((x - markerOffsetX) / (scrollWidth / 255.0f));
            if (tempColor < 0)
            {
                tempColor = 0;
            }
            else if (tempColor > 255)
            {
                tempColor = 255;
            }
            sendPacket(markerMoving, (byte) tempColor);
        }
        if (button != -1)
        {
            markerMoving = -1;
        }
    }

    @Override
    public int numberOfDataWatchers()
    {
        return 3;
    }

    @Override
    public void initDw()
    {
        COLORS = createDw(CartDataSerializers.VARINT);
        registerDw(COLORS, new int[]{255, 255, 255});
    }

    @Override
    public int numberOfPackets()
    {
        return 3;
    }

    @Override
    protected void receivePacket(final int id, final byte[] data, final Player player)
    {
        if (id >= 0 && id < 3)
        {
            setColorVal(id, data[0]);
        }
    }

    public int getColorVal(final int i)
    {
        if (isPlaceholder())
        {
            return 255;
        }
        int tempVal = getDw(COLORS)[i];
        if (tempVal < 0)
        {
            tempVal += 256;
        }
        return tempVal;
    }

    public void setColorVal(final int id, final int val)
    {
        int[] colors = getDw(COLORS);
        colors[id] = val;
        updateDw(COLORS, colors);
    }

    private float getColorComponent(final int i)
    {
        return getColorVal(i) / 255.0f;
    }

    @Override
    public float[] getColor()
    {
        return new float[]{getColorComponent(0), getColorComponent(1), getColorComponent(2)};
    }

    @Override
    protected void Save(final CompoundTag tagCompound, final int id)
    {
        tagCompound.putByte(generateNBTName("Red", id), (byte) getColorVal(0));
        tagCompound.putByte(generateNBTName("Green", id), (byte) getColorVal(1));
        tagCompound.putByte(generateNBTName("Blue", id), (byte) getColorVal(2));
    }

    @Override
    protected void Load(final CompoundTag tagCompound, final int id)
    {
        setColorVal(0, tagCompound.getByte(generateNBTName("Red", id)));
        setColorVal(1, tagCompound.getByte(generateNBTName("Green", id)));
        setColorVal(2, tagCompound.getByte(generateNBTName("Blue", id)));
    }
}
