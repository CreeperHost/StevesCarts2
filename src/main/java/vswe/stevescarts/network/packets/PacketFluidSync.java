package vswe.stevescarts.network.packets;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.handling.IPayloadHandler;
import vswe.stevescarts.Constants;
import vswe.stevescarts.blocks.tileentities.TileEntityLiquid;
import vswe.stevescarts.blocks.tileentities.TileEntityUpgrade;

public class PacketFluidSync implements CustomPacketPayload {
    public static final Type<PacketFluidSync> TYPE = new Type<>(Identifier.fromNamespaceAndPath(Constants.MOD_ID, "fluid_sync"));
    private FluidStack fluidStack;
    private BlockPos pos;
    private int tankID;

    public PacketFluidSync(FluidStack fluidStack, BlockPos pos, int tankID) {
        this.fluidStack = fluidStack;
        this.pos = pos;
        this.tankID = tankID;
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

    public static PacketFluidSync read(RegistryFriendlyByteBuf buffer) {
        boolean empty = buffer.readBoolean();
        FluidStack fstack = empty ? FluidStack.EMPTY : FluidStack.STREAM_CODEC.decode(buffer);
        return new PacketFluidSync(fstack, buffer.readBlockPos(), buffer.readInt());
    }

    public static class Handler implements IPayloadHandler<PacketFluidSync> {
        @Override
        public void handle(PacketFluidSync msg, IPayloadContext ctx) {
            if (ctx.flow() != PacketFlow.CLIENTBOUND) return;

            ctx.enqueueWork(() -> {
                ClientLevel level = Minecraft.getInstance().level;
                if (level == null) return;
                BlockEntity tile = level.getBlockEntity(msg.pos);
                if (tile instanceof TileEntityLiquid entityLiquid) {
                    entityLiquid.tanks[msg.tankID].setFluid(msg.fluidStack);
                } else if (tile instanceof TileEntityUpgrade entityUpgrade) {
                    entityUpgrade.tank.setFluid(msg.fluidStack);
                }
            });
        }
    }
}
