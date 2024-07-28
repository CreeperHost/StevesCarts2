package vswe.stevescarts.api.modules.template;

import net.creeperhost.polylib.data.serializable.IntData;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import vswe.stevescarts.api.modules.ModuleBase;
import vswe.stevescarts.client.guis.GuiMinecart;
import vswe.stevescarts.entities.EntityMinecartModular;
import vswe.stevescarts.helpers.Localization;
import vswe.stevescarts.helpers.ResourceHelper;
import vswe.stevescarts.polylib.EntityData;

public abstract class ModuleEngine extends ModuleBase
{
    private int fuel;
    protected int[] priorityButton;
    private final EntityData<Integer> option = new EntityData<>(getCart(), new IntData((byte) 0));

    public ModuleEngine(final EntityMinecartModular cart)
    {
        super(cart);
        initPriorityButton();
    }

    protected void initPriorityButton()
    {
        priorityButton = new int[]{78, 7, 16, 16};
    }

    @Override
    public void update()
    {
        super.update();
        loadFuel();
    }

    @Override
    public boolean hasFuel(final int comsumption)
    {
        return getFuelLevel() >= comsumption && !isDisabled();
    }

    public int getFuelLevel()
    {
        return fuel;
    }

    public void setFuelLevel(final int val)
    {
        fuel = val;
    }

    protected boolean isDisabled()
    {
        return getPriority() >= 3 || getPriority() < 0;
    }

    public int getPriority()
    {
        if (isPlaceholder())
        {
            return 0;
        }
        int temp = option.get();
        if (temp < 0 || temp > 3)
        {
            temp = 3;
        }
        return temp;
    }

    public void setPriority(int data)
    {
        if (data < 0)
        {
            data = 0;
        }
        else if (data > 3)
        {
            data = 3;
        }
        option.set(data);
    }

    public void consumeFuel(final int comsumption)
    {
        setFuelLevel(getFuelLevel() - comsumption);
    }

    protected abstract void loadFuel();

    public void smoke()
    {
    }

    public abstract int getTotalFuel();

    public abstract float[] getGuiBarColor();

    @Override
    public boolean hasGui()
    {
        return true;
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

    @OnlyIn(Dist.CLIENT)
    @Override
    public void drawBackground(GuiGraphics guiGraphics, final GuiMinecart gui, final int x, final int y)
    {
        ResourceHelper.bindResource("/gui/engine.png");
        final int sourceX = 16 * getPriority();
        int sourceY = 0;
        if (inRect(x, y, priorityButton))
        {
            sourceY = 16;
        }
        drawImage(guiGraphics, gui, priorityButton, sourceX, sourceY);
    }

    @Override
    public void drawMouseOver(GuiGraphics guiGraphics, GuiMinecart gui, final int x, final int y)
    {
        drawStringOnMouseOver(guiGraphics, gui, getPriorityText(), x, y, priorityButton);
    }

    private String getPriorityText()
    {
        if (isDisabled())
        {
            return Localization.MODULES.ENGINES.ENGINE_DISABLED.translate();
        }
        return Localization.MODULES.ENGINES.ENGINE_PRIORITY.translate(String.valueOf(getPriority()));
    }

    @Override
    public void mouseClicked(final GuiMinecart gui, final int x, final int y, final int button)
    {
        if (inRect(x, y, priorityButton) && (button == 0 || button == 1))
        {
            sendPacket(0, (byte) button);
        }
    }

    @Override
    protected void receivePacket(final int id, final byte[] data, final Player player)
    {
        if (id == 0)
        {
            int prio = getPriority();
            prio += ((data[0] == 0) ? 1 : -1);
            prio %= 4;
            if (prio < 0)
            {
                prio += 4;
            }
            setPriority(prio);
        }
    }

    @Override
    public int numberOfPackets()
    {
        return 1;
    }

    @Override
    protected void save(final CompoundTag tagCompound, final int id, HolderLookup.Provider provider)
    {
        tagCompound.putByte(generateNBTName("Priority", id), (byte) getPriority());
    }

    @Override
    protected void load(final CompoundTag tagCompound, final int id, HolderLookup.Provider provider)
    {
        setPriority(tagCompound.getByte(generateNBTName("Priority", id)));
    }
}
