package vswe.stevescarts.modules.workers.tools;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import vswe.stevescarts.SCConfig;
import vswe.stevescarts.entities.ModularMinecart;

public class ModuleWoodcutterDiamond extends ModuleWoodcutter {
    public ModuleWoodcutterDiamond(ModularMinecart cart) {
        super(cart);
    }

    @Override
    public int getPercentageDropChance() {
        return 80;
    }

    @Override
    public int getMaxDurability() {
        return SCConfig.COMMON.woodcutterDurabilityDiamond.get();
    }

    @Override
    public Identifier getRepairItem() {
        String item = SCConfig.COMMON.woodcutterRepairDiamond.get();
        return item.isEmpty() ? null : Identifier.parse(item);
    }

    @Override
    public int getRepairItemUnits(@NotNull ItemStack stack) {
        if (BuiltInRegistries.ITEM.getKey(stack.getItem()).equals(getRepairItem())) {
            return SCConfig.COMMON.woodcutterRepairAmountDiamond.get();
        }
        return 0;
    }

    @Override
    public int getRepairSpeed() {
        return 150;
    }
}
