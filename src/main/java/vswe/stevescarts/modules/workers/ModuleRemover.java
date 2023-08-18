package vswe.stevescarts.modules.workers;

import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RailBlock;
import net.minecraft.world.level.block.state.BlockState;
import vswe.stevescarts.api.modules.interfaces.IActivatorModule;
import vswe.stevescarts.api.modules.template.ModuleWorker;
import vswe.stevescarts.entities.EntityMinecartModular;

import javax.annotation.Nonnull;

public class ModuleRemover extends ModuleWorker implements IActivatorModule {
    @Nonnull
    private BlockPos remove;
    private EntityDataAccessor<Boolean> IS_ENABLED;

    public ModuleRemover(final EntityMinecartModular cart) {
        super(cart);
        remove = new BlockPos(0, -1, 0);
    }

    @Override
    public void initDw() {
        IS_ENABLED = createDw(EntityDataSerializers.BOOLEAN);
        registerDw(IS_ENABLED, true);
    }

    @Override
    public int numberOfPackets() {
        return 1;
    }

    @Override
    public int numberOfDataWatchers() {
        return 1;
    }

    @Override
    public byte getWorkPriority() {
        return 120;
    }

    @Override
    protected boolean preventTurnback() {
        return true;
    }

    @Override
    public boolean work() {
        EntityMinecartModular cart = getCart();
        Level world = cart.level();
        if (remove.getY() != -1 && (remove.getX() != cart.x() || remove.getZ() != cart.z()) && removeRail(world, remove, true)) {
            return false;
        }
        BlockPos next = getNextblock();
        BlockPos last = getLastblock();
        final boolean front = isRailAtCoords(world, next);
        final boolean back = isRailAtCoords(world, last);
        if (!front) {
            if (back) {
                turnback();
                if (removeRail(world, cart.getExactPosition(), false)) {
                    return true;
                }
            }
        } else if (!back && removeRail(world, cart.getExactPosition(), false)) {
            return true;
        }
        return false;
    }

    private boolean isRailAtCoords(Level world, BlockPos coords) {
        return RailBlock.isRail(world, coords.above()) || RailBlock.isRail(world, coords) || RailBlock.isRail(world, coords.below());
    }

    private boolean removeRail(Level world, BlockPos pos, final boolean flag) {
        if (flag) {
            BlockState blockState = world.getBlockState(pos);
            if (RailBlock.isRail(blockState)) {
                if (isRemovingEnabled()) {
                    if (doPreWork()) {
                        startWorking(12);
                        return true;
                    }
                    @Nonnull
                    ItemStack iStack = new ItemStack(blockState.getBlock(), 1);
                    getCart().addItemToChest(iStack);
                    if (iStack.getCount() == 0) {
                        world.removeBlock(pos, false);
                    }
                }
                remove = new BlockPos(pos.getX(), -1, pos.getZ());
            } else {
                remove = new BlockPos(pos.getX(), -1, pos.getZ());
            }
        } else if (RailBlock.isRail(world, pos.below())) {
            remove = pos.below();
        } else if (RailBlock.isRail(world, pos)) {
            remove = pos;
        } else if (RailBlock.isRail(world, pos.above())) {
            remove = pos.above();
        }
        stopWorking();
        return false;
    }

    private void enableRemoving(final boolean remove) {
        if (!isPlaceholder()) {
            updateDw(IS_ENABLED, remove);
        }
    }

    private boolean isRemovingEnabled() {
        return !isPlaceholder() && getDw(IS_ENABLED);
    }

    @Override
    public void doActivate(final int id) {
        enableRemoving(true);
    }

    @Override
    public void doDeActivate(final int id) {
        enableRemoving(false);
    }

    @Override
    public boolean isActive(final int id) {
        return isRemovingEnabled();
    }
}
