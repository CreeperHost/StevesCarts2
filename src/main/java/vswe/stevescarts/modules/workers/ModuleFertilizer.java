package vswe.stevescarts.modules.workers;

import net.creeperhost.polylib.data.serializable.IntData;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.common.Tags;
import vswe.stevescarts.api.modules.ModuleBase;
import vswe.stevescarts.api.modules.interfaces.ISuppliesModule;
import vswe.stevescarts.api.modules.template.ModuleWorker;
import vswe.stevescarts.api.slots.SlotStevesCarts;
import vswe.stevescarts.client.guis.GuiMinecart;
import vswe.stevescarts.containers.slots.SlotFertilizer;
import vswe.stevescarts.entities.ModularMinecart;
import vswe.stevescarts.helpers.Localization;
import vswe.stevescarts.helpers.ResourceHelper;
import vswe.stevescarts.modules.workers.tools.ModuleFarmer;
import vswe.stevescarts.polylib.EntityData;

import java.util.Random;

public class ModuleFertilizer extends ModuleWorker implements ISuppliesModule {
    private final int fertPerBonemeal = 4;
    private final int maxStacksOfBones = 1;
    private final Random random = new Random();
    private final EntityData<Integer> fertilizer = new EntityData<>(getCart(), new IntData(0));
    private final int tankPosX;
    private final int tankPosY;
    private int range;

    public ModuleFertilizer(ModularMinecart cart) {
        super(cart);
        tankPosX = guiWidth() - 21;
        tankPosY = 20;
        range = 1;
    }

    @Override
    public byte getWorkPriority() {
        return 127;
    }

    @Override
    public boolean hasGui() {
        return true;
    }

    @Override
    protected int getInventoryWidth() {
        return 1;
    }

    @Override
    public void init() {
        super.init();
        for (final ModuleBase module : getCart().modules()) {
            if (module instanceof ModuleFarmer) {
                range = ((ModuleFarmer) module).getExternalRange();
                break;
            }
        }
    }

    @Override
    public void drawBackground(GuiGraphicsExtractor GuiGraphicsExtractor, GuiMinecart gui, final int x, final int y) {
        Identifier texture = ResourceHelper.getResource("/gui/fertilize.png");
        drawImage(GuiGraphicsExtractor, texture, gui, tankPosX, tankPosY, 0, 0, 18, 27);
        float percentage = getFertAmount() / (float) getMaxFert();
        int size = (int) (percentage * 23.0f);
        drawImage(GuiGraphicsExtractor, texture, gui, tankPosX + 2, tankPosY + 2 + (23 - size), 18, 23 - size, 14, size);
    }

    @Override
    public void drawMouseOver(GuiGraphicsExtractor GuiGraphicsExtractor, GuiMinecart gui, final int x, final int y) {
        drawStringOnMouseOver(GuiGraphicsExtractor, gui, Localization.MODULES.ATTACHMENTS.FERTILIZERS.translate() + ": " + getFertAmount() + " / " + getMaxFert(), x, y, tankPosX, tankPosY, 18, 27);
    }

    @Override
    public void drawForeground(GuiGraphicsExtractor GuiGraphicsExtractor, GuiMinecart gui) {
        drawString(GuiGraphicsExtractor, gui, getModuleName(), 8, 6, 4210752);
    }

    @Override
    public int guiWidth() {
        return super.guiWidth() + 25;
    }


    @Override
    public int guiHeight() {
        return Math.max(super.guiHeight(), 50);
    }

    @Override
    protected SlotStevesCarts getSlot(final int slotId, final int x, final int y) {
        return new SlotFertilizer(getCart(), slotId, 8 + x * 18, 23 + y * 18);
    }

    @Override
    public boolean work() {
        Level world = getCart().level();
        BlockPos next = getNextblock();
        for (int i = -range; i <= range; ++i) {
            for (int j = -range; j <= range; ++j) {
                if (random.nextInt(25) == 0 && fertilize(world, next.offset(i, 0, j))) {
                    break;
                }
            }
        }
        return false;
    }

    private boolean fertilize(Level world, BlockPos pos) {
        BlockState stateOfTopBlock = world.getBlockState(pos);
        Block blockTop = stateOfTopBlock.getBlock();
        if (getFertAmount() > 0) {
            if (blockTop instanceof CropBlock growable) {
                if (growable.isValidBonemealTarget(world, pos, stateOfTopBlock)) {
                    if (growable.isBonemealSuccess(world, getCart().getRandom(), pos, stateOfTopBlock)) {
                        growable.performBonemeal((ServerLevel) world, getCart().getRandom(), pos, stateOfTopBlock);
                        setFertAmount(getFertAmount() - 2);
                        return true;
                    }
                }
            }
        }
        return false;
    }


    private void addFert(int amount) {
        int val = getFertAmount();
        val += amount;
        setFertAmount(val);
    }

    public int getFertAmount() {
        return fertilizer.get();
    }

    private void setFertAmount(int val) {
        if (!isPlaceholder()) {
            fertilizer.set(val);
        }
    }

    @Override
    public void update() {
        super.update();
        loadSupplies();
    }

    private void loadSupplies() {
        if (getCart().level().isClientSide()) {
            return;
        }
        ItemStack stack = getStack(0);
        if (!stack.isEmpty()) {
            int amount = 0;
            if (stack.is(Items.BONE_MEAL)) amount = 1;
            else if (stack.is(Tags.Items.BONES)) amount = 3;
            else if (stack.is(Items.BONE_BLOCK)) amount = 9;
            if (amount == 0) return;
            amount *= 4;
            if (getFertAmount() + amount <= 768) {
                stack.shrink(1);
                addFert(amount);
            }
        }
    }

    private int getMaxFert() {
        return 768;
    }

    @Override
    protected void save(ValueOutput output, int id) {
        super.save(output, id);
        output.putShort(generateNBTName("Fert", id), (short) getFertAmount());
    }

    @Override
    protected void load(ValueInput input, int id) {
        super.load(input, id);
        setFertAmount(input.getShortOr(generateNBTName("Fert", id), (short) 0));
    }

    @Override
    public boolean haveSupplies() {
        return getFertAmount() > 0;
    }
}
