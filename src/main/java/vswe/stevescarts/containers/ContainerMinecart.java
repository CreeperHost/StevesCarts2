package vswe.stevescarts.containers;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.IContainerListener;
import net.minecraft.inventory.container.Slot;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IntArray;
import vswe.stevescarts.containers.slots.SlotBase;
import vswe.stevescarts.entitys.EntityMinecartModular;
import vswe.stevescarts.init.ModContainers;
import vswe.stevescarts.modules.ModuleBase;

import java.util.ArrayList;
import java.util.HashMap;

public class ContainerMinecart extends ContainerBase
{
    private PlayerInventory playerInventory;
    public HashMap<Short, Short> cache;
    public EntityMinecartModular cart;
    private IIntArray data;

    public ContainerMinecart(int id, PlayerInventory playerInventory, PacketBuffer packetBuffer)
    {
        this(id, playerInventory, (EntityMinecartModular) playerInventory.player.level.getEntity(packetBuffer.readInt()), new IntArray(1));
    }

    public ContainerMinecart(int id, PlayerInventory playerInventory, EntityMinecartModular cart, IIntArray data)
    {
        super(ModContainers.CONTAINER_MINECART.get(), id);
        this.data = data;
        this.cart = cart;
        cartInv(cart);
        playerInv(playerInventory);
        addDataSlots(data);
    }

    protected void cartInv(final EntityMinecartModular cart)
    {
        this.cart = cart;
        if (cart.getModules() != null)
        {
            for (final ModuleBase module : cart.getModules())
            {
                if (module.hasSlots())
                {
                    final ArrayList<SlotBase> slotsList = module.getSlots();
                    for (final SlotBase slot : slotsList)
                    {
                        slot.x = (slot.getX() + module.getX() + 1);
                        slot.y = (slot.getY() + module.getY() + 1);
                        addSlot(slot);
                    }
                }
            }
        }
        else
        {
            for (int i = 0; i < 100; ++i)
            {
                addSlot(new Slot(cart, i, -1000, -1000));
            }
        }
    }

    protected void playerInv(final PlayerInventory playerInventory)
    {
        this.playerInventory = playerInventory;
        for (int i = 0; i < 3; ++i)
        {
            for (int k = 0; k < 9; ++k)
            {
                addSlot(new Slot(playerInventory, k + i * 9 + 9, offsetX() + k * 18, i * 18 + offsetY()));
            }
        }
        for (int j = 0; j < 9; ++j)
        {
            addSlot(new Slot(playerInventory, j, offsetX() + j * 18, 58 + offsetY()));
        }
    }

    protected int offsetX()
    {
        return 159;
    }

    protected int offsetY()
    {
        return 174;
    }

    //	@Override
    //	public ItemStack slotClick(int slotId, int dragType, ClickType clickTypeIn, EntityPlayer player) {
    //		ItemStack stack = super.slotClick(slotId, dragType, clickTypeIn, player);
    //		if (slotId > 0) {
    //			Slot slot = this.inventorySlots.get(slotId);
    //			if (slot instanceof SlotFake) {
    //				return ItemStack.EMPTY;
    //			}
    //		}
    //		return stack;
    //	}
    //
    //	@Override
    //	public boolean canInteractWith(final PlayerEntity entityplayer) {
    //		return cart.isUsableByPlayer(entityplayer);
    //	}
    //
    //	@Override
    //	public void onContainerClosed(final PlayerEntity par1EntityPlayer) {
    //		super.onContainerClosed(par1EntityPlayer);
    //		cart.closeInventory(par1EntityPlayer);
    //	}

    @Override
    public void addSlotListener(final IContainerListener par1ICrafting)
    {
        super.addSlotListener(par1ICrafting);
        if (cart.getModules() != null)
        {
            for (ModuleBase module : cart.getModules())
            {

            }
        }
    }
    //
    //	@SideOnly(Side.CLIENT)
    //	@Override
    //	public void updateProgressBar(final int par1, int par2) {
    //		par2 &= 0xFFFF;
    //		if (cart.getModules() != null) {
    //			for (final ModuleBase module : cart.getModules()) {
    //				if (par1 >= module.getGuiDataStart() && par1 < module.getGuiDataStart() + module.numberOfGuiData()) {
    //					module.receiveGuiData(par1 - module.getGuiDataStart(), (short) par2);
    //					break;
    //				}
    //			}
    //		}
    //	}
    //
    //	@Override
    //	public void detectAndSendChanges() {
    //		super.detectAndSendChanges();
    //		if (cart.getModules() != null && listeners.size() > 0) {
    //			for (final ModuleBase module : cart.getModules()) {
    //				module.checkGuiData(this, listeners, false);
    //			}
    //		}
    //	}

    @Override
    public boolean stillValid(PlayerEntity playerEntity)
    {
        return true;
    }
}
