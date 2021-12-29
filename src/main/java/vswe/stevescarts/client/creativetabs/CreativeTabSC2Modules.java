package vswe.stevescarts.client.creativetabs;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import vswe.stevescarts.init.ModItems;

public class CreativeTabSC2Modules extends ItemGroup
{
    public static final CreativeTabSC2Modules INSTANCE = new CreativeTabSC2Modules();

    public CreativeTabSC2Modules()
    {
        super("SC2Modules");
    }

    @Override
    public ItemStack makeIcon()
    {
        return new ItemStack(ModItems.MODULES.get(0));
    }
}
