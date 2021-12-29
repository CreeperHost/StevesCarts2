package vswe.stevescarts.containers;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IntArray;
import vswe.stevescarts.blocks.tileentities.TileEntityUpgrade;
import vswe.stevescarts.init.ModContainers;
import vswe.stevescarts.upgrades.InventoryEffect;

import java.util.Objects;

public class ContainerUpgrade extends ContainerBase
{
    private TileEntityUpgrade upgrade;
    private IntArray data;

    public ContainerUpgrade(int id, PlayerInventory playerInventory, PacketBuffer packetBuffer)
    {
        this(id, playerInventory, (TileEntityUpgrade) Objects.requireNonNull(Minecraft.getInstance().level.getBlockEntity(packetBuffer.readBlockPos())), new IntArray(7));
    }

    public ContainerUpgrade(int containerID, PlayerInventory invPlayer, final TileEntityUpgrade upgrade, IIntArray data)
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
    public boolean stillValid(PlayerEntity p_75145_1_)
    {
        return true;
    }
}
