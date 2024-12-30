package vswe.stevescarts.modules.workers.tools;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
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
        return SCConfig.drillDurabilityHardened.get();
    }

    @Override
    public ResourceLocation getRepairItem() {
        String item = SCConfig.drillRepairHardened.get();
        return item.isEmpty() ? null : new ResourceLocation(item);
    }

    @Override
    public int getRepairItemUnits(@NotNull ItemStack stack) {
        ResourceLocation name = ForgeRegistries.ITEMS.getKey(stack.getItem());
        if (name != null && name.equals(getRepairItem())) {
            return SCConfig.drillRepairAmountHardened.get();
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
