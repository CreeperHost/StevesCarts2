package vswe.stevescarts.modules.workers;

import net.creeperhost.polylib.data.serializable.BooleanData;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RailBlock;
import vswe.stevescarts.api.modules.interfaces.ISuppliesModule;
import vswe.stevescarts.api.modules.template.ModuleWorker;
import vswe.stevescarts.api.slots.SlotStevesCarts;
import vswe.stevescarts.client.guis.GuiMinecart;
import vswe.stevescarts.containers.slots.SlotBridge;
import vswe.stevescarts.entities.ModularMinecart;
import vswe.stevescarts.polylib.EntityData;

public class ModuleBridge extends ModuleWorker implements ISuppliesModule {
    private final EntityData<Boolean> bridge = new EntityData<>(getCart(), new BooleanData(false));

    public ModuleBridge(ModularMinecart cart) {
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
    protected SlotStevesCarts getSlot(final int slotId, final int x, final int y) {
        return new SlotBridge(getCart(), slotId, 8 + x * 18, 23 + y * 18);
    }

    @Override
    public void drawForeground(GuiGraphicsExtractor GuiGraphicsExtractor, GuiMinecart gui) {
        drawString(GuiGraphicsExtractor, gui, getModuleName(), 8, 6, 4210752);
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
        if ((countsAsAir(pos) || !world.getFluidState(pos).isEmpty()) && isValidForTrack(pos.above(), false)) {
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
        }
        return false;
    }

    private void setBridge(boolean val) {
        bridge.set(val);
    }

    public boolean needBridge() {
        if (isPlaceholder()) {
            return getSimInfo().getNeedBridge();
        }
        return bridge.get();
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
