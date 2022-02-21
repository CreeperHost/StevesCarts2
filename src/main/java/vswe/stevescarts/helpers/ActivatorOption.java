package vswe.stevescarts.helpers;

import net.minecraft.ChatFormatting;
import vswe.stevescarts.modules.ModuleBase;

public class ActivatorOption
{
    private Class<? extends ModuleBase> module;
    private int id;
    private Localization.GUI.TOGGLER name;
    private int option;

    public ActivatorOption(final Localization.GUI.TOGGLER name, final Class<? extends ModuleBase> module, final int id)
    {
        this.name = name;
        this.module = module;
        this.id = id;
    }

    public ActivatorOption(final Localization.GUI.TOGGLER name, final Class<? extends ModuleBase> module)
    {
        this(name, module, 0);
    }

    public Class<? extends ModuleBase> getModule()
    {
        return module;
    }

    public String getName()
    {
        return name.translate();
    }

    public int getOption()
    {
        return option;
    }

    public int getId()
    {
        return id;
    }

    public void setOption(final int val)
    {
        option = val;
    }

    public void changeOption(final boolean dif)
    {
        if (dif)
        {
            if (++option > 5)
            {
                option = 0;
            }
        }
        else if (--option < 0)
        {
            option = 5;
        }
    }

    public boolean isDisabled()
    {
        return option == 0;
    }

    public boolean shouldActivate(final boolean isOrange)
    {
        return option == 2 || (option == 4 && !isOrange) || (option == 5 && isOrange);
    }

    public boolean shouldDeactivate(final boolean isOrange)
    {
        return option == 1 || (option == 4 && isOrange) || (option == 5 && !isOrange);
    }

    public boolean shouldToggle()
    {
        return option == 3;
    }

    public String getInfo()
    {
        if (isDisabled())
        {
            return Localization.GUI.TOGGLER.SETTING_DISABLED.translate();
        }
        return ChatFormatting.GOLD + Localization.GUI.TOGGLER.SETTING_ORANGE.translate() + ": " + (shouldActivate(true) ? (ChatFormatting.DARK_GREEN + Localization.GUI.TOGGLER.STATE_ACTIVATE.translate()) : (shouldDeactivate(true) ? (ChatFormatting.DARK_RED + Localization.GUI.TOGGLER.STATE_DEACTIVATE.translate()) : (ChatFormatting.YELLOW + Localization.GUI.TOGGLER.STATE_TOGGLE.translate()))) + "\n" + ChatFormatting.DARK_BLUE + Localization.GUI.TOGGLER.SETTING_BLUE.translate() + ": " + (shouldActivate(false) ? (ChatFormatting.DARK_GREEN + Localization.GUI.TOGGLER.STATE_ACTIVATE.translate()) : (shouldDeactivate(false) ? (ChatFormatting.DARK_RED + Localization.GUI.TOGGLER.STATE_DEACTIVATE.translate()) : (ChatFormatting.YELLOW + Localization.GUI.TOGGLER.STATE_TOGGLE.translate())));
    }
}
