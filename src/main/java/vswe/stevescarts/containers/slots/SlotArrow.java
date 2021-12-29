package vswe.stevescarts.containers.slots;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import vswe.stevescarts.modules.realtimers.ModuleShooter;

import javax.annotation.Nonnull;

public class SlotArrow extends SlotBase
{
    private ModuleShooter shooter;

    public SlotArrow(final IInventory iinventory, final ModuleShooter shooter, final int i, final int j, final int k)
    {
        super(iinventory, i, j, k);
        this.shooter = shooter;
    }

    @Override
    public boolean mayPlace(@Nonnull ItemStack itemstack)
    {
        return shooter.isValidProjectileItem(itemstack);
    }
}
