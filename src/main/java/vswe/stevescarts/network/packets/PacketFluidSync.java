package vswe.stevescarts.network.packets;

import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.handling.IPayloadHandler;
import vswe.stevescarts.Constants;
import vswe.stevescarts.client.network.ClientPacketHandlers;

public class PacketFluidSync implements CustomPacketPayload {
    public static final Type<PacketFluidSync> TYPE = new Type<>(Identifier.fromNamespaceAndPath(Constants.MOD_ID, "fluid_sync"));
    private final FluidStack fluidStack;
    private final BlockPos pos;
    private final int tankID;

    public PacketFluidSync(FluidStack fluidStack, BlockPos pos, int tankID) {
        this.fluidStack = fluidStack;
        this.pos = pos;
        this.tankID = tankID;
    }

    public static PacketFluidSync read(RegistryFriendlyByteBuf buffer) {
        boolean empty = buffer.readBoolean();
        FluidStack fstack = empty ? FluidStack.EMPTY : FluidStack.STREAM_CODEC.decode(buffer);
        return new PacketFluidSync(fstack, buffer.readBlockPos(), buffer.readInt());
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public void write(RegistryFriendlyByteBuf buf) {
        buf.writeBoolean(fluidStack.isEmpty());
        if (!fluidStack.isEmpty()) {
            FluidStack.STREAM_CODEC.encode(buf, fluidStack);
        }
        buf.writeBlockPos(pos);
        buf.writeInt(tankID);
    }

    public static class Handler implements IPayloadHandler<PacketFluidSync> {
        @Override
        public void handle(PacketFluidSync msg, IPayloadContext ctx) {
            if (ctx.flow() != PacketFlow.CLIENTBOUND) return;
            ctx.enqueueWork(() -> ClientPacketHandlers.handleFluidSync(msg.fluidStack, msg.pos, msg.tankID));
        }
    }
}
