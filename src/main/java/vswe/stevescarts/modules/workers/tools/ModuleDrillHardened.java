package vswe.stevescarts.modules.workers.tools;

import net.minecraft.world.item.ItemStack;
import vswe.stevescarts.entities.EntityMinecartModular;
import vswe.stevescarts.items.ItemCartComponent;

import javax.annotation.Nonnull;

public class ModuleDrillHardened extends ModuleDrill
{
    public ModuleDrillHardened(final EntityMinecartModular cart)
    {
        super(cart);
    }

    @Override
    protected int blocksOnTop()
    {
        return 5;
    }

    @Override
    protected int blocksOnSide()
    {
        return 2;
    }

    @Override
    protected float getTimeMult()
    {
        return 4.0f;
    }

    @Override
    public int getMaxDurability()
    {
        return 1000000;
    }

    @Override
    public String getRepairItemName()
    {
        return "stevescarts:component_reinforced_metal";
    }

    @Override
    public int getRepairItemUnits(@Nonnull ItemStack item)
    {
        if (!item.isEmpty() && item.getItem() == ItemCartComponent.byId(22))
        {
            return 320000;
        }
        return 0;
    }

    @Override
    public int getRepairSpeed()
    {
        return 200;
    }

    @Override
    public boolean useDurability()
    {
        return true;
    }
}
