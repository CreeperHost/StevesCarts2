package vswe.stevescarts.modules.workers.tools;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import vswe.stevescarts.SCConfig;
import vswe.stevescarts.entities.EntityMinecartModular;

public class ModuleDrillIron extends ModuleDrill
{
    public ModuleDrillIron(final EntityMinecartModular cart)
    {
        super(cart);
    }

    @Override
    protected int blocksOnTop()
    {
        return 3;
    }

    @Override
    protected int blocksOnSide()
    {
        return 1;
    }

    @Override
    protected float getTimeMult()
    {
        return 40.0f;
    }

    @Override
    public int getMaxDurability()
    {
        return SCConfig.COMMON.drillDurabilityIron.get();
    }

    @Override
    public ResourceLocation getRepairItem() {
        String item = SCConfig.COMMON.drillRepairIron.get();
        return item.isEmpty() ? null : ResourceLocation.parse(item);
    }

    @Override
    public int getRepairItemUnits(@NotNull ItemStack stack) {
        if (BuiltInRegistries.ITEM.getKey(stack.getItem()).equals(getRepairItem())) {
            return SCConfig.COMMON.drillRepairAmountIron.get();
        }
        return 0;
    }

    @Override
    public int getRepairSpeed()
    {
        return 50;
    }

    @Override
    public boolean useDurability()
    {
        return true;
    }
}
