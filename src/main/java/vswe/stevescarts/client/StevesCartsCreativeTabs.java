package vswe.stevescarts.client;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import vswe.stevescarts.helpers.ComponentTypes;
import vswe.stevescarts.init.ModBlocks;
import vswe.stevescarts.init.ModItems;

public class StevesCartsCreativeTabs
{
    public static final CreativeModeTab BLOCKS = (new CreativeModeTab("SC2Blocks")
    {
        public @NotNull ItemStack makeIcon()
        {
            return new ItemStack(ModBlocks.CART_ASSEMBLER.get());
        }
    });

    public static final CreativeModeTab ITEMS = (new CreativeModeTab("SC2Items")
    {
        public @NotNull ItemStack makeIcon()
        {
            return new ItemStack(ModItems.COMPONENTS.get(ComponentTypes.WOODEN_WHEELS).get());
        }
    });

    public static final CreativeModeTab MODULES = (new CreativeModeTab("SC2Modules")
    {
        public @NotNull ItemStack makeIcon()
        {
            return new ItemStack(ModItems.COMPONENTS.get(ComponentTypes.WOODEN_WHEELS).get());
        }
    });
}
