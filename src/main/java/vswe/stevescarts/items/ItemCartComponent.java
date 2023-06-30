package vswe.stevescarts.items;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import vswe.stevescarts.helpers.ComponentTypes;

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
        //TODO clean up all of the component names so we can stop doing this
        String name = componentType.getName().trim().replace(" ", "_").replace("'", "_").toLowerCase(Locale.ROOT);
        return Component.translatable("item.stevescarts." + name);
    }
}
