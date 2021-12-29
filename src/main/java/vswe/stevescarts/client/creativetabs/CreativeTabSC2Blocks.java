package vswe.stevescarts.client.creativetabs;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import vswe.stevescarts.init.ModBlocks;

public class CreativeTabSC2Blocks extends ItemGroup
{
    public static final CreativeTabSC2Blocks INSTANCE = new CreativeTabSC2Blocks();

    public CreativeTabSC2Blocks()
    {
        super("SC2Blocks");
    }

    @Override
    public ItemStack makeIcon()
    {
        return new ItemStack(ModBlocks.CART_ASSEMBLER.get());
    }
}
