package vswe.stevescarts.containers.slots;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import vswe.stevescarts.helpers.ComponentTypes;

import javax.annotation.Nonnull;

public class SlotExplosion extends SlotBase implements ISlotExplosions
{
    public SlotExplosion(final IInventory iinventory, final int i, final int j, final int k)
    {
        super(iinventory, i, j, k);
    }

    //	@Override
    //	public int getSlotStackLimit() {
    //		return SCConfig.maxDynamites;
    //	}

    @Override
    public boolean mayPlace(@Nonnull ItemStack itemstack)
    {
        return ComponentTypes.DYNAMITE.isStackOfType(itemstack);
    }
}
