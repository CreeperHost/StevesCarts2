package vswe.stevescarts.helpers;

import net.minecraft.client.resources.language.I18n;
import net.minecraft.nbt.CompoundTag;
import vswe.stevescarts.api.modules.data.ModuleData;

public class ModuleCountPair
{
    private final ModuleData data;
    private int count;
    private String name;
    private CompoundTag extraData;

    public ModuleCountPair(final ModuleData data)
    {
        this.data = data;
        count = 1;
        name = "item.stevescarts." + data.getRawName();
    }

    public int getCount()
    {
        return count;
    }

    public void setName(final String name)
    {
        this.name = name;
    }

    public void increase()
    {
        ++count;
    }

    public boolean isContainingData(final ModuleData data)
    {
        return this.data.equals(data);
    }

    public ModuleData getData()
    {
        return data;
    }

    public void setExtraData(CompoundTag data)
    {
        extraData = data;
    }

    @Override
    public String toString()
    {
        String ret = data.getCartInfoText(I18n.get(name), extraData);
        if (count != 1)
        {
            ret = ret + " x" + count;
        }
        return ret;
    }
}
