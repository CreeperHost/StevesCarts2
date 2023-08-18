package vswe.stevescarts.containers.slots;

import net.minecraft.world.Container;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import vswe.stevescarts.api.slots.SlotStevesCarts;

import javax.annotation.Nonnull;

public class SlotHelmet extends SlotStevesCarts
{
    public SlotHelmet(final Container iinventory, final int i, final int j, final int k)
    {
        super(iinventory, i, j, k);
    }

    @Override
    public boolean mayPlace(@Nonnull ItemStack itemstack)
    {
        if(itemstack.getItem() instanceof ArmorItem armorItem)
        {
            EquipmentSlot equipmentSlot = armorItem.getEquipmentSlot(itemstack);
            if(equipmentSlot == EquipmentSlot.HEAD)
            {
                return true;
            }
        }
        return false;
    }
}
