package vswe.stevescarts.compat.ftbic;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IntArray;
import vswe.stevescarts.containers.ContainerBase;
import vswe.stevescarts.init.ModContainers;

import java.util.Objects;

public class ContainerIndustrialManager extends ContainerBase
{
    private IIntArray data;

    public ContainerIndustrialManager(int id, PlayerInventory playerInventory, PacketBuffer packetBuffer)
    {
        this(id, playerInventory, (TileIndustrialManager) Objects.requireNonNull(Minecraft.getInstance().level.getBlockEntity(packetBuffer.readBlockPos())), new IntArray(17));
    }

    public ContainerIndustrialManager(int id, PlayerInventory playerInventory, TileIndustrialManager tileEntityCargo, IIntArray data)
    {
        super(CompatFtbic.INDUSTRIAL_MANAGER_CONTAINER.get(), id);
        this.data = data;
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

    public int getEnergy()
    {
        return data.get(0);
    }

    protected int offsetX()
    {
        return 73;
    }

    @Override
    public boolean stillValid(PlayerEntity playerEntity)
    {
        return true;
    }
}
