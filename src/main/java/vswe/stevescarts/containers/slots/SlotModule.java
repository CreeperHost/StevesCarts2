package vswe.stevescarts.containers.slots;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import vswe.stevescarts.api.IModuleItem;

import javax.annotation.Nonnull;

public class SlotModule extends Slot
{
    public SlotModule(final Container iinventory, final int i, final int j, final int k)
    {
        super(iinventory, i, j, k);
    }

    @Override
    public boolean mayPlace(@Nonnull ItemStack itemstack)
    {
        return itemstack.getItem() instanceof IModuleItem;
    }
}
