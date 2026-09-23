package vswe.stevescarts.helpers;

import net.minecraft.network.chat.Component;

import net.minecraft.ChatFormatting;
import vswe.stevescarts.api.modules.ModuleBase;

public class ActivatorOption {
    private final Class<? extends ModuleBase> module;
    private final int id;
    private final String name;
    private int option;

    public ActivatorOption(final String name, final Class<? extends ModuleBase> module, final int id) {
        this.name = name;
        this.module = module;
        this.id = id;
    }

    public ActivatorOption(final String name, final Class<? extends ModuleBase> module) {
        this(name, module, 0);
    }

    public Class<? extends ModuleBase> getModule() {
        return module;
    }

    public String getName() {
        return Component.translatable(name).getString();
    }

    public int getOption() {
        return option;
    }

    public void setOption(final int val) {
        option = val;
    }

    public int getId() {
        return id;
    }

    public void changeOption(final boolean dif) {
        if (dif) {
            if (++option > 5) {
                option = 0;
            }
        } else if (--option < 0) {
            option = 5;
        }
    }

    public boolean isDisabled() {
        return option == 0;
    }

    public boolean shouldActivate(final boolean isOrange) {
        return option == 2 || (option == 4 && !isOrange) || (option == 5 && isOrange);
    }

    public boolean shouldDeactivate(final boolean isOrange) {
        return option == 1 || (option == 4 && isOrange) || (option == 5 && !isOrange);
    }

    public boolean shouldToggle() {
        return option == 3;
    }

    public String getInfo() {
        if (isDisabled()) {
            return Component.translatable("gui.stevescarts.settingDisabled").getString();
        }
        return ChatFormatting.GOLD + Component.translatable("gui.stevescarts.settingOrange").getString() + ": " + (shouldActivate(true) ? (ChatFormatting.DARK_GREEN + Component.translatable("gui.stevescarts.stateActivate").getString()) : (shouldDeactivate(true) ? (ChatFormatting.DARK_RED + Component.translatable("gui.stevescarts.stateDeactivate").getString()) : (ChatFormatting.YELLOW + Component.translatable("gui.stevescarts.stateToggle").getString()))) + "\n" + ChatFormatting.DARK_BLUE + Component.translatable("gui.stevescarts.settingBlue").getString() + ": " + (shouldActivate(false) ? (ChatFormatting.DARK_GREEN + Component.translatable("gui.stevescarts.stateActivate").getString()) : (shouldDeactivate(false) ? (ChatFormatting.DARK_RED + Component.translatable("gui.stevescarts.stateDeactivate").getString()) : (ChatFormatting.YELLOW + Component.translatable("gui.stevescarts.stateToggle").getString())));
    }
}
