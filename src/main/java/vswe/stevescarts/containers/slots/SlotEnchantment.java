package vswe.stevescarts.containers.slots;

import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import vswe.stevescarts.api.slots.SlotStevesCarts;
import vswe.stevescarts.helpers.ModularEnchantments;

import javax.annotation.Nonnull;
import java.util.ArrayList;

public class SlotEnchantment extends SlotStevesCarts
{
    private ArrayList<ModularEnchantments.EnchantmentType> enabledTypes;

    public SlotEnchantment(final Container iinventory, final ArrayList<ModularEnchantments.EnchantmentType> enabledTypes, final int i, final int j, final int k)
    {
        super(iinventory, i, j, k);
        this.enabledTypes = enabledTypes;
    }

    @Override
    public boolean mayPlace(@Nonnull ItemStack itemstack)
    {
        return ModularEnchantments.isValidBook(itemstack, enabledTypes);
    }
}
