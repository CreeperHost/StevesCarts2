package vswe.stevescarts.containers.slots;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import vswe.stevescarts.blocks.tileentities.TileEntityCartAssembler;
import vswe.stevescarts.init.ModItems;

import javax.annotation.Nonnull;

public class SlotOutput extends SlotAssembler
{
    public SlotOutput(final TileEntityCartAssembler assembler, final int i, final int j, final int k)
    {
        super(assembler, i, j, k, 0, true, 0);
    }

    @Override
    public void validate()
    {
    }

    @Override
    public void invalidate()
    {
    }

    @Override
    public boolean mayPlace(@Nonnull ItemStack itemstack)
    {
        if (!getAssembler().getIsAssembling() && itemstack.getItem() == ModItems.CARTS.get())
        {
            final CompoundNBT info = itemstack.getTag();
            if (info != null && info.contains("maxTime"))
            {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean shouldUpdatePlaceholder()
    {
        return false;
    }
}
