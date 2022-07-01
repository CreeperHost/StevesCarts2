package vswe.stevescarts.upgrades;

import net.minecraft.nbt.ByteArrayTag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import vswe.stevescarts.blocks.tileentities.TileEntityUpgrade;
import vswe.stevescarts.containers.slots.SlotCart;
import vswe.stevescarts.helpers.Localization;
import vswe.stevescarts.api.modules.data.ModuleData;

import javax.annotation.Nonnull;
import java.util.ArrayList;

public class Blueprint extends SimpleInventoryEffect
{
    public Blueprint()
    {
        super(1, 1);
    }

    @Override
    public Class<? extends Slot> getSlot(final int i)
    {
        return SlotCart.class;
    }

    @Override
    public String getName()
    {
        return Localization.UPGRADES.BLUEPRINT.translate();
    }

    public boolean isValidForBluePrint(final TileEntityUpgrade upgrade, final ArrayList<ModuleData> modules, final ModuleData module)
    {
        @Nonnull ItemStack blueprint = upgrade.getItem(0);
        if (blueprint.isEmpty())
        {
            return false;
        }
        final CompoundTag info = blueprint.getTag();
        if (info == null)
        {
            return false;
        }
        final ByteArrayTag moduleIDTag = (ByteArrayTag) info.get("Modules");
        if (moduleIDTag == null)
        {
            return false;
        }
        final byte[] IDs = moduleIDTag.getAsByteArray();
        final ArrayList<ModuleData> missing = new ArrayList<>();
        //TODO
//        for (final byte id : IDs)
//        {
//            final ModuleData blueprintModule = ModuleData.getList().get(id);
//            final int index = modules.indexOf(blueprintModule);
//            if (index != -1)
//            {
//                modules.remove(index);
//            }
//            else
//            {
//                missing.add(blueprintModule);
//            }
//        }
        return missing.contains(module);
    }
}
