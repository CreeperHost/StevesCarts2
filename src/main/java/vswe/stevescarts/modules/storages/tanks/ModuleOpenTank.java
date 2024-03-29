package vswe.stevescarts.modules.storages.tanks;

import net.minecraft.core.BlockPos;
import vswe.stevescarts.entities.EntityMinecartModular;

public class ModuleOpenTank extends ModuleTank
{
    int cooldown;

    public ModuleOpenTank(final EntityMinecartModular cart)
    {
        super(cart);
        cooldown = 0;
    }

    @Override
    protected int getTankSize()
    {
        return 7000;
    }

    @Override
    public void update()
    {
        super.update();
        if (cooldown > 0)
        {
            --cooldown;
        }
        else
        {
            cooldown = 20;
            if (getCart().level.isRaining() && getCart().level.canSeeSky(new BlockPos(getCart().blockPosition().getX(), getCart().blockPosition().getY() + 1, getCart().blockPosition().getZ())))
            {
                //TODO
                //                fill(new FluidStack(Fluids.WATER, getCart().level.getBiome(getCart().blockPosition()).shouldSnow(getCart().level, getCart().blockPosition()) ? 2 : 5), IFluidHandler.FluidAction.EXECUTE);
            }
        }
    }
}
