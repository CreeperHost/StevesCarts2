package vswe.stevescarts.modules.addons;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.world.entity.player.Player;
import vswe.stevescarts.api.modules.interfaces.IActivatorModule;
import vswe.stevescarts.api.modules.template.ModuleAddon;
import vswe.stevescarts.client.guis.GuiMinecart;
import vswe.stevescarts.entities.EntityMinecartModular;
import vswe.stevescarts.helpers.ResourceHelper;

public class ModuleChunkLoader extends ModuleAddon implements IActivatorModule
{
    private boolean rdyToInit;
    private int[] buttonRect;
    private EntityDataAccessor<Boolean> LOADING_CHUNK;

    public ModuleChunkLoader(final EntityMinecartModular cart)
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
        drawString(guiGraphics, gui, "Chunk Loader", 8, 6, 4210752);
    }

    @Override
    public void update()
    {
        super.update();
        if (!rdyToInit)
        {
            rdyToInit = true;
        }
        if (isLoadingChunk() && !getCart().hasFuelForModule() && !getCart().level().isClientSide)
        {
            setChunkLoading(false);
        }
    }

    public void setChunkLoading(final boolean val)
    {
        if (!isPlaceholder())
        {
            updateDw(LOADING_CHUNK, val);
            if (!getCart().level().isClientSide && rdyToInit)
            {
                if (val)
                {
                    getCart().initChunkLoading();
                }
                else
                {
                    getCart().dropChunkLoading();
                }
            }
        }
    }

    private boolean isLoadingChunk()
    {
        return !isPlaceholder() && getDw(LOADING_CHUNK);
    }

    @Override
    public void drawBackground(GuiGraphics guiGraphics, GuiMinecart gui, final int x, final int y)
    {
        ResourceHelper.bindResource("/gui/chunk.png");
        final int imageID = isLoadingChunk() ? 1 : 0;
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

    private String getStateName()
    {
        if (!isLoadingChunk())
        {
            return "Activate chunk loading";
        }
        return "Deactivate chunk loading";
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
            setChunkLoading(!isLoadingChunk());
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
        LOADING_CHUNK = createDw(EntityDataSerializers.BOOLEAN);
        registerDw(LOADING_CHUNK, false);
    }

    @Override
    public int getConsumption(final boolean isMoving)
    {
        return isLoadingChunk() ? 5 : super.getConsumption(isMoving);
    }

    @Override
    protected void Save(final CompoundTag tagCompound, final int id)
    {
        tagCompound.putBoolean(generateNBTName("ChunkLoading", id), isLoadingChunk());
    }

    @Override
    protected void Load(final CompoundTag tagCompound, final int id)
    {
        setChunkLoading(tagCompound.getBoolean(generateNBTName("ChunkLoading", id)));
    }

    @Override
    public void doActivate(final int id)
    {
        setChunkLoading(true);
    }

    @Override
    public void doDeActivate(final int id)
    {
        setChunkLoading(false);
    }

    @Override
    public boolean isActive(final int id)
    {
        return isLoadingChunk();
    }
}
