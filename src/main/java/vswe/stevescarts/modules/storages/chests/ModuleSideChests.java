package vswe.stevescarts.modules.storages.chests;

import vswe.stevescarts.api.modules.template.ModuleChest;
import vswe.stevescarts.entities.EntityMinecartModular;

public class ModuleSideChests extends ModuleChest
{
    public ModuleSideChests(final EntityMinecartModular cart)
    {
        super(cart);
    }

    @Override
    protected int getInventoryWidth()
    {
        return 5;
    }

    @Override
    protected int getInventoryHeight()
    {
        return 3;
    }
}
