package vswe.stevescarts.modules.storages.tanks;

import vswe.stevescarts.entities.EntityMinecartModular;

public class ModuleTopTank extends ModuleTank
{
    public ModuleTopTank(final EntityMinecartModular cart)
    {
        super(cart);
    }

    @Override
    protected int getTankSize()
    {
        return 14000;
    }
}
