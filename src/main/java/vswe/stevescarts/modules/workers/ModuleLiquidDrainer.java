package vswe.stevescarts.modules.workers;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.neoforge.fluids.FluidStack;
import vswe.stevescarts.api.modules.template.ModuleWorker;
import vswe.stevescarts.entities.ModularMinecart;
import vswe.stevescarts.helpers.BlockPosHelpers;
import vswe.stevescarts.helpers.storages.IFluidHandler;
import vswe.stevescarts.modules.workers.tools.ModuleDrill;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ModuleLiquidDrainer extends ModuleWorker {
    private static final int MAX_SOURCE_BLOCKS = 100;
    private static final int MILLIBUCKETS_PER_SOURCE = 1000;
    private static final int WORK_TIME_PER_SOURCE = 100;
    private static final int MAX_WORK_TIME = 200;

    private BlockPos drainTarget;
    private Fluid drainFluid;
    private int plannedSources;

    public ModuleLiquidDrainer(ModularMinecart cart) {
        super(cart);
    }

    @Override
    public byte getWorkPriority() {
        return 0;
    }

    @Override
    public boolean work() {
        if (drainTarget != null && drainFluid != null && plannedSources > 0) {
            drainSources(getCart().level(), drainTarget, drainFluid, plannedSources);
        }

        clearPlan();
        stopWorking();
        return false;
    }

    public void handleLiquid(final ModuleDrill drill, BlockPos pos) {
        if (!doPreWork()) {
            return;
        }

        FluidState fluidState = getCart().level().getFluidState(pos);
        if (fluidState.isEmpty() || !fluidState.isSource()) {
            return;
        }

        Fluid fluid = fluidState.getType();
        List<BlockPos> sources = findSources(getCart().level(), pos, fluid);
        if (sources.isEmpty()) {
            return;
        }

        FluidStack requested = new FluidStack(fluid, sources.size() * MILLIBUCKETS_PER_SOURCE);
        int accepted = getCart().fill(requested, IFluidHandler.FluidAction.SIMULATE);
        int sourceCount = Math.min(sources.size(), accepted / MILLIBUCKETS_PER_SOURCE);
        if (sourceCount <= 0) {
            clearPlan();
            stopWorking();
            return;
        }

        drainTarget = pos.immutable();
        drainFluid = fluid;
        plannedSources = sourceCount;
        drill.kill();
        startWorking(Math.min(MAX_WORK_TIME, sourceCount * WORK_TIME_PER_SOURCE));
    }

    private void drainSources(Level level, BlockPos start, Fluid fluid, int sourceLimit) {
        List<BlockPos> sources = findSources(level, start, fluid);
        int drained = 0;

        for (BlockPos source : sources) {
            if (drained >= sourceLimit) {
                break;
            }

            FluidStack bucket = new FluidStack(fluid, MILLIBUCKETS_PER_SOURCE);
            if (getCart().fill(bucket, IFluidHandler.FluidAction.SIMULATE) != MILLIBUCKETS_PER_SOURCE) {
                break;
            }

            if (getCart().fill(bucket, IFluidHandler.FluidAction.EXECUTE) == MILLIBUCKETS_PER_SOURCE) {
                level.setBlock(source, Blocks.AIR.defaultBlockState(), 11);
                drained++;
            }
        }
    }

    private List<BlockPos> findSources(Level level, BlockPos start, Fluid fluid) {
        ArrayDeque<BlockPos> pending = new ArrayDeque<>();
        Set<BlockPos> checked = new HashSet<>();
        List<BlockPos> sources = new ArrayList<>();
        pending.add(start.immutable());

        while (!pending.isEmpty() && sources.size() < MAX_SOURCE_BLOCKS) {
            BlockPos pos = pending.removeFirst();
            if (!checked.add(pos)) {
                continue;
            }

            FluidState state = level.getFluidState(pos);
            if (state.isEmpty() || !state.isSource() || state.getType() != fluid) {
                continue;
            }

            sources.add(pos);
            if (sources.size() >= MAX_SOURCE_BLOCKS
                    || BlockPosHelpers.getHorizontalDistToCartSquared(pos, getCart()) >= 200.0) {
                continue;
            }

            pending.addLast(pos.above());
            pending.addLast(pos.north());
            pending.addLast(pos.south());
            pending.addLast(pos.west());
            pending.addLast(pos.east());
        }

        return sources;
    }

    private void clearPlan() {
        drainTarget = null;
        drainFluid = null;
        plannedSources = 0;
    }
}
