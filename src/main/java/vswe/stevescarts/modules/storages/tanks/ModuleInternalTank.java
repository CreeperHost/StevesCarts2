package vswe.stevescarts.modules.storages.tanks;

import vswe.stevescarts.entities.ModularMinecart;

public class ModuleInternalTank extends ModuleTank
{
    public ModuleInternalTank(ModularMinecart cart)
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
