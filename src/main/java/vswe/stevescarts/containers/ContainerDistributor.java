package vswe.stevescarts.containers;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IntArray;
import vswe.stevescarts.blocks.tileentities.TileEntityDistributor;
import vswe.stevescarts.helpers.DistributorSide;
import vswe.stevescarts.init.ModContainers;

import java.util.ArrayList;
import java.util.Objects;

public class ContainerDistributor extends ContainerBase
{
    private TileEntityDistributor distributor;
    public ArrayList<Short> cachedValues;

    public ContainerDistributor(int id, PlayerInventory playerInventory, PacketBuffer packetBuffer)
    {
        this(id, playerInventory, (TileEntityDistributor) Objects.requireNonNull(Minecraft.getInstance().level.getBlockEntity(packetBuffer.readBlockPos())), new IntArray(7));
    }

    public ContainerDistributor(int id, PlayerInventory invPlayer, TileEntityDistributor distributor, IIntArray data)
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
    public boolean stillValid(PlayerEntity p_75145_1_)
    {
        return true;
    }
}
