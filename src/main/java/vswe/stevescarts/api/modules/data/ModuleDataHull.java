package vswe.stevescarts.api.modules.data;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import vswe.stevescarts.api.modules.ModuleBase;
import vswe.stevescarts.api.modules.ModuleType;

import java.util.List;

public class ModuleDataHull extends ModuleData
{
    private int modularCapacity;
    private int engineMaxCount;
    private int addonMaxCount;
    private int complexityMax;

    public ModuleDataHull(final ResourceLocation id, final String name, final Class<? extends ModuleBase> moduleClass, ModuleType moduleType)
    {
        super(id, name, moduleClass, moduleType, 0);
    }

    public ModuleDataHull setCapacity(final int val)
    {
        modularCapacity = val;
        return this;
    }

    public ModuleDataHull setEngineMax(final int val)
    {
        engineMaxCount = val;
        return this;
    }

    public ModuleDataHull setAddonMax(final int val)
    {
        addonMaxCount = val;
        return this;
    }

    public ModuleDataHull setComplexityMax(final int val)
    {
        complexityMax = val;
        return this;
    }

    public int getEngineMax()
    {
        return engineMaxCount;
    }

    public int getAddonMax()
    {
        return addonMaxCount;
    }

    public int getCapacity()
    {
        return modularCapacity;
    }

    public int getComplexityMax()
    {
        return complexityMax;
    }

    @Override
    public void addExtraMessage(List<Component> list) {
        super.addExtraMessage(list);
        list.add(Component.translatable("gui.stevescarts.hullCapacity")
                .append(": ")
                .withStyle(ChatFormatting.BLUE)
                .append(Component.literal(String.valueOf(getCapacity()))
                        .withStyle(ChatFormatting.WHITE)
                )
        );
        list.add(Component.translatable("gui.stevescarts.complexityCap")
                .append(": ")
                .withStyle(ChatFormatting.BLUE)
                .append(Component.literal(String.valueOf(getComplexityMax()))
                        .withStyle(ChatFormatting.WHITE)
                )
        );
        list.add(Component.translatable("gui.stevescarts.max_addons")
                .append(": ")
                .withStyle(ChatFormatting.BLUE)
                .append(Component.literal(String.valueOf(getAddonMax()))
                        .withStyle(ChatFormatting.WHITE)
                )
        );
        list.add(Component.translatable("gui.stevescarts.max_engines")
                .append(": ")
                .withStyle(ChatFormatting.BLUE)
                .append(Component.literal(String.valueOf(getEngineMax()))
                        .withStyle(ChatFormatting.WHITE)
                )
        );
    }
}
