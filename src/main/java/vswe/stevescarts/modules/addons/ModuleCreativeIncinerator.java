package vswe.stevescarts.modules.addons;

import net.minecraft.world.item.ItemStack;
import vswe.stevescarts.entities.EntityMinecartModular;

import javax.annotation.Nonnull;

public class ModuleCreativeIncinerator extends ModuleIncinerator
{
    public ModuleCreativeIncinerator(final EntityMinecartModular cart)
    {
        super(cart);
    }

    @Override
    protected int getIncinerationCost()
    {
        return 0;
    }

    @Override
    protected boolean isItemValid(@Nonnull ItemStack item)
    {
        return !item.isEmpty();
    }

    @Override
    public boolean hasGui()
    {
        return false;
    }
}
