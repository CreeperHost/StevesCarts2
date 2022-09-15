package vswe.stevescarts.modules.hull;

import vswe.stevescarts.api.modules.template.ModuleHull;
import vswe.stevescarts.entities.EntityMinecartModular;

public class ModuleReinforced extends ModuleHull
{
    public ModuleReinforced(final EntityMinecartModular cart)
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
        return 3;
    }
}
