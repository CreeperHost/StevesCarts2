package vswe.stevescarts.containers;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;
import vswe.stevescarts.blocks.tileentities.TileEntityUpgrade;
import vswe.stevescarts.init.ModContainers;
import vswe.stevescarts.network.PacketHandler;
import vswe.stevescarts.network.packets.PacketFluidSync;
import vswe.stevescarts.upgrades.InventoryEffect;

import java.util.Objects;

public class ContainerUpgrade extends ContainerBase
{
    private TileEntityUpgrade upgrade;
    private SimpleContainerData data;
    private FluidStack lastFluid = FluidStack.EMPTY;

    public ContainerUpgrade(int id, Inventory playerInventory, FriendlyByteBuf packetBuffer)
    {
        this(id, playerInventory, (TileEntityUpgrade) Objects.requireNonNull(Minecraft.getInstance().level.getBlockEntity(packetBuffer.readBlockPos())), new SimpleContainerData(7));
    }

    public ContainerUpgrade(int containerID, Inventory invPlayer, final TileEntityUpgrade upgrade, SimpleContainerData data)
    {
        super(ModContainers.CONTAINER_UPGRADE.get(), containerID);
        this.upgrade = upgrade;
        if (upgrade.getUpgrade() == null) return;

        if (upgrade.getUpgrade().getInventoryEffect() != null)
        {
            try
            {
                final InventoryEffect inventory = upgrade.getUpgrade().getInventoryEffect();
                inventory.clear();
                for (int id = 0; id < inventory.getInventorySize(); ++id)
                {
                    final Slot slot = inventory.createSlot(upgrade, id);
                    addSlot(slot);
                    inventory.addSlot(slot);
                }
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        for (int i = 0; i < 3; ++i)
        {
            for (int k = 0; k < 9; ++k)
            {
                addSlot(new Slot(invPlayer, k + i * 9 + 9, offsetX() + k * 18, i * 18 + offsetY()));
            }
        }
        for (int j = 0; j < 9; ++j)
        {
            addSlot(new Slot(invPlayer, j, offsetX() + j * 18, 58 + offsetY()));
        }
    }

    @Override
    public void broadcastChanges() {
        super.broadcastChanges();

        if (!lastFluid.equals(upgrade.tank.getFluid()) || lastFluid.getAmount() != upgrade.tank.getFluid().getAmount()) {
            lastFluid = upgrade.tank.getFluid().copy();
            PacketHandler.send(PacketDistributor.TRACKING_CHUNK.with(() -> upgrade.getLevel().getChunkAt(upgrade.getBlockPos())), new PacketFluidSync(lastFluid, upgrade.getBlockPos(), 0));
        }
    }

    public TileEntityUpgrade getUpgrade()
    {
        return upgrade;
    }

    protected int offsetX()
    {
        return 48;
    }

    protected int offsetY()
    {
        return 108;
    }

    @Override
    public boolean stillValid(@NotNull Player player)
    {
        return true;
    }
}
