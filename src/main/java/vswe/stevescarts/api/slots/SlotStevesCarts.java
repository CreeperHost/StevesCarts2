package vswe.stevescarts.api.slots;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;

public class SlotStevesCarts extends Slot
{
    private int defaultX;
    private int defaultY;

    public SlotStevesCarts(final Container inventory, final int id, final int x, final int y)
    {
        super(inventory, id, x, y);
        this.defaultX = x;
        this.defaultY = y;
    }

    public int getX()
    {
        return defaultX;
    }

    public int getY()
    {
        return defaultY;
    }

    public boolean containsValidItem()
    {
        return !getItem().isEmpty();
    }
}
