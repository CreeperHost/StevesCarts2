package vswe.stevescarts.modules.workers.tools;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import vswe.stevescarts.SCConfig;
import vswe.stevescarts.entities.EntityMinecartModular;

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
    public ResourceLocation getRepairItem() {
        String item = SCConfig.drillRepairDiamond.get();
        return item.isEmpty() ? null : new ResourceLocation(item);
    }

    @Override
    public int getRepairItemUnits(@NotNull ItemStack stack) {
        if (BuiltInRegistries.ITEM.getKey(stack.getItem()).equals(getRepairItem())) {
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
