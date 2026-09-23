package vswe.stevescarts.helpers;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;

import javax.annotation.Nonnull;

public class CargoItemSelection {
    private final Class validSlot;
    @Nonnull
    private final ItemStackTemplate icon;
    private final String name;

    public CargoItemSelection(final String name, final Class validSlot, @Nonnull ItemStackTemplate icon) {
        this.name = name;
        this.validSlot = validSlot;
        this.icon = icon;
    }

    public Class getValidSlot() {
        return validSlot;
    }

    @Nonnull
    public ItemStack getIcon() {
        return icon.create();
    }

    public String getName() {
        if (name == null) {
            return null;
        }
        return Component.translatable(name).getString();
    }
}
