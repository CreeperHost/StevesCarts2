package vswe.stevescarts.modules.engines;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import vswe.stevescarts.api.modules.template.ModuleEngine;
import vswe.stevescarts.client.guis.GuiMinecart;
import vswe.stevescarts.entities.EntityMinecartModular;
import vswe.stevescarts.helpers.Localization;

public class ModuleCheatEngine extends ModuleEngine
{
    private EntityDataAccessor<Integer> PRIORITY;

    public ModuleCheatEngine(final EntityMinecartModular cart)
    {
        super(cart);
    }

    @Override
    protected EntityDataAccessor<Integer> getPriorityDw()
    {
        return PRIORITY;
    }

    @Override
    public void loadFuel()
    {
    }

    @Override
    public int getFuelLevel()
    {
        return 9001;
    }

    @Override
    public void initDw()
    {
        PRIORITY = createDw(EntityDataSerializers.INT);
        super.initDw();
    }

    @Override
    public void setFuelLevel(final int val)
    {
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void drawForeground(GuiGraphics guiGraphics, GuiMinecart gui)
    {
        final String[] split = getModuleName().split(" ");
        drawString(guiGraphics, gui, split[0], 8, 6, 4210752);
        if (split.length > 1)
        {
            drawString(guiGraphics, gui, split[1], 8, 16, 4210752);
        }
        drawString(guiGraphics, gui, Localization.MODULES.ENGINES.OVER_9000.translate(String.valueOf(getFuelLevel())), 8, 42, 4210752);
    }

    @Override
    public int getTotalFuel()
    {
        return 9001000;
    }

    @Override
    public float[] getGuiBarColor()
    {
        return new float[]{0.97f, 0.58f, 0.11f};
    }

    @Override
    public boolean hasSlots()
    {
        return false;
    }
}
