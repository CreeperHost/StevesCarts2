package vswe.stevescarts.helpers;

import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;

public class CargoItemSelection
{
    private final Class validSlot;
    @Nonnull
    private final ItemStack icon;
    private final Localization.GUI.CARGO name;

    public CargoItemSelection(final Localization.GUI.CARGO name, final Class validSlot, @Nonnull ItemStack icon)
    {
        this.name = name;
        this.validSlot = validSlot;
        this.icon = icon;
    }

    public Class getValidSlot()
    {
        return validSlot;
    }

    @Nonnull
    public ItemStack getIcon()
    {
        return icon;
    }

    public String getName()
    {
        if (name == null)
        {
            return null;
        }
        return name.translate();
    }
}
