package vswe.stevescarts.client.creativetabs;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import vswe.stevescarts.init.ModItems;

public class CreativeTabSC2Items extends CreativeModeTab
{
    public static final CreativeTabSC2Items INSTANCE = new CreativeTabSC2Items();

    public CreativeTabSC2Items()
    {
        super("SC2Items");
    }

    @Override
    public ItemStack makeIcon()
    {
        return new ItemStack(ModItems.CART_COMPONENTS.get(0));
    }
}
