package vswe.stevescarts.modules.workers.tools;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import vswe.stevescarts.entitys.EntityMinecartModular;

import javax.annotation.Nonnull;

public class ModuleDrillIron extends ModuleDrill
{
    public ModuleDrillIron(final EntityMinecartModular cart)
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
        return 40.0f;
    }

    @Override
    public int getMaxDurability()
    {
        return 50000;
    }

    @Override
    public String getRepairItemName()
    {
        return "minecraft:iron_ingot";
    }

    @Override
    public int getRepairItemUnits(@Nonnull ItemStack item)
    {
        if (!item.isEmpty() && item.getItem() == Items.IRON_INGOT)
        {
            return 20000;
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
