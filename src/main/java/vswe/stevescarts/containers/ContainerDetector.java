package vswe.stevescarts.containers;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IntArray;
import vswe.stevescarts.blocks.tileentities.TileEntityDetector;
import vswe.stevescarts.helpers.LogicObject;
import vswe.stevescarts.init.ModContainers;

import java.util.Objects;

public class ContainerDetector extends ContainerBase
{
    private TileEntityDetector detector;
    public LogicObject mainObj;

    public ContainerDetector(int id, PlayerInventory playerInventory, PacketBuffer packetBuffer)
    {
        this(id, playerInventory, (TileEntityDetector) Objects.requireNonNull(Minecraft.getInstance().level.getBlockEntity(packetBuffer.readBlockPos())), new IntArray(7));
    }

    public ContainerDetector(int id, PlayerInventory invPlayer, TileEntityDetector detector, IIntArray data)
    {
        super(ModContainers.CONTAINER_DETECTOR.get(), id);
        mainObj = new LogicObject((byte) 1, (byte) 0);
        this.detector = detector;
    }

    @Override
    public boolean stillValid(PlayerEntity p_75145_1_)
    {
        return true;
    }

    public TileEntityDetector getDetector()
    {
        return detector;
    }

    //	@Override
    //	public IInventory getMyInventory() {
    //		return null;
    //	}
    //
    //	@Override
    //	public TileEntityBase getTileEntity() {
    //		return detector;
    //	}
    //
    //	public ContainerDetector(final IInventory invPlayer, final TileEntityDetector detector) {
    //		this.detector = detector;
    //		mainObj = new LogicObject((byte) 1, (byte) 0);
    //	}
}
