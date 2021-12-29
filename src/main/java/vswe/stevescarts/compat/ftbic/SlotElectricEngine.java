package vswe.stevescarts.compat.ftbic;

import dev.ftb.mods.ftbic.util.EnergyItemHandler;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import vswe.stevescarts.containers.slots.SlotBase;

public class SlotElectricEngine extends SlotBase
{
    public SlotElectricEngine(IInventory inventory, int id, int x, int y)
    {
        super(inventory, id, x, y);
    }

    @Override
    public boolean mayPlace(ItemStack itemStack)
    {
        return itemStack.getItem() instanceof EnergyItemHandler;
    }
}
