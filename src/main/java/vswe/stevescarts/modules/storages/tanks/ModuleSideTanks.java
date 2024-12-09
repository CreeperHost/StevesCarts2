package vswe.stevescarts.modules.storages.tanks;

import vswe.stevescarts.entities.ModularMinecart;

public class ModuleSideTanks extends ModuleTank
{
    public ModuleSideTanks(ModularMinecart cart)
    {
        super(cart);
    }

    @Override
    protected int getTankSize()
    {
        return 8000;
    }
}
