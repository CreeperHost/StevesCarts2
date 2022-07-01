package vswe.stevescarts.modules.storages.chests;

import vswe.stevescarts.api.modules.template.ModuleChest;
import vswe.stevescarts.entitys.EntityMinecartModular;

public class ModuleFrontChest extends ModuleChest
{
    public ModuleFrontChest(final EntityMinecartModular cart)
    {
        super(cart);
    }

    @Override
    protected int getInventoryWidth()
    {
        return 4;
    }

    @Override
    protected int getInventoryHeight()
    {
        return 3;
    }
}
