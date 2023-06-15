package vswe.stevescarts.upgrades;

import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import vswe.stevescarts.blocks.tileentities.TileEntityCartAssembler;
import vswe.stevescarts.blocks.tileentities.TileEntityUpgrade;
import vswe.stevescarts.containers.ContainerCartAssembler;
import vswe.stevescarts.containers.slots.SlotCart;
import vswe.stevescarts.containers.slots.SlotModule;
import vswe.stevescarts.helpers.Localization;
import vswe.stevescarts.helpers.storages.TransferHandler;
import vswe.stevescarts.init.ModItems;
import vswe.stevescarts.api.modules.data.ModuleData;

import javax.annotation.Nonnull;

public class Disassemble extends InventoryEffect
{
    @Override
    public int getInventorySize()
    {
        return 31;
    }

    @Override
    public int getSlotX(final int id)
    {
        if (id == 0)
        {
            return 178;
        }
        return 38 + (id - 1) % 10 * 18;
    }

    @Override
    public int getSlotY(final int id)
    {
        int y;
        if (id == 0)
        {
            y = 0;
        }
        else
        {
            y = (id - 1) / 10 + 2;
        }
        return 8 + y * 18;
    }

    @Override
    public Class<? extends Slot> getSlot(final int i)
    {
        if (i == 0)
        {
            return SlotCart.class;
        }
        return SlotModule.class;
    }

    @Override
    public void load(final TileEntityUpgrade upgrade, final CompoundTag compound)
    {
        this.setLastCart(upgrade, upgrade.getItem(0));
    }

    @Override
    public String getName()
    {
        return Localization.UPGRADES.DISASSEMBLE.translate();
    }

    @Override
    public void onInventoryChanged(final TileEntityUpgrade upgrade)
    {
        final ItemStack cart = upgrade.getItem(0);
        if (!this.updateCart(upgrade, cart))
        {
            boolean needsToPuke = true;
            for (int i = 1; i < this.getInventorySize(); ++i)
            {
                if (upgrade.getItem(i).isEmpty())
                {
                    final ItemStack item = upgrade.getItem(0);
                    upgrade.setItem(0, ItemStack.EMPTY);
                    upgrade.setItem(i, item);
                    needsToPuke = false;
                    break;
                }
            }
            if (needsToPuke)
            {
                if (!upgrade.getLevel().isClientSide)
                {
                    upgrade.getMaster().puke(upgrade.getItem(0).copy());
                }
                upgrade.setItem(0, ItemStack.EMPTY);
            }
        }
    }

    @Override
    public void removed(final TileEntityUpgrade upgrade)
    {
        this.updateCart(upgrade, ItemStack.EMPTY);
    }

    private void resetMaster(final TileEntityCartAssembler master, final boolean full)
    {
        for (int i = 0; i < master.getContainerSize() - master.nonModularSlots(); i++)
        {
            if (!master.getItem(i).isEmpty())
            {
                if (TileEntityCartAssembler.getSlotStatus(master.getItem(i)) <= 0)
                {
                    master.setItem(i, ItemStack.EMPTY);
                }
                else if (full)
                {
                    if (!master.getLevel().isClientSide)
                    {
                        master.puke(master.getItem(i).copy());
                    }
                    master.setItem(i, ItemStack.EMPTY);
                }
            }
        }
    }

    private void setLastCart(final TileEntityUpgrade upgrade, final ItemStack cart)
    {
        if (!cart.isEmpty())
        {
            cart.save(upgrade.getCompound());
        }
    }

    private ItemStack getLastCart(final TileEntityUpgrade upgrade)
    {
        return ItemStack.of(upgrade.getCompound());
    }

    private boolean updateCart(final TileEntityUpgrade upgrade, final ItemStack cart)
    {
        if (upgrade.getMaster() != null)
        {
            if (cart.isEmpty() || cart.getItem() != ModItems.CARTS.get() || cart.getTag() == null || cart.getTag().contains("maxTime"))
            {
                this.resetMaster(upgrade.getMaster(), false);
                this.setLastCart(upgrade, ItemStack.EMPTY);
                if (!cart.isEmpty())
                {
                    upgrade.getMaster().puke(cart);
                    upgrade.setItem(0, ItemStack.EMPTY);
                }
            }
            else
            {
                @Nonnull final ItemStack last = this.getLastCart(upgrade);
                this.setLastCart(upgrade, cart);

                int result = this.canDisassemble(upgrade);
                boolean reset = false;
                if (result > 0 && !last.isEmpty() && !ItemStack.isSameItem(cart, last))
                {
                    result = 2;
                    reset = true;
                }

                if (result != 2)
                {
                    return result == 1 && !upgrade.getMaster().getItem(0).isEmpty();
                }

                if (reset)
                {
                    this.resetMaster(upgrade.getMaster(), true);
                }

                boolean addedHull = false;
                final NonNullList<ItemStack> modules = ModuleData.getModularItems(cart);
                for (ItemStack item : modules)
                {
                    TileEntityCartAssembler.getOrCreateCompound(item).putInt(TileEntityCartAssembler.MODIFY_STATUS, 0);
                    TransferHandler.TransferItem(item, upgrade.getMaster(), new ContainerCartAssembler(0, null, upgrade.getMaster(), new SimpleContainerData(0)), 1);
                    if (!addedHull)
                    {
                        addedHull = true;
                        upgrade.getMaster().updateSlots();
                    }
                }
            }
        }
        return true;
    }

    public int canDisassemble(final TileEntityUpgrade upgrade)
    {
        int disassembleCount = 0;
        for (final BaseEffect effect : upgrade.getMaster().getEffects())
        {
            if (effect instanceof Disassemble)
            {
                disassembleCount++;
            }
        }
        if (disassembleCount != 1)
        {
            return 0;
        }
        for (int i = 0; i < upgrade.getMaster().getContainerSize() - upgrade.getMaster().nonModularSlots(); i++)
        {
            if (!upgrade.getMaster().getItem(i).isEmpty() && TileEntityCartAssembler.getSlotStatus(upgrade.getMaster().getItem(i)) <= 0)
            {
                return 1;
            }
        }
        for (int i = 0; i < upgrade.getMaster().getContainerSize() - upgrade.getMaster().nonModularSlots(); i++)
        {
            if (!upgrade.getMaster().getItem(i).isEmpty())
            {
                return 0;
            }
        }
        return 2;
    }
}
