package vswe.stevescarts.containers.slots;

import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidUtil;
import vswe.stevescarts.helpers.storages.SCTank;

public class SlotLiquidInput extends SlotBase
{
    private SCTank tank;
    private int maxsize;

    public SlotLiquidInput(final Container iinventory, final SCTank tank, final int maxsize, final int i, final int j, final int k)
    {
        super(iinventory, i, j, k);
        this.tank = tank;
        this.maxsize = maxsize;
    }

    @Override
    public boolean mayPlace(ItemStack itemStack)
    {
        return FluidUtil.getFluidHandler(itemStack).isPresent();
    }

    @Override
    public int getMaxStackSize()
    {
        return 1;
    }
}
