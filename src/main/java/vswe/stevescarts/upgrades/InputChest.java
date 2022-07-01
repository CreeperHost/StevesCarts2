package vswe.stevescarts.upgrades;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import vswe.stevescarts.api.IModuleItem;
import vswe.stevescarts.blocks.tileentities.TileEntityCartAssembler;
import vswe.stevescarts.blocks.tileentities.TileEntityUpgrade;
import vswe.stevescarts.containers.ContainerCartAssembler;
import vswe.stevescarts.containers.slots.SlotAssemblerFuel;
import vswe.stevescarts.containers.slots.SlotModule;
import vswe.stevescarts.helpers.Localization;
import vswe.stevescarts.helpers.storages.TransferHandler;
import vswe.stevescarts.api.modules.data.ModuleData;
import vswe.stevescarts.api.modules.data.ModuleDataHull;

import javax.annotation.Nonnull;
import java.util.ArrayList;

public class InputChest extends SimpleInventoryEffect
{
    public InputChest(final int inventoryWidth, final int inventoryHeight)
    {
        super(inventoryWidth, inventoryHeight);
    }

    @Override
    public String getName()
    {
        return Localization.UPGRADES.INPUT_CHEST.translate(String.valueOf(getInventorySize()));
    }

    @Override
    public void init(final TileEntityUpgrade upgrade)
    {
        upgrade.getCompound().putByte("TransferCooldown", (byte) 0);
    }

    @Override
    public Class<? extends Slot> getSlot(final int i)
    {
        return SlotModule.class;
    }

    @Override
    public void update(final TileEntityUpgrade upgrade)
    {
        if (!upgrade.getLevel().isClientSide && upgrade.getMaster() != null)
        {
            final CompoundTag comp = upgrade.getCompound();
            if (comp.getByte("TransferCooldown") != 0)
            {
                comp.putByte("TransferCooldown", (byte) (comp.getByte("TransferCooldown") - 1));
            }
            else
            {
                comp.putByte("TransferCooldown", (byte) 20);
                for (int slotId = 0; slotId < upgrade.getUpgrade().getInventorySize(); ++slotId)
                {
                    @Nonnull ItemStack itemstack = upgrade.getItem(slotId);
                    if (!itemstack.isEmpty())
                    {
                        IModuleItem itemCartModule = (IModuleItem) itemstack.getItem();
                        final ModuleData module = itemCartModule.getModuleData();
                        if (module != null)
                        {
                            if (isValidForBluePrint(upgrade.getMaster(), module))
                            {
                                if (!willInvalidate(upgrade.getMaster(), module))
                                {
                                    final int stackSize = itemstack.getCount();
                                    TransferHandler.TransferItem(itemstack, upgrade.getMaster(), new ContainerCartAssembler(0, null, upgrade.getMaster(), new SimpleContainerData(17)), Slot.class, SlotAssemblerFuel.class, 1);
                                    if (itemstack.getCount() == 0)
                                    {
                                        upgrade.setItem(slotId, ItemStack.EMPTY);
                                    }
                                    if (stackSize != itemstack.getCount())
                                    {
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private boolean willInvalidate(final TileEntityCartAssembler assembler, final ModuleData module)
    {
        final ModuleDataHull hull = assembler.getHullModule();
        if (hull == null)
        {
            return false;
        }
        final ArrayList<ModuleData> modules = assembler.getNonHullModules();
        modules.add(module);
        return ModuleData.checkForErrors(hull, modules) != null;
    }

    private boolean isValidForBluePrint(final TileEntityCartAssembler assembler, final ModuleData module)
    {
        for (final TileEntityUpgrade tile : assembler.getUpgradeTiles())
        {
            for (final BaseEffect effect : tile.getUpgrade().getEffects())
            {
                if (effect instanceof Blueprint)
                {
                    return ((Blueprint) effect).isValidForBluePrint(tile, assembler.getModules(true), module);
                }
            }
        }
        return true;
    }
}
