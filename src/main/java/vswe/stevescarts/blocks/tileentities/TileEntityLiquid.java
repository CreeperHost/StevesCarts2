package vswe.stevescarts.blocks.tileentities;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;
import vswe.stevescarts.client.guis.GuiBase;
import vswe.stevescarts.containers.ContainerLiquid;
import vswe.stevescarts.entities.EntityMinecartModular;
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

public class TileEntityLiquid extends TileEntityManager implements ITankHolder, MenuProvider
{
    public SCTank[] tanks;
    private int tick;
    @SuppressWarnings("unused")
    private static final int[] topSlots;
    @SuppressWarnings("unused")
    private static final int[] botSlots;
    @SuppressWarnings("unused")
    private static final int[] sideSlots;
    protected final SimpleContainerData dataAccess = new SimpleContainerData(12)
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
                case 9 -> doReturn[0] ? 1 : 0;
                case 10 -> doReturn[1] ? 1 : 0;
                case 11 -> doReturn[2] ? 1 : 0;
                case 12 -> doReturn[3] ? 1 : 0;
                default -> throw new IllegalArgumentException("Invalid index: " + id);
            };
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


    public TileEntityLiquid(BlockPos blockPos, BlockState blockState)
    {
        super(ModBlocks.LIQUID_MANAGER_TILE.get(), blockPos, blockState);
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
        if(level == null) return;
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
        if(level == null) return;
        if (!level.isClientSide)
        {
            if (getTanks() != null)
            {
                for (int i = 0; i < getTanks().length; i++)
                {
                    PacketHandler.send(PacketDistributor.TRACKING_CHUNK.with(() -> level.getChunkAt(getBlockPos())), new PacketFluidSync(this.getTanks()[i].getFluid(), this.getBlockPos(), i));
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
        TransferHandler.TransferItem(item, this, tankid * 3 + 1, tankid * 3 + 1, new ContainerLiquid(0, null, this, new SimpleContainerData(13)), Slot.class, null, -1);
    }

    @Override
    public void onFluidUpdated(final int tankid)
    {
        setChanged();
        syncTanks();
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void drawImage(GuiGraphics guiGraphics, int tankid, AbstractContainerScreen<?> gui, TextureAtlasSprite sprite, int targetX, int targetY, int width, int height)
    {
        guiGraphics.blit(targetX, targetY, 0, width, height, sprite);
    }

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
        if (facing == null) return false;

        return switch (layoutType)
        {
            case 0 -> true;
            case 1 -> tankId == facingToTankId(facing);
            case 2 -> color[tankId] == facingToColorId(facing);
            default -> false;
        };
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
        return switch (getAmountId(id))
        {
            case 1 -> 0.25f;
            case 2 -> 0.5f;
            case 3 -> 0.75f;
            case 4 -> 1.0f;
            case 5 -> 2.0f;
            case 6 -> 3.0f;
            case 7 -> 5.0f;
            case 8 -> 7.5f;
            case 9 -> 10.0f;
            case 10 -> 15.0f;
            default -> 0.0f;
        };
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
    public void load(@NotNull CompoundTag compoundTag)
    {
        super.load(compoundTag);
        for (int i = 0; i < 4; ++i)
        {
            tanks[i].setFluid(FluidStack.loadFluidStackFromNBT(compoundTag.getCompound("Fluid" + i)));
        }
        setWorkload(compoundTag.getShort("workload"));
    }


    @Override
    public void saveAdditional(@NotNull CompoundTag compoundTag)
    {
        super.saveAdditional(compoundTag);
        for (int i = 0; i < 4; ++i)
        {
            if (!tanks[i].getFluid().isEmpty())
            {
                final CompoundTag compound = new CompoundTag();
                tanks[i].getFluid().writeToNBT(compound);
                compoundTag.put("Fluid" + i, compound);
            }
        }
        compoundTag.putShort("workload", (short) getWorkload());
    }

    @SuppressWarnings("unused")
    private boolean isInput(final int id)
    {
        return id % 3 == 0;
    }

    @SuppressWarnings("unused")
    private boolean isOutput(final int id)
    {
        return id % 3 == 1;
    }

    @SuppressWarnings("unused")
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
        return switch (facing.ordinal())
        {
            case 2 -> 3; // north, yellow
            case 3 -> 2; // south, blue
            case 4 -> 4; // west, green
            case 5 -> 1; // east, red
            default -> 1;
        };
    }

    private int facingToTankId(Direction facing)
    {
        return switch (facing.ordinal())
        {
            case 2 -> 2; // north, yellow
            case 3 -> 1; // south, blue
            case 4 -> 3; // west, green
            case 5 -> 0; // east, red
            default -> 0;
        };
    }

    static
    {
        topSlots = new int[]{0, 3, 6, 9};
        botSlots = new int[]{1, 4, 7, 10};
        sideSlots = new int[0];
    }

    @Override
    public @NotNull Component getDisplayName()
    {
        return Component.translatable("container.liquidmanager");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, @NotNull Inventory playerInventory, @NotNull Player player)
    {
        if (level != null && !level.isClientSide)
        {
            syncTanks();
        }
        return new ContainerLiquid(id, playerInventory, this, dataAccess);
    }

    @Override
    public boolean stillValid(@NotNull Player playerEntity)
    {
        return true;
    }
}
