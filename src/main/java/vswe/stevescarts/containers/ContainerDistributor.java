package vswe.stevescarts.containers;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.SimpleContainerData;
import org.jetbrains.annotations.NotNull;
import vswe.stevescarts.blocks.tileentities.TileEntityDistributor;
import vswe.stevescarts.helpers.DistributorSide;
import vswe.stevescarts.init.ModContainers;

import java.util.ArrayList;
import java.util.Objects;

public class ContainerDistributor extends ContainerBase
{
    private TileEntityDistributor distributor;
    public ArrayList<Short> cachedValues;

    public ContainerDistributor(int id, Inventory playerInventory, FriendlyByteBuf packetBuffer)
    {
        this(id, playerInventory, (TileEntityDistributor) Objects.requireNonNull(Minecraft.getInstance().level.getBlockEntity(packetBuffer.readBlockPos())), new SimpleContainerData(7));
    }

    public ContainerDistributor(int id, Inventory invPlayer, TileEntityDistributor distributor, SimpleContainerData data)
    {
        super(ModContainers.CONTAINER_DISTRIBUTOR.get(), id);
        this.distributor = distributor;
        cachedValues = new ArrayList<>();
        for (final DistributorSide side : distributor.getSides())
        {
            cachedValues.add((short) 0);
            cachedValues.add((short) 0);
        }
    }

    public TileEntityDistributor getDistributor()
    {
        return distributor;
    }

    @Override
    public boolean stillValid(@NotNull Player player)
    {
        return true;
    }
}
