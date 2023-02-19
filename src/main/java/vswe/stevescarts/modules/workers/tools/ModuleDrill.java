package vswe.stevescarts.modules.workers.tools;

import com.mojang.blaze3d.vertex.PoseStack;
import net.creeperhost.polylib.helpers.LevelHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fluids.IFluidBlock;
import vswe.stevescarts.SCConfig;
import vswe.stevescarts.client.guis.GuiMinecart;
import vswe.stevescarts.entities.EntityMinecartModular;
import vswe.stevescarts.helpers.Localization;
import vswe.stevescarts.helpers.ResourceHelper;
import vswe.stevescarts.init.ModBlocks;
import vswe.stevescarts.api.modules.interfaces.IActivatorModule;
import vswe.stevescarts.api.modules.ModuleBase;
import vswe.stevescarts.modules.addons.*;
import vswe.stevescarts.api.modules.template.ModuleChest;

import javax.annotation.Nonnull;
import java.util.List;

public abstract class ModuleDrill extends ModuleTool implements IActivatorModule
{
    private ModuleDrillIntelligence intelligence;
    private ModuleLiquidSensors liquidsensors;
    private ModuleOreTracker tracker;
    private boolean hasHeightController;
    private byte sensorLight;
    private float drillRotation;
    private int miningCoolDown;
    private int[] buttonRect;
    private boolean setup;
    private EntityDataAccessor<Boolean> IS_MINING;
    private EntityDataAccessor<Boolean> IS_ENABLED;

    public ModuleDrill(final EntityMinecartModular cart)
    {
        super(cart);
        sensorLight = 1;
        buttonRect = new int[]{15, 30, 24, 12};
    }

    @Override
    public byte getWorkPriority()
    {
        return 50;
    }

    @Override
    public void init()
    {
        super.init();
        for (final ModuleBase module : getCart().getModules())
        {
            if (module instanceof ModuleDrillIntelligence)
            {
                intelligence = (ModuleDrillIntelligence) module;
            }
            if (module instanceof ModuleLiquidSensors)
            {
                liquidsensors = (ModuleLiquidSensors) module;
            }
            if (module instanceof ModuleOreTracker)
            {
                tracker = (ModuleOreTracker) module;
            }
            if (module instanceof ModuleHeightControl)
            {
                hasHeightController = true;
            }
        }
    }

    @Override
    public boolean work()
    {
        Level world = getCart().level;
        if (!isDrillEnabled())
        {
            stopDrill();
            stopWorking();
            return false;
        }
        else if (!doPreWork())
        {
            stopDrill();
            stopWorking();
        }
        if (isBroken())
        {
            return false;
        }
        BlockPos next = getNextblock();
        int[] range = mineRange();
        for (int holeY = range[1]; holeY >= range[0]; holeY--)
        {
            for (int holeX = -blocksOnSide(); holeX <= blocksOnSide(); holeX++)
            {
                if (isMiningSpotAllowed(next, holeX, holeY, range))
                {
                    BlockPos mine = next.offset(((getCart().z() != next.getZ()) ? holeX : 0), holeY, ((getCart().x() != next.getX()) ? holeX : 0));
                    if (mineBlockAndRevive(world, mine, next, holeX, holeY)) {
                        return true;
                    }
                }
            }
        }

        BlockPos pos = next.offset(0, range[0], 0);
        if (LevelHelper.isAir(getCart().level, pos) && !isValidForTrack(pos, true) && mineBlockAndRevive(world, pos.below(), next, 0, range[0] - 1))
        {
            return true;
        }
        stopWorking();
        stopDrill();
        return false;
    }

    private boolean isMiningSpotAllowed(BlockPos next, int holeX, int holeY, int[] range)
    {
        int maxHeight = SCConfig.drillSize.get() * 2 + 1 - (hasHeightController ? range[2] == 0 ? -1 : 1 : 0);
        if (Math.abs(holeX) <= SCConfig.drillSize.get() && holeY <= maxHeight)
        {
            return intelligence == null || intelligence.isActive(holeX + blocksOnSide(), holeY, range[2], next.getX() > getCart().x() || next.getZ() < getCart().z());
        }
        return false;
    }

    private int[] mineRange()
    {
        BlockPos next = getNextblock();
        int yTarget = getCart().getYTarget();
        if (BaseRailBlock.isRail(getCart().level, next) || BaseRailBlock.isRail(getCart().level, next.below()))
        {
            return new int[]{0, blocksOnTop() - 1, 1};
        }
        if (next.getY() > yTarget)
        {
            return new int[]{-1, blocksOnTop() - 1, 1};
        }
        if (next.getY() < yTarget)
        {
            return new int[]{1, blocksOnTop() + 1, 0};
        }
        return new int[]{0, blocksOnTop() - 1, 1};
    }

    protected abstract int blocksOnTop();

    protected abstract int blocksOnSide();

    public int getAreaWidth()
    {
        return blocksOnSide() * 2 + 1;
    }

    public int getAreaHeight()
    {
        return blocksOnTop();
    }

    private boolean mineBlockAndRevive(Level world, BlockPos coord, BlockPos next, final int holeX, final int holeY)
    {
        if (mineBlock(world, coord, next, holeX, holeY, false))
        {
            return true;
        }
        else if (isDead())
        {
            revive();
            return true;
        }
        return false;
    }

    protected boolean mineBlock(Level world, BlockPos coord, BlockPos next, final int holeX, final int holeY, final boolean flag)
    {
        if (tracker != null)
        {
            final BlockPos target = tracker.findBlockToMine(this, coord);
            if (target != null)
            {
                coord = target;
            }
        }
        final Object valid = isValidBlock(world, coord, holeX, holeY, flag);
        BlockEntity storage = null;
        if (valid instanceof BlockEntity)
        {
            storage = (BlockEntity) valid;
        }
        else if (valid == null)
        {
            return false;
        }
        BlockState blockState = world.getBlockState(coord);
        final Block block = blockState.getBlock();
        float h = blockState.getDestroySpeed(world, coord);
        if (h < 0.0f)
        {
            h = 0.0f;
        }
        if (storage != null)
        {
            for (int i = 0; i < ((Container) storage).getContainerSize(); ++i)
            {
                ItemStack iStack = ((Container) storage).getItem(i);
                if (!iStack.isEmpty())
                {
                    if (!minedItem(world, iStack, next))
                    {
                        return false;
                    }
                    ((Container) storage).setItem(i, ItemStack.EMPTY);
                }
            }
        }
        final int fortune = (enchanter != null) ? enchanter.getFortuneLevel() : 0;
        if (block.getDrops(blockState, new LootContext.Builder((ServerLevel) world).withParameter(LootContextParams.TOOL, new ItemStack(Items.DIAMOND_PICKAXE)).withParameter(LootContextParams.ORIGIN, getCart().position())).size() != 0)
        {
            List<ItemStack> stacks = block.getDrops(blockState, new LootContext.Builder((ServerLevel) world).withParameter(LootContextParams.TOOL, new ItemStack(Items.DIAMOND_PICKAXE)).withParameter(LootContextParams.ORIGIN, getCart().position()));
            boolean shouldRemove = false;
            for (int j = 0; j < stacks.size(); ++j)
            {
                if (!minedItem(world, stacks.get(j), next))
                {
                    return false;
                }
                shouldRemove = true;
            }
            if (shouldRemove)
            {
                world.removeBlock(coord, false);
            }
        }
        else
        {
            world.removeBlock(coord, false);
        }
        damageTool(1 + (int) h);
        startWorking(getTimeToMine(h));
        startDrill();
        return true;
    }

    protected boolean minedItem(Level world, @Nonnull ItemStack iStack, BlockPos Coords)
    {
        if (iStack.isEmpty() || iStack.getCount() <= 0)
        {
            return true;
        }
        for (ModuleBase module : getCart().getModules())
        {
            if (module instanceof ModuleIncinerator)
            {
                ((ModuleIncinerator) module).incinerate(iStack);
                if (iStack.getCount() <= 0)
                {
                    return true;
                }
            }
        }
        int size = iStack.getCount();
        getCart().addItemToChest(iStack);
        if (iStack.getCount() == 0)
        {
            return true;
        }
        boolean hasChest = false;
        for (ModuleBase module2 : getCart().getModules())
        {
            if (module2 instanceof ModuleChest)
            {
                hasChest = true;
                break;
            }
        }
        if (!hasChest)
        {
            final ItemEntity entityitem = new ItemEntity(world, getCart().x(), getCart().y(), getCart().z(), iStack);
            world.addFreshEntity(entityitem);
            return true;
        }
        if (iStack.getCount() != size)
        {
            final ItemEntity entityitem = new ItemEntity(world, getCart().x(), getCart().y(), getCart().z(), iStack);
            world.addFreshEntity(entityitem);
            return true;
        }
        return false;
    }

    private int getTimeToMine(final float hardness)
    {
        final int efficiency = (enchanter != null) ? enchanter.getEfficiencyLevel() : 0;
        return (int) (getTimeMult() * hardness / Math.pow(1.2999999523162842, efficiency)) + ((liquidsensors != null) ? 2 : 0);
    }

    protected abstract float getTimeMult();

    public Object isValidBlock(Level world, BlockPos pos, final int holeX, final int holeY, final boolean flag)
    {
        if ((!flag && BaseRailBlock.isRail(world, pos)) || BaseRailBlock.isRail(world, pos.above()))
        {
            return null;
        }
        BlockState blockState = world.getBlockState(pos);
        final Block block = blockState.getBlock();
        if (block == null)
        {
            return null;
        }
        if (block == Blocks.AIR)
        {
            return null;
        }
        if (block == Blocks.BEDROCK)
        {
            return null;
        }
        if (block instanceof IFluidBlock)
        {
            return null;
        }
        if (blockState.getDestroySpeed(world, pos) < 0.0f)
        {
            return null;
        }
        if(!world.getFluidState(pos).isEmpty())
        {
            return null;
        }
        if ((holeX != 0 || holeY > 0) && (block == Blocks.TORCH || block == Blocks.REDSTONE_WIRE || block == Blocks.REDSTONE_TORCH || block == Blocks.REPEATER || block == Blocks.COMPARATOR || block == ModBlocks.MODULE_TOGGLER.get()))
        {
            return null;
        }
        if (block instanceof BaseEntityBlock)
        {
            final BlockEntity tileentity = world.getBlockEntity(pos);
            if (Container.class.isInstance(tileentity))
            {
                if (holeX != 0 || holeY > 0)
                {
                    return null;
                }
                return tileentity;
            }
        }
        if (liquidsensors != null)
        {
            if (liquidsensors.isDangerous(this, pos.offset(0, 1, 0), true) || liquidsensors.isDangerous(this, pos.offset(1, 0, 0), false) || liquidsensors.isDangerous(this, pos.offset(-1, 0, 0), false) || liquidsensors.isDangerous(this, pos.offset(0, 0, 1), false) || liquidsensors.isDangerous(this, pos.offset(0, 0, -1), false))
            {
                sensorLight = 3;
                return null;
            }
            sensorLight = 2;
        }
        return false;
    }

    @Override
    public void update()
    {
        super.update();
        if (getCart().level.isClientSide && !setup)
        {
            if (isPlaceholder() || !getDw(IS_MINING))
            {
                drillRotation = 0;
                miningCoolDown = 10;
            }
            setup = true;
        }
        if ((getCart().hasFuel() && isMining()) || miningCoolDown < 10)
        {
            drillRotation = (float) ((drillRotation + 0.03f * (10 - miningCoolDown)) % (Math.PI * 2));
            if (isMining())
            {
                miningCoolDown = 0;
            }
            else
            {
                ++miningCoolDown;
            }
        }
        if (!getCart().level.isClientSide && liquidsensors != null)
        {
            byte data = sensorLight;
            if (isDrillSpinning())
            {
                data |= 0x4;
            }
            liquidsensors.getInfoFromDrill(data);
            sensorLight = 1;
        }
    }

    protected void startDrill()
    {
        updateDw(IS_MINING, true);
    }

    protected void stopDrill()
    {
        updateDw(IS_MINING, false);
    }

    protected boolean isMining()
    {
        if (isPlaceholder())
        {
            return getSimInfo().getDrillSpinning();
        }
        return getDw(IS_MINING);
    }

    protected boolean isDrillSpinning()
    {
        return isMining() || miningCoolDown < 10;
    }

    @Override
    public void initDw()
    {
        super.initDw();
        IS_MINING = createDw(EntityDataSerializers.BOOLEAN);
        IS_ENABLED = createDw(EntityDataSerializers.BOOLEAN);
        registerDw(IS_MINING, false);
        registerDw(IS_ENABLED, true);
    }

    @Override
    public int numberOfDataWatchers()
    {
        return 2 + super.numberOfDataWatchers();
    }

    public float getDrillRotation()
    {
        return drillRotation;
    }

    private boolean isDrillEnabled()
    {
        return getDw(IS_ENABLED);
    }

    public void setDrillEnabled(final boolean val)
    {
        updateDw(IS_ENABLED, val);
    }

    @Override
    public void mouseClicked(final GuiMinecart gui, final int x, final int y, final int button)
    {
        if (button == 0 && inRect(x, y, buttonRect))
        {
            sendPacket(0);
        }
    }

    @Override
    protected void receivePacket(final int id, final byte[] data, final Player player)
    {
        setDrillEnabled(!isDrillEnabled());
    }

    @Override
    public int numberOfPackets()
    {
        return 1;
    }

    @Override
    public boolean hasGui()
    {
        return true;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void drawForeground(PoseStack matrixStack, GuiMinecart gui)
    {
        drawString(matrixStack, gui, Localization.MODULES.TOOLS.DRILL.translate(), 8, 6, 4210752);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void drawBackground(PoseStack matrixStack, GuiMinecart gui, final int x, final int y)
    {
        super.drawBackground(matrixStack, gui, x, y);
        ResourceHelper.bindResource("/gui/drill.png");
        final int imageID = isDrillEnabled() ? 1 : 0;
        int borderID = 0;
        if (inRect(x, y, buttonRect))
        {
            borderID = 1;
        }
        drawImage(matrixStack, gui, buttonRect, 0, buttonRect[3] * borderID);
        final int srcY = buttonRect[3] * 2 + imageID * (buttonRect[3] - 2);
        drawImage(matrixStack, gui, buttonRect[0] + 1, buttonRect[1] + 1, 0, srcY, buttonRect[2] - 2, buttonRect[3] - 2);
    }

    @Override
    public void drawMouseOver(PoseStack matrixStack, GuiMinecart gui, final int x, final int y)
    {
        super.drawMouseOver(matrixStack, gui, x, y);
        drawStringOnMouseOver(matrixStack, gui, getStateName(), x, y, buttonRect);
    }

    private String getStateName()
    {
        return Localization.MODULES.TOOLS.TOGGLE.translate(isDrillEnabled() ? "1" : "0");
    }

    @Override
    protected void Save(final CompoundTag tagCompound, final int id)
    {
        super.Save(tagCompound, id);
        tagCompound.putBoolean(generateNBTName("DrillEnabled", id), isDrillEnabled());
    }

    @Override
    protected void Load(final CompoundTag tagCompound, final int id)
    {
        super.Load(tagCompound, id);
        setDrillEnabled(tagCompound.getBoolean(generateNBTName("DrillEnabled", id)));
    }

    @Override
    public void doActivate(final int id)
    {
        setDrillEnabled(true);
    }

    @Override
    public void doDeActivate(final int id)
    {
        setDrillEnabled(false);
    }

    @Override
    public boolean isActive(final int id)
    {
        return isDrillEnabled();
    }
}
