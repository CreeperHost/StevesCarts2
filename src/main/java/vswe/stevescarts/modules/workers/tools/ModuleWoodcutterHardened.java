package vswe.stevescarts.modules.workers.tools;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import vswe.stevescarts.SCConfig;
import vswe.stevescarts.entities.ModularMinecart;

public class ModuleWoodcutterHardened extends ModuleWoodcutter
{
    public ModuleWoodcutterHardened(ModularMinecart cart)
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
        return SCConfig.COMMON.woodcutterDurabilityHardened.get();
    }

    @Override
    public Identifier getRepairItem() {
        String item = SCConfig.COMMON.woodcutterRepairHardened.get();
        return item.isEmpty() ? null : Identifier.parse(item);
    }

    @Override
    public int getRepairItemUnits(@NotNull ItemStack stack) {
        if (BuiltInRegistries.ITEM.getKey(stack.getItem()).equals(getRepairItem())) {
            return SCConfig.COMMON.woodcutterRepairAmountHardened.get();
        }
        return 0;
    }

    @Override
    public int getRepairSpeed()
    {
        return 400;
    }
}
