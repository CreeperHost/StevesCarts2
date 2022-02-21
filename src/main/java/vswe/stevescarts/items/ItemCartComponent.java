package vswe.stevescarts.items;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
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
        setRegistryName(getComponentName().trim().replace(" ", "_").replace("'", "_").toLowerCase(Locale.ROOT));
    }

    @Override
    public Component getName(ItemStack stack)
    {
        return new TranslatableComponent(componentType.getName());
    }

    public String getComponentName()
    {
        if (componentType == null || componentType.getName() == null)
        {
            return "Unknown_SC2_Component";
        }
        return "component_" + componentType.getName();
    }

    @Override
    public int getEntityLifespan(ItemStack itemStack, Level world) {
        return super.getEntityLifespan(itemStack, world);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level p_77624_2_, List<Component> tooltip, TooltipFlag p_77624_4_)
    {
        if (!stack.isEmpty())
        {
            ItemCartComponent itemCartComponent = (ItemCartComponent) stack.getItem();
            int id = itemCartComponent.componentType.getId();
            tooltip.add(new TranslatableComponent("Component id " + id));
        }
    }
}
