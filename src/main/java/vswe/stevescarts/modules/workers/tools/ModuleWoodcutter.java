package vswe.stevescarts.modules.workers.tools;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraftforge.common.util.FakePlayer;
import vswe.stevescarts.api.StevesCartsAPI;
import vswe.stevescarts.api.farms.ITreeModule;
import vswe.stevescarts.client.guis.GuiMinecart;
import vswe.stevescarts.containers.slots.SlotBase;
import vswe.stevescarts.containers.slots.SlotSapling;
import vswe.stevescarts.entities.EntityMinecartModular;
import vswe.stevescarts.helpers.BlockPosHelpers;
import vswe.stevescarts.helpers.Localization;
import vswe.stevescarts.api.modules.interfaces.ISuppliesModule;
import vswe.stevescarts.api.modules.ModuleBase;
import vswe.stevescarts.modules.addons.plants.ModulePlantSize;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public abstract class ModuleWoodcutter extends ModuleTool implements ISuppliesModule
{
    private ArrayList<ITreeModule> treeModules;
    private ModulePlantSize plantSize;
    private boolean isPlanting;
    private float cutterAngle;
    private EntityDataAccessor<Boolean> IS_CUTTING;

    public ModuleWoodcutter(final EntityMinecartModular cart)
    {
        super(cart);
        cutterAngle = 0.7853982f;
    }

    @Override
    public byte getWorkPriority()
    {
        return 80;
    }

    @Override
    public boolean hasGui()
    {
        return true;
    }

    @Override
    public void drawForeground(PoseStack matrixStack, GuiMinecart gui)
    {
        drawString(matrixStack, gui, Localization.MODULES.TOOLS.CUTTER.translate(), 8, 6, 4210752);
    }

    @Override
    protected int getInventoryWidth()
    {
        return super.getInventoryWidth() + 3;
    }

    @Override
    protected SlotBase getSlot(final int slotId, int x, final int y)
    {
        if (x == 0)
        {
            return super.getSlot(slotId, x, y);
        }
        --x;
        return new SlotSapling(getCart(), this, slotId, 8 + x * 18, 28 + y * 18);
    }

    @Override
    public boolean useDurability()
    {
        return true;
    }

    @Override
    public void init()
    {
        super.init();
        treeModules = new ArrayList<>();
        for (final ModuleBase module : getCart().getModules())
        {
            if (module instanceof ITreeModule)
            {
                treeModules.add((ITreeModule) module);
            }
            else
            {
                if (!(module instanceof ModulePlantSize))
                {
                    continue;
                }
                plantSize = (ModulePlantSize) module;
            }
        }
        treeModules.addAll(StevesCartsAPI.TREE_MODULES);
    }

    public abstract int getPercentageDropChance();

    public NonNullList<ItemStack> getTierDrop(List<ItemStack> baseItems)
    {
        NonNullList<ItemStack> nerfedItems = NonNullList.create();
        for (@Nonnull ItemStack item : baseItems)
        {
            if (!item.isEmpty())
            {
                dropItemByMultiplierChance(nerfedItems, item, getPercentageDropChance());
            }
        }
        return nerfedItems;
    }

    private void dropItemByMultiplierChance(List<ItemStack> items, @Nonnull ItemStack item, int percentage)
    {
        while (percentage > 0)
        {
            if (getCart().random.nextInt(100) < percentage)
            {
                items.add(item.copy());
            }
            percentage -= 100;
        }
    }

    @Override
    public boolean work()
    {
        Level world = getCart().level;
        BlockPos next = getNextblock();
        final int size = getPlantSize();
        destroyLeaveBlockOnTrack(world, next);
        destroyLeaveBlockOnTrack(world, next.above());
        for (int i = -size; i <= size; ++i)
        {
            if (i != 0)
            {
                int i2 = i;
                if (i2 < 0)
                {
                    i2 = -size - i2 - 1;
                }
                BlockPos plant = next.offset(((getCart().z() != next.getZ()) ? i2 : 0), -1, ((getCart().x() != next.getX()) ? i2 : 0));
                if (plant(size, plant, next.getX(), next.getZ()))
                {
                    setCutting(false);
                    return true;
                }
            }
        }
        if (!isPlanting)
        {
            for (int i = -1; i <= 1; ++i)
            {
                for (int j = -1; j <= 1; ++j)
                {
                    BlockPos farm = next.offset(i, -1, j);
                    if (farm(world, farm))
                    {
                        setCutting(true);
                        return true;
                    }
                }
            }
        }
        setCutting(isPlanting = false);
        return false;
    }

    private boolean plant(final int size, BlockPos pos, final int cx, final int cz)
    {
        if (size == 1)
        {
            if ((pos.getX() + pos.getZ()) % 2 == 0)
            {
                return false;
            }
        }
        else if ((pos.getX() == cx && pos.getX() / size % 2 == 0) || (pos.getZ() == cz && pos.getZ() / size % 2 == 0))
        {
            return false;
        }
        @Nonnull ItemStack sapling = ItemStack.EMPTY;
        for (int i = 0; i < getInventorySize(); ++i)
        {
            final SlotBase slot = getSlots().get(i);
            if (slot.containsValidItem())
            {
                sapling = getStack(i);
                break;
            }
        }
        if (!sapling.isEmpty())
        {
            if (doPreWork())
            {
                if (isSaplingHandler(sapling))
                {
                    if (plantSapling(getCart().level, pos, sapling, getFakePlayer()))
                    {
                        sapling.shrink(1);
                        startWorking(25);
                        return isPlanting = true;
                    }
                }
                stopWorking();
                isPlanting = false;
            }
            else
            {
                stopWorking();
                isPlanting = false;
            }
        }
        return false;
    }

    public boolean plantSapling(Level world, BlockPos pos, ItemStack stack, FakePlayer fakePlayer)
    {
        if (stack.isEmpty())
        {
            return false;
        }
        Block block = Block.byItem(stack.getItem());
        if (block instanceof SaplingBlock blockSapling)
        {
            if (blockSapling.canSurvive(blockSapling.defaultBlockState(), world, pos.above()))
            {
                if (fakePlayer.mayUseItemAt(pos.above(), Direction.UP, stack))
                {
                    if (world.getBlockState(pos.above()).isAir())
                    {
                        world.setBlock(pos.above(), blockSapling.defaultBlockState(), 3);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean farm(Level world, BlockPos pos)
    {
        if (!isBroken())
        {
            pos = pos.above();
            BlockState state = world.getBlockState(pos);
            if (isWoodHandler(state, pos))
            {
                final ArrayList<BlockPos> checked = new ArrayList<>();
                if (removeAt(world, pos, checked))
                {
                    return true;
                }
                stopWorking();
            }
        }
        return false;
    }

    private boolean removeAt(Level world, BlockPos here, final ArrayList<BlockPos> checked)
    {
        checked.add(here);
        BlockState blockState = world.getBlockState(here);
        final Block block = blockState.getBlock();
        if (world.isEmptyBlock(here))
        {
            return false;
        }
        if (checked.size() < 125 && BlockPosHelpers.getHorizontalDistToCartSquared(here, getCart()) < 175.0)
        {
            for (int type = 0; type < 2; ++type)
            {
                boolean hitWood = false;
                if (isLeavesHandler(blockState, here))
                {
                    type = 1;
                }
                else if (type == 1)
                {
                    hitWood = true;
                }
                for (int x = -1; x <= 1; ++x)
                {
                    for (int y = 1; y >= 0; --y)
                    {
                        for (int z = -1; z <= 1; ++z)
                        {
                            BlockPos pos = here.offset(x, y, z);
                            BlockState currentState = world.getBlockState(pos);
                            if (!currentState.isAir())
                            {
                                if (hitWood)
                                {
                                    if (!isWoodHandler(currentState, pos))
                                    {
                                        continue;
                                    }
                                }
                                else if (!isLeavesHandler(currentState, pos))
                                {
                                    continue;
                                }
                                if (!checked.contains(pos))
                                {
                                    return removeAt(world, pos, checked);
                                }
                            }
                        }
                    }
                }
            }
        }
        List<ItemStack> stuff;
        final int fortune = (enchanter != null) ? enchanter.getFortuneLevel() : 0;
        stuff = block.getDrops(blockState, new LootContext.Builder((ServerLevel) world).withParameter(LootContextParams.TOOL, ItemStack.EMPTY).withParameter(LootContextParams.ORIGIN, getCart().position()));
        List<ItemStack> dropList = new ArrayList<>();
        for (ItemStack drop : dropList)
        { //Here to filter out any bad itemstacks, the mod I was testing with returned stacks with a size of 0
            if (!drop.isEmpty() && drop.getCount() > 0)
            {
                stuff.add(drop);
            }
        }

        int applerand = 200;
        if (fortune > 0)
        {
            applerand -= 10 << fortune;
            if (applerand < 40)
            {
                applerand = 40;
            }
        }

        List<ItemStack> nerfedstuff = getTierDrop(stuff);
        boolean first = true;
        for (@Nonnull ItemStack iStack : nerfedstuff)
        {
            getCart().addItemToChest(iStack);
            if (iStack.getCount() != 0)
            {
                if (first)
                {
                    return false;
                }
                final ItemEntity entityitem = new ItemEntity(world, getCart().x(), getCart().y(), getCart().z(), iStack);
                world.addFreshEntity(entityitem);
            }
            first = false;
        }
        world.removeBlock(here, false);
        int basetime;
        if (isLeavesHandler(blockState, here))
        {
            basetime = 2;
            damageTool(1);
        }
        else
        {
            basetime = 25;
            damageTool(5);
        }
        final int efficiency = (enchanter != null) ? enchanter.getEfficiencyLevel() : 0;
        startWorking((int) (basetime / Math.pow(1.2999999523162842, efficiency)));
        return true;
    }

    @Override
    public void initDw()
    {
        super.initDw();
        IS_CUTTING = createDw(EntityDataSerializers.BOOLEAN);
        registerDw(IS_CUTTING, false);
    }

    @Override
    public int numberOfDataWatchers()
    {
        return 1 + super.numberOfDataWatchers();
    }

    private void setCutting(final boolean val)
    {
        updateDw(IS_CUTTING, val);
    }

    protected boolean isCutting()
    {
        if (isPlaceholder())
        {
            return getSimInfo().getIsCutting();
        }
        return getDw(IS_CUTTING);
    }

    public float getCutterAngle()
    {
        return cutterAngle;
    }

    @Override
    public void update()
    {
        super.update();
        final boolean cuttingflag = isCutting();
        if (cuttingflag || cutterAngle != 0.7853982f)
        {
            boolean flag = false;
            if (!cuttingflag && cutterAngle < 0.7853982f)
            {
                flag = true;
            }
            cutterAngle = (float) ((cutterAngle + 0.9f) % 6.283185307179586);
            if (!cuttingflag && cutterAngle > 0.7853982f && flag)
            {
                cutterAngle = 0.7853982f;
            }
        }
    }

    @Override
    public boolean haveSupplies()
    {
        for (int i = 0; i < getInventorySize(); ++i)
        {
            if (getSlots().get(i).containsValidItem())
            {
                return true;
            }
        }
        return false;
    }

    public boolean isLeavesHandler(BlockState blockState, BlockPos pos)
    {
        return blockState.is(BlockTags.LEAVES);
    }

    public boolean isWoodHandler(BlockState blockState, BlockPos pos)
    {
        return blockState.is(BlockTags.LOGS);
    }

    public boolean isSaplingHandler(@Nonnull ItemStack sapling)
    {
        return sapling.is(ItemTags.SAPLINGS);
    }

    private int getPlantSize()
    {
        if (plantSize != null)
        {
            return plantSize.getSize();
        }
        return 1;
    }

    private void destroyLeaveBlockOnTrack(Level world, BlockPos pos)
    {
        BlockState state = world.getBlockState(pos);
        if (isLeavesHandler(state, pos))
        {
            world.removeBlock(pos, false);
        }
    }
}
