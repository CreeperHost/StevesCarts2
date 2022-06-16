package vswe.stevescarts.blocks.tileentities;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
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

public class TileEntityUpgrade extends TileEntityBase implements WorldlyContainer, ITankHolder, MenuProvider
{
    public SCTank tank = new SCTank(this, 0, 0);
    private TileEntityCartAssembler master;
    private int type;
    private boolean initialized;
    private CompoundTag comp;
    private NonNullList<ItemStack> inventoryStacks;
    private int[] slotsForSide;
    private boolean isCreativeBroken;

    public TileEntityUpgrade(BlockPos blockPos, BlockState blockState)
    {
        super(ModBlocks.UPGRADE_TILE.get(), blockPos, blockState);
    }

    public TileEntityUpgrade(AssemblerUpgrade assemblerUpgrade, BlockPos blockPos, BlockState blockState)
    {
        super(ModBlocks.UPGRADE_TILE.get(), blockPos, blockState);
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
                comp = new CompoundTag();
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

    public CompoundTag getCompound()
    {
        return comp;
    }

    //TODO
    //    @Nullable
    //    @Override
    //    public SUpdateTileEntityPacket getUpdatePacket()
    //    {
    //        return new SUpdateTileEntityPacket(getBlockPos(), 1, save(new CompoundNBT()));
    //    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt)
    {
        load(pkt.getTag());
    }

    //TODO
    //    @Override
    //    public CompoundTag getUpdateTag()
    //    {
    //        return save(new CompoundTag());
    //    }

    public AssemblerUpgrade getUpgrade()
    {
        return AssemblerUpgrade.getUpgrade(type);
    }

    public boolean hasInventory()
    {
        return inventoryStacks != null;
    }

    @Override
    public void load(CompoundTag compoundNBT)
    {
        super.load(compoundNBT);
        setType(compoundNBT.getByte("Type"), false);
        ContainerHelper.loadAllItems(compoundNBT, inventoryStacks);
        setChanged();
        final AssemblerUpgrade upgrade = getUpgrade();
        if (upgrade != null)
        {
            upgrade.load(this, compoundNBT);
        }
    }


    @Override
    public void saveAdditional(CompoundTag compoundNBT)
    {
        super.saveAdditional(compoundNBT);
        if (inventoryStacks != null)
        {
            ContainerHelper.saveAllItems(compoundNBT, inventoryStacks);
        }
        compoundNBT.putByte("Type", (byte) type);
        final AssemblerUpgrade upgrade = getUpgrade();
        if (upgrade != null)
        {
            upgrade.save(this, compoundNBT);
        }
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
        ItemStack itemStack = ContainerHelper.removeItem(this.inventoryStacks, id, amount);
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
    public boolean stillValid(Player player)
    {
        return true;
    }

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
        TransferHandler.TransferItem(item, this, 1, 1, new ContainerUpgrade(0, null, this, new SimpleContainerData(0)), Slot.class, null, -1);
    }

    @Override
    public void onFluidUpdated(final int tankid)
    {
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void drawImage(int tankid, GuiBase gui, TextureAtlasSprite sprite, int targetX, int targetY, int srcX, int srcY, int width, int height)
    {
    }

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
    public Component getDisplayName()
    {
        return Component.translatable(getUpgrade().getName());
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory playerInventory, Player p_createMenu_3_)
    {
        return new ContainerUpgrade(id, playerInventory, this, new SimpleContainerData(0));
    }
}
