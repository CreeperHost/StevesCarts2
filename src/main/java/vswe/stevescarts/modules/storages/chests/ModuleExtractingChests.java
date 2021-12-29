package vswe.stevescarts.modules.storages.chests;

import vswe.stevescarts.entitys.EntityMinecartModular;

public class ModuleExtractingChests extends ModuleChest
{
    private final float startOffset;
    private final float endOffset;
    private float chestOffset;

    public ModuleExtractingChests(final EntityMinecartModular cart)
    {
        super(cart);
        startOffset = -14.0f;
        endOffset = -24.5f;
        chestOffset = -14.0f;
    }

    @Override
    protected int getInventoryWidth()
    {
        return 18;
    }

    @Override
    protected int getInventoryHeight()
    {
        return 4;
    }

    @Override
    protected float chestFullyOpenAngle()
    {
        return 1.5707964f;
    }

    @Override
    protected void handleChest()
    {
        if (isChestActive() && lidClosed() && chestOffset > endOffset)
        {
            chestOffset -= 0.5f;
        }
        else if (!isChestActive() && lidClosed() && chestOffset < startOffset)
        {
            chestOffset += 0.5f;
        }
        else
        {
            super.handleChest();
        }
    }

    public float getChestOffset()
    {
        return chestOffset;
    }
}
