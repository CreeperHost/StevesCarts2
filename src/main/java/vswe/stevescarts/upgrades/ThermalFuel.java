package vswe.stevescarts.upgrades;

import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import vswe.stevescarts.blocks.tileentities.TileEntityUpgrade;
import vswe.stevescarts.helpers.Localization;

public class ThermalFuel extends TankUpgradeEffect
{
    public static final int LAVA_EFFICIENCY = 3;

    @Override
    public int getTankSize()
    {
        return 12000;
    }

    @Override
    public String getName()
    {
        return Localization.UPGRADES.THERMAL.translate();
    }

    @Override
    public void update(final TileEntityUpgrade upgrade)
    {
        super.update(upgrade);
        if(upgrade.getLevel() == null) return;

        if (!upgrade.getLevel().isClientSide && upgrade.getMaster() != null && !upgrade.tank.getFluid().isEmpty())
        {
            final int fuelspace = upgrade.getMaster().getMaxFuelLevel() - upgrade.getMaster().getFuelLevel();
            final int unitspace = Math.min(fuelspace / LAVA_EFFICIENCY, 200);
            if (unitspace > 100 && upgrade.tank.getFluid().getFluid().equals(Fluids.LAVA))
            {
                final FluidStack drain = upgrade.tank.drain(unitspace, IFluidHandler.FluidAction.SIMULATE);
                if (!drain.isEmpty() && drain.getAmount() > 0)
                {
                    upgrade.getMaster().setFuelLevel(upgrade.getMaster().getFuelLevel() + drain.getAmount() * LAVA_EFFICIENCY);
                    upgrade.tank.drain(unitspace, IFluidHandler.FluidAction.EXECUTE);
                }
            }
        }
    }
}
