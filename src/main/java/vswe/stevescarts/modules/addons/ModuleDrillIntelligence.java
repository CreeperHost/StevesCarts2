package vswe.stevescarts.modules.addons;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import vswe.stevescarts.SCConfig;
import vswe.stevescarts.api.modules.ModuleBase;
import vswe.stevescarts.api.modules.template.ModuleAddon;
import vswe.stevescarts.client.guis.GuiMinecart;
import vswe.stevescarts.entities.EntityMinecartModular;
import vswe.stevescarts.helpers.Localization;
import vswe.stevescarts.helpers.ResourceHelper;
import vswe.stevescarts.modules.workers.tools.ModuleDrill;
import vswe.stevescarts.network.DataSerializers;
import vswe.stevescarts.network.DataSerializers.BoolArray;

public class ModuleDrillIntelligence extends ModuleAddon
{
    private final EntityDataAccessor<BoolArray> DISABLED_ARRAY = createDw(DataSerializers.BOOL_ARRAY);
    private ModuleDrill drill;
    private boolean hasHeightController;
    private int guiW;
    private int guiH;
    private boolean clickedState;
    private boolean clicked;
    private int lastId;

    public ModuleDrillIntelligence(final EntityMinecartModular cart)
    {
        super(cart);
        guiW = -1;
        guiH = -1;
    }

    @Override
    public void initDw() {
        registerDw(DISABLED_ARRAY, new BoolArray(getDrillWidth() * getDrillHeight()));
    }

    public BoolArray getDisabledArray() {
        return isPlaceholder() ? new BoolArray(16*16) : getDw(DISABLED_ARRAY);
    }

    public void setDisabledArray(BoolArray array) {
        updateDw(DISABLED_ARRAY, array);
    }

    @Override
    public void preInit()
    {
        super.preInit();
        for (final ModuleBase module : getCart().getModules())
        {
            if (module instanceof ModuleDrill)
            {
                drill = (ModuleDrill) module;
            }
            else
            {
                if (!(module instanceof ModuleHeightControl))
                {
                    continue;
                }
                hasHeightController = true;
            }
        }
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
    @OnlyIn(Dist.CLIENT)
    public void drawForeground(GuiGraphics guiGraphics, GuiMinecart gui)
    {
        drawString(guiGraphics, gui, getModuleName(), 8, 6, 0x404040);
    }

    private int getDrillWidth()
    {
        if (drill == null)
        {
            return 0;
        }
        return drill.getAreaWidth();
    }

    private int getDrillHeight()
    {
        if (drill == null)
        {
            return 0;
        }
        return drill.getAreaHeight() + (hasHeightController ? 2 : 0);
    }

    @Override
    public int guiWidth()
    {
        if (guiW == -1)
        {
            guiW = Math.max(15 + getDrillWidth() * 10 + 5, 93);
        }
        return guiW;
    }

    @Override
    public int guiHeight()
    {
        if (guiH == -1)
        {
            guiH = 20 + getDrillHeight() * 10 + 5;
        }
        return guiH;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void drawBackground(GuiGraphics guiGraphics, GuiMinecart gui, final int x, final int y)
    {
        ResourceHelper.bindResource("/gui/intelligence.png");
        final int w = getDrillWidth();
        final int h = getDrillHeight();
        for (int i = 0; i < w; ++i)
        {
            for (int j = 0; j < h; ++j)
            {
                final int[] rect = getSettingRect(i, j);
                int maxY = ((getDrillHeight() / 2 - 1) - SCConfig.drillSize.get()) * 2;

                int srcX = (!hasHeightController || (j != maxY && j != h - 1)) ? 0 : 8;
                int srcY = 0;
                drawImage(guiGraphics, gui, rect, srcX, srcY);
                if (isRestricted(j * w + i))
                {
                    srcX = 16;
                    srcY = 8;
                    drawImage(guiGraphics, gui, rect, srcX, srcY);
                }
                else if (isActive(j * w + i))
                {
                    srcX = (isLocked(j * w + i) ? 8 : 0);
                    srcY = 8;
                    drawImage(guiGraphics, gui, rect, srcX, srcY);
                }
                srcX = (inRect(x, y, rect) ? 8 : 0);
                srcY = 16;
                drawImage(guiGraphics, gui, rect, srcX, srcY);
            }
        }
    }


    public boolean isActive(int x, int y, final int offset, final boolean direction)
    {
        y = getDrillHeight() - 1 - y;
        if (hasHeightController)
        {
            y -= offset;
        }
        if (!direction)
        {
            x = getDrillWidth() - 1 - x;
        }
        return isActive(y * getDrillWidth() + x);
    }

    private boolean isActive(int id)
    {
        BoolArray array = getDisabledArray();
        return !isRestricted(id) && (isLocked(id) || !array.get(id));
    }

    private boolean isLocked(int id)
    {
        final int x = id % getDrillWidth();
        final int y = id / getDrillWidth();
        return (y == getDrillHeight() - 1 || (hasHeightController && y == getDrillHeight() - 2)) && x == (getDrillWidth() - 1) / 2;
    }

    private boolean isRestricted(int id)
    {
        int size = SCConfig.drillSize.get();
        int centerX = (getDrillWidth() - 1) / 2;
        int x = id % getDrillWidth();

        int y = getDrillHeight() - (id / getDrillWidth()) - 3;
        int maxHeight = size * 2 + 1 - (hasHeightController ? 1 : 3);

        boolean validX = x > centerX + size || x < centerX - size;
        boolean validY = y > maxHeight;
        return validX || validY;
    }

    private void swapActiveness(final int id)
    {
        if (!isRestricted(id) && !isLocked(id))
        {
            BoolArray array = getDisabledArray();
            array.set(id, !array.get(id));
            setDisabledArray(array);
        }
    }

    private int[] getSettingRect(final int x, final int y)
    {
        return new int[]{15 + x * 10, 20 + y * 10, 8, 8};
    }

    @Override
    public void drawMouseOver(GuiGraphics guiGraphics, GuiMinecart gui, final int x, final int y)
    {
        final int w = getDrillWidth();
        final int h = getDrillHeight();
        for (int i = 0; i < w; ++i)
        {
            for (int j = 0; j < h; ++j)
            {
                final int[] rect = getSettingRect(i, j);
                final String str = isRestricted(j * w + i) ? Localization.MODULES.ADDONS.RESTRICTED_INTELLIGENCE.translate() : isLocked(j * w + i) ? Localization.MODULES.ADDONS.LOCKED.translate() : (Localization.MODULES.ADDONS.CHANGE_INTELLIGENCE.translate() + "\n" + Localization.MODULES.ADDONS.CURRENT_INTELLIGENCE.translate(isActive(j * w + i) ? "0" : "1"));
                drawStringOnMouseOver(guiGraphics, gui, str, x, y, rect);
            }
        }
    }

    @Override
    public int numberOfGuiData()
    {
        final int maxDrillWidth = 9;
        final int maxDrillHeight = 9;
        return (int) Math.ceil(maxDrillWidth * (maxDrillHeight + 2) / 16.0f);
    }

    @Override
    public int numberOfPackets()
    {
        return 1;
    }

    @Override
    protected void receivePacket(final int id, final byte[] data, final Player player)
    {
        if (id == 0)
        {
            swapActiveness(data[0]);
        }
    }

    @Override
    protected void Save(final CompoundTag tagCompound, final int id)
    {
        tagCompound.putByteArray(generateNBTName("enabled_data", id), getDisabledArray().getBytes());
    }

    @Override
    protected void Load(final CompoundTag tagCompound, final int id)
    {
        int baseSize = getDrillWidth() * getDrillHeight();
        BoolArray loaded = BoolArray.fromBytes(tagCompound.getByteArray(generateNBTName("enabled_data", id)));
        if (loaded.getBytes().length * 8 >= baseSize) {
            setDisabledArray(loaded);
        }
    }

    @Override
    public void mouseMovedOrUp(final GuiMinecart gui, final int x, final int y, final int button)
    {
        if (button == -1 && clicked)
        {
            final int w = getDrillWidth();
            final int h = getDrillHeight();
            for (int i = 0; i < w; ++i)
            {
                for (int j = 0; j < h; ++j)
                {
                    if (lastId != j * w + i)
                    {
                        if (isActive(j * w + i) == clickedState)
                        {
                            final int[] rect = getSettingRect(i, j);
                            if (inRect(x, y, rect))
                            {
                                lastId = j * w + i;
                                sendPacket(0, (byte) (j * w + i));
                                return;
                            }
                        }
                    }
                }
            }
        }
        if (button == 0)
        {
            clicked = false;
        }
    }

    @Override
    public void mouseClicked(final GuiMinecart gui, final int x, final int y, final int button)
    {
        if (button == 0)
        {
            final int w = getDrillWidth();
            final int h = getDrillHeight();
            for (int i = 0; i < w; ++i)
            {
                for (int j = 0; j < h; ++j)
                {
                    final int[] rect = getSettingRect(i, j);
                    if (inRect(x, y, rect))
                    {
                        clicked = true;
                        clickedState = isActive(j * w + i);
                        lastId = j * w + i;
                        sendPacket(0, (byte) (j * w + i));
                        return;
                    }
                }
            }
        }
    }

    @Override
    public int numberOfDataWatchers() {
        return 1;
    }
}
