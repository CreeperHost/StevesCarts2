package vswe.stevescarts.containers.slots;

import net.minecraft.inventory.IInventory;

public class SlotIncinerator extends SlotBase implements ISpecialSlotValidator
{
    public SlotIncinerator(final IInventory inv, final int id, final int x, final int y)
    {
        super(inv, id, x, y);
    }

    @Override
    public boolean isSlotValid()
    {
        return false;
    }
}
