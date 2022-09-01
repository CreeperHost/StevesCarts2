package vswe.stevescarts.containers;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import org.jetbrains.annotations.NotNull;
import vswe.stevescarts.blocks.tileentities.TileEntityCartAssembler;
import vswe.stevescarts.containers.slots.SlotAssembler;
import vswe.stevescarts.init.ModContainers;

import java.util.ArrayList;
import java.util.Objects;

public class ContainerCartAssembler extends ContainerBase
{
    private TileEntityCartAssembler assembler;
    private SimpleContainerData data;

    public ContainerCartAssembler(int id, Inventory playerInventory, FriendlyByteBuf packetBuffer)
    {
        this(id, playerInventory, (TileEntityCartAssembler) Objects.requireNonNull(Minecraft.getInstance().level.getBlockEntity(packetBuffer.readBlockPos())), new SimpleContainerData(7));
    }

    public ContainerCartAssembler(int id, Inventory invPlayer, TileEntityCartAssembler assembler, SimpleContainerData data)
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
        return data.get(5);
    }

    public int getAssemblingTime()
    {
        return data.get(2);
    }

    public int getMaxAssemblingTime()
    {
        return data.get(0);
    }

    public boolean getIsAssembling()
    {
        return data.get(4) == 1;
    }

    @Override
    public boolean stillValid(@NotNull Player player)
    {
        return assembler.isUsableByPlayer(player);
    }
}
