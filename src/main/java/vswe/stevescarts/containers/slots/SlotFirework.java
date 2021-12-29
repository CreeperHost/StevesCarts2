package vswe.stevescarts.containers.slots;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

import javax.annotation.Nonnull;

public class SlotFirework extends SlotBase
{
    public SlotFirework(final IInventory iinventory, final int i, final int j, final int k)
    {
        super(iinventory, i, j, k);
    }

    @Override
    public boolean mayPlace(@Nonnull ItemStack itemstack)
    {
        final Item item = itemstack.getItem();
        return item == Items.FIREWORK_ROCKET || item == Items.GUNPOWDER || item == Items.FIREWORK_STAR || item == Items.BLACK_DYE || item == Items.PAPER || item == Items.GLOWSTONE_DUST || item == Items.DIAMOND || item == Items.FIRE_CHARGE || item == Items.FEATHER || item == Items.GOLD_NUGGET || item == Items.SKELETON_SKULL;
    }
}
