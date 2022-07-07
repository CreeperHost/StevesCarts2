package vswe.stevescarts.containers.slots;

import net.creeperhost.polylib.helpers.FuelHelper;
import net.minecraft.world.item.ItemStack;
import vswe.stevescarts.api.modules.ModuleType;
import vswe.stevescarts.blocks.tileentities.TileEntityCartAssembler;

import javax.annotation.Nonnull;

public class SlotAssemblerFuel extends SlotAssembler
{
    public SlotAssemblerFuel(final TileEntityCartAssembler assembler, final int i, final int j, final int k)
    {
        super(assembler, i, j, k, ModuleType.NONE, true, 0);
    }

    @Override
    public boolean mayPlace(@Nonnull ItemStack itemstack)
    {
        return FuelHelper.isItemFuel(itemstack);
    }

    public int getFuelLevel(@Nonnull ItemStack itemstack)
    {
        if (mayPlace(itemstack))
        {
            return (int) (FuelHelper.getItemBurnTime(itemstack) * 0.25);
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
