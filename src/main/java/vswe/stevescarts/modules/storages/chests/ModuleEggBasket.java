package vswe.stevescarts.modules.storages.chests;

import vswe.stevescarts.api.modules.template.ModuleChest;
import vswe.stevescarts.entities.EntityMinecartModular;

public class ModuleEggBasket extends ModuleChest
{
    public ModuleEggBasket(final EntityMinecartModular cart)
    {
        super(cart);
    }

    @Override
    protected int getInventoryWidth()
    {
        return 6;
    }

    @Override
    protected int getInventoryHeight()
    {
        return 4;
    }

    @Override
    protected boolean playChestSound()
    {
        return false;
    }

    @Override
    protected float getLidSpeed()
    {
        return 0.02094395f;
    }

    @Override
    protected float chestFullyOpenAngle()
    {
        return 0.3926991f;
    }
}
