package vswe.stevescarts.containers.slots;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ForgeHooks;
import vswe.stevescarts.blocks.tileentities.TileEntityCartAssembler;

import javax.annotation.Nonnull;

public class SlotAssemblerFuel extends SlotAssembler
{
    public SlotAssemblerFuel(final TileEntityCartAssembler assembler, final int i, final int j, final int k)
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
        return ForgeHooks.getBurnTime(itemstack) != 0;
    }

    public int getFuelLevel(@Nonnull ItemStack itemstack)
    {
        if (mayPlace(itemstack))
        {
            return (int) ((int) ForgeHooks.getBurnTime(itemstack) * 0.25);//(TileEntityFurnace.getItemBurnTime(itemstack) * 0.25f);
        }
        return 0;
    }

    @Override
    public int getMaxStackSize()
    {
        return 64;
    }

    @Override
    public boolean shouldUpdatePlaceholder()
    {
        return false;
    }
}
