package vswe.stevescarts.modules.storages.tanks;

import vswe.stevescarts.entitys.EntityMinecartModular;

public class ModuleSideTanks extends ModuleTank
{
    public ModuleSideTanks(final EntityMinecartModular cart)
    {
        super(cart);
    }

    @Override
    protected int getTankSize()
    {
        return 8000;
    }
}
