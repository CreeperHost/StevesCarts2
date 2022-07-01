package vswe.stevescarts.modules.workers.tools;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraftforge.common.IPlantable;
import vswe.stevescarts.Constants;
import vswe.stevescarts.api.StevesCartsAPI;
import vswe.stevescarts.api.farms.ICropModule;
import vswe.stevescarts.client.guis.GuiMinecart;
import vswe.stevescarts.containers.slots.SlotBase;
import vswe.stevescarts.containers.slots.SlotSeed;
import vswe.stevescarts.entitys.EntityMinecartModular;
import vswe.stevescarts.helpers.Localization;
import vswe.stevescarts.api.modules.interfaces.ISuppliesModule;
import vswe.stevescarts.api.modules.ModuleBase;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public abstract class ModuleFarmer extends ModuleTool implements ISuppliesModule
{
    private ArrayList<ICropModule> plantModules;
    private int farming;
    private float farmAngle;
    private float rigAngle;
    private EntityDataAccessor<Boolean> IS_FARMING;

    public ModuleFarmer(final EntityMinecartModular cart)
    {
        super(cart);
        rigAngle = -3.926991f;
    }

    protected abstract int getRange();

    public int getExternalRange()
    {
        return getRange();
    }

    @Override
    public void init()
    {
        super.init();
        plantModules = new ArrayList<>();
        for (final ModuleBase module : getCart().getModules())
        {
            if (module instanceof ICropModule)
            {
                plantModules.add((ICropModule) module);
            }
        }
        plantModules.addAll(StevesCartsAPI.CROP_MODULES);
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
        drawString(matrixStack, gui, Localization.MODULES.TOOLS.FARMER.translate(), 8, 6, 4210752);
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
        return new SlotSeed(getCart(), this, slotId, 8 + x * 18, 28 + y * 18);
    }

    @Override
    public boolean work()
    {
        Level world = getCart().level;
        BlockPos next = getNextblock();
        for (int i = -getRange(); i <= getRange(); ++i)
        {
            for (int j = -getRange(); j <= getRange(); ++j)
            {
                BlockPos coord = next.offset(i, 0, j);
                if (farm(world, coord))
                {
                    return true;
                }
                if (till(world, coord))
                {
                    return true;
                }
                if (plant(world, coord))
                {
                    return true;
                }
            }
        }
        return false;
    }

    protected boolean till(Level world, BlockPos pos)
    {
        if (world.getBlockState(pos).is(BlockTags.DIRT) && world.getBlockState(pos.above()).isAir())
        {
            if (doPreWork())
            {
                startWorking(10);
                return true;
            }
            stopWorking();
            world.setBlock(pos, Blocks.FARMLAND.defaultBlockState(), 3);
        }
        return false;
    }

    protected boolean plant(Level world, BlockPos pos)
    {
        int hasSeeds = -1;
        BlockState soilState = world.getBlockState(pos);
        Block soilblock = soilState.getBlock();
        if (soilblock != null)
        {
            for (int i = 0; i < getInventorySize(); ++i)
            {
                if (!getStack(i).isEmpty() && isSeedValidHandler(getStack(i)))
                {
                    BlockState cropblock = getCropFromSeedHandler(getStack(i), world, pos);
                    if (cropblock != null && cropblock.getBlock() instanceof IPlantable && world.getBlockState(pos.above()).isAir() && soilblock.canSustainPlant(soilState, world, pos, Direction.UP, (IPlantable) cropblock.getBlock()))
                    {
                        hasSeeds = i;
                        break;
                    }
                }
            }
            if (hasSeeds != -1)
            {
                if (doPreWork())
                {
                    startWorking(25);
                    return true;
                }
                stopWorking();
                BlockState cropblock2 = getCropFromSeedHandler(getStack(hasSeeds), world, pos);
                world.setBlock(pos.above(), cropblock2, 3);
                ItemStack stack = getStack(hasSeeds);
                stack.shrink(1);
                if (getStack(hasSeeds).getCount() <= 0)
                {
                    setStack(hasSeeds, ItemStack.EMPTY);
                }
            }
        }
        return false;
    }

    protected boolean farm(Level world, BlockPos pos)
    {
        EntityMinecartModular cart = getCart();
        if (!isBroken())
        {
            pos = pos.above();
            BlockState blockState = world.getBlockState(pos);
            Block block = blockState.getBlock();
            if (isReadyToHarvestHandler(world, pos))
            {
                if (doPreWork())
                {
                    final int efficiency = (enchanter != null) ? enchanter.getEfficiencyLevel() : 0;
                    final int workingtime = (int) (getBaseFarmingTime() / Math.pow(1.2999999523162842, efficiency));
                    setFarming(workingtime * 4);
                    startWorking(workingtime);
                    return true;
                }
                stopWorking();
                List<ItemStack> stuff;

                final int fortune = (enchanter != null) ? enchanter.getFortuneLevel() : 0;
                stuff = block.getDrops(blockState, new LootContext.Builder((ServerLevel) world).withParameter(LootContextParams.TOOL, ItemStack.EMPTY).withParameter(LootContextParams.ORIGIN, getCart().position()));
                for (@Nonnull ItemStack iStack : stuff)
                {
                    cart.addItemToChest(iStack);
                    if (iStack.getCount() != 0)
                    {
                        final ItemEntity entityitem = new ItemEntity(world, cart.x(), cart.y(), cart.z(), iStack);
                        world.addFreshEntity(entityitem);
                    }
                }
                world.removeBlock(pos, false);
                damageTool(3);
            }
        }
        return false;
    }

    protected int getBaseFarmingTime()
    {
        return 25;
    }

    public boolean isSeedValidHandler(@Nonnull ItemStack seed)
    {
        return seed.is(Constants.SEEDS);
    }

    protected BlockState getCropFromSeedHandler(@Nonnull ItemStack seed, Level world, BlockPos pos)
    {
        Block cropBlock = Block.byItem(seed.getItem());
        if (cropBlock == null) return null;
        if (cropBlock instanceof CropBlock)
        {
            CropBlock cropsBlock = (CropBlock) cropBlock;
            return cropsBlock.defaultBlockState();

        }
        return null;
    }

    protected boolean isReadyToHarvestHandler(Level world, BlockPos pos)
    {
        if (world.getBlockState(pos) != null && world.getBlockState(pos).getBlock() instanceof CropBlock)
        {
            CropBlock cropsBlock = (CropBlock) world.getBlockState(pos).getBlock();
            return cropsBlock.isMaxAge(world.getBlockState(pos));
        }
        return false;
    }

    public float getFarmAngle()
    {
        return farmAngle;
    }

    public float getRigAngle()
    {
        return rigAngle;
    }

    @Override
    public void initDw()
    {
        super.initDw();
        IS_FARMING = createDw(EntityDataSerializers.BOOLEAN);
        registerDw(IS_FARMING, false);
    }

    @Override
    public int numberOfDataWatchers()
    {
        return 1 + super.numberOfDataWatchers();
    }

    private void setFarming(final int val)
    {
        farming = val;
        updateDw(IS_FARMING, val > 0);
    }

    protected boolean isFarming()
    {
        if (isPlaceholder())
        {
            return getSimInfo().getIsFarming();
        }
        return getCart().isEngineBurning() && getDw(IS_FARMING);
    }

    @Override
    public void update()
    {
        super.update();
        if (!getCart().level.isClientSide)
        {
            setFarming(farming - 1);
        }
        else
        {
            final float up = -3.926991f;
            final float down = -3.1415927f;
            final boolean flag = isFarming();
            if (flag)
            {
                if (rigAngle < down)
                {
                    rigAngle += 0.1f;
                    if (rigAngle > down)
                    {
                        rigAngle = down;
                    }
                }
                else
                {
                    farmAngle = (float) ((farmAngle + 0.15f) % 6.283185307179586);
                }
            }
            else if (rigAngle > up)
            {
                rigAngle -= 0.075f;
                if (rigAngle < up)
                {
                    rigAngle = up;
                }
            }
        }
    }

    @Override
    public boolean haveSupplies()
    {
        for (int i = 0; i < getInventorySize(); ++i)
        {
            @Nonnull ItemStack item = getStack(i);
            if (!item.isEmpty() && isSeedValidHandler(item))
            {
                return true;
            }
        }
        return false;
    }
}
