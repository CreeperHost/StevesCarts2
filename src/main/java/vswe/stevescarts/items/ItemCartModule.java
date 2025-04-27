package vswe.stevescarts.items;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import vswe.stevescarts.api.IModuleItem;
import vswe.stevescarts.api.modules.data.ModuleData;
import vswe.stevescarts.helpers.Localization;
import vswe.stevescarts.init.ModItemData;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Consumer;

public class ItemCartModule extends Item implements IModuleItem
{
    public ModuleData moduleData;

    public ItemCartModule(ModuleData moduleData, Item.Properties properties)
    {
        super(properties);
        this.moduleData = moduleData;
    }

    @Override
    public @NotNull Component getName(@NotNull ItemStack stack)
    {
        return Localization.translate("item.stevescarts." + moduleData.getRawName());
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display, Consumer<Component> consumer, TooltipFlag flag) {
        if (moduleData != null)
        {
            moduleData.addInformation(consumer, ModItemData.getTagCopy(stack));
            if(flag.isAdvanced())
                consumer.accept(Component.literal("ID " + moduleData.getID()));
        }
    }

    @Override
    public ModuleData getModuleData()
    {
        return moduleData;
    }

    @Override
    public void addExtraDataToCart(final CompoundTag save, @Nonnull ItemStack module, final int i)
    {
        CompoundTag tag = ModItemData.getTagCopy(module);
        if (tag.contains("Data"))
        {
            save.putByte("Data" + i, tag.getByteOr("Data", (byte) 0));
        }
        else
        {
            IModuleItem itemCartModule = (IModuleItem) module.getItem();
            final ModuleData data = itemCartModule.getModuleData();
            if (data.isUsingExtraData())
            {
                save.putByte("Data" + i, data.getDefaultExtraData());
            }
        }
    }
}
