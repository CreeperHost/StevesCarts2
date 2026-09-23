package vswe.stevescarts.upgrades;

import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import vswe.stevescarts.api.IModuleItem;
import vswe.stevescarts.api.modules.data.ModuleData;
import vswe.stevescarts.blocks.tileentities.TileEntityUpgrade;
import vswe.stevescarts.containers.slots.SlotCart;

import javax.annotation.Nonnull;
import java.util.ArrayList;

public class Blueprint extends SimpleInventoryUpgradeEffect {
    public Blueprint() {
        super(1, 1);
    }

    @Override
    public Class<? extends Slot> getSlot(final int i) {
        return SlotCart.class;
    }

    @Override
    public Component getName() {
        return Component.translatable("info.stevescarts.effectBlueprint");
    }

    public boolean isValidForBluePrint(final TileEntityUpgrade upgrade, final ArrayList<ModuleData> modules, final ModuleData module) {
        @Nonnull ItemStack blueprint = upgrade.getItem(0);
        if (blueprint.isEmpty()) {
            return false;
        }

        final NonNullList<ItemStack> blueprintModules = ModuleData.getModularItems(blueprint);
        if (blueprintModules.isEmpty()) {
            return false;
        }

        // Treat both the blueprint and assembler contents as multisets. This is
        // important for modules which allow duplicates: one installed copy must
        // only satisfy one copy requested by the blueprint.
        final ArrayList<ModuleData> unmatchedAssemblerModules = new ArrayList<>(modules);
        for (final ItemStack blueprintStack : blueprintModules) {
            if (!(blueprintStack.getItem() instanceof IModuleItem moduleItem)) {
                continue;
            }

            final ModuleData blueprintModule = moduleItem.getModuleData();
            if (blueprintModule == null) {
                continue;
            }

            final int installedIndex = unmatchedAssemblerModules.indexOf(blueprintModule);
            if (installedIndex >= 0) {
                unmatchedAssemblerModules.remove(installedIndex);
            } else if (blueprintModule == module) {
                return true;
            }
        }

        return false;
    }
}
