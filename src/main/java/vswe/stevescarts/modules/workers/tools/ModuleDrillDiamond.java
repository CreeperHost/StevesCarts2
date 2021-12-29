package vswe.stevescarts.modules.workers.tools;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import vswe.stevescarts.entitys.EntityMinecartModular;

import javax.annotation.Nonnull;

public class ModuleDrillDiamond extends ModuleDrill
{
    public ModuleDrillDiamond(final EntityMinecartModular cart)
    {
        super(cart);
    }

    @Override
    protected int blocksOnTop()
    {
        return 3;
    }

    @Override
    protected int blocksOnSide()
    {
        return 1;
    }

    @Override
    protected float getTimeMult()
    {
        return 8.0f;
    }

    @Override
    public int getMaxDurability()
    {
        return 300000;
    }

    @Override
    public String getRepairItemName()
    {
        return "minecraft:diamond";
    }

    @Override
    public int getRepairItemUnits(@Nonnull ItemStack item)
    {
        if (!item.isEmpty() && item.getItem() == Items.DIAMOND)
        {
            return 100000;
        }
        return 0;
    }

    @Override
    public int getRepairSpeed()
    {
        return 50;
    }

    @Override
    public boolean useDurability()
    {
        return true;
    }
}
