package vswe.stevescarts.modules.workers.tools;

import net.minecraft.world.item.ItemStack;
import vswe.stevescarts.entitys.EntityMinecartModular;
import vswe.stevescarts.items.ItemCartComponent;

import javax.annotation.Nonnull;

public class ModuleWoodcutterHardened extends ModuleWoodcutter
{
    public ModuleWoodcutterHardened(final EntityMinecartModular cart)
    {
        super(cart);
    }

    @Override
    public int getPercentageDropChance()
    {
        return 100;
    }

    @Override
    public int getMaxDurability()
    {
        return 640000;
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
        return 400;
    }
}
