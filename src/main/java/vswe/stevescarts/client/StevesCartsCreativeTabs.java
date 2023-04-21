package vswe.stevescarts.client;

import dev.architectury.registry.CreativeTabRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import vswe.stevescarts.Constants;
import vswe.stevescarts.helpers.ComponentTypes;
import vswe.stevescarts.init.ModBlocks;
import vswe.stevescarts.init.ModItems;
import vswe.stevescarts.init.StevesCartsModules;

public class StevesCartsCreativeTabs
{
    public static final CreativeTabRegistry.TabSupplier BLOCKS = CreativeTabRegistry.create(new ResourceLocation(Constants.MOD_ID, "blocks"), () ->
            new ItemStack(ModBlocks.CART_ASSEMBLER.get()));

    public static final CreativeTabRegistry.TabSupplier ITEMS = CreativeTabRegistry.create(new ResourceLocation(Constants.MOD_ID, "items"), () ->
            new ItemStack(ModItems.COMPONENTS.get(ComponentTypes.WOODEN_WHEELS).get()));

    public static final CreativeTabRegistry.TabSupplier  MODULES = CreativeTabRegistry.create(new ResourceLocation(Constants.MOD_ID, "modules"), () ->
            new ItemStack(ModItems.MODULES.get(StevesCartsModules.COAL_ENGINE).get()));

}
