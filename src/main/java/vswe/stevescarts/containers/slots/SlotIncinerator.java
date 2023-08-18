package vswe.stevescarts.containers.slots;

import net.minecraft.world.Container;
import vswe.stevescarts.api.slots.SlotStevesCarts;

public class SlotIncinerator extends SlotStevesCarts implements ISpecialSlotValidator
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
