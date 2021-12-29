package vswe.stevescarts.blocks.tileentities;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import vswe.stevescarts.entitys.EntityMinecartModular;
import vswe.stevescarts.helpers.NBTHelper;
import vswe.stevescarts.helpers.storages.TransferManager;
import vswe.stevescarts.network.PacketHandler;
import vswe.stevescarts.network.packets.PacketCargpManager;

import javax.annotation.Nonnull;

public abstract class TileEntityManager extends TileEntityBase implements IInventory
{
    private TransferManager standardTransferHandler;
    private NonNullList<ItemStack> cargoItemStacks;
    public int layoutType;
    public int moveTime;
    public boolean[] toCart;
    public boolean[] doReturn;
    public int[] amount;
    public int[] color;

    public TileEntityManager(TileEntityType<?> p_i48289_1_)
    {
        super(p_i48289_1_);
        toCart = new boolean[]{true, true, true, true};
        doReturn = new boolean[]{false, false, false, false};
        amount = new int[]{0, 0, 0, 0};
        color = new int[]{1, 2, 3, 4};
        cargoItemStacks = NonNullList.withSize(getContainerSize(), ItemStack.EMPTY);
        moveTime = 0;
        standardTransferHandler = new TransferManager();
    }

    @Override
    public ItemStack getItem(int i)
    {
        return cargoItemStacks.get(i);
    }

    @Override
    public void setItem(int i, ItemStack itemstack)
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
    public void load(BlockState blockState, CompoundNBT nbttagcompound)
    {
        super.load(blockState, nbttagcompound);
        final ListNBT nbttaglist = nbttagcompound.getList("Items", NBTHelper.COMPOUND.getId());
        cargoItemStacks = NonNullList.withSize(getContainerSize(), ItemStack.EMPTY);
        for (int i = 0; i < nbttaglist.size(); ++i)
        {
            final CompoundNBT nbttagcompound2 = nbttaglist.getCompound(i);
            final byte byte0 = nbttagcompound2.getByte("Slot");
            if (byte0 >= 0 && byte0 < cargoItemStacks.size())
            {
                cargoItemStacks.set(byte0, ItemStack.of(nbttagcompound2));
            }
        }
        moveTime = nbttagcompound.getByte("movetime");
        setLowestSetting(nbttagcompound.getByte("lowestNumber"));
        layoutType = nbttagcompound.getByte("layout");
        final byte temp = nbttagcompound.getByte("tocart");
        final byte temp2 = nbttagcompound.getByte("doReturn");
        for (int j = 0; j < 4; ++j)
        {
            amount[j] = nbttagcompound.getByte("amount" + j);
            color[j] = nbttagcompound.getByte("color" + j);
            if (color[j] == 0)
            {
                color[j] = j + 1;
            }
            toCart[j] = ((temp & 1 << j) != 0x0);
            doReturn[j] = ((temp2 & 1 << j) != 0x0);
        }
    }

    @Override
    public CompoundNBT save(CompoundNBT nbttagcompound)
    {
        super.save(nbttagcompound);
        nbttagcompound.putByte("movetime", (byte) moveTime);
        nbttagcompound.putByte("lowestNumber", (byte) getLowestSetting());
        nbttagcompound.putByte("layout", (byte) layoutType);
        byte temp = 0;
        byte temp2 = 0;
        for (int i = 0; i < 4; ++i)
        {
            nbttagcompound.putByte("amount" + i, (byte) amount[i]);
            nbttagcompound.putByte("color" + i, (byte) color[i]);
            if (toCart[i])
            {
                temp |= (byte) (1 << i);
            }
            if (doReturn[i])
            {
                temp2 |= (byte) (1 << i);
            }
        }
        nbttagcompound.putByte("tocart", temp);
        nbttagcompound.putByte("doReturn", temp2);
        final ListNBT nbttaglist = new ListNBT();
        for (int j = 0; j < cargoItemStacks.size(); ++j)
        {
            if (!cargoItemStacks.get(j).isEmpty())
            {
                final CompoundNBT nbttagcompound2 = new CompoundNBT();
                nbttagcompound2.putByte("Slot", (byte) j);
                cargoItemStacks.get(j).save(nbttagcompound2);
                nbttaglist.add(nbttagcompound2);
            }
        }
        nbttagcompound.put("Items", nbttaglist);
        return nbttagcompound;
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
        if (level.isClientSide)
        {
            updateLayout();
            return;
        }
        if (getCart() == null || getCart().removed || getSide() < 0 || getSide() > 3 || !getCart().isDisabled())
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
                getCart().releaseCart();
                if (doReturn[getSide()])
                {
                    getCart().turnback();
                }
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

        //TODO Packets
        //		PacketStevesCarts.sendPacket(id, data);
    }

    public void receivePacket(final int id, final byte[] data, final PlayerEntity player)
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
    //
    //	@Override
    //	public void initGuiData(final Container con, final IContainerListener crafting) {
    //		checkGuiData((ContainerManager) con, crafting, true);
    //	}
    //
    //	@Override
    //	public void checkGuiData(final Container con, final IContainerListener crafting) {
    //		checkGuiData((ContainerManager) con, crafting, false);
    //	}

    //TODO
    //	public void checkGuiData(final ContainerManager con, final IContainerListener crafting, final boolean isNew) {
    //		short header = (short) (moveTime & 0x1F);
    //		header |= (short) ((layoutType & 0x3) << 5);
    //		for (int i = 0; i < 4; ++i) {
    //			header |= (short) ((toCart[i] ? 1 : 0) << 7 + i);
    //		}
    //		for (int i = 0; i < 4; ++i) {
    //			header |= (short) ((doReturn[i] ? 1 : 0) << 11 + i);
    //		}
    //		if (isNew || con.lastHeader != header) {
    //			updateGuiData(con, crafting, 0, header);
    //			con.lastHeader = header;
    //		}
    //		short colorShort = 0;
    //		for (int j = 0; j < 4; ++j) {
    //			colorShort |= (short) ((color[j] & 0x7) << j * 3);
    //		}
    //		colorShort |= (short) ((getLastSetting() & 0x7) << 12);
    //		if (isNew || con.lastColor != colorShort) {
    //			updateGuiData(con, crafting, 1, colorShort);
    //			con.lastColor = colorShort;
    //		}
    //		short amountShort = 0;
    //		for (int k = 0; k < 4; ++k) {
    //			amountShort |= (short) ((amount[k] & 0xF) << k * 4);
    //		}
    //		if (isNew || con.lastAmount != amountShort) {
    //			updateGuiData(con, crafting, 3, amountShort);
    //			con.lastAmount = amountShort;
    //		}
    //	}

    //	@Override
    //	public void receiveGuiData(final int id, final short data) {
    //		if (id == 0) {
    //			moveTime = (data & 0x1F);
    //			layoutType = (data & 0x60) >> 5;
    //			updateLayout();
    //			for (int i = 0; i < 4; ++i) {
    //				toCart[i] = ((data & 1 << 7 + i) != 0x0);
    //			}
    //			for (int i = 0; i < 4; ++i) {
    //				doReturn[i] = ((data & 1 << 11 + i) != 0x0);
    //			}
    //		} else if (id == 1) {
    //			for (int i = 0; i < 4; ++i) {
    //				color[i] = (data & 7 << i * 3) >> i * 3;
    //			}
    //			setLastSetting((data & 0x7000) >> 12);
    //		} else if (id == 3) {
    //			for (int i = 0; i < 4; ++i) {
    //				amount[i] = (data & 15 << i * 4) >> i * 4;
    //			}
    //		}
    //	}

    public int moveProgressScaled(final int i)
    {
        return moveTime * i / 24;
    }

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
    public ItemStack removeItem(int p_70298_1_, int p_70298_2_)
    {
        return ItemStackHelper.removeItem(cargoItemStacks, p_70298_1_, p_70298_2_);
    }

    @Override
    public ItemStack removeItemNoUpdate(int id)
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
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
        {
            //			return LazyOptional.of(new InvWrapper(this));
        }
        return super.getCapability(cap);
    }
}
