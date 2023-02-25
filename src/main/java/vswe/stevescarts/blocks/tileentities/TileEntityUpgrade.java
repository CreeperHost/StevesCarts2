package vswe.stevescarts.blocks.tileentities;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
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
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import vswe.stevescarts.blocks.BlockUpgrade;
import vswe.stevescarts.client.guis.GuiBase;
import vswe.stevescarts.containers.ContainerUpgrade;
import vswe.stevescarts.helpers.storages.ITankHolder;
import vswe.stevescarts.helpers.storages.SCTank;
import vswe.stevescarts.helpers.storages.TransferHandler;
import vswe.stevescarts.init.ModBlocks;
import vswe.stevescarts.upgrades.AssemblerUpgrade;
import vswe.stevescarts.upgrades.InventoryEffect;

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
        if(level == null) return;
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
        if(level == null) return Direction.NORTH;
        return level.getBlockState(getBlockPos()).getValue(BlockUpgrade.FACING);
    }

    public TileEntityCartAssembler getMaster()
    {
        return master;
    }

    public void setType(final int type)
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

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt)
    {
        if(pkt.getTag() != null) load(pkt.getTag());
    }

    @Override
    public CompoundTag getUpdateTag() {
        return saveWithoutMetadata();
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public AssemblerUpgrade getUpgrade()
    {
        return AssemblerUpgrade.getUpgrade(type);
    }

    @SuppressWarnings("unused")
    public boolean hasInventory()
    {
        return inventoryStacks != null;
    }

    @Override
    public void load(@NotNull CompoundTag compoundNBT)
    {
        super.load(compoundNBT);
        setType(compoundNBT.getByte("Type"));
        ContainerHelper.loadAllItems(compoundNBT, inventoryStacks);
        setChanged();
        final AssemblerUpgrade upgrade = getUpgrade();
        if (upgrade != null)
        {
            upgrade.load(this, compoundNBT);
        }
    }


    @Override
    public void saveAdditional(@NotNull CompoundTag compoundNBT)
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
    public @NotNull ItemStack getItem(int i)
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
    public @NotNull ItemStack removeItem(int id, int amount)
    {
        ItemStack itemStack = ContainerHelper.removeItem(this.inventoryStacks, id, amount);
        if (!itemStack.isEmpty()) this.setChanged();
        return itemStack;
    }

    @Override
    public @NotNull ItemStack removeItemNoUpdate(int id)
    {
        ItemStack itemStack = this.inventoryStacks.get(id);
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
    public void setItem(int i, @NotNull ItemStack itemstack)
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
    public boolean stillValid(@NotNull Player player)
    {
        return true;
    }

    @Override
    @NotNull
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
    public void addToOutputContainer(final int tankid, @NotNull ItemStack item)
    {
        TransferHandler.TransferItem(item, this, 1, 1, new ContainerUpgrade(0, null, this, new SimpleContainerData(0)), Slot.class, null, -1);
    }

    @Override
    public void onFluidUpdated(final int tankid)
    {
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void drawImage(int tankid, AbstractContainerScreen<?> gui, TextureAtlasSprite sprite, int targetX, int targetY, int width, int height)
    {
        GuiComponent.blit(new PoseStack(), gui.getGuiLeft() + targetX, gui.getGuiTop() + targetY, 0, width, height, sprite);
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
    public int @NotNull [] getSlotsForFace(@NotNull Direction direction)
    {
        return new int[0];
    }

    @Override
    public boolean canPlaceItemThroughFace(int id, @NotNull ItemStack itemStack, @Nullable Direction direction)
    {
        return false;
    }

    @Override
    public boolean canTakeItemThroughFace(int p_180461_1_, @NotNull ItemStack itemStack, @NotNull Direction direction)
    {
        return false;
    }

    @Override
    public void clearContent()
    {
    }


    @Override
    public @NotNull Component getDisplayName()
    {
        return Component.translatable(getUpgrade().getName());
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, @NotNull Inventory playerInventory, @NotNull Player player)
    {
        return new ContainerUpgrade(id, playerInventory, this, new SimpleContainerData(0));
    }
}
