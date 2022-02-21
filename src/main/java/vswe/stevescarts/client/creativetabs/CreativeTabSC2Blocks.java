package vswe.stevescarts.client.creativetabs;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import vswe.stevescarts.init.ModBlocks;

public class CreativeTabSC2Blocks extends CreativeModeTab
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
