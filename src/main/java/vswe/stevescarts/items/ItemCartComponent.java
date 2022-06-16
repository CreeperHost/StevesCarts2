package vswe.stevescarts.items;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import vswe.stevescarts.client.creativetabs.CreativeTabSC2Items;
import vswe.stevescarts.helpers.ComponentTypes;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Locale;

public class ItemCartComponent extends Item
{
    public static int size()
    {
        return ComponentTypes.values().length;
    }

    public ComponentTypes componentType;

    public ItemCartComponent(ComponentTypes componentType)
    {
        super(new Item.Properties().tab(CreativeTabSC2Items.INSTANCE));
        this.componentType = componentType;
    }

    @Override
    public @NotNull Component getName(@NotNull ItemStack itemStack)
    {
        String name = componentType.getName().trim().replace(" ", "_").replace("'", "_").toLowerCase(Locale.ROOT);
        return Component.translatable("item.stevescarts." + name);
    }

    @Override
    public int getEntityLifespan(ItemStack itemStack, Level world)
    {
        return super.getEntityLifespan(itemStack, world);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level p_77624_2_, @NotNull List<Component> tooltip, @NotNull TooltipFlag flag)
    {
        if (!stack.isEmpty())
        {
            ItemCartComponent itemCartComponent = (ItemCartComponent) stack.getItem();
            int id = itemCartComponent.componentType.getId();
            tooltip.add(Component.literal("Component id " + id));
        }
    }
}
