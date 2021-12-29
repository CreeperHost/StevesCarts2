package vswe.stevescarts.modules.realtimers;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.world.Explosion;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import vswe.stevescarts.client.guis.GuiMinecart;
import vswe.stevescarts.containers.slots.SlotBase;
import vswe.stevescarts.containers.slots.SlotExplosion;
import vswe.stevescarts.entitys.EntityMinecartModular;
import vswe.stevescarts.helpers.ComponentTypes;
import vswe.stevescarts.helpers.Localization;
import vswe.stevescarts.helpers.ResourceHelper;
import vswe.stevescarts.modules.ModuleBase;

public class ModuleDynamite extends ModuleBase
{
    private boolean markerMoving;
    private int fuseStartX;
    private int fuseStartY;
    private final int maxFuseLength = 150;

    private DataParameter<Byte> FUSE;
    private DataParameter<Byte> FUSE_LENGTH;
    private DataParameter<Byte> EXPLOSION;

    public ModuleDynamite(final EntityMinecartModular cart)
    {
        super(cart);
        fuseStartX = super.guiWidth() + 5;
        fuseStartY = 27;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void drawForeground(MatrixStack matrixStack, GuiMinecart gui)
    {
        drawString(matrixStack, gui, Localization.MODULES.ATTACHMENTS.EXPLOSIVES.translate(), 8, 6, 4210752);
    }

    @Override
    protected SlotBase getSlot(final int slotId, final int x, final int y)
    {
        return new SlotExplosion(getCart(), slotId, 8 + x * 18, 23 + y * 18);
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
    public void activatedByRail(final int x, final int y, final int z, final boolean active)
    {
        if (active && getFuse() == 0)
        {
            prime();
        }
    }

    @Override
    public void update()
    {
        super.update();
        if (isPlaceholder())
        {
            if (getFuse() == 0 && getSimInfo().getShouldExplode())
            {
                setFuse(1);
            }
            else if (getFuse() != 0 && !getSimInfo().getShouldExplode())
            {
                setFuse(0);
            }
        }
        if (getFuse() > 0)
        {
            setFuse(getFuse() + 1);
            if (getFuse() == getFuseLength())
            {
                explode();
                if (!isPlaceholder())
                {
                    getCart().remove();
                }
            }
        }
    }

    @Override
    public int guiWidth()
    {
        return super.guiWidth() + 136;
    }

    private int[] getMovableMarker()
    {
        return new int[]{fuseStartX + (int) (105.0f * (1.0f - getFuseLength() / 150.0f)), fuseStartY, 4, 10};
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void drawBackground(MatrixStack matrixStack, GuiMinecart gui, final int x, final int y)
    {
        ResourceHelper.bindResource("/gui/explosions.png");
        drawImage(matrixStack, gui, fuseStartX, fuseStartY + 3, 12, 0, 105, 4);
        drawImage(matrixStack, gui, fuseStartX + 105, fuseStartY - 4, 0, 10, 16, 16);
        drawImage(matrixStack, gui, fuseStartX + (int) (105.0f * (1.0f - (getFuseLength() - getFuse()) / 150.0f)), fuseStartY, isPrimed() ? 8 : 4, 0, 4, 10);
        drawImage(matrixStack, gui, getMovableMarker(), 0, 0);
    }

    @Override
    public void mouseClicked(final GuiMinecart gui, final int x, final int y, final int button)
    {
        if (button == 0 && getFuse() == 0 && inRect(x, y, getMovableMarker()))
        {
            markerMoving = true;
        }
    }

    @Override
    public void mouseMovedOrUp(final GuiMinecart gui, final int x, final int y, final int button)
    {
        if (getFuse() != 0)
        {
            markerMoving = false;
        }
        else if (markerMoving)
        {
            int tempfuse = 150 - (int) ((x - fuseStartX) / 0.7f);
            if (tempfuse < 2)
            {
                tempfuse = 2;
            }
            else if (tempfuse > 150)
            {
                tempfuse = 150;
            }
            sendPacket(0, (byte) tempfuse);
        }
        if (button != -1)
        {
            markerMoving = false;
        }
    }

    private boolean isPrimed()
    {
        return getFuse() / 5 % 2 == 0 && getFuse() != 0;
    }

    private void explode()
    {
        if (isPlaceholder())
        {
            setFuse(1);
        }
        else
        {
            final float f = explosionSize();
            setStack(0, ItemStack.EMPTY);
            getCart().level.explode(null, getCart().blockPosition().getX(), getCart().blockPosition().getY(), getCart().blockPosition().getZ(), f, Explosion.Mode.BREAK);
        }
    }

    @Override
    public void onInventoryChanged()
    {
        super.onInventoryChanged();
        createExplosives();
    }

    @Override
    public boolean dropOnDeath()
    {
        return getFuse() == 0;
    }

    @Override
    public void onDeath()
    {
        if (getFuse() > 0 && getFuse() < getFuseLength())
        {
            explode();
        }
    }

    public float explosionSize()
    {
        if (isPlaceholder())
        {
            return getSimInfo().getExplosionSize() / 2.5f;
        }
        return getDw(EXPLOSION) / 2.5f;
    }

    public void createExplosives()
    {
        if (isPlaceholder() || getCart().level.isClientSide)
        {
            return;
        }
        int f = 8;
        if (ComponentTypes.DYNAMITE.isStackOfType(getStack(0)))
        {
            f += getStack(0).getCount() * 2;
        }
        updateDw(EXPLOSION, (byte) f);
    }

    @Override
    public int numberOfDataWatchers()
    {
        return 3;
    }

    @Override
    public void initDw()
    {
        FUSE = createDw(DataSerializers.BYTE);
        FUSE_LENGTH = createDw(DataSerializers.BYTE);
        EXPLOSION = createDw(DataSerializers.BYTE);
        registerDw(FUSE, (byte) 0);
        registerDw(FUSE_LENGTH, (byte) 70);
        registerDw(EXPLOSION, (byte) 8);
    }

    public int getFuse()
    {
        if (isPlaceholder())
        {
            return getSimInfo().fuse;
        }
        final int val = getDw(FUSE);
        if (val < 0)
        {
            return val + 256;
        }
        return val;
    }

    private void setFuse(final int val)
    {
        if (isPlaceholder())
        {
            getSimInfo().fuse = val;
        }
        else
        {
            updateDw(FUSE, (byte) val);
        }
    }

    public void setFuseLength(int val)
    {
        if (val > 150)
        {
            val = 150;
        }
        updateDw(FUSE_LENGTH, (byte) val);
    }

    public int getFuseLength()
    {
        if (isPlaceholder())
        {
            return getSimInfo().getFuseLength();
        }
        final int val = getDw(FUSE_LENGTH);
        if (val < 0)
        {
            return val + 256;
        }
        return val;
    }

    public void prime()
    {
        setFuse(1);
    }

    protected int getMaxFuse()
    {
        return 150;
    }

    @Override
    public int numberOfPackets()
    {
        return 1;
    }

    @Override
    protected void receivePacket(final int id, final byte[] data, final PlayerEntity player)
    {
        if (id == 0)
        {
            setFuseLength(data[0]);
        }
    }

    @Override
    protected void Save(final CompoundNBT tagCompound, final int id)
    {
        tagCompound.putShort(generateNBTName("FuseLength", id), (short) getFuseLength());
        tagCompound.putShort(generateNBTName("Fuse", id), (short) getFuse());
    }

    @Override
    protected void Load(final CompoundNBT tagCompound, final int id)
    {
        setFuseLength(tagCompound.getShort(generateNBTName("FuseLength", id)));
        setFuse(tagCompound.getShort(generateNBTName("Fuse", id)));
        createExplosives();
    }
}
