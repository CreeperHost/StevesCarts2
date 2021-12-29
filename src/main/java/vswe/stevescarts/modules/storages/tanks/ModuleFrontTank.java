package vswe.stevescarts.modules.storages.tanks;

import vswe.stevescarts.entitys.EntityMinecartModular;

public class ModuleFrontTank extends ModuleTank
{
    public ModuleFrontTank(final EntityMinecartModular cart)
    {
        super(cart);
    }

    @Override
    protected int getTankSize()
    {
        return 8000;
    }
}
