package vswe.stevescarts.modules.realtimers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import vswe.stevescarts.client.guis.GuiMinecart;
import vswe.stevescarts.containers.slots.SlotBase;
import vswe.stevescarts.containers.slots.SlotCake;
import vswe.stevescarts.entitys.EntityMinecartModular;
import vswe.stevescarts.helpers.Localization;
import vswe.stevescarts.helpers.ResourceHelper;
import vswe.stevescarts.api.modules.ISuppliesModule;
import vswe.stevescarts.api.modules.ModuleBase;

import javax.annotation.Nonnull;

public class ModuleCakeServer extends ModuleBase implements ISuppliesModule
{
    private int cooldown;
    private static final int MAX_CAKES = 10;
    private static final int SLICES_PER_CAKE = 6;
    private static final int MAX_TOTAL_SLICES = 66;
    private int[] rect;
    private EntityDataAccessor<Integer> BUFFER;

    public ModuleCakeServer(final EntityMinecartModular cart)
    {
        super(cart);
        cooldown = 0;
        rect = new int[]{40, 20, 13, 36};
    }

    @Override
    public void update()
    {
        super.update();
        if (!getCart().level.isClientSide)
        {
            if (getCart().hasCreativeSupplies())
            {
                if (cooldown >= 20)
                {
                    if (getCakeBuffer() < 66)
                    {
                        setCakeBuffer(getCakeBuffer() + 1);
                    }
                    cooldown = 0;
                }
                else
                {
                    ++cooldown;
                }
            }
            @Nonnull ItemStack item = getStack(0);
            if (!item.isEmpty() && item.getItem().equals(Items.CAKE) && getCakeBuffer() + 6 <= 66)
            {
                setCakeBuffer(getCakeBuffer() + 6);
                setStack(0, ItemStack.EMPTY);
            }
        }
    }

    private void setCakeBuffer(final int i)
    {
        updateDw(BUFFER, i);
    }

    private int getCakeBuffer()
    {
        if (isPlaceholder())
        {
            return 6;
        }
        return getDw(BUFFER);
    }

    @Override
    public int numberOfDataWatchers()
    {
        return 1;
    }

    @Override
    public void initDw()
    {
        BUFFER = createDw(EntityDataSerializers.INT);
        registerDw(BUFFER, 0);
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
    protected SlotBase getSlot(final int slotId, final int x, final int y)
    {
        return new SlotCake(getCart(), slotId, 8 + x * 18, 38 + y * 18);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void drawForeground(PoseStack matrixStack, GuiMinecart gui)
    {
        drawString(matrixStack, gui, Localization.MODULES.ATTACHMENTS.CAKE_SERVER.translate(), 8, 6, 4210752);
    }

    @Override
    protected void Save(final CompoundTag tagCompound, final int id)
    {
        tagCompound.putShort(generateNBTName("Cake", id), (short) getCakeBuffer());
    }

    @Override
    protected void Load(final CompoundTag tagCompound, final int id)
    {
        setCakeBuffer(tagCompound.getShort(generateNBTName("Cake", id)));
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void drawMouseOver(PoseStack matrixStack, GuiMinecart gui, final int x, final int y)
    {
        drawStringOnMouseOver(matrixStack, gui, Localization.MODULES.ATTACHMENTS.CAKES.translate(String.valueOf(getCakes()), String.valueOf(10)) + "\n" + Localization.MODULES.ATTACHMENTS.SLICES.translate(String.valueOf(getSlices()), String.valueOf(6)), x, y, rect);
    }

    private int getCakes()
    {
        if (getCakeBuffer() == 66)
        {
            return 10;
        }
        return getCakeBuffer() / 6;
    }

    private int getSlices()
    {
        if (getCakeBuffer() == 66)
        {
            return 6;
        }
        return getCakeBuffer() % 6;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void drawBackground(PoseStack matrixStack, GuiMinecart gui, final int x, final int y)
    {
        ResourceHelper.bindResource("/gui/cake.png");
        drawImage(matrixStack, gui, rect, 0, inRect(x, y, rect) ? rect[3] : 0);
        final int maxHeight = rect[3] - 2;
        int height = (int) (getCakes() / 10.0f * maxHeight);
        if (height > 0)
        {
            drawImage(matrixStack, gui, rect[0] + 1, rect[1] + 1 + maxHeight - height, rect[2], maxHeight - height, 7, height);
        }
        height = (int) (getSlices() / 6.0f * maxHeight);
        if (height > 0)
        {
            drawImage(matrixStack, gui, rect[0] + 9, rect[1] + 1 + maxHeight - height, rect[2] + 7, maxHeight - height, 3, height);
        }
    }

    @Override
    public int guiWidth()
    {
        return 75;
    }

    @Override
    public int guiHeight()
    {
        return 60;
    }

    @Override
    public boolean onInteractFirst(final Player entityplayer)
    {
        if (getCakeBuffer() > 0)
        {
            if (!getCart().level.isClientSide && entityplayer.canEat(false))
            {
                setCakeBuffer(getCakeBuffer() - 1);
                entityplayer.getFoodData().eat(2, 0.1F);
            }
            return true;
        }
        return false;
    }

    public int getRenderSliceCount()
    {
        int count = getSlices();
        if (count == 0 && getCakes() > 0)
        {
            count = 6;
        }
        return count;
    }

    @Override
    public boolean haveSupplies()
    {
        return getCakeBuffer() > 0;
    }
}
