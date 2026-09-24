package vswe.stevescarts.containers.slots;


import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import vswe.stevescarts.api.upgrades.BaseUpgradeEffect;
import vswe.stevescarts.blocks.tileentities.TileEntityUpgrade;
import vswe.stevescarts.upgrades.Disassemble;

public class SlotCartDisassemble extends SlotCart {
    public SlotCartDisassemble(final Container iinventory, final int i, final int j, final int k) {
        super(iinventory, i, j, k);
    }

    @Override
    public boolean mayPlace(ItemStack itemstack) {
        if (!(container instanceof TileEntityUpgrade upgrade)
                || upgrade.getUpgrade() == null
                || upgrade.getMaster() == null) {
            return false;
        }

        for (BaseUpgradeEffect effect : upgrade.getUpgrade().getEffects()) {
            if (effect instanceof Disassemble disassemble) {
                return disassemble.canDisassemble(upgrade) == 2 && super.mayPlace(itemstack);
            }
        }
        return false;
    }
}
