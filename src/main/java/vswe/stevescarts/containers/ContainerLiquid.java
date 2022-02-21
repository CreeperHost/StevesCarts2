package vswe.stevescarts.containers;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraftforge.fluids.FluidStack;
import vswe.stevescarts.blocks.tileentities.TileEntityLiquid;
import vswe.stevescarts.containers.slots.SlotLiquidFilter;
import vswe.stevescarts.containers.slots.SlotLiquidManagerInput;
import vswe.stevescarts.containers.slots.SlotLiquidOutput;
import vswe.stevescarts.init.ModContainers;

import java.util.Objects;

public class ContainerLiquid extends ContainerBase
{
    public FluidStack[] oldLiquids;
    public SimpleContainerData data;
    private TileEntityLiquid tileEntityLiquid;
    private Player playerEntity;

    public ContainerLiquid(int id, Inventory playerInventory, FriendlyByteBuf packetBuffer)
    {
        this(id, playerInventory, (TileEntityLiquid) Objects.requireNonNull(Minecraft.getInstance().level.getBlockEntity(packetBuffer.readBlockPos())), new SimpleContainerData(13));
    }

    public ContainerLiquid(int id, Inventory playerInventory, TileEntityLiquid tileEntityLiquid, SimpleContainerData data)
    {
        super(ModContainers.CONTAINER_LIQUID.get(), id);
        this.playerEntity = playerInventory.player;
        oldLiquids = new FluidStack[4];
        this.tileEntityLiquid = tileEntityLiquid;
        this.data = data;
        for (int i = 0; i < 4; ++i)
        {
            final int x = i % 2;
            final int y = i / 2;
            addSlot(new SlotLiquidManagerInput(tileEntityLiquid, i, i * 3, (x == 0) ? 6 : 208, (y == 0) ? 17 : 80));
            addSlot(new SlotLiquidOutput(tileEntityLiquid, i * 3 + 1, (x == 0) ? 6 : 208, (y == 0) ? 42 : 105));
            addSlot(new SlotLiquidFilter(tileEntityLiquid, i * 3 + 2, (x == 0) ? 66 : 148, (y == 0) ? 12 : 110));
        }
        for (int k = 0; k < 3; ++k)
        {
            for (int j1 = 0; j1 < 9; ++j1)
            {
                Slot s = new Slot(playerInventory, j1 + k * 9 + 9, j1 * 18 + offsetX(), 104 + k * 18 + 36);
                addSlot(s);
            }
        }
        for (int l = 0; l < 9; ++l)
        {
            addSlot(new Slot(playerInventory, l, l * 18 + offsetX(), 198));
        }
        addDataSlots(data);
    }

    public int getLayoutType()
    {
        return data.get(0);
    }

    public int[] getColor()
    {
        return new int[]{data.get(1), data.get(2), data.get(3), data.get(4)};
    }

    public boolean[] toCart()
    {
        return new boolean[]{data.get(5) == 1, data.get(6) == 1, data.get(7) == 1, data.get(8) == 1};
    }

    public boolean[] doReturn()
    {
        return new boolean[]{data.get(9) == 1, data.get(10) == 1, data.get(11) == 1, data.get(12) == 1};
    }

    protected int offsetX()
    {
        return 35;
    }

    public TileEntityLiquid getTileEntityLiquid()
    {
        return tileEntityLiquid;
    }

    @Override
    public boolean stillValid(Player playerEntity)
    {
        return true;
    }
}
