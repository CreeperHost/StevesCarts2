package vswe.stevescarts.modules.workers.tools;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import vswe.stevescarts.SCConfig;
import vswe.stevescarts.entities.EntityMinecartModular;

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
    public ResourceLocation getRepairItem() {
        String item = SCConfig.farmerRepairDiamond.get();
        return item.isEmpty() ? null : new ResourceLocation(item);
    }

    @Override
    public int getRepairItemUnits(@NotNull ItemStack stack) {
        if (BuiltInRegistries.ITEM.getKey(stack.getItem()).equals(getRepairItem())) {
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
