package vswe.stevescarts.modules.engines;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import vswe.stevescarts.api.modules.template.ModuleEngine;
import vswe.stevescarts.client.guis.GuiMinecart;
import vswe.stevescarts.entities.ModularMinecart;
import vswe.stevescarts.helpers.Localization;

public abstract class ModuleThermalBase extends ModuleEngine
{
    private short coolantLevel;
    private static final int RELOAD_LIQUID_SIZE = 1;

    public ModuleThermalBase(ModularMinecart cart)
    {
        super(cart);
    }

    private int getCoolantLevel()
    {
        return coolantLevel;
    }

    private void setCoolantLevel(final int val)
    {
        coolantLevel = (short) val;
    }

    @Override
    protected void initPriorityButton()
    {
        priorityButton = new int[]{72, 17, 16, 16};
    }

    protected abstract int getEfficiency();

    protected abstract int getCoolantEfficiency();

    private boolean requiresCoolant()
    {
        return getCoolantEfficiency() > 0;
    }

    @Override
    public int guiHeight()
    {
        return 40;
    }

    @Override
    public boolean hasFuel(final int consumption)
    {
        return super.hasFuel(consumption) && (!requiresCoolant() || getCoolantLevel() >= consumption);
    }

    @Override
    public void consumeFuel(final int consumption)
    {
        super.consumeFuel(consumption);
        setCoolantLevel(getCoolantLevel() - consumption);
    }

    @Override
    protected void loadFuel()
    {
        final int consumption = getCart().getConsumption(true) * 2;
        while (getFuelLevel() <= consumption)
        {
            final int amount = getCart().drain(Fluids.LAVA, 1, IFluidHandler.FluidAction.SIMULATE);
            if (amount <= 0)
            {
                break;
            }
            getCart().drain(Fluids.LAVA, amount, IFluidHandler.FluidAction.EXECUTE);
            setFuelLevel(getFuelLevel() + amount * getEfficiency());
        }
        while (requiresCoolant() && getCoolantLevel() <= consumption)
        {
            final int amount = getCart().drain(Fluids.WATER, 1, IFluidHandler.FluidAction.SIMULATE);
            if (amount <= 0)
            {
                break;
            }
            getCart().drain(Fluids.WATER, amount, IFluidHandler.FluidAction.EXECUTE);
            setCoolantLevel(getCoolantLevel() + amount * getCoolantEfficiency());
        }
    }

    @Override
    public int getTotalFuel()
    {
        final int totalfuel = getFuelLevel() + getCart().drain(Fluids.LAVA, Integer.MAX_VALUE, IFluidHandler.FluidAction.SIMULATE) * getEfficiency();
        if (requiresCoolant())
        {
            final int totalcoolant = getCoolantLevel() + getCart().drain(Fluids.WATER, Integer.MAX_VALUE, IFluidHandler.FluidAction.SIMULATE) * getCoolantEfficiency();
            return Math.min(totalcoolant, totalfuel);
        }
        return totalfuel;
    }

    @Override
    public float[] getGuiBarColor()
    {
        return new float[]{1.0f, 0.0f, 0.0f};
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void drawForeground(GuiGraphics guiGraphics, GuiMinecart gui)
    {
        drawString(guiGraphics, gui, Localization.MODULES.ENGINES.THERMAL.translate(), 8, 6, 4210752);
        int consumption = getCart().getConsumption();
        if (consumption == 0)
        {
            consumption = 1;
        }
        String str;
        if (getFuelLevel() >= consumption && (!requiresCoolant() || getCoolantLevel() >= consumption))
        {
            str = Localization.MODULES.ENGINES.POWERED.translate();
        }
        else if (getFuelLevel() >= consumption)
        {
            str = Localization.MODULES.ENGINES.NO_WATER.translate();
        }
        else
        {
            str = Localization.MODULES.ENGINES.NO_LAVA.translate();
        }
        drawString(guiGraphics, gui, str, 8, 22, 4210752);
    }

    @Override
    public boolean hasSlots()
    {
        return false;
    }

    @Override
    public int numberOfGuiData()
    {
        return 2;
    }

    @Override
    protected void checkGuiData(final Object[] info)
    {
        updateGuiData(info, 0, (short) getFuelLevel());
        if (requiresCoolant())
        {
            updateGuiData(info, 1, (short) getCoolantLevel());
        }
    }

    @Override
    public void receiveGuiData(final int id, final short data)
    {
        if (id == 0)
        {
            setFuelLevel(data);
        }
        else if (id == 1)
        {
            setCoolantLevel(data);
        }
    }

    @Override
    protected void save(final CompoundTag tagCompound, final int id, HolderLookup.Provider provider)
    {
        super.save(tagCompound, id, provider);
        tagCompound.putShort(generateNBTName("Fuel", id), (short) getFuelLevel());
        if (requiresCoolant())
        {
            tagCompound.putShort(generateNBTName("Coolant", id), (short) getCoolantLevel());
        }
    }

    @Override
    protected void load(final CompoundTag tagCompound, final int id, HolderLookup.Provider provider)
    {
        super.load(tagCompound, id, provider);
        setFuelLevel(tagCompound.getShortOr(generateNBTName("Fuel", id), (short) 0));
        if (requiresCoolant())
        {
            setCoolantLevel(tagCompound.getShortOr(generateNBTName("Coolant", id), (short) 0));
        }
    }
}
