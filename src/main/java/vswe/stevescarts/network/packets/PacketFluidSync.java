package vswe.stevescarts.network.packets;

import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;
import vswe.stevescarts.blocks.tileentities.TileEntityLiquid;

import java.util.function.Supplier;

public class PacketFluidSync
{
    private FluidStack fluidStack;
    private BlockPos pos;
    private int worldID;
    private int tankID;

    public PacketFluidSync(FluidStack fluidStack, BlockPos pos, int worldID, int tankID)
    {
        this.fluidStack = fluidStack;
        this.pos = pos;
        this.worldID = worldID;
        this.tankID = tankID;
    }

    public static void encode(PacketFluidSync msg, PacketBuffer buffer)
    {
        buffer.writeFluidStack(msg.fluidStack);
        buffer.writeBlockPos(msg.pos);
        buffer.writeInt(msg.worldID);
        buffer.writeInt(msg.tankID);
    }

    public static PacketFluidSync decode(PacketBuffer buffer)
    {
        return new PacketFluidSync(buffer.readFluidStack(), buffer.readBlockPos(), buffer.readInt(), buffer.readInt());
    }

    public static class Handler
    {
        public static void handle(final PacketFluidSync msg, Supplier<NetworkEvent.Context> ctx)
        {
            if (ctx.get().getDirection() == NetworkDirection.PLAY_TO_CLIENT)
            {
                ctx.get().enqueueWork(() ->
                {
                    ClientWorld clientWorld = Minecraft.getInstance().level;
                    if (clientWorld != null)
                    {
                        if (clientWorld.getBlockEntity(msg.pos) != null && clientWorld.getBlockEntity(msg.pos) instanceof TileEntityLiquid)
                        {
                            TileEntityLiquid tileEntity = (TileEntityLiquid) clientWorld.getBlockEntity(msg.pos);
                            tileEntity.tanks[msg.tankID].setFluid(msg.fluidStack);
                        }
                    }
                });
                ctx.get().setPacketHandled(true);
            }
        }
    }
}
