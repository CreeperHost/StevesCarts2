package vswe.stevescarts.modules.workers.tools;

import net.minecraft.item.ItemStack;
import vswe.stevescarts.entitys.EntityMinecartModular;

import javax.annotation.Nonnull;

public class ModuleWoodcutterGalgadorian extends ModuleWoodcutter
{
    public ModuleWoodcutterGalgadorian(final EntityMinecartModular cart)
    {
        super(cart);
    }

    @Override
    public int getPercentageDropChance()
    {
        return 125;
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
