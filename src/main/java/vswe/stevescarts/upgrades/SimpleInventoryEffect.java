package vswe.stevescarts.upgrades;

public abstract class SimpleInventoryEffect extends InventoryEffect
{
    private final int inventoryWidth;
    private final int inventoryHeight;

    public SimpleInventoryEffect(final int inventoryWidth, final int inventoryHeight)
    {
        this.inventoryWidth = inventoryWidth;
        this.inventoryHeight = inventoryHeight;
    }

    @Override
    public int getInventorySize()
    {
        return inventoryWidth * inventoryHeight;
    }

    @Override
    public int getSlotX(final int id)
    {
        return (256 - 18 * inventoryWidth) / 2 + id % inventoryWidth * 18;
    }

    @Override
    public int getSlotY(final int id)
    {
        return (107 - 18 * inventoryHeight) / 2 + id / inventoryWidth * 18;
    }
}
