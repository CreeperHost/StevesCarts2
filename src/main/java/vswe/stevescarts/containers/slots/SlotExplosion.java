package vswe.stevescarts.containers.slots;

import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import vswe.stevescarts.helpers.ComponentTypes;

import javax.annotation.Nonnull;

public class SlotExplosion extends SlotBase implements ISlotExplosions
{
    public SlotExplosion(final Container iinventory, final int i, final int j, final int k)
    {
        super(iinventory, i, j, k);
    }

    @Override
    public boolean mayPlace(@Nonnull ItemStack itemstack)
    {
        return ComponentTypes.DYNAMITE.isStackOfType(itemstack);
    }
}
