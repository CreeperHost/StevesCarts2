package vswe.stevescarts.blocks.tileentities;

import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IntArray;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fml.network.PacketDistributor;
import vswe.stevescarts.client.guis.GuiBase;
import vswe.stevescarts.containers.ContainerLiquid;
import vswe.stevescarts.entitys.EntityMinecartModular;
import vswe.stevescarts.helpers.storages.ITankHolder;
import vswe.stevescarts.helpers.storages.SCTank;
import vswe.stevescarts.helpers.storages.TransferHandler;
import vswe.stevescarts.helpers.storages.TransferManager;
import vswe.stevescarts.init.ModBlocks;
import vswe.stevescarts.modules.storages.tanks.ModuleTank;
import vswe.stevescarts.network.PacketHandler;
import vswe.stevescarts.network.packets.PacketFluidSync;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;

public class TileEntityLiquid extends TileEntityManager implements INamedContainerProvider, ITankHolder
{
    public SCTank[] tanks;
    private int tick;
    private static final int[] topSlots;
    private static final int[] botSlots;
    private static final int[] sideSlots;
    protected final IIntArray dataAccess = new IIntArray()
    {
        public int get(int id)
        {
            switch (id)
            {
                case 0:
                    return layoutType;
                case 1:
                    return color[0];
                case 2:
                    return color[1];
                case 3:
                    return color[2];
                case 4:
                    return color[3];
                case 5:
                    return toCart[0] ? 1 : 0;
                case 6:
                    return toCart[1] ? 1 : 0;
                case 7:
                    return toCart[2] ? 1 : 0;
                case 8:
                    return toCart[3] ? 1 : 0;
                case 9:
                    return doReturn[0] ? 1 : 0;
                case 10:
                    return doReturn[1] ? 1 : 0;
                case 11:
                    return doReturn[2] ? 1 : 0;
                case 12:
                    return doReturn[3] ? 1 : 0;
                default:
                    throw new IllegalArgumentException("Invalid index: " + id);
            }
        }

        public void set(int p_221477_1_, int p_221477_2_)
        {
            throw new IllegalStateException("Cannot set values through IIntArray");
        }

        public int getCount()
        {
            return 13;
        }
    };


    public TileEntityLiquid()
    {
        super(ModBlocks.LIQUID_MANAGER_TILE.get());
        tanks = new SCTank[4];
        for (int i = 0; i < 4; ++i)
        {
            tanks[i] = new SCTank(this, 32000, i);
        }
    }

    public SCTank[] getTanks()
    {
        return tanks;
    }

    @Override
    public void tick()
    {
        super.tick();
        if (tick-- <= 0)
        {
            tick = 5;
            if (!level.isClientSide)
            {
                for (int i = 0; i < 4; ++i)
                {
                    tanks[i].containerTransfer();
                }
            }
        }
    }

    public void syncTanks()
    {
        if (!level.isClientSide)
        {
            if (getTanks() != null)
            {
                for (int i = 0; i < getTanks().length; i++)
                {
                    PacketHandler.send(PacketDistributor.TRACKING_CHUNK.with(() -> level.getChunkAt(getBlockPos())), new PacketFluidSync(this.getTanks()[i].getFluid(), this.getBlockPos(), 0, i));
                }
            }
        }
    }

    @Override
    protected boolean isTargetValid(TransferManager p0)
    {
        return true;
    }

    @Override
    public int getContainerSize()
    {
        return 12;
    }

    @Override
    @Nonnull
    public ItemStack getInputContainer(final int tankid)
    {
        return getItem(tankid * 3);
    }

    @Override
    public void setInputContainer(final int tankid, ItemStack stack)
    {
        setItem(tankid * 3, stack);
    }

    @Override
    public void addToOutputContainer(final int tankid, @Nonnull ItemStack item)
    {
        TransferHandler.TransferItem(item, this, tankid * 3 + 1, tankid * 3 + 1, new ContainerLiquid(0, null, this, new IntArray(13)), Slot.class, null, -1);
    }

    @Override
    public void onFluidUpdated(final int tankid)
    {
        setChanged();
        syncTanks();
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void drawImage(int tankid, GuiBase gui, TextureAtlasSprite sprite, int targetX, int targetY, int srcX, int srcY, int width, int height) {}

    @Override
    protected boolean doTransfer(final TransferManager transfer)
    {
        final int maximumToTransfer = hasMaxAmount(transfer.getSetting()) ? Math.min(getMaxAmount(transfer.getSetting()) - transfer.getWorkload(), 1000) : 1000;
        boolean sucess = false;

        if (toCart[transfer.getSetting()])
        {
            boolean allFull = true;
            for (int i = 0; i < tanks.length; i++)
            {
                final int fill = fillTank(transfer.getCart(), i, transfer.getSetting(), maximumToTransfer, IFluidHandler.FluidAction.SIMULATE);
                if (fill > 0)
                {
                    fillTank(transfer.getCart(), i, transfer.getSetting(), fill, IFluidHandler.FluidAction.EXECUTE);
                    sucess = true;
                    if (fill >= maximumToTransfer) allFull = false;
                    if (hasMaxAmount(transfer.getSetting()))
                    {
                        transfer.setWorkload(transfer.getWorkload() + fill);
                    }
                    break;
                }
            }
            if (allFull)
            {
                return false;
            }
        }
        else
        {
            final ArrayList<ModuleTank> cartTanks = transfer.getCart().getModuleTanks();
            for (final IFluidTank cartTank : cartTanks)
            {
                final int drain = drainTank(cartTank, transfer.getSetting(), maximumToTransfer, IFluidHandler.FluidAction.SIMULATE);
                if (drain > 0)
                {
                    drainTank(cartTank, transfer.getSetting(), drain, IFluidHandler.FluidAction.EXECUTE);
                    sucess = true;
                    if (hasMaxAmount(transfer.getSetting()))
                    {
                        transfer.setWorkload(transfer.getWorkload() + drain);
                    }
                    break;
                }
            }
        }
        if (sucess && hasMaxAmount(transfer.getSetting()) && transfer.getWorkload() == getMaxAmount(transfer.getSetting()))
        {
            transfer.setLowestSetting(transfer.getSetting() + 1);
        }
        return sucess;
    }

    private int fillTank(final EntityMinecartModular cart, final int tankId, final int sideId, int fillAmount, final IFluidHandler.FluidAction doFill)
    {
        if (isTankValid(tankId, sideId))
        {
            final FluidStack fluidToFill = tanks[tankId].drain(fillAmount, doFill);
            if (fluidToFill.isEmpty())
            {
                return 0;
            }
            fillAmount = fluidToFill.getAmount();
            if (isFluidValid(sideId, fluidToFill))
            {
                final ArrayList<ModuleTank> cartTanks = cart.getModuleTanks();
                for (final IFluidTank cartTank : cartTanks)
                {
                    fluidToFill.shrink(cartTank.fill(fluidToFill, doFill));
                    if (fluidToFill.getAmount() <= 0)
                    {
                        return fillAmount;
                    }
                }
                syncTanks();
                return fillAmount - fluidToFill.getAmount();
            }
        }
        return 0;
    }

    private int drainTank(final IFluidTank cartTank, final int sideId, int drainAmount, final IFluidHandler.FluidAction doDrain)
    {
        final FluidStack drainedFluid = cartTank.drain(drainAmount, doDrain);
        if (drainedFluid.isEmpty())
        {
            return 0;
        }
        drainAmount = drainedFluid.getAmount();
        if (isFluidValid(sideId, drainedFluid))
        {
            for (int i = 0; i < tanks.length; ++i)
            {
                final SCTank tank = tanks[i];
                if (isTankValid(i, sideId))
                {
                    drainedFluid.shrink(tank.fill(drainedFluid, doDrain));
                    if (drainedFluid.getAmount() <= 0)
                    {
                        return drainAmount;
                    }
                }
            }
            return drainAmount - drainedFluid.getAmount();
        }
        return 0;
    }

    private boolean isTankValid(final int tankId, int sideId)
    {
        return (layoutType != 1 || tankId == sideId) && (layoutType != 2 || color[sideId] == color[tankId]);
    }

    private boolean isTankValid(final int tankId, Direction facing)
    {
        if (facing == null)
        {
            return false;
        }
        switch (layoutType)
        {
            case 0:
                return true;
            case 1:
                return tankId == facingToTankId(facing);
            case 2:
                return color[tankId] == facingToColorId(facing);
            default:
                return false;
        }
    }

    private boolean isFluidValid(final int sideId, final FluidStack fluid)
    {
        @Nonnull ItemStack filter = getItem(sideId * 3 + 2);
        final FluidStack filterFluid = FluidUtil.getFluidContained(filter).orElse(FluidStack.EMPTY);
        return filterFluid.isEmpty() || filterFluid.isFluidEqual(fluid);
    }

    public int getMaxAmount(final int id)
    {
        return (int) (getMaxAmountBuckets(id) * 1000);
    }

    public float getMaxAmountBuckets(final int id)
    {
        switch (getAmountId(id))
        {
            case 1:
                return 0.25f;
            case 2:
                return 0.5f;
            case 3:
                return 0.75f;
            case 4:
                return 1.0f;
            case 5:
                return 2.0f;
            case 6:
                return 3.0f;
            case 7:
                return 5.0f;
            case 8:
                return 7.5f;
            case 9:
                return 10.0f;
            case 10:
                return 15.0f;
            default:
                return 0.0f;
        }
    }

    public boolean hasMaxAmount(final int id)
    {
        return getAmountId(id) != 0;
    }

    @Override
    public int getAmountCount()
    {
        return 11;
    }

    @Override
    public void load(BlockState blockState, CompoundNBT nbttagcompound)
    {
        super.load(blockState, nbttagcompound);
        for (int i = 0; i < 4; ++i)
        {
            tanks[i].setFluid(FluidStack.loadFluidStackFromNBT(nbttagcompound.getCompound("Fluid" + i)));
        }
        setWorkload(nbttagcompound.getShort("workload"));
    }

    @Override
    public CompoundNBT save(CompoundNBT nbttagcompound)
    {
        super.save(nbttagcompound);
        for (int i = 0; i < 4; ++i)
        {
            if (!tanks[i].getFluid().isEmpty())
            {
                final CompoundNBT compound = new CompoundNBT();
                tanks[i].getFluid().writeToNBT(compound);
                nbttagcompound.put("Fluid" + i, compound);
            }
        }
        nbttagcompound.putShort("workload", (short) getWorkload());
        return nbttagcompound;
    }

    private boolean isInput(final int id)
    {
        return id % 3 == 0;
    }

    private boolean isOutput(final int id)
    {
        return id % 3 == 1;
    }

    //
    //	@Override
    //	public boolean isItemValidForSlot(final int slotId, @Nonnull ItemStack item) {
    //		if (isInput(slotId)) {
    //			return SlotLiquidManagerInput.isItemStackValid(item, this, -1);
    //		}
    //		if (isOutput(slotId)) {
    //			return SlotLiquidOutput.isItemStackValid(item);
    //		}
    //		return SlotLiquidFilter.isItemStackValid(item);
    //	}
    //
    //	public int[] getAccessibleSlotsFromSide(final int side) {
    //		if (side == 1) {
    //			return TileEntityLiquid.topSlots;
    //		}
    //		if (side == 0) {
    //			return TileEntityLiquid.botSlots;
    //		}
    //		return TileEntityLiquid.sideSlots;
    //	}
    //
    //	public boolean canInsertItem(final int slot, @Nonnull ItemStack item, final int side) {
    //		return side == 1 && isInput(slot) && isItemValidForSlot(slot, item);
    //	}
    //
    //	public boolean canExtractItem(final int slot, @Nonnull ItemStack item, final int side) {
    //		return side == 0 && isOutput(slot);
    //	}
    //
    //	@Override
    //	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
    //		if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
    //			return (T) getValidTank(facing);
    //		}
    //		return super.getCapability(capability, facing);
    //	}
    //
    public SCTank getValidTank(Direction facing)
    {
        for (int i = 0; i < getTanks().length; i++)
        {
            if (isTankValid(i, facing))
            {
                return getTanks()[i];
            }
        }
        return null;
    }

    private int facingToColorId(Direction facing)
    {
        switch (facing.ordinal())
        {
            case 2:
                return 3; // north, yellow
            case 3:
                return 2; // south, blue
            case 4:
                return 4; // west, green
            case 5:
                return 1; // east, red
            default:
                return 1;
        }
    }

    private int facingToTankId(Direction facing)
    {
        switch (facing.ordinal())
        {
            case 2:
                return 2; // north, yellow
            case 3:
                return 1; // south, blue
            case 4:
                return 3; // west, green
            case 5:
                return 0; // east, red
            default:
                return 0;
        }
    }

    static
    {
        topSlots = new int[]{0, 3, 6, 9};
        botSlots = new int[]{1, 4, 7, 10};
        sideSlots = new int[0];
    }

    @Override
    public ITextComponent getDisplayName()
    {
        return new StringTextComponent("container.liquidmanager");
    }

    @Nullable
    @Override
    public Container createMenu(int id, PlayerInventory playerInventory, PlayerEntity player)
    {
        if (!level.isClientSide)
        {
            syncTanks();
        }
        return new ContainerLiquid(id, playerInventory, this, dataAccess);
    }

    @Override
    public boolean stillValid(PlayerEntity playerEntity)
    {
        return true;
    }
}
