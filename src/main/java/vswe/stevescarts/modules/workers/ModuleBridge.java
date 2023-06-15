package vswe.stevescarts.modules.workers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RailBlock;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fluids.IFluidBlock;
import vswe.stevescarts.client.guis.GuiMinecart;
import vswe.stevescarts.containers.slots.SlotBase;
import vswe.stevescarts.containers.slots.SlotBridge;
import vswe.stevescarts.entities.EntityMinecartModular;
import vswe.stevescarts.api.modules.interfaces.ISuppliesModule;

import javax.annotation.Nonnull;

public class ModuleBridge extends ModuleWorker implements ISuppliesModule {
    private EntityDataAccessor<Boolean> BRIDGE;

    public ModuleBridge(final EntityMinecartModular cart) {
        super(cart);
    }

    @Override
    public boolean hasGui() {
        return true;
    }

    @Override
    public int guiWidth() {
        return 80;
    }

    @Override
    protected SlotBase getSlot(final int slotId, final int x, final int y) {
        return new SlotBridge(getCart(), slotId, 8 + x * 18, 23 + y * 18);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void drawForeground(GuiGraphics guiGraphics, GuiMinecart gui) {
        drawString(guiGraphics, gui, getModuleName(), 8, 6, 4210752);
    }

    @Override
    public byte getWorkPriority() {
        return 98;
    }

    @Override
    public boolean work() {
        Level world = getCart().level();
        BlockPos next = getNextblock();
        if (getCart().getYTarget() < next.getY()) {
            next = next.below(2);
        } else if (getCart().getYTarget() == next.getY()) {
            next = next.below(1);
        }
        if (!RailBlock.isRail(world, next) && !RailBlock.isRail(world, next.below())) {
            if (doPreWork()) {
                if (tryBuildBridge(world, next, false)) {
                    startWorking(22);
                    setBridge(true);
                    return true;
                }
            } else if (tryBuildBridge(world, next, true)) {
                stopWorking();
            }
        }
        setBridge(false);
        return false;
    }

    private boolean tryBuildBridge(Level world, BlockPos pos, final boolean doPlace) {
        final Block blockAtPos = world.getBlockState(pos).getBlock();
        if ((countsAsAir(pos) || blockAtPos instanceof IFluidBlock) && isValidForTrack(pos.above(), false)) {
            for (int slot = 0; slot < getInventorySize(); ++slot) {
                ItemStack stack = getStack(slot);
                if (stack.isEmpty() || !SlotBridge.isBridgeMaterial(stack)) continue;
                if (doPlace) {
                    Block block = Block.byItem(stack.getItem());
                    boolean placed = getCart().level().setBlock(pos, block.defaultBlockState(), 3);
                    if (!placed) return false;
                    if (!getCart().hasCreativeSupplies()) {
                        stack.shrink(1);
                        if (getStack(slot).getCount() == 0) {
                            setStack(slot, ItemStack.EMPTY);
                        }
                        getCart().setChanged();
                    }
                }
                return true;
            }
//            if (isValidForTrack(pos, true) || isValidForTrack(pos.above(), true) || !isValidForTrack(pos.above(2), true)) {
//            }
        }
        return false;
    }

    @Override
    public void initDw() {
        BRIDGE = createDw(EntityDataSerializers.BOOLEAN);
        registerDw(BRIDGE, false);
    }

    @Override
    public int numberOfDataWatchers() {
        return 1;
    }

    private void setBridge(final boolean val) {
        updateDw(BRIDGE, val);
    }

    public boolean needBridge() {
        if (isPlaceholder()) {
            return getSimInfo().getNeedBridge();
        }
        return getDw(BRIDGE);
    }

    @Override
    public boolean haveSupplies() {
        for (int i = 0; i < getInventorySize(); ++i) {
            ItemStack item = getStack(i);
            if (!item.isEmpty() && SlotBridge.isBridgeMaterial(item)) {
                return true;
            }
        }
        return false;
    }
}
