package vswe.stevescarts.containers.slots;

import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import vswe.stevescarts.SCConfig;
import vswe.stevescarts.api.slots.SlotStevesCarts;
import vswe.stevescarts.helpers.ComponentTypes;

import javax.annotation.Nonnull;

public class SlotExplosion extends SlotStevesCarts implements ISlotExplosions
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

    @Override
    public int getMaxStackSize() {
        return SCConfig.maxDynamites.get();
    }
}
