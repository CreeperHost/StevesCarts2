package vswe.stevescarts.containers;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.SimpleContainerData;
import vswe.stevescarts.blocks.tileentities.TileEntityDetector;
import vswe.stevescarts.helpers.LogicObject;
import vswe.stevescarts.init.ModContainers;

import java.util.Objects;

public class ContainerDetector extends ContainerBase
{
    private TileEntityDetector detector;
    public LogicObject mainObj;

    public ContainerDetector(int id, Inventory playerInventory, FriendlyByteBuf packetBuffer)
    {
        this(id, playerInventory, (TileEntityDetector) Objects.requireNonNull(Minecraft.getInstance().level.getBlockEntity(packetBuffer.readBlockPos())), new SimpleContainerData(7));
    }

    public ContainerDetector(int id, Inventory invPlayer, TileEntityDetector detector, SimpleContainerData data)
    {
        super(ModContainers.CONTAINER_DETECTOR.get(), id);
        mainObj = new LogicObject((byte) 1, (byte) 0);
        this.detector = detector;
    }

    @Override
    public boolean stillValid(Player p_75145_1_)
    {
        return true;
    }

    public TileEntityDetector getDetector()
    {
        return detector;
    }
}
