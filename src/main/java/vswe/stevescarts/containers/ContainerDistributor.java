package vswe.stevescarts.containers;

import net.creeperhost.polylib.client.modulargui.lib.container.DataSync;
import net.creeperhost.polylib.containers.ModularGuiContainerMenu;
import net.creeperhost.polylib.data.serializable.AbstractDataStore;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;
import vswe.stevescarts.blocks.tileentities.TileEntityDistributor;
import vswe.stevescarts.helpers.DistributorSide;
import vswe.stevescarts.init.ModContainers;

import java.util.ArrayList;
import java.util.Objects;

public class ContainerDistributor extends ModularGuiContainerMenu {
    private final TileEntityDistributor distributor;
    public ArrayList<DataSync<DistributorSide>> sideSyncs = new ArrayList<>();

    public ContainerDistributor(int id, Inventory playerInventory, FriendlyByteBuf packetBuffer) {
        this(id, playerInventory, (TileEntityDistributor) Objects.requireNonNull(Minecraft.getInstance().level.getBlockEntity(packetBuffer.readBlockPos())));
    }

    public ContainerDistributor(int id, Inventory invPlayer, TileEntityDistributor distributor) {
        super(ModContainers.CONTAINER_DISTRIBUTOR.get(), id, invPlayer);
        this.distributor = distributor;

        ArrayList<DistributorSide> sides = distributor.getSides();
        for (int i = 0; i < sides.size(); i++) {
            DistributorSide side = sides.get(i);
            int finalI = i;
            sideSyncs.add(new DataSync<>(this, new SideData(side), () -> sides.get(finalI)));
        }
    }

    public TileEntityDistributor getDistributor() {
        return distributor;
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return true;
    }

    public static class SideData extends AbstractDataStore<DistributorSide> {
        public SideData(DistributorSide initial) {
            super(initial.copy().setData(0));
        }

        @Override
        public DistributorSide set(DistributorSide value) {
            if (!Objects.equals(value.getData(), this.value.getData()) && validator.test(value)) {
                this.value = value.copy();
                markDirty();
            }
            return this.value;
        }

        @Override
        public void toBytes(FriendlyByteBuf buf) {
            buf.writeVarInt(value.getData());
        }

        @Override
        public void fromBytes(FriendlyByteBuf buf) {
            value.setData(buf.readVarInt());
        }

        @Override
        public Tag toTag() {
            return IntTag.valueOf(value.getData());
        }

        @Override
        public void fromTag(Tag tag) {
            value.setData(((IntTag) tag).getAsInt());
        }

        @Override
        public boolean isSameValue(DistributorSide newValue) {
            return value.getData() == newValue.getData();
        }
    }
}
