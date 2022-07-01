package vswe.stevescarts.items;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import vswe.stevescarts.api.IModuleItem;
import vswe.stevescarts.client.StevesCartsCreativeTabs;
import vswe.stevescarts.api.modules.data.ModuleData;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class ItemCartModule extends Item implements IModuleItem
{
    public ModuleData moduleData;

    public ItemCartModule(ModuleData moduleData)
    {
        super(new Item.Properties().tab(StevesCartsCreativeTabs.MODULES));
        this.moduleData = moduleData;
    }

    @Override
    public @NotNull Component getName(@NotNull ItemStack stack)
    {
        return Component.translatable("item.stevescarts." + moduleData.getRawName());
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level world, @NotNull List<Component> textComponents, @NotNull TooltipFlag flag)
    {
        if (moduleData != null)
        {
            moduleData.addInformation(textComponents, stack.getTag());
            textComponents.add(Component.literal("ID " + moduleData.getID()));
        }
        else if (!stack.isEmpty() && stack.getItem() instanceof ItemCartModule)
        {
            textComponents.add(Component.literal("Module id " + moduleData.getID()));
        }
        else
        {
            textComponents.add(Component.literal("Unknown module id"));
        }
        super.appendHoverText(stack, world, textComponents, flag);
    }

    public ModuleData getModuleData()
    {
        return moduleData;
    }

    public void addExtraDataToCart(final CompoundTag save, @Nonnull ItemStack module, final int i)
    {
        if (module.getTag() != null && module.getTag().contains("Data"))
        {
            save.putByte("Data" + i, module.getTag().getByte("Data"));
        }
        else
        {
            ItemCartModule itemCartModule = (ItemCartModule) module.getItem();
            final ModuleData data = itemCartModule.getModuleData();
            if (data.isUsingExtraData())
            {
                save.putByte("Data" + i, data.getDefaultExtraData());
            }
        }
    }
}
