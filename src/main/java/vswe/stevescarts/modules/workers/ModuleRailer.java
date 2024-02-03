package vswe.stevescarts.modules.workers;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RailBlock;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.common.util.FakePlayer;
import vswe.stevescarts.api.modules.interfaces.ISuppliesModule;
import vswe.stevescarts.api.modules.template.ModuleWorker;
import vswe.stevescarts.client.guis.GuiMinecart;
import vswe.stevescarts.api.slots.SlotStevesCarts;
import vswe.stevescarts.containers.slots.SlotBuilder;
import vswe.stevescarts.entities.EntityMinecartModular;
import vswe.stevescarts.helpers.Localization;

import java.util.ArrayList;

public class ModuleRailer extends ModuleWorker implements ISuppliesModule {
    private boolean hasGeneratedAngles;
    private float[] railAngles;
    private EntityDataAccessor<Byte> RAILS;

    public ModuleRailer(final EntityMinecartModular cart) {
        super(cart);
        hasGeneratedAngles = false;
    }

    @Override
    public boolean hasGui() {
        return true;
    }

    @Override
    protected SlotStevesCarts getSlot(final int slotId, final int x, final int y) {
        return new SlotBuilder(getCart(), slotId, 8 + x * 18, 23 + y * 18);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void drawForeground(GuiGraphics guiGraphics, GuiMinecart gui) {
        drawString(guiGraphics, gui, Localization.MODULES.ATTACHMENTS.RAILER.translate(), 8, 6, 4210752);
    }

    @Override
    public byte getWorkPriority() {
        return 100;
    }

    @Override
    public boolean work() {
        BlockPos next = getNextblock();
        ArrayList<BlockPos> positions = getValidRailPositions(next);
        //To pre-check where we confirm it is possible to place the next track
        if (doPreWork()) {
            boolean foundValidPosition = false;
            for (BlockPos pos : positions) {
                if (tryPlaceTrack(pos, false)) {
                    foundValidPosition = true;
                    break;
                }
            }

            if (foundValidPosition) {
                //Schedule work
                startWorking(12);
            } else {
                //Check if we failed to place because there was already a valid track there.
                boolean canContinue = false;
                for (BlockPos pos : positions) {
                    if (RailBlock.isRail(getCart().level(), pos)) {
                        canContinue = true;
                        break;
                    }
                }
                if (!canContinue) {
                    turnback();
                }
            }
            return true;
        }

        //Do the work.
        stopWorking();
        for (BlockPos position : positions) {
            if (tryPlaceTrack(position, true)) {
                break;
            }
        }
        return false;
    }

    protected ArrayList<BlockPos> getValidRailPositions(BlockPos pos) {
        ArrayList<BlockPos> lst = new ArrayList<>();
        if (pos.getY() >= getCart().y()) {
            lst.add(pos.above());
        }
        lst.add(pos);
        lst.add(pos.below());
        return lst;
    }

    protected boolean validRail(Item item) {
        ItemStack stack = new ItemStack(item);
        return stack.is(ItemTags.RAILS);
    }

    private boolean tryPlaceTrack(BlockPos pos, boolean doPlace) {
        if (!isValidForTrack(pos, true)) {
            return false;
        }

        FakePlayer fakePlayer = getFakePlayer();
        for (int slot = 0; slot < getInventorySize(); slot++) {
            ItemStack stack = getStack(slot);
            if (stack.isEmpty() || !validRail(stack.getItem())) continue;
            if (!fakePlayer.mayUseItemAt(pos, Direction.DOWN, stack)) return false;

            if (doPlace) {
                Block block = Block.byItem(stack.getItem());
                boolean placed = getCart().level().setBlock(pos, block.defaultBlockState(), 3);
                if (!placed) return false;
                if (!getCart().hasCreativeSupplies()) {
                    stack.shrink(1);
                    getCart().setChanged();
                }
            }
            return true;

        }
        turnback();
        return true;
    }

    @Override
    public void initDw() {
        RAILS = createDw(EntityDataSerializers.BYTE);
        registerDw(RAILS, (byte) 0);
    }

    @Override
    public int numberOfDataWatchers() {
        return 1;
    }

    @Override
    public void onInventoryChanged() {
        super.onInventoryChanged();
        calculateRails();
    }

    private void calculateRails() {
        if (getCart().level().isClientSide) {
            return;
        }
        byte valid = 0;
        for (int i = 0; i < getInventorySize(); ++i) {
            if (!getStack(i).isEmpty() && validRail(getStack(i).getItem())) {
                ++valid;
            }
        }
        updateDw(RAILS, valid);
    }

    public int getRails() {
        if (isPlaceholder()) {
            return getSimInfo().getRailCount();
        }
        return getDw(RAILS);
    }

    public float getRailAngle(final int i) {
        if (!hasGeneratedAngles) {
            railAngles = new float[getInventorySize()];
            for (int j = 0; j < getInventorySize(); ++j) {
                railAngles[j] = getCart().random.nextFloat() / 2.0f - 0.25f;
            }
            hasGeneratedAngles = true;
        }
        return railAngles[i];
    }

    @Override
    protected void Load(CompoundTag tagCompound, final int id) {
        calculateRails();
    }

    @Override
    public boolean haveSupplies() {
        for (int i = 0; i < getInventorySize(); ++i) {
            ItemStack item = getStack(i);
            if (!item.isEmpty() && validRail(item.getItem())) {
                return true;
            }
        }
        return false;
    }
}
