package vswe.stevescarts.modules.workers;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.block.AbstractRailBlock;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import vswe.stevescarts.client.guis.GuiMinecart;
import vswe.stevescarts.containers.slots.SlotBase;
import vswe.stevescarts.containers.slots.SlotBuilder;
import vswe.stevescarts.entitys.EntityMinecartModular;
import vswe.stevescarts.helpers.Localization;
import vswe.stevescarts.modules.ISuppliesModule;

import java.util.ArrayList;

public class ModuleRailer extends ModuleWorker implements ISuppliesModule
{
    private boolean hasGeneratedAngles;
    private float[] railAngles;
    private DataParameter<Byte> RAILS;

    public ModuleRailer(final EntityMinecartModular cart)
    {
        super(cart);
        hasGeneratedAngles = false;
    }

    @Override
    public boolean hasGui()
    {
        return true;
    }

    @Override
    protected SlotBase getSlot(final int slotId, final int x, final int y)
    {
        return new SlotBuilder(getCart(), slotId, 8 + x * 18, 23 + y * 18);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void drawForeground(MatrixStack matrixStack, GuiMinecart gui)
    {
        drawString(matrixStack, gui, Localization.MODULES.ATTACHMENTS.RAILER.translate(), 8, 6, 4210752);
    }

    @Override
    public byte getWorkPriority()
    {
        return 100;
    }

    @Override
    public boolean work()
    {
        World world = getCart().level;
        BlockPos next = getNextblock();
        int x = next.getX();
        int y = next.getY();
        int z = next.getZ();
        final ArrayList<Integer[]> pos = getValidRailPositions(x, y, z);
        if (doPreWork())
        {
            boolean valid = false;
            for (int i = 0; i < pos.size(); ++i)
            {
                if (tryPlaceTrack(pos.get(i)[0], pos.get(i)[1], pos.get(i)[2], false))
                {
                    valid = true;
                    break;
                }
            }
            if (valid)
            {
                startWorking(12);
            }
            else
            {
                boolean front = false;
                for (int j = 0; j < pos.size(); ++j)
                {
                    if (AbstractRailBlock.isRail(world, new BlockPos(pos.get(j)[0], pos.get(j)[1], pos.get(j)[2])))
                    {
                        front = true;
                        break;
                    }
                }
                if (!front)
                {
                    turnback();
                }
            }
            return true;
        }
        stopWorking();
        for (int k = 0; k < pos.size() && !tryPlaceTrack(pos.get(k)[0], pos.get(k)[1], pos.get(k)[2], true); ++k)
        {
        }
        return false;
    }

    protected ArrayList<Integer[]> getValidRailPositions(int x, int y, int z)
    {
        ArrayList<Integer[]> lst = new ArrayList<>();
        if (y >= getCart().y())
        {
            lst.add(new Integer[]{x, y + 1, z});
        }
        lst.add(new Integer[]{x, y, z});
        lst.add(new Integer[]{x, y - 1, z});
        return lst;
    }

    protected boolean validRail(Item item)
    {
        return ItemTags.RAILS.contains(item);
    }

    private boolean tryPlaceTrack(int i, int j, int k, boolean flag)
    {
        BlockPos blockPos = new BlockPos(i, j, k);
        if (isValidForTrack(blockPos, true))
        {
            for (int l = 0; l < getInventorySize(); ++l)
            {
                if (!getStack(l).isEmpty() && validRail(getStack(l).getItem()))
                {
                    ItemStack stack = getStack(l);
                    if (flag)
                    {
                        if (getCart().level.setBlock(new BlockPos(i, j, k), Block.byItem(stack.getItem()).defaultBlockState(), 3))
                        {
                            if (!getCart().hasCreativeSupplies())
                            {
                                stack.shrink(1);
                                if (getStack(l).getCount() == 0)
                                {
                                    setStack(l, ItemStack.EMPTY);
                                }
                                getCart().setChanged();
                            }
                        }
                    }
                    return true;
                }
            }
            turnback();
            return true;
        }
        return false;
    }

    @Override
    public void initDw()
    {
        RAILS = createDw(DataSerializers.BYTE);
        registerDw(RAILS, (byte) 0);
    }

    @Override
    public int numberOfDataWatchers()
    {
        return 1;
    }

    @Override
    public void onInventoryChanged()
    {
        super.onInventoryChanged();
        calculateRails();
    }

    private void calculateRails()
    {
        if (getCart().level.isClientSide)
        {
            return;
        }
        byte valid = 0;
        for (int i = 0; i < getInventorySize(); ++i)
        {
            if (!getStack(i).isEmpty() && validRail(getStack(i).getItem()))
            {
                ++valid;
            }
        }
        updateDw(RAILS, valid);
    }

    public int getRails()
    {
        if (isPlaceholder())
        {
            return getSimInfo().getRailCount();
        }
        return getDw(RAILS);
    }

    public float getRailAngle(final int i)
    {
        if (!hasGeneratedAngles)
        {
            railAngles = new float[getInventorySize()];
            for (int j = 0; j < getInventorySize(); ++j)
            {
                railAngles[j] = getCart().random.nextFloat() / 2.0f - 0.25f;
            }
            hasGeneratedAngles = true;
        }
        return railAngles[i];
    }

    @Override
    protected void Load(CompoundNBT tagCompound, final int id)
    {
        calculateRails();
    }

    @Override
    public boolean haveSupplies()
    {
        for (int i = 0; i < getInventorySize(); ++i)
        {
            ItemStack item = getStack(i);
            if (!item.isEmpty() && validRail(item.getItem()))
            {
                return true;
            }
        }
        return false;
    }
}
