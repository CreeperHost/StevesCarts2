package vswe.stevescarts.network.packets;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;
import vswe.stevescarts.Constants;
import vswe.stevescarts.blocks.tileentities.TileEntityLiquid;
import vswe.stevescarts.blocks.tileentities.TileEntityUpgrade;

public class PacketFluidSync implements CustomPacketPayload {
    public static final ResourceLocation ID = new ResourceLocation(Constants.MOD_ID, "fluid_sync");
    private FluidStack fluidStack;
    private BlockPos pos;
    private int tankID;

    public PacketFluidSync(FluidStack fluidStack, BlockPos pos, int tankID) {
        this.fluidStack = fluidStack;
        this.pos = pos;
        this.tankID = tankID;
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeFluidStack(fluidStack);
        buf.writeBlockPos(pos);
        buf.writeInt(tankID);
    }

    @Override
    public ResourceLocation id() {
        return ID;
    }

    public static PacketFluidSync read(FriendlyByteBuf buffer) {
        return new PacketFluidSync(buffer.readFluidStack(), buffer.readBlockPos(), buffer.readInt());
    }

    public static void handle(final PacketFluidSync msg, PlayPayloadContext ctx) {
        if (ctx.flow() != PacketFlow.CLIENTBOUND) return;

        ctx.workHandler().execute(() -> {
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
