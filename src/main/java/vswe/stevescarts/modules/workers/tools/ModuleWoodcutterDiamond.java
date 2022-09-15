package vswe.stevescarts.modules.workers.tools;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import vswe.stevescarts.entities.EntityMinecartModular;

import javax.annotation.Nonnull;

public class ModuleWoodcutterDiamond extends ModuleWoodcutter
{
    public ModuleWoodcutterDiamond(final EntityMinecartModular cart)
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
        return "minecraft:diamond";
    }

    @Override
    public int getRepairItemUnits(@Nonnull ItemStack item)
    {
        if (!item.isEmpty() && item.getItem() == Items.DIAMOND)
        {
            return 16000;
        }
        return 0;
    }

    @Override
    public int getRepairSpeed()
    {
        return 150;
    }
}
