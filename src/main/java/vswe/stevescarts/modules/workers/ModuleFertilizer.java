package vswe.stevescarts.modules.workers;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import vswe.stevescarts.api.modules.ModuleBase;
import vswe.stevescarts.api.modules.interfaces.ISuppliesModule;
import vswe.stevescarts.api.modules.template.ModuleWorker;
import vswe.stevescarts.client.guis.GuiMinecart;
import vswe.stevescarts.containers.slots.SlotBase;
import vswe.stevescarts.containers.slots.SlotFertilizer;
import vswe.stevescarts.entities.EntityMinecartModular;
import vswe.stevescarts.helpers.Localization;
import vswe.stevescarts.helpers.ResourceHelper;
import vswe.stevescarts.modules.workers.tools.ModuleFarmer;

import javax.annotation.Nonnull;
import java.util.Random;

public class ModuleFertilizer extends ModuleWorker implements ISuppliesModule
{
    private int tankPosX;
    private int tankPosY;
    private int range;
    private EntityDataAccessor<Integer> FERTILIZER;
    private final int fertPerBonemeal = 4;
    private final int maxStacksOfBones = 1;
    private final Random random = new Random();

    public ModuleFertilizer(final EntityMinecartModular cart)
    {
        super(cart);
        tankPosX = guiWidth() - 21;
        tankPosY = 20;
        range = 1;
    }

    @Override
    public byte getWorkPriority()
    {
        return 127;
    }

    @Override
    public boolean hasGui()
    {
        return true;
    }

    @Override
    protected int getInventoryWidth()
    {
        return 1;
    }

    @Override
    public void init()
    {
        super.init();
        for (final ModuleBase module : getCart().getModules())
        {
            if (module instanceof ModuleFarmer)
            {
                range = ((ModuleFarmer) module).getExternalRange();
                break;
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void drawBackground(GuiGraphics guiGraphics, GuiMinecart gui, final int x, final int y)
    {
        ResourceHelper.bindResource("/gui/fertilize.png");
        drawImage(guiGraphics, gui, tankPosX, tankPosY, 0, 0, 18, 27);
        final float percentage = getFertAmount() / getMaxFert();
        final int size = (int) (percentage * 23.0f);
        drawImage(guiGraphics, gui, tankPosX + 2, tankPosY + 2 + (23 - size), 18, 23 - size, 14, size);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void drawMouseOver(GuiGraphics guiGraphics, GuiMinecart gui, final int x, final int y)
    {
        drawStringOnMouseOver(guiGraphics, gui, Localization.MODULES.ATTACHMENTS.FERTILIZERS.translate() + ": " + getFertAmount() + " / " + getMaxFert(), x, y, tankPosX, tankPosY, 18, 27);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void drawForeground(GuiGraphics guiGraphics, GuiMinecart gui)
    {
        drawString(guiGraphics, gui, getModuleName(), 8, 6, 4210752);
    }

    @Override
    public int guiWidth()
    {
        return super.guiWidth() + 25;
    }

    @Override
    public int guiHeight()
    {
        return Math.max(super.guiHeight(), 50);
    }

    @Override
    protected SlotBase getSlot(final int slotId, final int x, final int y)
    {
        return new SlotFertilizer(getCart(), slotId, 8 + x * 18, 23 + y * 18);
    }

    @Override
    public boolean work()
    {
        Level world = getCart().level();
        BlockPos next = getNextblock();
        for (int i = -range; i <= range; ++i)
        {
            for (int j = -range; j <= range; ++j)
            {
                if (random.nextInt(25) == 0 && fertilize(world, next.offset(i, +1, j)))
                {
                    break;
                }
            }
        }
        return false;
    }

    private boolean fertilize(Level world, BlockPos pos)
    {
        BlockState stateOfTopBlock = world.getBlockState(pos);
        Block blockTop = stateOfTopBlock.getBlock();
        if (getFertAmount() > 0)
        {
            if (blockTop instanceof CropBlock)
            {
                CropBlock growable = (CropBlock) blockTop;
                if (growable.isValidBonemealTarget(world, pos, stateOfTopBlock, false))
                {
                    if (growable.isBonemealSuccess(world, getCart().random, pos, stateOfTopBlock))
                    {
                        growable.performBonemeal((ServerLevel) world, getCart().random, pos, stateOfTopBlock);
                        setFertAmount(getFertAmount() - 2);
                        return true;
                    }
                }
            }
        }
        return false;
    }


    private void addFert(int amount)
    {
        int val = getFertAmount();
        val += amount;
        setFertAmount(val);
    }

    public int getFertAmount()
    {
        return getDw(FERTILIZER);
    }

    private void setFertAmount(final int val)
    {
        if (!isPlaceholder())
        {
            updateDw(FERTILIZER, val);
        }
    }

    @Override
    public int numberOfDataWatchers()
    {
        return 1;
    }

    @Override
    public void initDw()
    {
        FERTILIZER = createDw(EntityDataSerializers.INT);
        registerDw(FERTILIZER, 0);
    }

    @Override
    public void update()
    {
        super.update();
        loadSupplies();
    }

    private void loadSupplies()
    {
        if (getCart().level().isClientSide)
        {
            return;
        }
        if (!getStack(0).isEmpty())
        {
            final boolean isBone = getStack(0).getItem() == Items.BONE;
            final boolean isBoneMeal = getStack(0).getItem() == Items.BONE_MEAL;
            if (isBone || isBoneMeal)
            {
                int amount;
                if (isBoneMeal)
                {
                    amount = 1;
                }
                else
                {
                    amount = 3;
                }
                if (getFertAmount() <= 4 * (192 - amount) && getStack(0).getCount() > 0)
                {
                    @Nonnull ItemStack stack = getStack(0);
                    stack.shrink(1);
                    addFert(amount * 4);
                }
                if (getStack(0).getCount() == 0)
                {
                    setStack(0, ItemStack.EMPTY);
                }
            }
        }
    }

    private int getMaxFert()
    {
        return 768;
    }

    @Override
    protected void Save(final CompoundTag tagCompound, final int id)
    {
        tagCompound.putShort(generateNBTName("Fert", id), (short) getFertAmount());
    }

    @Override
    protected void Load(final CompoundTag tagCompound, final int id)
    {
        setFertAmount(tagCompound.getShort(generateNBTName("Fert", id)));
    }

    @Override
    public boolean haveSupplies()
    {
        return getFertAmount() > 0;
    }
}
