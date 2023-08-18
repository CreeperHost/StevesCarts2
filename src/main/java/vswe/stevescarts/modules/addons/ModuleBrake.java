package vswe.stevescarts.modules.addons;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.world.entity.player.Player;
import vswe.stevescarts.api.modules.interfaces.ILeverModule;
import vswe.stevescarts.api.modules.template.ModuleAddon;
import vswe.stevescarts.client.guis.GuiMinecart;
import vswe.stevescarts.entities.EntityMinecartModular;
import vswe.stevescarts.helpers.Localization;
import vswe.stevescarts.helpers.ResourceHelper;

public class ModuleBrake extends ModuleAddon implements ILeverModule
{
    private int[] startstopRect;
    private int[] turnbackRect;
    private EntityDataAccessor<Boolean> FORGE_STOPPING;

    public ModuleBrake(final EntityMinecartModular cart)
    {
        super(cart);
        startstopRect = new int[]{15, 20, 24, 12};
        turnbackRect = new int[]{startstopRect[0] + startstopRect[2] + 5, startstopRect[1], startstopRect[2], startstopRect[3]};
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
        return 35;
    }

    @Override
    public void drawForeground(GuiGraphics guiGraphics, GuiMinecart gui)
    {
        drawString(guiGraphics, gui, Localization.MODULES.ADDONS.CONTROL_LEVER.translate(), 8, 6, 4210752);
    }

    @Override
    public void drawBackground(GuiGraphics guiGraphics, GuiMinecart gui, final int x, final int y)
    {
        ResourceHelper.bindResource("/gui/lever.png");
        drawButton(guiGraphics, gui, x, y, startstopRect, isForceStopping() ? 2 : 1);
        drawButton(guiGraphics, gui, x, y, turnbackRect, 0);
    }

    private void drawButton(GuiGraphics guiGraphics, GuiMinecart gui, final int x, final int y, final int[] coords, final int imageID)
    {
        if (inRect(x, y, coords))
        {
            drawImage(guiGraphics, gui, coords, 0, coords[3]);
        }
        else
        {
            drawImage(guiGraphics, gui, coords, 0, 0);
        }
        final int srcY = coords[3] * 2 + imageID * (coords[3] - 2);
        drawImage(guiGraphics, gui, coords[0] + 1, coords[1] + 1, 0, srcY, coords[2] - 2, coords[3] - 2);
    }

    @Override
    public boolean stopEngines()
    {
        return isForceStopping();
    }

    private boolean isForceStopping()
    {
        if (isPlaceholder())
        {
            return getSimInfo().getBrakeActive();
        }
        return getDw(FORGE_STOPPING);
    }

    private void setForceStopping(final boolean val)
    {
        updateDw(FORGE_STOPPING, val);
    }

    @Override
    public void drawMouseOver(GuiGraphics guiGraphics, GuiMinecart gui, final int x, final int y)
    {
        drawStringOnMouseOver(guiGraphics, gui, isForceStopping() ? Localization.MODULES.ADDONS.LEVER_START.translate() : Localization.MODULES.ADDONS.LEVER_STOP.translate(), x, y, startstopRect);
        drawStringOnMouseOver(guiGraphics, gui, Localization.MODULES.ADDONS.LEVER_TURN.translate(), x, y, turnbackRect);
    }

    @Override
    public void mouseClicked(final GuiMinecart gui, final int x, final int y, final int button)
    {
        if (button == 0)
        {
            if (inRect(x, y, startstopRect))
            {
                sendPacket(0);
            }
            else if (inRect(x, y, turnbackRect))
            {
                sendPacket(1);
            }
        }
    }

    @Override
    protected void receivePacket(final int id, final byte[] data, final Player player)
    {
        if (id == 0)
        {
            setForceStopping(!isForceStopping());
        }
        else if (id == 1)
        {
            turnback();
        }
    }

    @Override
    public int numberOfPackets()
    {
        return 2;
    }

    @Override
    public float getLeverState()
    {
        if (isForceStopping())
        {
            return 0.0f;
        }
        return 1.0f;
    }

    @Override
    public int numberOfDataWatchers()
    {
        return 1;
    }

    @Override
    public void initDw()
    {
        FORGE_STOPPING = createDw(EntityDataSerializers.BOOLEAN);
        registerDw(FORGE_STOPPING, false);
    }

    @Override
    protected void Save(final CompoundTag tagCompound, final int id)
    {
        tagCompound.putBoolean(generateNBTName("ForceStop", id), isForceStopping());
    }

    @Override
    protected void Load(final CompoundTag tagCompound, final int id)
    {
        setForceStopping(tagCompound.getBoolean(generateNBTName("ForceStop", id)));
    }
}
