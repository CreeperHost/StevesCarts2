package vswe.stevescarts.client.creativetabs;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import vswe.stevescarts.init.ModItems;

public class CreativeTabSC2Modules extends CreativeModeTab
{
    public static final CreativeTabSC2Modules INSTANCE = new CreativeTabSC2Modules();

    public CreativeTabSC2Modules()
    {
        super("SC2Modules");
    }

    @Override
    public ItemStack makeIcon()
    {
        return new ItemStack(Items.DIAMOND);
        //TODO
//        return new ItemStack(ModItems.MODULES.get(0));
    }
}
