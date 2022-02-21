package vswe.stevescarts.modules.addons;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.world.entity.player.Player;
import vswe.stevescarts.client.guis.GuiMinecart;
import vswe.stevescarts.entitys.EntityMinecartModular;
import vswe.stevescarts.helpers.Localization;
import vswe.stevescarts.helpers.ResourceHelper;
import vswe.stevescarts.modules.IActivatorModule;

public class ModuleInvisible extends ModuleAddon implements IActivatorModule
{
    private int[] buttonRect;
    private EntityDataAccessor<Boolean> VISABLE;

    public ModuleInvisible(final EntityMinecartModular cart)
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
    public void drawForeground(PoseStack matrixStack, GuiMinecart gui)
    {
        drawString(matrixStack, gui, getModuleName(), 8, 6, 4210752);
    }

    @Override
    public void drawBackground(PoseStack matrixStack, GuiMinecart gui, final int x, final int y)
    {
        ResourceHelper.bindResource("/gui/invis.png");
        final int imageID = isVisible() ? 1 : 0;
        int borderID = 0;
        if (inRect(x, y, buttonRect))
        {
            borderID = 1;
        }
        drawImage(matrixStack, gui, buttonRect, 0, buttonRect[3] * borderID);
        final int srcY = buttonRect[3] * 2 + imageID * (buttonRect[3] - 2);
        drawImage(matrixStack, gui, buttonRect[0] + 1, buttonRect[1] + 1, 0, srcY, buttonRect[2] - 2, buttonRect[3] - 2);
    }

    @Override
    public void drawMouseOver(PoseStack matrixStack, GuiMinecart gui, final int x, final int y)
    {
        drawStringOnMouseOver(matrixStack, gui, getStateName(), x, y, buttonRect);
    }

    @Override
    public void update()
    {
        super.update();
        if (!isVisible() && !getCart().hasFuelForModule() && !getCart().level.isClientSide)
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
        return getDw(VISABLE);
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
        updateDw(VISABLE, val);
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
    public int numberOfDataWatchers()
    {
        return 1;
    }

    @Override
    public void initDw()
    {
        VISABLE = createDw(EntityDataSerializers.BOOLEAN);
        registerDw(VISABLE, true);
    }

    @Override
    public int getConsumption(final boolean isMoving)
    {
        return isVisible() ? super.getConsumption(isMoving) : 3;
    }

    @Override
    protected void Save(final CompoundTag tagCompound, final int id)
    {
        tagCompound.putBoolean(generateNBTName("Invis", id), !isVisible());
    }

    @Override
    protected void Load(final CompoundTag tagCompound, final int id)
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
