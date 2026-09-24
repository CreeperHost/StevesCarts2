package vswe.stevescarts.modules.workers.tools;

import net.minecraft.network.chat.Component;

import net.creeperhost.polylib.data.serializable.BooleanData;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.neoforged.neoforge.common.Tags;
import vswe.stevescarts.api.StevesCartsAPI;
import vswe.stevescarts.api.farms.ICropModule;
import vswe.stevescarts.api.modules.ModuleBase;
import vswe.stevescarts.api.modules.interfaces.ISuppliesModule;
import vswe.stevescarts.api.modules.template.ModuleTool;
import vswe.stevescarts.api.slots.SlotStevesCarts;
import vswe.stevescarts.client.guis.GuiMinecart;
import vswe.stevescarts.containers.slots.SlotSeed;
import vswe.stevescarts.entities.IModularCart;
import vswe.stevescarts.entities.ModularMinecart;
import vswe.stevescarts.polylib.EntityData;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class ModuleFarmer extends ModuleTool implements ISuppliesModule {
    private final EntityData<Boolean> isFarming = new EntityData<>(getCart(), new BooleanData(false));
    private ArrayList<ICropModule> plantModules;
    private int farming;
    private float farmAngle;
    private float rigAngle;

    public ModuleFarmer(ModularMinecart cart) {
        super(cart);
        rigAngle = -3.926991f;
    }

    protected abstract int getRange();

    public int getExternalRange() {
        return getRange();
    }

    @Override
    public void init() {
        super.init();
        plantModules = new ArrayList<>();
        for (final ModuleBase module : getCart().modules()) {
            if (module instanceof ICropModule) {
                plantModules.add((ICropModule) module);
            }
        }
        plantModules.addAll(StevesCartsAPI.CROP_MODULES);
    }

    @Override
    public byte getWorkPriority() {
        return 80;
    }

    @Override
    public boolean hasGui() {
        return true;
    }

    @Override
    public void drawForeground(GuiGraphicsExtractor GuiGraphicsExtractor, GuiMinecart gui) {
        drawString(GuiGraphicsExtractor, gui, Component.translatable("modules.tools.stevescarts.farmerTitle").getString(), 8, 6, 4210752);
    }

    @Override
    protected int getInventoryWidth() {
        return super.getInventoryWidth() + 3;
    }

    @Override
    protected SlotStevesCarts getSlot(final int slotId, int x, final int y) {
        if (x == 0) {
            return super.getSlot(slotId, x, y);
        }
        --x;
        return new SlotSeed(getCart(), slotId, 8 + x * 18, 28 + y * 18);
    }

    @Override
    public boolean work() {
        Level world = getCart().level();
        BlockPos next = getCurrentRailBlock();
        for (int i = -getRange(); i <= getRange(); ++i) {
            for (int j = -getRange(); j <= getRange(); ++j) {
                BlockPos coord = next.offset(i, -1, j);
                if (farm(world, coord)) {
                    return true;
                }
                if (till(world, coord)) {
                    return true;
                }
                if (plant(world, coord)) {
                    return true;
                }
            }
        }
        return false;
    }

    protected boolean till(Level world, BlockPos pos) {
        BlockPos cropPos = pos.above();
        BlockState aboveState = world.getBlockState(cropPos);
        boolean canClearAbove = aboveState.isAir()
                || (aboveState.canBeReplaced() && aboveState.getFluidState().isEmpty());
        BlockState soilState = world.getBlockState(pos);
        boolean isTillableSoil = soilState.is(BlockTags.DIRT) || soilState.is(Blocks.GRASS_BLOCK);
        if (isTillableSoil && canClearAbove) {
            if (doPreWork()) {
                final int workingTime = 10;
                setFarming(workingTime * 4);
                startWorking(workingTime);
                return true;
            }
            stopWorking();
            if (!aboveState.isAir()) {
                world.destroyBlock(cropPos, false);
            }
            world.setBlock(pos, Blocks.FARMLAND.defaultBlockState(), 3);
        }
        return false;
    }

    protected boolean plant(Level world, BlockPos pos) {
        BlockState soilState = world.getBlockState(pos);
        Block soilblock = soilState.getBlock();
        if (soilblock != null) {
            IModularCart.ModuleSlot seed = findPlantableSeed(world, pos);
            if (seed != null) {
                if (doPreWork()) {
                    startWorking(25);
                    return true;
                }
                stopWorking();
                BlockState cropblock2 = getCropFromSeedHandler(seed.getItem(), world, pos);
                world.setBlock(pos.above(), cropblock2, 3);
                seed.take(1);
            }
        }
        return false;
    }

    private IModularCart.ModuleSlot findPlantableSeed(Level world, BlockPos pos) {
        for (int i = 0; i < getInventorySize(); ++i) {
            ItemStack stack = getStack(i);
            if (canPlantSeed(stack, world, pos)) {
                return new IModularCart.ModuleSlot(this, i);
            }
        }
        return getCart().findItemInAccessibleStorage(stack -> canPlantSeed(stack, world, pos)).orElse(null);
    }

    private boolean canPlantSeed(ItemStack stack, Level world, BlockPos pos) {
        if (stack.isEmpty() || !isSeedValidHandler(stack) || !world.getBlockState(pos.above()).isAir()) {
            return false;
        }
        BlockState crop = getCropFromSeedHandler(stack, world, pos);
        return crop != null && crop.canSurvive(world, pos.above());
    }

    protected boolean farm(Level world, BlockPos pos) {
        ModularMinecart cart = getCart();
        if (!isBroken()) {
            pos = pos.above();
            BlockState blockState = world.getBlockState(pos);
            Block block = blockState.getBlock();
            if (isReadyToHarvestHandler(world, pos)) {
                if (doPreWork()) {
                    final int efficiency = (enchanter != null) ? enchanter.getEfficiencyLevel() : 0;
                    final int workingtime = (int) (getBaseFarmingTime() / Math.pow(1.2999999523162842, efficiency));
                    setFarming(workingtime * 4);
                    startWorking(workingtime);
                    return true;
                }
                stopWorking();
                List<ItemStack> stuff;

                LootParams.Builder builder = new LootParams.Builder((ServerLevel) world)
                        .withParameter(LootContextParams.TOOL, createFortuneTool(Items.DIAMOND_HOE))
                        .withParameter(LootContextParams.ORIGIN, getCart().position());

                BlockEntity blockEntity = world.getBlockEntity(pos);
                if (blockEntity != null) {
                    builder.withParameter(LootContextParams.BLOCK_ENTITY, blockEntity);
                }

                stuff = blockState.getDrops(builder);
                for (@Nonnull ItemStack iStack : stuff) {
                    cart.addItemToChest(iStack);
                    if (iStack.getCount() != 0) {
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

    protected int getBaseFarmingTime() {
        return 25;
    }

    public boolean isSeedValidHandler(@Nonnull ItemStack seed) {
        return seed.is(Tags.Items.SEEDS) || seed.is(ItemTags.VILLAGER_PLANTABLE_SEEDS) || plantModules.stream().anyMatch(e -> e.isSeedValid(seed));
    }

    protected BlockState getCropFromSeedHandler(@Nonnull ItemStack seed, Level level, BlockPos pos) {
        Block cropBlock = Block.byItem(seed.getItem());
        BlockState state = cropBlock.defaultBlockState();
        if (cropBlock instanceof CropBlock) {
            return state;
        }
        state = plantModules.stream()
                .map(e -> e.getCropFromSeed(seed, level, pos))
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
        return state;
    }

    protected boolean isReadyToHarvestHandler(Level world, BlockPos pos) {
        BlockState state = world.getBlockState(pos);
        if (state.getBlock() instanceof CropBlock cropsBlock) {
            return cropsBlock.isMaxAge(world.getBlockState(pos));
        }
        return plantModules.stream().anyMatch(e -> e.isReadyToHarvest(world, pos));
    }

    public float getFarmAngle() {
        return farmAngle;
    }

    public float getRigAngle() {
        return rigAngle;
    }

    protected boolean isFarming() {
        if (isPlaceholder()) {
            return getSimInfo().getIsFarming();
        }
        return getCart().isEngineBurning() && isFarming.get();
    }

    private void setFarming(int val) {
        farming = val;
        isFarming.set(val > 0);
    }

    @Override
    public void update() {
        super.update();
        if (!getCart().level().isClientSide()) {
            setFarming(farming - 1);
        } else {
            final float up = -3.926991f;
            final float down = -3.1415927f;
            final boolean flag = isFarming();
            if (flag) {
                if (rigAngle < down) {
                    rigAngle += 0.1f;
                    if (rigAngle > down) {
                        rigAngle = down;
                    }
                } else {
                    farmAngle = (float) ((farmAngle + 0.15f) % 6.283185307179586);
                }
            } else if (rigAngle > up) {
                rigAngle -= 0.075f;
                if (rigAngle < up) {
                    rigAngle = up;
                }
            }
        }
    }

    @Override
    public boolean haveSupplies() {
        for (int i = 0; i < getInventorySize(); ++i) {
            @Nonnull ItemStack item = getStack(i);
            if (!item.isEmpty() && isSeedValidHandler(item)) {
                return true;
            }
        }
        return getCart().findItemInAccessibleStorage(this::isSeedValidHandler).isPresent();
    }
}
