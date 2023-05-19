package vswe.stevescarts.helpers;

import net.minecraft.network.chat.Component;

public abstract class LabelInformation
{
    private final Localization.MODULES.ADDONS name;

    public LabelInformation(final Localization.MODULES.ADDONS name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name.translate();
    }

    public abstract Component getLabel();
}
