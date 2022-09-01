package vswe.stevescarts.containers;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.SimpleContainerData;
import org.jetbrains.annotations.NotNull;
import vswe.stevescarts.blocks.tileentities.TileEntityActivator;
import vswe.stevescarts.helpers.ActivatorOption;
import vswe.stevescarts.init.ModContainers;

import java.util.ArrayList;
import java.util.Objects;

public class ContainerActivator extends ContainerBase
{
    private TileEntityActivator activator;
    public ArrayList<Integer> lastOptions;

    public ContainerActivator(int id, Inventory playerInventory, FriendlyByteBuf packetBuffer)
    {
        this(id, playerInventory, (TileEntityActivator) Objects.requireNonNull(Minecraft.getInstance().level.getBlockEntity(packetBuffer.readBlockPos())), new SimpleContainerData(17));
    }

    public ContainerActivator(int id, Inventory playerInventory, TileEntityActivator tileEntityActivator, SimpleContainerData data)
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
    public boolean stillValid(@NotNull Player player)
    {
        return true;
    }
}
