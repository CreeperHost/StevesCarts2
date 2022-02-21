package vswe.stevescarts.modules.workers.tools;

import net.minecraft.world.item.ItemStack;
import vswe.stevescarts.entitys.EntityMinecartModular;

import javax.annotation.Nonnull;

public class ModuleDrillGalgadorian extends ModuleDrill
{
    public ModuleDrillGalgadorian(final EntityMinecartModular cart)
    {
        super(cart);
    }

    @Override
    protected int blocksOnTop()
    {
        return 9;
    }

    @Override
    protected int blocksOnSide()
    {
        return 4;
    }

    @Override
    protected float getTimeMult()
    {
        return 0.0f;
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
}
