package vswe.stevescarts.containers.slots;

import net.minecraft.tags.ItemTags;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.Tags;
import vswe.stevescarts.api.slots.SlotStevesCarts;

import javax.annotation.Nonnull;

public class SlotFirework extends SlotStevesCarts {
    public SlotFirework(final Container iinventory, final int i, final int j, final int k) {
        super(iinventory, i, j, k);
    }

    @Override
    public boolean mayPlace(@Nonnull ItemStack itemstack) {
        final Item item = itemstack.getItem();
        return itemstack.is(Tags.Items.DYES) || itemstack.is(ItemTags.SKULLS) || item == Items.FIREWORK_ROCKET || item == Items.GUNPOWDER || item == Items.FIREWORK_STAR || item == Items.PAPER || item == Items.GLOWSTONE_DUST || item == Items.DIAMOND || item == Items.FIRE_CHARGE || item == Items.FEATHER || item == Items.GOLD_NUGGET;
    }
}
