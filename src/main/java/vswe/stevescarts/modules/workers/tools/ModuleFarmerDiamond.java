package vswe.stevescarts.modules.workers.tools;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import vswe.stevescarts.SCConfig;
import vswe.stevescarts.entities.ModularMinecart;

public class ModuleFarmerDiamond extends ModuleFarmer
{
    public ModuleFarmerDiamond(ModularMinecart cart)
    {
        super(cart);
    }

    @Override
    public int getMaxDurability()
    {
        return SCConfig.COMMON.farmerDurabilityDiamond.get();
    }

    @Override
    public ResourceLocation getRepairItem() {
        String item = SCConfig.COMMON.farmerRepairDiamond.get();
        return item.isEmpty() ? null : ResourceLocation.parse(item);
    }

    @Override
    public int getRepairItemUnits(@NotNull ItemStack stack) {
        if (BuiltInRegistries.ITEM.getKey(stack.getItem()).equals(getRepairItem())) {
            return SCConfig.COMMON.farmerRepairAmountDiamond.get();
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
