package vswe.stevescarts.modules.addons.plants;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import vswe.stevescarts.client.guis.GuiMinecart;
import vswe.stevescarts.entities.ModularMinecart;
import vswe.stevescarts.helpers.Localization;
import vswe.stevescarts.helpers.ResourceHelper;
import vswe.stevescarts.api.modules.template.ModuleAddon;

public class ModulePlantSize extends ModuleAddon
{
    private int size;
    private int[] boxrect;

    public ModulePlantSize(ModularMinecart cart)
    {
        super(cart);
        size = 1;
        boxrect = new int[]{10, 18, 44, 44};
    }

    public int getSize()
    {
        return size;
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
        return 80;
    }

    @Override
    public int guiHeight()
    {
        return 70;
    }

    @Override
    public void drawForeground(GuiGraphics guiGraphics, GuiMinecart gui)
    {
        drawString(guiGraphics, gui, Localization.MODULES.ADDONS.PLANTER_RANGE.translate(), 8, 6, 4210752);
    }

    @Override
    public void drawBackground(GuiGraphics guiGraphics, GuiMinecart gui, final int x, final int y)
    {
        Identifier texture = ResourceHelper.getResource("/gui/plantsize.png");
        final int srcX = (size - 1) % 5 * 44;
        final int srcY = ((size - 1) / 5 + 1) * 44;
        drawImage(guiGraphics, texture, gui, boxrect, srcX, srcY);
        if (inRect(x, y, boxrect))
        {
            drawImage(guiGraphics, texture, gui, boxrect, 0, 0);
        }
    }

    @Override
    public void drawMouseOver(GuiGraphics guiGraphics, GuiMinecart gui, final int x, final int y)
    {
        drawStringOnMouseOver(guiGraphics, gui, Localization.MODULES.ADDONS.SAPLING_AMOUNT.translate() + ": " + size + "x" + size, x, y, boxrect);
    }

    @Override
    public void mouseClicked(final GuiMinecart gui, final int x, final int y, final int button)
    {
        if ((button == 0 || button == 1) && inRect(x, y, boxrect))
        {
            sendPacket(0, (byte) button);
        }
    }

    @Override
    protected void receivePacket(final int id, final byte[] data, final Player player)
    {
        if (id == 0)
        {
            if (data[0] == 1)
            {
                --size;
                if (size < 1)
                {
                    size = 7;
                }
            }
            else
            {
                ++size;
                if (size > 7)
                {
                    size = 1;
                }
            }
        }
    }

    @Override
    public int numberOfPackets()
    {
        return 1;
    }

    @Override
    public int numberOfGuiData()
    {
        return 1;
    }

    @Override
    protected void checkGuiData(final Object[] info)
    {
        updateGuiData(info, 0, (short) size);
    }

    @Override
    public void receiveGuiData(final int id, final short data)
    {
        if (id == 0)
        {
            size = data;
        }
    }

    @Override
    protected void save(ValueOutput output, int id) {
        super.save(output, id);
        output.putByte(generateNBTName("size", id), (byte) size);
    }

    @Override
    protected void load(ValueInput input, int id) {
        super.load(input, id);
        size = input.getByteOr(generateNBTName("size", id), (byte) 0);
    }
}
