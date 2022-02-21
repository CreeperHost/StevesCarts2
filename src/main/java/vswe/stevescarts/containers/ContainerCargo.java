package vswe.stevescarts.containers;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import vswe.stevescarts.blocks.tileentities.TileEntityCargo;
import vswe.stevescarts.containers.slots.SlotCargo;
import vswe.stevescarts.init.ModContainers;

import java.util.ArrayList;
import java.util.Objects;

public class ContainerCargo extends ContainerBase
{
    public short lastTarget;
    public TileEntityCargo tileEntityCargo;
    public SimpleContainerData data;

    public ContainerCargo(int id, Inventory playerInventory, FriendlyByteBuf packetBuffer)
    {
        this(id, playerInventory, (TileEntityCargo) Objects.requireNonNull(Minecraft.getInstance().level.getBlockEntity(packetBuffer.readBlockPos())), new SimpleContainerData(17));
    }

    public ContainerCargo(int id, Inventory playerInventory, TileEntityCargo tileEntityCargo, SimpleContainerData data)
    {
        super(ModContainers.CONTAINER_CARGO.get(), id);
        this.tileEntityCargo = tileEntityCargo;
        this.tileEntityCargo.cargoSlots = new ArrayList<>();
        this.tileEntityCargo.lastLayout = -1;
        this.data = data;
        for (int i = 0; i < 60; ++i)
        {
            final SlotCargo slot = new SlotCargo(tileEntityCargo, this, i);
            addSlot(slot);
            this.tileEntityCargo.cargoSlots.add(slot);
        }

        for (int k = 0; k < 3; ++k)
        {
            for (int j1 = 0; j1 < 9; ++j1)
            {
                Slot s = new Slot(playerInventory, j1 + k * 9 + 9, j1 * 18 + offsetX(), 104 + k * 18 + 36);
                addSlot(s);
            }
        }
        for (int l = 0; l < 9; ++l)
        {
            addSlot(new Slot(playerInventory, l, l * 18 + offsetX(), 198));
        }
        addDataSlots(data);
    }

    public int getLayoutType()
    {
        return data.get(0);
    }

    public int[] getColor()
    {
        return new int[]{data.get(1), data.get(2), data.get(3), data.get(4)};
    }

    public boolean[] toCart()
    {
        return new boolean[]{data.get(5) == 1, data.get(6) == 1, data.get(7) == 1, data.get(8) == 1};
    }

    public int[] getTarget()
    {
        return new int[]{data.get(9), data.get(10), data.get(11), data.get(12)};
    }

    public boolean[] doReturn()
    {
        return new boolean[]{data.get(13) == 1, data.get(14) == 1, data.get(15) == 1, data.get(16) == 1};
    }

    protected int offsetX()
    {
        return 73;
    }

    @Override
    public boolean stillValid(Player playerEntity)
    {
        return true;
    }
}
