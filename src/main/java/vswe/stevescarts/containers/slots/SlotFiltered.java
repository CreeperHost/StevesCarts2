package vswe.stevescarts.containers.slots;

import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class SlotFiltered extends SlotBase
{
    Supplier<Boolean> supplier;
    
    public SlotFiltered(Container inventory, int id, int x, int y, Supplier<Boolean> supplier)
    {
        super(inventory, id, x, y);
        this.supplier = supplier;
    }

    @Override
    public boolean mayPlace(@NotNull ItemStack itemStack)
    {
        return supplier.get();
    }
}
