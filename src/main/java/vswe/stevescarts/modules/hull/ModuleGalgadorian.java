package vswe.stevescarts.modules.hull;

import vswe.stevescarts.api.modules.template.ModuleHull;
import vswe.stevescarts.entitys.EntityMinecartModular;

public class ModuleGalgadorian extends ModuleHull
{
    public ModuleGalgadorian(final EntityMinecartModular cart)
    {
        super(cart);
    }

    @Override
    public int getConsumption(final boolean isMoving)
    {
        if (!isMoving)
        {
            return super.getConsumption(false);
        }
        return 9;
    }
}
