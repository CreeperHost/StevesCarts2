package vswe.stevescarts.modules.workers.tools;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import vswe.stevescarts.entitys.EntityMinecartModular;

import javax.annotation.Nonnull;

public class ModuleWoodcutterNetherite extends ModuleWoodcutter
{
    public ModuleWoodcutterNetherite(final EntityMinecartModular cart)
    {
        super(cart);
    }

    @Override
    public int getPercentageDropChance()
    {
        return 80;
    }

    @Override
    public int getMaxDurability()
    {
        return 320000;
    }

    @Override
    public String getRepairItemName()
    {
        return "minecraft:netherite_ingot";
    }

    @Override
    public int getRepairItemUnits(@Nonnull ItemStack item)
    {
        if (!item.isEmpty() && item.getItem() == Items.NETHERITE_INGOT)
        {
            return 160000;
        }
        return 0;
    }

    @Override
    public int getRepairSpeed()
    {
        return 150;
    }
}
