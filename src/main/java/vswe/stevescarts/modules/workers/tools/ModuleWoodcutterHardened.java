package vswe.stevescarts.modules.workers.tools;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import vswe.stevescarts.SCConfig;
import vswe.stevescarts.entities.EntityMinecartModular;

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
        return SCConfig.woodcutterDurabilityHardened.get();
    }

    @Override
    public ResourceLocation getRepairItem() {
        String item = SCConfig.woodcutterRepairHardened.get();
        return item.isEmpty() ? null : new ResourceLocation(item);
    }

    @Override
    public int getRepairItemUnits(@NotNull ItemStack stack) {
        if (BuiltInRegistries.ITEM.getKey(stack.getItem()).equals(getRepairItem())) {
            return SCConfig.woodcutterRepairAmountHardened.get();
        }
        return 0;
    }

    @Override
    public int getRepairSpeed()
    {
        return 400;
    }
}
