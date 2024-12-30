package vswe.stevescarts.modules.workers.tools;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
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
        return SCConfig.farmerDurabilityDiamond.get();
    }

    @Override
    public ResourceLocation getRepairItem() {
        String item = SCConfig.farmerRepairDiamond.get();
        return item.isEmpty() ? null : new ResourceLocation(item);
    }

    @Override
    public int getRepairItemUnits(@NotNull ItemStack stack) {
        ResourceLocation name = ForgeRegistries.ITEMS.getKey(stack.getItem());
        if (name != null && name.equals(getRepairItem())) {
            return SCConfig.farmerRepairAmountDiamond.get();
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
