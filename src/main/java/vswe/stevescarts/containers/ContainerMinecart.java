package vswe.stevescarts.containers;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import org.jetbrains.annotations.NotNull;
import vswe.stevescarts.api.modules.ModuleBase;
import vswe.stevescarts.api.slots.SlotStevesCarts;
import vswe.stevescarts.entities.EntityMinecartModular;
import vswe.stevescarts.init.ModContainers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class ContainerMinecart extends ContainerBase
{
    private Inventory playerInventory;
    public HashMap<Short, Short> cache;
    public EntityMinecartModular cart;
    private final SimpleContainerData data;

    public ContainerMinecart(int id, Inventory playerInventory, FriendlyByteBuf packetBuffer)
    {
        this(id, playerInventory, (EntityMinecartModular) playerInventory.player.level().getEntity(packetBuffer.readInt()), new SimpleContainerData(1));
    }

    public ContainerMinecart(int id, Inventory playerInventory, EntityMinecartModular cart, SimpleContainerData data)
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
                    final ArrayList<SlotStevesCarts> slotsList = module.getSlots();
                    for (final SlotStevesCarts slot : slotsList)
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

    protected void playerInv(final Inventory playerInventory)
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

    @Override
    public boolean stillValid(@NotNull Player playerEntity)
    {
        return true;
    }

    
    @Override //Detect and send
    public void broadcastChanges() {
        super.broadcastChanges();
        Player player = playerInventory.player;
        
        if (cart.getModules() != null) {
            for (final ModuleBase module : cart.getModules()) {
                module.checkGuiData(this, Collections.singletonList(player), false);
            }
        }
    }

    @Override
    public void receiveGuiData(int id, int data) {
        data &= 0xFFFF;
        if (cart.getModules() != null) {
            for (final ModuleBase module : cart.getModules()) {
                if (id >= module.getGuiDataStart() && id < module.getGuiDataStart() + module.numberOfGuiData()) {
                    module.receiveGuiData(id - module.getGuiDataStart(), (short) data);
                    break;
                }
            }
        }
    }
}
