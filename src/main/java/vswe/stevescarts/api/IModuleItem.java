package vswe.stevescarts.api;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import vswe.stevescarts.api.modules.data.ModuleData;

import javax.annotation.Nonnull;

public interface IModuleItem
{
    ModuleData getModuleData();

    void addExtraDataToCart(final CompoundTag save, @Nonnull ItemStack module, final int i);
}
