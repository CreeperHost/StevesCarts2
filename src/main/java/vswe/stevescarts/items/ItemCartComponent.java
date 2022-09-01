package vswe.stevescarts.items;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import vswe.stevescarts.client.StevesCartsCreativeTabs;
import vswe.stevescarts.helpers.ComponentTypes;

import java.util.Locale;

public class ItemCartComponent extends Item
{
    public ComponentTypes componentType;

    public ItemCartComponent(ComponentTypes componentType)
    {
        super(new Item.Properties().tab(StevesCartsCreativeTabs.ITEMS));
        this.componentType = componentType;
    }

    @Override
    public @NotNull Component getName(@NotNull ItemStack itemStack)
    {
        String name = componentType.getName().trim().replace(" ", "_").replace("'", "_").toLowerCase(Locale.ROOT);
        return Component.translatable("item.stevescarts." + name);
    }
}
