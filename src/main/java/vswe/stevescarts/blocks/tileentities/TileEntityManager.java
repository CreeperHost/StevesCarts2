package vswe.stevescarts.blocks.tileentities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.wrapper.InvWrapper;
import org.jetbrains.annotations.NotNull;
import vswe.stevescarts.entitys.EntityMinecartModular;
import vswe.stevescarts.polylib.NBTHelper;
import vswe.stevescarts.helpers.storages.TransferManager;
import vswe.stevescarts.network.PacketHandler;
import vswe.stevescarts.network.packets.PacketCargpManager;

import javax.annotation.Nonnull;

public abstract class TileEntityManager extends TileEntityBase implements Container
{
    private final TransferManager standardTransferHandler;
    private NonNullList<ItemStack> cargoItemStacks;
    public int layoutType;
    public int moveTime;
    public boolean[] toCart;
    public boolean[] doReturn;
    public int[] amount;
    public int[] color;

    public TileEntityManager(BlockEntityType<?> p_i48289_1_, BlockPos blockPos, BlockState blockState)
    {
        super(p_i48289_1_, blockPos, blockState);
        toCart = new boolean[]{true, true, true, true};
        doReturn = new boolean[]{false, false, false, false};
        amount = new int[]{0, 0, 0, 0};
        color = new int[]{1, 2, 3, 4};
        cargoItemStacks = NonNullList.withSize(getContainerSize(), ItemStack.EMPTY);
        moveTime = 0;
        standardTransferHandler = new TransferManager();
    }

    @Override
    public @NotNull ItemStack getItem(int i)
    {
        return cargoItemStacks.get(i);
    }

    @Override
    public void setItem(int i, @NotNull ItemStack itemstack)
    {
        cargoItemStacks.set(i, itemstack);
        if (!itemstack.isEmpty() && itemstack.getCount() > getMaxStackSize())
        {
            itemstack.setCount(getMaxStackSize());
        }
        setChanged();
    }

    @Override
    public void clearContent()
    {
        cargoItemStacks.clear();
    }

    @Override
    public void load(@NotNull CompoundTag compoundTag)
    {
        super.load(compoundTag);
        final ListTag nbttaglist = compoundTag.getList("Items", NBTHelper.COMPOUND.getId());
        cargoItemStacks = NonNullList.withSize(getContainerSize(), ItemStack.EMPTY);
        for (int i = 0; i < nbttaglist.size(); ++i)
        {
            final CompoundTag nbttagcompound2 = nbttaglist.getCompound(i);
            final byte byte0 = nbttagcompound2.getByte("Slot");
            if (byte0 >= 0 && byte0 < cargoItemStacks.size())
            {
                cargoItemStacks.set(byte0, ItemStack.of(nbttagcompound2));
            }
        }
        moveTime = compoundTag.getByte("movetime");
        setLowestSetting(compoundTag.getByte("lowestNumber"));
        layoutType = compoundTag.getByte("layout");
        final byte temp = compoundTag.getByte("tocart");
        final byte temp2 = compoundTag.getByte("doReturn");
        for (int j = 0; j < 4; ++j)
        {
            amount[j] = compoundTag.getByte("amount" + j);
            color[j] = compoundTag.getByte("color" + j);
            if (color[j] == 0)
            {
                color[j] = j + 1;
            }
            toCart[j] = ((temp & 1 << j) != 0x0);
            doReturn[j] = ((temp2 & 1 << j) != 0x0);
        }
    }

    @Override
    public void saveAdditional(@NotNull CompoundTag compoundTag)
    {
        super.saveAdditional(compoundTag);
        compoundTag.putByte("movetime", (byte) moveTime);
        compoundTag.putByte("lowestNumber", (byte) getLowestSetting());
        compoundTag.putByte("layout", (byte) layoutType);
        byte temp = 0;
        byte temp2 = 0;
        for (int i = 0; i < 4; ++i)
        {
            compoundTag.putByte("amount" + i, (byte) amount[i]);
            compoundTag.putByte("color" + i, (byte) color[i]);
            if (toCart[i])
            {
                temp |= (byte) (1 << i);
            }
            if (doReturn[i])
            {
                temp2 |= (byte) (1 << i);
            }
        }
        compoundTag.putByte("tocart", temp);
        compoundTag.putByte("doReturn", temp2);
        final ListTag nbttaglist = new ListTag();
        for (int j = 0; j < cargoItemStacks.size(); ++j)
        {
            if (!cargoItemStacks.get(j).isEmpty())
            {
                final CompoundTag nbttagcompound2 = new CompoundTag();
                nbttagcompound2.putByte("Slot", (byte) j);
                cargoItemStacks.get(j).save(nbttagcompound2);
                nbttaglist.add(nbttagcompound2);
            }
        }
        compoundTag.put("Items", nbttaglist);
    }

    public EntityMinecartModular getCart()
    {
        return standardTransferHandler.getCart();
    }

    public void setCart(final EntityMinecartModular cart)
    {
        standardTransferHandler.setCart(cart);
    }

    public int getSetting()
    {
        return standardTransferHandler.getSetting();
    }

    @SuppressWarnings("unused")
    public void setSetting(final int val)
    {
        standardTransferHandler.setSetting(val);
    }

    public int getSide()
    {
        return standardTransferHandler.getSide();
    }

    public void setSide(final int val)
    {
        standardTransferHandler.setSide(val);
    }

    public int getLastSetting()
    {
        return standardTransferHandler.getLastSetting();
    }

    @SuppressWarnings("unused")
    public void setLastSetting(final int val)
    {
        standardTransferHandler.setLastSetting(val);
    }

    public int getLowestSetting()
    {
        return standardTransferHandler.getLowestSetting();
    }

    public void setLowestSetting(final int val)
    {
        standardTransferHandler.setLowestSetting(val);
    }

    public int getWorkload()
    {
        return standardTransferHandler.getWorkload();
    }

    public void setWorkload(final int val)
    {
        standardTransferHandler.setWorkload(val);
    }

    @Override
    public void tick()
    {
        if(level == null) return;

        if (level.isClientSide)
        {
            updateLayout();
            return;
        }
        if (getCart() == null || getCart().isRemoved() || getSide() < 0 || getSide() > 3 || !getCart().isDisabled())
        {
            standardTransferHandler.reset();
            return;
        }
        ++moveTime;
        if (moveTime >= 24 || (getSetting() == -1) && color[0] - 1 != getSide())
        {
            moveTime = 0;
            if (!exchangeItems(standardTransferHandler))
            {
                getCart().releaseCart(doReturn[getSide()]);

                standardTransferHandler.reset();
            }
        }
    }

    public boolean exchangeItems(final TransferManager transfer)
    {
        for (transfer.setSetting(transfer.getLowestSetting()); transfer.getSetting() < 4; transfer.setSetting(transfer.getSetting() + 1))
        {
            if (color[transfer.getSetting()] - 1 != transfer.getSide())
            {
                continue;
            }

            transfer.setLowestSetting(transfer.getSetting());
            if (transfer.getLastSetting() != transfer.getSetting())
            {
                transfer.setWorkload(0);
                transfer.setLastSetting(transfer.getSetting());
                return true;
            }

            if (!(toCart[transfer.getSetting()] ? transfer.getToCartEnabled() : transfer.getFromCartEnabled()) || !isTargetValid(transfer))
            {
                transfer.setLowestSetting(transfer.getSetting() + 1);
                return true;
            }

            if (doTransfer(transfer))
            {
                return true;
            }
        }

        return false;
    }

    public void sendPacket(final int id)
    {
        sendPacket(id, new byte[0]);
    }

    public void sendPacket(final int id, final byte data)
    {
        sendPacket(id, new byte[]{data});
    }

    public void sendPacket(final int id, final byte[] data)
    {
        PacketHandler.sendToServer(new PacketCargpManager(this.getBlockPos(), id, data));
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

    public int moveProgressScaled(final int i)
    {
        return moveTime * i / 24;
    }

    @SuppressWarnings("unused")
    @Nonnull
    public ItemStack getStackInSlotOnClosing(final int par1)
    {
        if (!cargoItemStacks.get(par1).isEmpty())
        {
            @Nonnull ItemStack var2 = cargoItemStacks.get(par1);
            cargoItemStacks.set(par1, ItemStack.EMPTY);
            return var2;
        }
        return ItemStack.EMPTY;
    }

    protected void updateLayout()
    {
    }

    protected void receiveClickData(final int packetid, final int id, final int dif)
    {
    }

    protected abstract boolean isTargetValid(final TransferManager p0);

    protected abstract boolean doTransfer(final TransferManager p0);

    public abstract int getAmountCount();

    protected void reset()
    {
        setWorkload(moveTime = 0);
    }

    protected int getAmountId(final int id)
    {
        return amount[id];
    }

    @Override
    public int getContainerSize()
    {
        return cargoItemStacks.size();
    }

    @Override
    public @NotNull ItemStack removeItem(int id, int amount)
    {
        return ContainerHelper.removeItem(cargoItemStacks, id, amount);
    }

    @Override
    public @NotNull ItemStack removeItemNoUpdate(int id)
    {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean isEmpty()
    {
        for (ItemStack itemstack : cargoItemStacks)
        {
            if (!itemstack.isEmpty())
            {
                return false;
            }
        }
        return true;
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap)
    {
        //TODO
//        if (cap == ForgeCapabilities.ITEM_HANDLER)
//        {
//            return LazyOptional.of(new InvWrapper(this));
//        }
        return super.getCapability(cap);
    }
}
