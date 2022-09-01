package vswe.stevescarts.containers.slots;

import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import vswe.stevescarts.modules.realtimers.ModuleShooter;

import javax.annotation.Nonnull;

public class SlotArrow extends SlotBase
{
    private final ModuleShooter shooter;

    public SlotArrow(final Container iinventory, final ModuleShooter shooter, final int i, final int j, final int k)
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
