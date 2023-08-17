package vswe.stevescarts.upgrades;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import vswe.stevescarts.blocks.tileentities.TileEntityUpgrade;

import javax.annotation.Nonnull;
import java.lang.reflect.Constructor;
import java.util.ArrayList;

public abstract class InventoryUpgradeEffect extends InterfaceUpgradeEffect
{
    protected ArrayList<Slot> slots;

    public InventoryUpgradeEffect()
    {
        slots = new ArrayList<>();
    }

    public Class<? extends Slot> getSlot(final int id)
    {
        return Slot.class;
    }

    public void onInventoryChanged(final TileEntityUpgrade upgrade)
    {
    }

    public abstract int getInventorySize();

    public abstract int getSlotX(final int p0);

    public abstract int getSlotY(final int p0);

    public void addSlot(final Slot slot)
    {
        slots.add(slot);
    }

    public void clear()
    {
        slots.clear();
    }

    public boolean isItemValid(final int slotId, @Nonnull ItemStack item)
    {
        return slotId >= 0 && slotId < slots.size() && slots.get(slotId).mayPlace(item);
    }

    public Slot createSlot(final TileEntityUpgrade upgrade, final int id)
    {
        try
        {
            final Class<? extends Slot> slotClass = getSlot(id);
            final Constructor slotConstructor = slotClass.getConstructor(Container.class, Integer.TYPE, Integer.TYPE, Integer.TYPE);
            final Object slotObject = slotConstructor.newInstance(upgrade, id, getSlotX(id), getSlotY(id));
            return (Slot) slotObject;
        } catch (Exception e)
        {
            System.out.println("Failed to create slot! More info below.");
            e.printStackTrace();
            return null;
        }
    }
}
