package vswe.stevescarts.modules.workers;

import vswe.stevescarts.entities.ModularMinecart;

public class ModuleRailerLarge extends ModuleRailer
{
    public ModuleRailerLarge(ModularMinecart cart)
    {
        super(cart);
    }

    @Override
    protected int getInventoryHeight()
    {
        return 2;
    }
}
