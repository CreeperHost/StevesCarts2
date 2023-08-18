package vswe.stevescarts.modules.addons;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.world.entity.player.Player;
import vswe.stevescarts.api.modules.template.ModuleAddon;
import vswe.stevescarts.client.guis.GuiMinecart;
import vswe.stevescarts.entities.CartDataSerializers;
import vswe.stevescarts.entities.EntityMinecartModular;
import vswe.stevescarts.helpers.Localization;
import vswe.stevescarts.helpers.ResourceHelper;

import java.util.Random;

public class ModuleColorRandomizer extends ModuleAddon
{
    private int[] button;
    private int cooldown;
    private boolean hover;
    private Random random;
    private EntityDataAccessor<int[]> COLORS;

    public ModuleColorRandomizer(final EntityMinecartModular cart)
    {
        super(cart);
        button = new int[]{10, 26, 16, 16};
        random = new Random();
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
        return 100;
    }

    @Override
    public int guiHeight()
    {
        return 50;
    }

    @Override
    public void drawBackground(GuiGraphics guiGraphics, GuiMinecart gui, final int x, final int y)
    {
        ResourceHelper.bindResource("/gui/color_randomizer.png");
        drawImage(guiGraphics, gui, 50, 20, 0, 16, 28, 28);
        if (inRect(x, y, button))
        {
            drawImage(guiGraphics, gui, 10, 26, 32, 0, 16, 16);
        }
        else
        {
            drawImage(guiGraphics, gui, 10, 26, 16, 0, 16, 16);
        }
        drawImage(guiGraphics, gui, 10, 26, 0, 0, 16, 16);
    }

    @Override
    public void drawMouseOver(GuiGraphics guiGraphics, GuiMinecart gui, final int x, final int y)
    {
        if (inRect(x, y, button))
        {
            final String randomizeString = Localization.MODULES.ADDONS.BUTTON_RANDOMIZE.translate();
            drawStringOnMouseOver(guiGraphics, gui, randomizeString, x, y, button);
        }
    }

    @Override
    public void mouseClicked(final GuiMinecart gui, final int x, final int y, final int button)
    {
        if (button == 0 && inRect(x, y, this.button))
        {
            sendPacket(0);
        }
    }

    @Override
    public void activatedByRail(final int x, final int y, final int z, final boolean active)
    {
        if (active && cooldown == 0)
        {
            randomizeColor();
            cooldown = 5;
        }
    }

    @Override
    public void update()
    {
        if (cooldown > 0)
        {
            --cooldown;
        }
    }

    private void randomizeColor()
    {
        final int red = random.nextInt(256);
        final int green = random.nextInt(256);
        final int blue = random.nextInt(256);
        setColorVal(0, (byte) red);
        setColorVal(1, (byte) green);
        setColorVal(2, (byte) blue);
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
        if (id == 0)
        {
            randomizeColor();
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
