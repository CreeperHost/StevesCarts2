package vswe.stevescarts.blocks.tileentities;

import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.IntArray;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import vswe.stevescarts.blocks.BlockUpgrade;
import vswe.stevescarts.client.guis.GuiBase;
import vswe.stevescarts.containers.ContainerUpgrade;
import vswe.stevescarts.helpers.storages.ITankHolder;
import vswe.stevescarts.helpers.storages.SCTank;
import vswe.stevescarts.helpers.storages.TransferHandler;
import vswe.stevescarts.init.ModBlocks;
import vswe.stevescarts.upgrades.AssemblerUpgrade;
import vswe.stevescarts.upgrades.InventoryEffect;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TileEntityUpgrade extends TileEntityBase implements IInventory, ISidedInventory, ITankHolder, ITickableTileEntity, INamedContainerProvider
{
    public SCTank tank = new SCTank(this, 0, 0);
    private TileEntityCartAssembler master;
    private int type;
    private boolean initialized;
    private CompoundNBT comp;
    private NonNullList<ItemStack> inventoryStacks;
    private int[] slotsForSide;
    private boolean isCreativeBroken;

    public TileEntityUpgrade()
    {
        super(ModBlocks.UPGRADE_TILE.get());
    }

    public TileEntityUpgrade(AssemblerUpgrade assemblerUpgrade)
    {
        super(ModBlocks.UPGRADE_TILE.get());
        this.type = assemblerUpgrade.getId();
        setType(assemblerUpgrade.getId());
    }

    public void setMaster(final TileEntityCartAssembler master, Direction side)
    {
        this.master = master;
        if (level.getBlockState(getBlockPos()).getBlock() instanceof BlockUpgrade)
        {
            if (side != null)
            {
                level.getBlockState(getBlockPos()).setValue(BlockUpgrade.CONNECTED, master != null);
            }
            setChanged();
        }
    }

    public Direction getSide()
    {
        return level.getBlockState(getBlockPos()).getValue(BlockUpgrade.FACING);
    }

    public TileEntityCartAssembler getMaster()
    {
        return master;
    }

    public void setType(final int type)
    {
        setType(type, true);
    }

    public void setType(final int type, boolean setBlockState)
    {
        this.type = type;
        if (!initialized)
        {
            initialized = true;
            final AssemblerUpgrade upgrade = getUpgrade();
            if (upgrade != null)
            {
                comp = new CompoundNBT();
                slotsForSide = new int[upgrade.getInventorySize()];
                upgrade.init(this);
                if (upgrade.getInventorySize() > 0)
                {
                    inventoryStacks = NonNullList.withSize(upgrade.getInventorySize(), ItemStack.EMPTY);
                    for (int i = 0; i < slotsForSide.length; ++i)
                    {
                        slotsForSide[i] = i;
                    }
                }
            }
            else
            {
                inventoryStacks = null;
            }
        }
    }

    public CompoundNBT getCompound()
    {
        return comp;
    }

    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket()
    {
        return new SUpdateTileEntityPacket(getBlockPos(), 1, save(new CompoundNBT()));
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt)
    {
        load(level.getBlockState(pkt.getPos()), pkt.getTag());
    }

    @Override
    public CompoundNBT getUpdateTag()
    {
        return save(new CompoundNBT());
    }

    public AssemblerUpgrade getUpgrade()
    {
        return AssemblerUpgrade.getUpgrade(type);
    }

    public boolean hasInventory()
    {
        return inventoryStacks != null;
    }

    @Override
    public void load(BlockState state, CompoundNBT compoundNBT)
    {
        super.load(state, compoundNBT);
        setType(compoundNBT.getByte("Type"), false);
        ItemStackHelper.loadAllItems(compoundNBT, inventoryStacks);
        setChanged();
        final AssemblerUpgrade upgrade = getUpgrade();
        if (upgrade != null)
        {
            upgrade.load(this, compoundNBT);
        }
    }

    @Override
    public CompoundNBT save(CompoundNBT compoundNBT)
    {
        super.save(compoundNBT);
        if (inventoryStacks != null)
        {
            ItemStackHelper.saveAllItems(compoundNBT, inventoryStacks);
        }
        compoundNBT.putByte("Type", (byte) type);
        final AssemblerUpgrade upgrade = getUpgrade();
        if (upgrade != null)
        {
            upgrade.save(this, compoundNBT);
        }
        return compoundNBT;
    }

    @Override
    public void tick()
    {
        if (getUpgrade() != null && getMaster() != null)
        {
            getUpgrade().update(this);
        }
    }

    @Override
    public int getContainerSize()
    {
        if (inventoryStacks != null)
        {
            return inventoryStacks.size();
        }
        if (master == null)
        {
            return 0;
        }
        return master.getContainerSize();
    }

    @Override
    public boolean isEmpty()
    {
        for (ItemStack stack : inventoryStacks)
        {
            if (!stack.isEmpty())
            {
                return false;
            }
        }
        return true;
    }

    @Override
    public ItemStack getItem(int i)
    {
        if (inventoryStacks == null)
        {
            if (master == null)
            {
                return ItemStack.EMPTY;
            }
            return master.getItem(i);
        }
        else
        {
            if (i < 0 || i >= getContainerSize())
            {
                return ItemStack.EMPTY;
            }
            return inventoryStacks.get(i);
        }
    }

    @Override
    public ItemStack removeItem(int id, int amount)
    {
        ItemStack itemStack = ItemStackHelper.removeItem(this.inventoryStacks, id, amount);
        if (!itemStack.isEmpty()) this.setChanged();
        return itemStack;
    }

    @Override
    public ItemStack removeItemNoUpdate(int id)
    {
        ItemStack itemStack = (ItemStack) this.inventoryStacks.get(id);
        if (itemStack.isEmpty())
        {
            return ItemStack.EMPTY;
        }
        else
        {
            this.inventoryStacks.set(id, ItemStack.EMPTY);
            return itemStack;
        }
    }

    @Override
    public void setItem(int i, ItemStack itemstack)
    {
        if (inventoryStacks == null)
        {
            if (master != null)
            {
                master.setItem(i, itemstack);
            }
        }
        else
        {
            if (i < 0 || i >= getContainerSize())
            {
                return;
            }
            inventoryStacks.set(i, itemstack);
            if (!itemstack.isEmpty() && itemstack.getCount() > getMaxStackSize())
            {
                itemstack.setCount(getMaxStackSize());
            }
            setChanged();
        }
    }

    @Override
    public void setChanged()
    {
        super.setChanged();
        if (getUpgrade() != null)
        {
            final InventoryEffect inv = getUpgrade().getInventoryEffect();
            if (inv != null)
            {
                inv.onInventoryChanged(this);
            }
        }
    }

    @Override
    public boolean stillValid(PlayerEntity player)
    {
        return true;
    }

    //
    //	@Override
    //	@Nonnull
    //	public ItemStack decrStackSize(final int i, final int j) {
    //		if (inventoryStacks == null) {
    //			if (master == null) {
    //				return ItemStack.EMPTY;
    //			}
    //			return master.decrStackSize(i, j);
    //		} else {
    //			if (i < 0 || i >= getSizeInventory()) {
    //				return ItemStack.EMPTY;
    //			}
    //			if (inventoryStacks.get(i).isEmpty()) {
    //				return ItemStack.EMPTY;
    //			}
    //			if (inventoryStacks.get(i).getCount() <= j) {
    //				@Nonnull
    //				ItemStack itemstack = inventoryStacks.get(i);
    //				inventoryStacks.set(i, ItemStack.EMPTY);
    //				markDirty();
    //				return itemstack;
    //			}
    //			@Nonnull
    //			ItemStack itemstack2 = inventoryStacks.get(i).splitStack(j);
    //			if (inventoryStacks.get(i).getCount() == 0) {
    //				inventoryStacks.set(i, ItemStack.EMPTY);
    //			}
    //			markDirty();
    //			return itemstack2;
    //		}
    //	}
    //	@Override
    //	public boolean isItemValidForSlot(final int slot, @Nonnull ItemStack item) {
    //		if (getUpgrade() != null) {
    //			final InventoryEffect inv = getUpgrade().getInventoryEffect();
    //			if (inv != null) {
    //				return inv.isItemValid(slot, item);
    //			}
    //		}
    //		return getMaster() != null && getMaster().isItemValidForSlot(slot, item);
    //	}

    @Override
    @Nonnull
    public ItemStack getInputContainer(final int tankid)
    {
        return getItem(0);
    }

    @Override
    public void setInputContainer(final int tankid, ItemStack stack)
    {
        setItem(0, stack);
    }

    @Override
    public void addToOutputContainer(final int tankid, @Nonnull ItemStack item)
    {
        TransferHandler.TransferItem(item, this, 1, 1, new ContainerUpgrade(0, null, this, new IntArray(0)), Slot.class, null, -1);
    }

    @Override
    public void onFluidUpdated(final int tankid)
    {
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void drawImage(int tankid, GuiBase gui, TextureAtlasSprite sprite, int targetX, int targetY, int srcX, int srcY, int width, int height) {}

    public void setCreativeBroken()
    {
        isCreativeBroken = true;
    }

    public boolean isCreativeBroken()
    {
        return isCreativeBroken;
    }

    //ISided
    @Override
    public int[] getSlotsForFace(Direction p_180463_1_)
    {
        return new int[0];
    }

    @Override
    public boolean canPlaceItemThroughFace(int p_180462_1_, ItemStack p_180462_2_, @Nullable Direction p_180462_3_)
    {
        return false;
    }

    @Override
    public boolean canTakeItemThroughFace(int p_180461_1_, ItemStack p_180461_2_, Direction p_180461_3_)
    {
        return false;
    }

    @Override
    public void clearContent()
    {
    }


    @Override
    public ITextComponent getDisplayName()
    {
        return new StringTextComponent(getUpgrade().getName());
    }

    @Nullable
    @Override
    public Container createMenu(int id, PlayerInventory playerInventory, PlayerEntity p_createMenu_3_)
    {
        return new ContainerUpgrade(id, playerInventory, this, new IntArray(0));
    }
}
