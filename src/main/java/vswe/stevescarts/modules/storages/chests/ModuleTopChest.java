package vswe.stevescarts.modules.storages.chests;

import vswe.stevescarts.api.modules.template.ModuleChest;
import vswe.stevescarts.entities.EntityMinecartModular;

public class ModuleTopChest extends ModuleChest
{
    public ModuleTopChest(final EntityMinecartModular cart)
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
        return 3;
    }
}
