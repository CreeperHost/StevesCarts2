package vswe.stevescarts.modules.workers.tools;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import vswe.stevescarts.SCConfig;
import vswe.stevescarts.entities.ModularMinecart;

public class ModuleWoodcutterNetherite extends ModuleWoodcutter
{
    public ModuleWoodcutterNetherite(ModularMinecart cart)
    {
        super(cart);
    }

    @Override
    public int getPercentageDropChance()
    {
        return 90;
    }

    @Override
    public int getMaxDurability()
    {
        return SCConfig.COMMON.woodcutterDurabilityNetherite.get();
    }

    @Override
    public ResourceLocation getRepairItem() {
        String item = SCConfig.COMMON.woodcutterRepairNetherite.get();
        return item.isEmpty() ? null : ResourceLocation.parse(item);
    }

    @Override
    public int getRepairItemUnits(@NotNull ItemStack stack) {
        if (BuiltInRegistries.ITEM.getKey(stack.getItem()).equals(getRepairItem())) {
            return SCConfig.COMMON.woodcutterRepairAmountNetherite.get();
        }
        return 0;
    }

    @Override
    public int getRepairSpeed()
    {
        return 250;
    }
}
