package vswe.stevescarts.modules.storages.tanks;

import vswe.stevescarts.entities.ModularMinecart;

public class ModuleTopTank extends ModuleTank
{
    public ModuleTopTank(ModularMinecart cart)
    {
        super(cart);
    }

    @Override
    protected int getTankSize()
    {
        return 14000;
    }
}
