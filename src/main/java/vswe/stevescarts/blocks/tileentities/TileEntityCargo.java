package vswe.stevescarts.blocks.tileentities;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.InvWrapper;
import vswe.stevescarts.blocks.BlockCargoManager;
import vswe.stevescarts.containers.ContainerCargo;
import vswe.stevescarts.containers.slots.*;
import vswe.stevescarts.helpers.CargoItemSelection;
import vswe.stevescarts.helpers.ComponentTypes;
import vswe.stevescarts.helpers.Localization;
import vswe.stevescarts.helpers.storages.TransferHandler;
import vswe.stevescarts.helpers.storages.TransferManager;
import vswe.stevescarts.init.ModBlocks;
import vswe.stevescarts.init.ModItems;
import vswe.stevescarts.items.ItemCartModule;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;

public class TileEntityCargo extends TileEntityManager implements MenuProvider
{
    public static ArrayList<CargoItemSelection> itemSelections;
    public int[] target;
    public ArrayList<SlotCargo> cargoSlots;
    public int lastLayout;
    private TransferManager latestTransferToBeUsed;
    protected final SimpleContainerData dataAccess = new SimpleContainerData(16)
    {
        public int get(int id)
        {
            return switch (id)
                    {
                        case 0 -> layoutType;
                        case 1 -> color[0];
                        case 2 -> color[1];
                        case 3 -> color[2];
                        case 4 -> color[3];
                        case 5 -> toCart[0] ? 1 : 0;
                        case 6 -> toCart[1] ? 1 : 0;
                        case 7 -> toCart[2] ? 1 : 0;
                        case 8 -> toCart[3] ? 1 : 0;
                        case 9 -> target[0];
                        case 10 -> target[1];
                        case 11 -> target[2];
                        case 12 -> target[3];
                        case 13 -> doReturn[0] ? 1 : 0;
                        case 14 -> doReturn[1] ? 1 : 0;
                        case 15 -> doReturn[2] ? 1 : 0;
                        case 16 -> doReturn[3] ? 1 : 0;
                        default -> throw new IllegalArgumentException("Invalid index: " + id);
                    };
        }

        public void set(int p_221477_1_, int p_221477_2_)
        {
            throw new IllegalStateException("Cannot set values through IIntArray");
        }

        public int getCount()
        {
            return 17;
        }
    };

    public TileEntityCargo(BlockPos blockPos, BlockState blockState)
    {
        super(ModBlocks.CARGO_MANAGER_TILE.get(), blockPos, blockState);
        target = new int[]{0, 0, 0, 0};
        lastLayout = -1;
    }

    public static void loadSelectionSettings()
    {
        (TileEntityCargo.itemSelections = new ArrayList<>()).add(new CargoItemSelection(Localization.GUI.CARGO.AREA_ALL, Slot.class, new ItemStack(ModItems.CARTS.get(), 1)));
        //TODO API CHANGE
//        TileEntityCargo.itemSelections.add(new CargoItemSelection(Localization.GUI.CARGO.AREA_ENGINE, SlotFuel.class, ItemCartModule.createModuleStack(0)));
//        TileEntityCargo.itemSelections.add(new CargoItemSelection(Localization.GUI.CARGO.AREA_RAILER, SlotBuilder.class, ItemCartModule.createModuleStack(12)));
        TileEntityCargo.itemSelections.add(new CargoItemSelection(Localization.GUI.CARGO.AREA_STORAGE, SlotChest.class, new ItemStack(Blocks.CHEST, 1)));
        TileEntityCargo.itemSelections.add(new CargoItemSelection(Localization.GUI.CARGO.AREA_TORCHES, SlotTorch.class, new ItemStack(Blocks.TORCH, 1)));
        TileEntityCargo.itemSelections.add(new CargoItemSelection(Localization.GUI.CARGO.AREA_EXPLOSIVES, ISlotExplosions.class, ComponentTypes.DYNAMITE.getItemStack()));
        TileEntityCargo.itemSelections.add(new CargoItemSelection(Localization.GUI.CARGO.AREA_ARROWS, SlotArrow.class, new ItemStack(Items.ARROW, 1)));
        TileEntityCargo.itemSelections.add(new CargoItemSelection(Localization.GUI.CARGO.AREA_BRIDGE, SlotBridge.class, new ItemStack(Blocks.BRICKS, 1)));
        TileEntityCargo.itemSelections.add(new CargoItemSelection(Localization.GUI.CARGO.AREA_SEEDS, SlotSeed.class, new ItemStack(Items.WHEAT_SEEDS, 1)));
        TileEntityCargo.itemSelections.add(new CargoItemSelection(Localization.GUI.CARGO.AREA_FERTILIZER, SlotFertilizer.class, new ItemStack(Items.BONE_MEAL, 1)));
        TileEntityCargo.itemSelections.add(new CargoItemSelection(null, null, ItemStack.EMPTY));
        TileEntityCargo.itemSelections.add(new CargoItemSelection(Localization.GUI.CARGO.AREA_SAPLINGS, SlotSapling.class, new ItemStack(Blocks.OAK_SAPLING, 1)));
        TileEntityCargo.itemSelections.add(new CargoItemSelection(Localization.GUI.CARGO.AREA_FIREWORK, SlotFirework.class, new ItemStack(Items.FIREWORK_ROCKET, 1)));
        TileEntityCargo.itemSelections.add(new CargoItemSelection(Localization.GUI.CARGO.AREA_BUCKETS, SlotMilker.class, new ItemStack(Items.BUCKET, 1)));
        TileEntityCargo.itemSelections.add(new CargoItemSelection(Localization.GUI.CARGO.AREA_CAKES, SlotCake.class, new ItemStack(Items.CAKE, 1)));
    }

    @Override
    protected void updateLayout()
    {
        if (cargoSlots != null && lastLayout != layoutType)
        {
            for (final SlotCargo slot : cargoSlots)
            {
                slot.updatePosition();
            }
            lastLayout = layoutType;
        }
    }

    @Override
    protected boolean isTargetValid(final TransferManager transfer)
    {
        return target[transfer.getSetting()] >= 0 && target[transfer.getSetting()] < TileEntityCargo.itemSelections.size();
    }

    @Override
    protected void receiveClickData(final int packetid, final int id, final int dif)
    {
        if (packetid == 1)
        {
            final int[] target = this.target;
            target[id] += dif;
            if (this.target[id] >= TileEntityCargo.itemSelections.size())
            {
                this.target[id] = 0;
            }
            else if (this.target[id] < 0)
            {
                this.target[id] = TileEntityCargo.itemSelections.size() - 1;
            }
            if (color[id] - 1 == getSide())
            {
                reset();
            }
            if (TileEntityCargo.itemSelections.get(this.target[id]).getValidSlot() == null && dif != 0)
            {
                receiveClickData(packetid, id, dif);
            }
        }
    }

    public int getAmount(final int id)
    {
        final int val = getAmountId(id);
        switch (val)
        {
            case 1, 7 -> {
                return 1;
            }
            case 2, 9 -> {
                return 3;
            }
            case 3 -> {
                return 8;
            }
            case 4 -> {
                return 16;
            }
            case 5 -> {
                return 32;
            }
            case 6 -> {
                return 64;
            }
            case 8 -> {
                return 2;
            }
            case 10 -> {
                return 5;
            }
            default -> {
                return 0;
            }
        }
    }

    public int getAmountType(final int id)
    {
        final int val = getAmountId(id);
        if (val == 0)
        {
            return 0;
        }
        if (val <= 6)
        {
            return 1;
        }
        return 2;
    }

    @Override
    public int getAmountCount()
    {
        return 11;
    }

    @Override
    public void load(CompoundTag nbttagcompound)
    {
        super.load(nbttagcompound);
        setWorkload(nbttagcompound.getByte("workload"));
        for (int i = 0; i < 4; ++i)
        {
            target[i] = nbttagcompound.getByte("target" + i);
        }
    }

    @Override
    public void saveAdditional(CompoundTag nbttagcompound)
    {
        super.saveAdditional(nbttagcompound);
        nbttagcompound.putByte("workload", (byte) getWorkload());
        for (int i = 0; i < 4; ++i)
        {
            nbttagcompound.putByte("target" + i, (byte) target[i]);
        }
    }

    public void receivePacket(final int id, final byte[] data, final Player player)
    {
        if (id == 0)
        {
            final int railID = data[0];
            toCart[railID] = !toCart[railID];
            if (color[railID] - 1 == getSide())
            {
                reset();
            }
        }
        else if (id == 4)
        {
            final int railID = data[0];
            if (color[railID] != 5)
            {
                doReturn[color[railID] - 1] = !doReturn[color[railID] - 1];
            }
        }
        else if (id == 5)
        {
            final int difference = data[0];
            layoutType += difference;
            if (layoutType > 2)
            {
                layoutType = 0;
            }
            else if (layoutType < 0)
            {
                layoutType = 2;
            }
            reset();
        }
        else
        {
            final byte railsAndDifferenceCombined = data[0];
            final int railID2 = railsAndDifferenceCombined & 0x3;
            final int k = (railsAndDifferenceCombined & 0x4) >> 2;
            int difference2;
            if (k == 0)
            {
                difference2 = 1;
            }
            else
            {
                difference2 = -1;
            }
            if (id == 2)
            {
                final int[] amount = this.amount;
                final int n = railID2;
                amount[n] += difference2;
                if (this.amount[railID2] >= getAmountCount())
                {
                    this.amount[railID2] = 0;
                }
                else if (this.amount[railID2] < 0)
                {
                    this.amount[railID2] = getAmountCount() - 1;
                }
                if (color[railID2] - 1 == getSide())
                {
                    reset();
                }
            }
            else if (id == 3)
            {
                if (color[railID2] != 5)
                {
                    boolean willStillExist = false;
                    for (int side = 0; side < 4; ++side)
                    {
                        if (side != railID2 && color[railID2] == color[side])
                        {
                            willStillExist = true;
                            break;
                        }
                    }
                    if (!willStillExist)
                    {
                        doReturn[color[railID2] - 1] = false;
                    }
                }
                final int[] color = this.color;
                final int n2 = railID2;
                color[n2] += difference2;
                if (this.color[railID2] > 5)
                {
                    this.color[railID2] = 1;
                }
                else if (this.color[railID2] < 1)
                {
                    this.color[railID2] = 5;
                }
                if (this.color[railID2] - 1 == getSide())
                {
                    reset();
                }
            }
            else
            {
                receiveClickData(id, railID2, difference2);
            }
        }
    }

    @Override
    protected boolean doTransfer(final TransferManager transfer)
    {
        final Class<?> slotCart = TileEntityCargo.itemSelections.get(target[transfer.getSetting()]).getValidSlot();
        if (slotCart == null)
        {
            transfer.setLowestSetting(transfer.getSetting() + 1);
            return true;
        }
        final Class<?> slotCargo = SlotCargo.class;
        Container fromInv;
        AbstractContainerMenu fromCont;
        Class<?> fromValid;
        Container toInv;
        AbstractContainerMenu toCont;
        Class<?> toValid;
        if (toCart[transfer.getSetting()])
        {
            fromInv = this;
            fromCont = new ContainerCargo(0, null, this, new SimpleContainerData(0));
            fromValid = slotCargo;
            toInv = transfer.getCart();
            toCont = transfer.getCart().getCon(null);
            toValid = slotCart;
        }
        else
        {
            fromInv = transfer.getCart();
            fromCont = transfer.getCart().getCon(null);
            fromValid = slotCart;
            toInv = this;
            toCont = new ContainerCargo(0, null, this, new SimpleContainerData(0));
            toValid = slotCargo;
        }
        latestTransferToBeUsed = transfer;
        for (int i = 0; i < fromInv.getContainerSize(); ++i)
        {
            if (TransferHandler.isSlotOfType(fromCont.getSlot(i), fromValid) && !fromInv.getItem(i).isEmpty())
            {
                @Nonnull ItemStack iStack = fromInv.getItem(i);
                final int stacksize = iStack.getCount();
                int maxNumber;
                if (getAmountType(transfer.getSetting()) == 1)
                {
                    maxNumber = getAmount(transfer.getSetting()) - transfer.getWorkload();
                }
                else
                {
                    maxNumber = -1;
                }
                TransferHandler.TransferItem(iStack, toInv, toCont, toValid, maxNumber, TransferHandler.TRANSFER_TYPE.MANAGER);
                if (iStack.getCount() != stacksize)
                {
                    if (getAmountType(transfer.getSetting()) == 1)
                    {
                        transfer.setWorkload(transfer.getWorkload() + stacksize - iStack.getCount());
                    }
                    else if (getAmountType(transfer.getSetting()) == 2)
                    {
                        transfer.setWorkload(transfer.getWorkload() + 1);
                    }
                    setChanged();
                    transfer.getCart().setChanged();
                    if (iStack.getCount() == 0)
                    {
                        fromInv.setItem(i, ItemStack.EMPTY);
                    }
                    if (transfer.getWorkload() >= getAmount(transfer.getSetting()) && getAmountType(transfer.getSetting()) != 0)
                    {
                        transfer.setLowestSetting(transfer.getSetting() + 1);
                    }
                    return true;
                }
            }
        }
        return false;
    }

    public TransferManager getCurrentTransferForSlots()
    {
        return latestTransferToBeUsed;
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability)
    {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
        {
            if (createHandler() != null)
            {
                return LazyOptional.of(this::createHandler).cast();
            }
        }
        return super.getCapability(capability);
    }

    private IItemHandlerModifiable createHandler()
    {
        BlockState state = this.getBlockState();
        if (!(state.getBlock() instanceof BlockCargoManager))
        {
            return new InvWrapper(this);
        }
        return null;
    }

    @Override
    public int getContainerSize()
    {
        return 60;
    }

    @Override
    public boolean stillValid(Player playerEntity)
    {
        return true;
    }

    @Override
    public Component getDisplayName()
    {
        return Component.translatable("screen.cargo.manager");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory playerInventory, Player playerEntity)
    {
        return new ContainerCargo(id, playerInventory, this, this.dataAccess);
    }
}
