package vswe.stevescarts.containers.slots;

import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;

public class SlotFurnaceInput extends SlotFake
{
    public SlotFurnaceInput(final Container iinventory, final int i, final int j, final int k)
    {
        super(iinventory, i, j, k);
    }

    @Override
    public boolean mayPlace(@Nonnull ItemStack itemstack)
    {
        //TODO make this check if item is smeltable
        return true;
    }
}
