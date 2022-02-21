package vswe.stevescarts.modules.engines;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import vswe.stevescarts.client.guis.GuiMinecart;
import vswe.stevescarts.entitys.EntityMinecartModular;
import vswe.stevescarts.helpers.Localization;
import vswe.stevescarts.helpers.ResourceHelper;

public abstract class ModuleSolarBase extends ModuleEngine
{
    private int light;
    private boolean maxLight;
    private int panelCoolDown;
    protected boolean down;
    private boolean upState;
    private boolean setup;

    private EntityDataAccessor<Integer> LIGHT;
    private EntityDataAccessor<Boolean> UP_STATE;
    private EntityDataAccessor<Integer> PRIORITY;

    public ModuleSolarBase(final EntityMinecartModular cart)
    {
        super(cart);
        down = true;
    }

    @Override
    protected EntityDataAccessor<Integer> getPriorityDw()
    {
        return PRIORITY;
    }

    @Override
    public boolean hasSlots()
    {
        return false;
    }

    @Override
    public void update()
    {
        super.update();
        updateSolarModel();
    }

    @Override
    protected void loadFuel()
    {
        updateLight();
        updateDataForModel();
        chargeSolar();
    }

    @Override
    public int getTotalFuel()
    {
        return getFuelLevel();
    }

    @Override
    public float[] getGuiBarColor()
    {
        return new float[]{1.0f, 1.0f, 0.0f};
    }

    private void updateLight()
    {
        if (!getCart().level.isDay() || getCart().level.isRaining())
        {
            light = 0;
        }
        else
        {
            if (getCart().level.canSeeSky(getCart().blockPosition()))
            {
                light = 15;
            }
        }
    }

    private void updateDataForModel()
    {
        if (isPlaceholder())
        {
            light = (getSimInfo().getMaxLight() ? 15 : 14);
        }
        else if (getCart().level.isClientSide)
        {
            light = getDw(LIGHT);
        }
        else
        {
            updateDw(LIGHT, light);
        }
        maxLight = (light == 15);
        if (!upState && light == 15)
        {
            light = 14;
        }
    }

    private void chargeSolar()
    {
        if (light == 15 && getCart().level.random.nextInt(8) < 4)
        {
            setFuelLevel(getFuelLevel() + getGenSpeed());
            if (getFuelLevel() > getMaxCapacity())
            {
                setFuelLevel(getMaxCapacity());
            }
        }
    }

    public int getLight()
    {
        return light;
    }

    @Override
    public void drawForeground(PoseStack matrixStack, GuiMinecart gui)
    {
        drawString(matrixStack, gui, Localization.MODULES.ENGINES.SOLAR.translate(), 8, 6, 4210752);
        String strfuel = Localization.MODULES.ENGINES.NO_POWER.translate();
        if (getFuelLevel() > 0)
        {
            strfuel = "Power: " + getFuelLevel();//Localization.MODULES.ENGINES.POWER.translate(String.valueOf(getFuelLevel()));
        }
        drawString(matrixStack, gui, strfuel, 8, 42, 4210752);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void drawBackground(PoseStack matrixStack, GuiMinecart gui, final int x, final int y)
    {
        super.drawBackground(matrixStack, gui, x, y);
        ResourceHelper.bindResource("/gui/solar.png");
        int lightWidth = light * 3;
        if (light == 15)
        {
            lightWidth += 2;
        }
        drawImage(matrixStack, gui, 9, 20, 0, 0, 54, 18);
        drawImage(matrixStack, gui, 15, 21, 0, 18, lightWidth, 16);
    }

    @Override
    public int numberOfDataWatchers()
    {
        return super.numberOfDataWatchers() + 2;
    }

    @Override
    public void initDw()
    {
        PRIORITY = createDw(EntityDataSerializers.INT);
        super.initDw();
        LIGHT = createDw(EntityDataSerializers.INT);
        UP_STATE = createDw(EntityDataSerializers.BOOLEAN);
        registerDw(LIGHT, 0);
        registerDw(UP_STATE, false);
    }

    protected boolean isGoingDown()
    {
        return down;
    }

    public void updateSolarModel()
    {
        if (getCart().level.isClientSide)
        {
            updateDataForModel();
            if (UP_STATE != null && !setup)
            {
                boolean tmpUp = getDw(UP_STATE);
                if (tmpUp)
                {
                    setAnimDone();
                    upState = true;
                    down = false;
                }
                setup = true;
            }
        }
        panelCoolDown += (maxLight ? 1 : -1);
        if (down && panelCoolDown < 0)
        {
            panelCoolDown = 0;
        }
        else if (!down && panelCoolDown > 0)
        {
            panelCoolDown = 0;
        }
        else if (Math.abs(panelCoolDown) > 20)
        {
            panelCoolDown = 0;
            down = !down;
        }
        upState = updatePanels();
        if (!getCart().level.isClientSide)
        {
            updateDw(UP_STATE, upState);
        }
    }

    @Override
    public int numberOfGuiData()
    {
        return 2;
    }

    @Override
    protected void checkGuiData(final Object[] info)
    {
        updateGuiData(info, 0, (short) (getFuelLevel() & 0xFFFF));
        updateGuiData(info, 1, (short) (getFuelLevel() >> 16 & 0xFFFF));
    }

    @Override
    public void receiveGuiData(final int id, final short data)
    {
        if (id == 0)
        {
            int dataint = data;
            if (dataint < 0)
            {
                dataint += 65536;
            }
            setFuelLevel((getFuelLevel() & 0xFFFF0000) | dataint);
        }
        else if (id == 1)
        {
            setFuelLevel((getFuelLevel() & 0xFFFF) | data << 16);
        }
    }

    protected abstract int getMaxCapacity();

    protected abstract int getGenSpeed();

    protected abstract boolean updatePanels();

    protected abstract void setAnimDone();

    @Override
    protected void Save(final CompoundTag tagCompound, final int id)
    {
        super.Save(tagCompound, id);
        tagCompound.putInt(generateNBTName("Fuel", id), getFuelLevel());
        tagCompound.putBoolean(generateNBTName("Up", id), upState);
    }

    @Override
    protected void Load(final CompoundTag tagCompound, final int id)
    {
        super.Load(tagCompound, id);
        setFuelLevel(tagCompound.getInt(generateNBTName("Fuel", id)));
        upState = tagCompound.getBoolean(generateNBTName("Up", id));
        if (upState)
        {
            setAnimDone();
        }
    }
}
