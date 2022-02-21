package vswe.stevescarts.modules.workers.tools;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import vswe.stevescarts.entitys.EntityMinecartModular;

import javax.annotation.Nonnull;

public class ModuleFarmerDiamond extends ModuleFarmer
{
    public ModuleFarmerDiamond(final EntityMinecartModular cart)
    {
        super(cart);
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
            return 150000;
        }
        return 0;
    }

    @Override
    public boolean useDurability()
    {
        return true;
    }

    @Override
    public int getRepairSpeed()
    {
        return 500;
    }

    @Override
    public int getRange()
    {
        return 1;
    }
}
