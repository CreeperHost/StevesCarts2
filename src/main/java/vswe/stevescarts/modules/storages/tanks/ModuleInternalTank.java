package vswe.stevescarts.modules.storages.tanks;

import vswe.stevescarts.entitys.EntityMinecartModular;

public class ModuleInternalTank extends ModuleTank
{
    public ModuleInternalTank(final EntityMinecartModular cart)
    {
        super(cart);
    }

    @Override
    protected int getTankSize()
    {
        return 4000;
    }

    @Override
    public boolean hasVisualTank()
    {
        return false;
    }
}
