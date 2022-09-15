package vswe.stevescarts.modules.storages.chests;

import vswe.stevescarts.api.modules.template.ModuleChest;
import vswe.stevescarts.entities.EntityMinecartModular;

public class ModuleInternalStorage extends ModuleChest
{
    public ModuleInternalStorage(final EntityMinecartModular cart)
    {
        super(cart);
    }

    @Override
    protected int getInventoryWidth()
    {
        return 3;
    }

    @Override
    protected int getInventoryHeight()
    {
        return 3;
    }

    @Override
    protected boolean hasVisualChest()
    {
        return false;
    }

    @Override
    public int guiWidth()
    {
        return super.guiWidth() + 30;
    }
}
