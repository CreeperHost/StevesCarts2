package vswe.stevescarts.modules.workers.tools;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import vswe.stevescarts.entities.ModularMinecart;

public class ModuleWoodcutterGalgadorian extends ModuleWoodcutter
{
    public ModuleWoodcutterGalgadorian(ModularMinecart cart)
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
    public ResourceLocation getRepairItem() {
        return null;
    }

    @Override
    public int getRepairItemUnits(@NotNull ItemStack item)
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
