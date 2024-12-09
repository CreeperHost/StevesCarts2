package vswe.stevescarts.containers.slots;

import net.minecraft.world.item.ItemStack;
import vswe.stevescarts.api.slots.SlotStevesCarts;
import vswe.stevescarts.entities.ModularMinecart;
import vswe.stevescarts.polylib.FuelHelper;

import javax.annotation.Nonnull;

public class SlotFuel extends SlotStevesCarts
{
    private final ModularMinecart minecart;

    public SlotFuel(final ModularMinecart minecart, final int i, final int j, final int k)
    {
        super(minecart, i, j, k);
        this.minecart = minecart;
    }

    @Override
    public boolean mayPlace(@Nonnull ItemStack itemstack)
    {
        return FuelHelper.isItemFuel(itemstack, minecart.level());
    }
}
