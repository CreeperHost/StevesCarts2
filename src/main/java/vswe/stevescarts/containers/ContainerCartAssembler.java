package vswe.stevescarts.containers;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IntArray;
import vswe.stevescarts.blocks.tileentities.TileEntityCartAssembler;
import vswe.stevescarts.containers.slots.SlotAssembler;
import vswe.stevescarts.init.ModContainers;

import java.util.ArrayList;
import java.util.Objects;

public class ContainerCartAssembler extends ContainerBase
{
    private TileEntityCartAssembler assembler;
    private IIntArray data;

    public ContainerCartAssembler(int id, PlayerInventory playerInventory, PacketBuffer packetBuffer)
    {
        this(id, playerInventory, (TileEntityCartAssembler) Objects.requireNonNull(Minecraft.getInstance().level.getBlockEntity(packetBuffer.readBlockPos())), new IntArray(7));
    }

    public ContainerCartAssembler(int id, PlayerInventory invPlayer, TileEntityCartAssembler assembler, IIntArray data)
    {
        super(ModContainers.CONTAINER_CART_ASSEMBLER.get(), id);
        this.data = data;
        this.assembler = assembler;
        final ArrayList<SlotAssembler> slots = assembler.getSlots();
        for (final SlotAssembler slot : slots)
        {
            addSlot(slot);
        }
        for (int i = 0; i < 3; ++i)
        {
            for (int k = 0; k < 9; ++k)
            {
                addSlot(new Slot(invPlayer, k + i * 9 + 9, offsetX() + k * 18, i * 18 + offsetY()));
            }
        }
        for (int j = 0; j < 9; ++j)
        {
            addSlot(new Slot(invPlayer, j, offsetX() + j * 18, 58 + offsetY()));
        }
        addDataSlots(data);
    }

    public TileEntityCartAssembler getAssembler()
    {
        return assembler;
    }


    protected int offsetX()
    {
        return 176;
    }

    protected int offsetY()
    {
        return 174;
    }

    public int getFuel()
    {
        return assembler.getIntFromShorts((short)data.get(5), (short)data.get(6));
    }

    public int getAssemblingTime()
    {
        return assembler.getIntFromShorts((short)data.get(2), (short)data.get(3));
    }

    public int getMaxAssemblingTime()
    {
        return assembler.getIntFromShorts((short)data.get(0), (short)data.get(1));
    }

    public boolean getIsAssembling()
    {
        return data.get(4) == 1;
    }

    @Override
    public boolean stillValid(PlayerEntity player)
    {
        return assembler.isUsableByPlayer(player);
    }
}
