package vswe.stevescarts.items;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import vswe.stevescarts.helpers.ComponentTypes;
import vswe.stevescarts.helpers.Localization;

import java.util.Locale;

public class ItemCartComponent extends Item
{
    public ComponentTypes componentType;

    public ItemCartComponent(ComponentTypes componentType)
    {
        super(new Item.Properties());
        this.componentType = componentType;
    }

    @Override
    public @NotNull Component getName(@NotNull ItemStack itemStack)
    {
        return Localization.translate("item.stevescarts." + componentType.getName());
    }
}
