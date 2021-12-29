package vswe.stevescarts.containers;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IntArray;
import vswe.stevescarts.blocks.tileentities.TileEntityActivator;
import vswe.stevescarts.helpers.ActivatorOption;
import vswe.stevescarts.init.ModContainers;

import java.util.ArrayList;
import java.util.Objects;

public class ContainerActivator extends ContainerBase
{
    private TileEntityActivator activator;
    public ArrayList<Integer> lastOptions;

    public ContainerActivator(int id, PlayerInventory playerInventory, PacketBuffer packetBuffer)
    {
        this(id, playerInventory, (TileEntityActivator) Objects.requireNonNull(Minecraft.getInstance().level.getBlockEntity(packetBuffer.readBlockPos())), new IntArray(17));
    }

    public ContainerActivator(int id, PlayerInventory playerInventory, TileEntityActivator tileEntityActivator, IIntArray data)
    {
        super(ModContainers.CONTAINER_ACTIVATOR.get(), id);
        this.activator = tileEntityActivator;
        lastOptions = new ArrayList<>();
        for (final ActivatorOption option : activator.getOptions())
        {
            lastOptions.add(option.getOption());
        }
    }

    public TileEntityActivator getActivator()
    {
        return activator;
    }

    @Override
    public boolean stillValid(PlayerEntity playerEntity)
    {
        return true;
    }
}
