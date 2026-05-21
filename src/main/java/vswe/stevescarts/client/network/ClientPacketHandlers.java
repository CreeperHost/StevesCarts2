package vswe.stevescarts.client.network;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import vswe.stevescarts.api.modules.ModuleBase;
import vswe.stevescarts.blocks.tileentities.TileEntityLiquid;
import vswe.stevescarts.blocks.tileentities.TileEntityUpgrade;
import vswe.stevescarts.containers.ContainerBase;
import vswe.stevescarts.entities.ModularMinecart;
import vswe.stevescarts.network.packets.PacketEntityData;
import vswe.stevescarts.network.packets.PacketGuiData;
import vswe.stevescarts.network.packets.PacketMinecartButton;
import vswe.stevescarts.polylib.DataEntity;
import vswe.stevescarts.polylib.EntityData;

import java.util.List;

public class ClientPacketHandlers {

    public static void handleMinecartButton(PacketMinecartButton msg, IPayloadContext ctx) {
        Minecraft mc = Minecraft.getInstance();
        Level level = mc.level;
        Player player = mc.player;
        PacketMinecartButton.handle(msg, level, player);
    }

    public static void handleGuiData(PacketGuiData msg, IPayloadContext ctx) {
        Player player = Minecraft.getInstance().player;
        if (player != null && player.containerMenu instanceof ContainerBase menu && menu.containerId == msg.getContainerId()) {
            menu.receiveGuiData(msg.getDataId(), msg.getData());
        }
    }

    public static void handleEntityData(PacketEntityData msg, IPayloadContext ctx) {
        Level level = Minecraft.getInstance().level;
        if (level == null) return;

        if (!(level.getEntity(msg.getEntityId()) instanceof DataEntity dataEntity)) return;

        List<EntityData<?>> list = dataEntity.getEntityDataList();
        if (msg.getIndex() < 0 || msg.getIndex() >= list.size()) return;

        EntityData<?> data = list.get(msg.getIndex());
        if (msg.getBuffer() != null) {
            data.fromBytes(msg.getBuffer());
        } else {
            data.set(cast(msg.getData().get()));
        }
    }

    public static void handleFluidSync(FluidStack fluidStack, BlockPos pos, int tankID) {
        ClientLevel level = Minecraft.getInstance().level;
        if (level == null) return;
        BlockEntity tile = level.getBlockEntity(pos);
        if (tile instanceof TileEntityLiquid entityLiquid) {
            entityLiquid.tanks[tankID].setFluid(fluidStack);
        } else if (tile instanceof TileEntityUpgrade entityUpgrade) {
            entityUpgrade.tank.setFluid(fluidStack);
        }
    }

    @SuppressWarnings("unchecked")
    private static <T> T cast(Object object) {
        return (T) object;
    }
}
