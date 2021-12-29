package vswe.stevescarts.modules.storages.tanks;

import vswe.stevescarts.entitys.EntityMinecartModular;

public class ModuleAdvancedTank extends ModuleTank
{
    public ModuleAdvancedTank(final EntityMinecartModular cart)
    {
        super(cart);
    }

    @Override
    protected int getTankSize()
    {
        return 32000;
    }
}
