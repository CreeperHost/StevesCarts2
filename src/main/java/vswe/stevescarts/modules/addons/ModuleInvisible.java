package vswe.stevescarts.modules.addons;

import net.creeperhost.polylib.data.serializable.BooleanData;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import vswe.stevescarts.api.modules.interfaces.IActivatorModule;
import vswe.stevescarts.api.modules.template.ModuleAddon;
import vswe.stevescarts.client.guis.GuiMinecart;
import vswe.stevescarts.entities.ModularMinecart;
import vswe.stevescarts.helpers.Localization;
import vswe.stevescarts.helpers.ResourceHelper;
import vswe.stevescarts.polylib.EntityData;

public class ModuleInvisible extends ModuleAddon implements IActivatorModule
{
    private int[] buttonRect;
    private final EntityData<Boolean> visable = new EntityData<>(getCart(), new BooleanData(true));

    public ModuleInvisible(ModularMinecart cart)
    {
        super(cart);
        buttonRect = new int[]{20, 20, 24, 12};
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
        return 90;
    }

    @Override
    public int guiHeight()
    {
        return 35;
    }

    @Override
    public void drawForeground(GuiGraphics guiGraphics, GuiMinecart gui)
    {
        drawString(guiGraphics, gui, getModuleName(), 8, 6, 4210752);
    }

    @Override
    public void drawBackground(GuiGraphics guiGraphics, GuiMinecart gui, final int x, final int y)
    {
        ResourceHelper.bindResource("/gui/invis.png");
        final int imageID = isVisible() ? 1 : 0;
        int borderID = 0;
        if (inRect(x, y, buttonRect))
        {
            borderID = 1;
        }
        drawImage(guiGraphics, gui, buttonRect, 0, buttonRect[3] * borderID);
        final int srcY = buttonRect[3] * 2 + imageID * (buttonRect[3] - 2);
        drawImage(guiGraphics, gui, buttonRect[0] + 1, buttonRect[1] + 1, 0, srcY, buttonRect[2] - 2, buttonRect[3] - 2);
    }

    @Override
    public void drawMouseOver(GuiGraphics guiGraphics, GuiMinecart gui, final int x, final int y)
    {
        drawStringOnMouseOver(guiGraphics, gui, getStateName(), x, y, buttonRect);
    }

    @Override
    public void update()
    {
        super.update();
        if (!isVisible() && !getCart().hasFuelForModule() && !getCart().level().isClientSide)
        {
            setIsVisible(true);
        }
    }

    private boolean isVisible()
    {
        if (isPlaceholder())
        {
            return !getSimInfo().getInvisActive();
        }
        return visable.get();
    }

    private String getStateName()
    {
        return Localization.MODULES.ADDONS.INVISIBILITY.translate(isVisible() ? "0" : "1");
    }

    @Override
    public void mouseClicked(final GuiMinecart gui, final int x, final int y, final int button)
    {
        if (button == 0 && inRect(x, y, buttonRect))
        {
            sendPacket(0);
        }
    }

    @Override
    protected void receivePacket(final int id, final byte[] data, final Player player)
    {
        if (id == 0)
        {
            setIsVisible(!isVisible());
        }
    }

    public void setIsVisible(final boolean val)
    {
        visable.set(val);
    }

    @Override
    public int numberOfPackets()
    {
        return 1;
    }

    @Override
    public boolean shouldCartRender()
    {
        return isVisible();
    }

    @Override
    public int getConsumption(final boolean isMoving)
    {
        return isVisible() ? super.getConsumption(isMoving) : 3;
    }

    @Override
    protected void save(CompoundTag tagCompound, int id, HolderLookup.Provider provider)
    {
        tagCompound.putBoolean(generateNBTName("Invis", id), !isVisible());
    }

    @Override
    protected void load(CompoundTag tagCompound, int id, HolderLookup.Provider provider)
    {
        setIsVisible(!tagCompound.getBoolean(generateNBTName("Invis", id)));
    }

    @Override
    public void doActivate(final int id)
    {
        setIsVisible(false);
    }

    @Override
    public void doDeActivate(final int id)
    {
        setIsVisible(true);
    }

    @Override
    public boolean isActive(final int id)
    {
        return !isVisible();
    }
}
