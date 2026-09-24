package vswe.stevescarts.containers;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import org.jetbrains.annotations.NotNull;
import vswe.stevescarts.blocks.tileentities.TileEntityActivator;
import vswe.stevescarts.init.ModContainers;

import java.util.Objects;

public class ContainerActivator extends ContainerBase {
    private final TileEntityActivator activator;

    public ContainerActivator(int id, Inventory playerInventory, FriendlyByteBuf packetBuffer) {
        this(id, playerInventory, (TileEntityActivator) Objects.requireNonNull(Minecraft.getInstance().level.getBlockEntity(packetBuffer.readBlockPos())), new SimpleContainerData(7));
    }

    public ContainerActivator(int id, Inventory playerInventory, TileEntityActivator tileEntityActivator, ContainerData data) {
        super(ModContainers.CONTAINER_ACTIVATOR.get(), id);
        this.activator = tileEntityActivator;
        addDataSlots(data);
    }

    public TileEntityActivator getActivator() {
        return activator;
    }

    @Override
    public void setData(int id, int value) {
        super.setData(id, value);
        if (id >= 0 && id < activator.getOptions().size()) {
            activator.getOptions().get(id).setOption(value);
        }
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return true;
    }
}
