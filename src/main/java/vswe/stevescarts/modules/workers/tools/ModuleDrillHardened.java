package vswe.stevescarts.modules.workers.tools;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import vswe.stevescarts.SCConfig;
import vswe.stevescarts.entities.EntityMinecartModular;

public class ModuleDrillHardened extends ModuleDrill
{
    public ModuleDrillHardened(final EntityMinecartModular cart)
    {
        super(cart);
    }

    @Override
    protected int blocksOnTop()
    {
        return 5;
    }

    @Override
    protected int blocksOnSide()
    {
        return 2;
    }

    @Override
    protected float getTimeMult()
    {
        return 4.0f;
    }

    @Override
    public int getMaxDurability()
    {
        return 320000*3;
    }

    @Override
    public ResourceLocation getRepairItem() {
        String item = SCConfig.COMMON.drillRepairHardened.get();
        return item.isEmpty() ? null : ResourceLocation.parse(item);
    }

    @Override
    public int getRepairItemUnits(@NotNull ItemStack stack) {
        if (BuiltInRegistries.ITEM.getKey(stack.getItem()).equals(getRepairItem())) {
            return 320000;
        }
        return 0;
    }

    @Override
    public int getRepairSpeed()
    {
        return 200;
    }

    @Override
    public boolean useDurability()
    {
        return true;
    }
}
