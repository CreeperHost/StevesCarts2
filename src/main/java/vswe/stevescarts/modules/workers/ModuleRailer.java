package vswe.stevescarts.modules.workers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.creeperhost.polylib.helpers.LevelHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RailBlock;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.FakePlayer;
import vswe.stevescarts.client.guis.GuiMinecart;
import vswe.stevescarts.containers.slots.SlotBase;
import vswe.stevescarts.containers.slots.SlotBuilder;
import vswe.stevescarts.entitys.EntityMinecartModular;
import vswe.stevescarts.helpers.Localization;
import vswe.stevescarts.api.modules.interfaces.ISuppliesModule;

import java.util.ArrayList;

public class ModuleRailer extends ModuleWorker implements ISuppliesModule
{
    private boolean hasGeneratedAngles;
    private float[] railAngles;
    private EntityDataAccessor<Byte> RAILS;

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
    public void drawForeground(PoseStack matrixStack, GuiMinecart gui)
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
        Level world = getCart().level;
        BlockPos next = getNextblock();
        int x = next.getX();
        int y = next.getY();
        int z = next.getZ();
        final ArrayList<Integer[]> pos = getValidRailPositions(x, y, z);
        if (doPreWork())
        {
            boolean valid = false;
            for (Integer[] po : pos)
            {
                if (tryPlaceTrack(po[0], po[1], po[2]))
                {
                    valid = true;
                    break;
                }
            }
            if (valid)
            {
                startWorking(12);
            }
            return true;
        }
        stopWorking();
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
        ItemStack stack = new ItemStack(item);
        return stack.is(ItemTags.RAILS);
    }

    private boolean tryPlaceTrack(int i, int j, int k)
    {
        BlockPos blockPos = new BlockPos(i, j, k);
        FakePlayer fakePlayer = getFakePlayer();
        if(RailBlock.canSupportRigidBlock(getCart().getLevel(), blockPos) && (!RailBlock.isRail(getCart().level, blockPos.above()) && LevelHelper.isAir(getCart().level, blockPos.above())))
        {
            for (int l = 0; l < getInventorySize(); l++)
            {
                if(!getStack(l).isEmpty() && validRail(getStack(l).getItem()))
                {
                    ItemStack stack = getStack(l);
                    if(fakePlayer.mayUseItemAt(blockPos, Direction.DOWN, stack))
                    {
                        Block block = Block.byItem(stack.getItem());
                        boolean placed = getCart().level.setBlock(blockPos.above(), block.defaultBlockState(), 3);
                        if(placed)
                        {
                            if (!getCart().hasCreativeSupplies())
                            {
                                stack.shrink(1);
                                getCart().setChanged();
                            }
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    @Override
    public void initDw()
    {
        RAILS = createDw(EntityDataSerializers.BYTE);
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
    protected void Load(CompoundTag tagCompound, final int id)
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
