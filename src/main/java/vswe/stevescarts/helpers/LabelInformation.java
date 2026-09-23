package vswe.stevescarts.helpers;

import net.minecraft.network.chat.Component;

public abstract class LabelInformation {
    private final String name;

    public LabelInformation(final String name) {
        this.name = name;
    }

    public String getName() {
        return Component.translatable(name).getString();
    }

    public abstract Component getLabel();
}
