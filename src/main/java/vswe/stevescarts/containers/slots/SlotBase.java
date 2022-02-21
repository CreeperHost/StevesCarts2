package vswe.stevescarts.containers.slots;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;

public class SlotBase extends Slot
{
    private int ourX;
    private int ourY;

    public SlotBase(final Container inventory, final int id, final int x, final int y)
    {
        super(inventory, id, x, y);
        this.ourX = x;
        this.ourY = y;
    }

    public void setX(int x)
    {
        this.ourX = x;
    }

    public void setY(int y)
    {
        this.ourY = y;
    }

    public int getX()
    {
        return ourX;
    }

    public int getY()
    {
        return ourY;
    }

    public boolean containsValidItem()
    {
        //TODO
        return !getItem().isEmpty();// && isItemValid(getStack());
    }
}
