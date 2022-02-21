package vswe.stevescarts.containers.slots;

import net.minecraft.world.Container;

public class SlotIncinerator extends SlotBase implements ISpecialSlotValidator
{
    public SlotIncinerator(final Container inv, final int id, final int x, final int y)
    {
        super(inv, id, x, y);
    }

    @Override
    public boolean isSlotValid()
    {
        return false;
    }
}
