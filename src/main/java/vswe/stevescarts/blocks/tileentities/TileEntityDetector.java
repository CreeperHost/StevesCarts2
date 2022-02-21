package vswe.stevescarts.blocks.tileentities;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.Container;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerListener;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import vswe.stevescarts.blocks.BlockDetector;
import vswe.stevescarts.containers.ContainerDetector;
import vswe.stevescarts.entitys.EntityMinecartModular;
import vswe.stevescarts.helpers.DetectorType;
import vswe.stevescarts.helpers.LogicObject;
import vswe.stevescarts.init.ModBlocks;

import javax.annotation.Nullable;

public class TileEntityDetector extends TileEntityBase implements MenuProvider
{
    public LogicObject mainObj;
    private int activeTimer;
    private short oldData;
    private boolean hasOldData;

    public TileEntityDetector(BlockPos blockPos, BlockState blockState)
    {
        super(ModBlocks.DETECTOR_TILE.get(), blockPos, blockState);
        activeTimer = 20;
        mainObj = new LogicObject((byte) 1, (byte) 0);
    }

    @Override
    public void load(CompoundTag nbttagcompound)
    {
        super.load(nbttagcompound);
        final byte count = nbttagcompound.getByte("LogicObjectCount");
        for (int i = 0; i < count; ++i)
        {
            loadLogicObjectFromInteger(nbttagcompound.getInt("LogicObject" + i));
        }
    }

    @Override
    public CompoundTag save(CompoundTag nbttagcompound)
    {
        super.save(nbttagcompound);
        final int count = saveLogicObject(nbttagcompound, mainObj, 0, false);
        nbttagcompound.putByte("LogicObjectCount", (byte) count);
        return nbttagcompound;
    }

    private int saveLogicObject(final CompoundTag nbttagcompound, final LogicObject obj, int id, final boolean saveMe)
    {
        if (saveMe)
        {
            nbttagcompound.putInt("LogicObject" + id++, saveLogicObjectToInteger(obj));
        }
        for (final LogicObject child : obj.getChilds())
        {
            id = saveLogicObject(nbttagcompound, child, id, true);
        }
        return id;
    }

    private int saveLogicObjectToInteger(final LogicObject obj)
    {
        int returnVal = 0;
        returnVal |= obj.getId() << 24;
        returnVal |= obj.getParent().getId() << 16;
        returnVal |= obj.getExtra() << 8;
        returnVal |= obj.getData() << 0;
        return returnVal;
    }

    private void loadLogicObjectFromInteger(final int val)
    {
        final byte id = (byte) (val >> 24 & 0xFF);
        final byte parent = (byte) (val >> 16 & 0xFF);
        final byte extra = (byte) (val >> 8 & 0xFF);
        final byte data = (byte) (val >> 0 & 0xFF);
        createObject(id, parent, extra, data);
    }

    @Override
    public void tick()
    {
        if (activeTimer > 0 && --activeTimer == 0)
        {
            BlockState blockState = level.getBlockState(getBlockPos());
            Block block = blockState.getBlock();
            if (block instanceof BlockDetector)
            {
                DetectorType.getTypeFromSate(blockState).deactivate(this);
                level.setBlock(getBlockPos(), blockState.setValue(BlockDetector.POWERED, false), 3);
            }
        }
    }

    public void receivePacket(final int id, final byte[] data, final Player player)
    {
        if (id == 0)
        {
            byte lowestId = -1;
            for (int i = 0; i < 128; ++i)
            {
                if (!isIdOccupied(mainObj, i))
                {
                    lowestId = (byte) i;
                    break;
                }
            }
            if (lowestId == -1)
            {
                return;
            }
            createObject(lowestId, data[0], data[1], data[2]);
        }
        else if (id == 1)
        {
            removeObject(mainObj, data[0]);
        }
    }

    private void createObject(final byte id, final byte parentId, final byte extra, final byte data)
    {
        final LogicObject newObject = new LogicObject(id, extra, data);
        final LogicObject parent = getObjectFromId(mainObj, parentId);
        if (parent != null)
        {
            newObject.setParent(parent);
        }
    }

    private LogicObject getObjectFromId(final LogicObject object, final int id)
    {
        if (object.getId() == id)
        {
            return object;
        }
        for (final LogicObject child : object.getChilds())
        {
            final LogicObject result = getObjectFromId(child, id);
            if (result != null)
            {
                return result;
            }
        }
        return null;
    }

    private boolean removeObject(final LogicObject object, final int idToRemove)
    {
        if (object.getId() == idToRemove)
        {
            object.setParent(null);
            return true;
        }
        for (final LogicObject child : object.getChilds())
        {
            if (removeObject(child, idToRemove))
            {
                return true;
            }
        }
        return false;
    }

    private boolean isIdOccupied(final LogicObject object, final int id)
    {
        if (object.getId() == id)
        {
            return true;
        }
        for (final LogicObject child : object.getChilds())
        {
            if (isIdOccupied(child, id))
            {
                return true;
            }
        }
        return false;
    }

    //	@Override
    //	public void initGuiData(final Container con, final IContainerListener crafting) {
    //	}
    //
    //	@Override
    //	public void checkGuiData(final Container con, final IContainerListener crafting) {
    //		sendUpdatedLogicObjects(con, crafting, mainObj, ((ContainerDetector) con).mainObj);
    //	}

    private void sendUpdatedLogicObjects(final Container con, final ContainerListener crafting, final LogicObject real, LogicObject cache)
    {
        if (!real.equals(cache))
        {
            final LogicObject parent = cache.getParent();
            cache.setParent(null);
            final LogicObject clone = real.copy(parent);
            removeLogicObject(con, crafting, cache);
            sendLogicObject(con, crafting, clone);
            cache = clone;
        }
        while (real.getChilds().size() > cache.getChilds().size())
        {
            final int i = cache.getChilds().size();
            final LogicObject clone = real.getChilds().get(i).copy(cache);
            sendLogicObject(con, crafting, clone);
        }
        while (real.getChilds().size() < cache.getChilds().size())
        {
            final int i = real.getChilds().size();
            final LogicObject toBeRemoved = cache.getChilds().get(i);
            toBeRemoved.setParent(null);
            removeLogicObject(con, crafting, toBeRemoved);
        }
        for (int i = 0; i < real.getChilds().size(); ++i)
        {
            sendUpdatedLogicObjects(con, crafting, real.getChilds().get(i), cache.getChilds().get(i));
        }
    }

    private void sendAllLogicObjects(final Container con, final ContainerListener crafting, final LogicObject obj)
    {
        sendLogicObject(con, crafting, obj);
        for (final LogicObject child : obj.getChilds())
        {
            sendAllLogicObjects(con, crafting, child);
        }
    }

    private void sendLogicObject(final Container con, final ContainerListener crafting, final LogicObject obj)
    {
        if (obj.getParent() == null)
        {
            return;
        }
        final short data = (short) (obj.getId() << 8 | obj.getParent().getId());
        final short data2 = (short) (obj.getExtra() << 8 | obj.getData());
    }

    private void removeLogicObject(final Container con, final ContainerListener crafting, final LogicObject obj) {}

    public void recalculateTree()
    {
        mainObj.generatePosition(5, 60, 245, 0);
    }

    public boolean evaluate(final EntityMinecartModular cart, final int depth)
    {
        return mainObj.evaluateLogicTree(this, cart, depth);
    }

    public void handleCart(final EntityMinecartModular cart)
    {
        final boolean truthValue = evaluate(cart, 0);
        BlockState blockState = level.getBlockState(getBlockPos());
        boolean isOn = blockState.getValue(BlockDetector.POWERED);
        boolean power = false;
        if (truthValue)
        {
            DetectorType.getTypeFromSate(blockState).activate(this, cart);
            power = true;
        }
        else
        {
            DetectorType.getTypeFromSate(blockState).deactivate(this);
            power &= false;
        }
        if (power != isOn)
        {
            level.setBlock(getBlockPos(), blockState.setValue(BlockDetector.POWERED, power), 3);
        }

        if (truthValue)
        {
            activeTimer = 20;
        }
    }

    @Override
    public Component getDisplayName()
    {
        return new TextComponent("container.detector");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory playerInventory, Player playerEntity)
    {
        return new ContainerDetector(id, playerInventory, this, new SimpleContainerData(0));
    }

    public DetectorType getDetectorType()
    {
        return level.getBlockState(getBlockPos()).getValue(BlockDetector.SATE);
    }
}
