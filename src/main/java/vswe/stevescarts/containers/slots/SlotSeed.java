package vswe.stevescarts.containers.slots;

import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import vswe.stevescarts.Constants;
import vswe.stevescarts.modules.workers.tools.ModuleFarmer;

import javax.annotation.Nonnull;

public class SlotSeed extends SlotBase
{
    private ModuleFarmer module;

    public SlotSeed(final Container iinventory, final ModuleFarmer module, final int i, final int j, final int k)
    {
        super(iinventory, i, j, k);
        this.module = module;
    }

    @Override
    public boolean mayPlace(@Nonnull ItemStack itemstack)
    {
        return Constants.SEEDS.contains(itemstack.getItem());
    }
}
