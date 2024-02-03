package vswe.stevescarts.client;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import vswe.stevescarts.Constants;
import vswe.stevescarts.helpers.ComponentTypes;
import vswe.stevescarts.init.ModItems;
import vswe.stevescarts.init.StevesCartsModules;

public class StevesCartsCreativeTabs
{
    public static final DeferredRegister<CreativeModeTab> CREATIVE_TAB = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Constants.MOD_ID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> BLOCKS = CREATIVE_TAB.register(Constants.MOD_ID + ".blocks", () -> CreativeModeTab.builder()
            .title(Component.translatable("stevescarts.creativetab.blocks"))
            .icon(() -> new ItemStack(ModItems.CART_ASSEMBLER.get())).build());

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> ITEMS = CREATIVE_TAB.register(Constants.MOD_ID + ".items", () -> CreativeModeTab.builder()
            .title(Component.translatable("stevescarts.creativetab.items"))
            .icon(() -> new ItemStack(ModItems.COMPONENTS.get(ComponentTypes.WOODEN_WHEELS).get())).build());

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> MODULES = CREATIVE_TAB.register(Constants.MOD_ID + ".modules", () -> CreativeModeTab.builder()
            .title(Component.translatable("stevescarts.creativetab.modules"))
            .icon(() -> new ItemStack(ModItems.MODULES.get(StevesCartsModules.COAL_ENGINE).get())).build());
}
