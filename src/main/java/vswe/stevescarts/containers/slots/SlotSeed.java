package vswe.stevescarts.containers.slots;

import net.minecraft.tags.ItemTags;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.Tags;
import vswe.stevescarts.Constants;
import vswe.stevescarts.modules.workers.tools.ModuleFarmer;

import javax.annotation.Nonnull;

public class SlotSeed extends SlotBase
{

    public SlotSeed(final Container iinventory, final int i, final int j, final int k)
    {
        super(iinventory, i, j, k);
    }

    @Override
    public boolean mayPlace(@Nonnull ItemStack itemstack)
    {
        return itemstack.is(Tags.Items.SEEDS) || itemstack.is(Tags.Items.CROPS);
    }
}
