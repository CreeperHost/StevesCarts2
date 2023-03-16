package vswe.stevescarts.network.packets;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import vswe.stevescarts.blocks.tileentities.TileEntityLiquid;
import vswe.stevescarts.blocks.tileentities.TileEntityUpgrade;

import java.util.function.Supplier;

public class PacketFluidSync
{
    private FluidStack fluidStack;
    private BlockPos pos;
    private int tankID;

    public PacketFluidSync(FluidStack fluidStack, BlockPos pos, int tankID)
    {
        this.fluidStack = fluidStack;
        this.pos = pos;
        this.tankID = tankID;
    }

    public static void encode(PacketFluidSync msg, FriendlyByteBuf buffer)
    {
        buffer.writeFluidStack(msg.fluidStack);
        buffer.writeBlockPos(msg.pos);
        buffer.writeInt(msg.tankID);
    }

    public static PacketFluidSync decode(FriendlyByteBuf buffer)
    {
        return new PacketFluidSync(buffer.readFluidStack(), buffer.readBlockPos(), buffer.readInt());
    }

    public static class Handler
    {
        public static void handle(final PacketFluidSync msg, Supplier<NetworkEvent.Context> ctx)
        {
            if (ctx.get().getDirection() != NetworkDirection.PLAY_TO_CLIENT) return;

            ctx.get().enqueueWork(() -> {
                ClientLevel clientWorld = Minecraft.getInstance().level;
                if (clientWorld == null) return;
                BlockEntity tile = clientWorld.getBlockEntity(msg.pos);
                if (tile instanceof TileEntityLiquid entityLiquid) {
                    entityLiquid.tanks[msg.tankID].setFluid(msg.fluidStack);
                } else if (tile instanceof TileEntityUpgrade entityUpgrade) {
                    entityUpgrade.tank.setFluid(msg.fluidStack);
                }
            });
            ctx.get().setPacketHandled(true);
        }
    }
}
