package vswe.stevescarts.containers.slots;

import net.minecraft.tags.ItemTags;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import vswe.stevescarts.api.slots.SlotStevesCarts;

public class SlotBuilder extends SlotStevesCarts
{
    public SlotBuilder(final Container iinventory, final int i, final int j, final int k)
    {
        super(iinventory, i, j, k);
    }

    @Override
    public boolean mayPlace(ItemStack itemstack)
    {
        return itemstack.is(ItemTags.RAILS);
    }
}
