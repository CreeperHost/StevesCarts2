package vswe.stevescarts.modules.workers.tools;

import net.minecraft.world.item.ItemStack;
import vswe.stevescarts.entitys.EntityMinecartModular;

import javax.annotation.Nonnull;

public class ModuleFarmerGalgadorian extends ModuleFarmer
{
    public ModuleFarmerGalgadorian(final EntityMinecartModular cart)
    {
        super(cart);
    }

    @Override
    public int getMaxDurability()
    {
        return 1;
    }

    @Override
    public String getRepairItemName()
    {
        return null;
    }

    @Override
    public int getRepairItemUnits(@Nonnull ItemStack item)
    {
        return 0;
    }

    @Override
    public boolean useDurability()
    {
        return false;
    }

    @Override
    public int getRepairSpeed()
    {
        return 1;
    }

    @Override
    public int getRange()
    {
        return 2;
    }
}
