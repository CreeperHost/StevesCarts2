package vswe.stevescarts.containers.slots;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import vswe.stevescarts.helpers.EnchantmentInfo;

import javax.annotation.Nonnull;
import java.util.ArrayList;

public class SlotEnchantment extends SlotBase
{
    private ArrayList<EnchantmentInfo.ENCHANTMENT_TYPE> enabledTypes;

    public SlotEnchantment(final IInventory iinventory, final ArrayList<EnchantmentInfo.ENCHANTMENT_TYPE> enabledTypes, final int i, final int j, final int k)
    {
        super(iinventory, i, j, k);
        this.enabledTypes = enabledTypes;
    }

    @Override
    public boolean mayPlace(@Nonnull ItemStack itemstack)
    {
        return EnchantmentInfo.isItemValid(enabledTypes, itemstack);
    }
}
