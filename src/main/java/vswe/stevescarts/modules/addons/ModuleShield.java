package vswe.stevescarts.modules.addons;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.util.DamageSource;
import vswe.stevescarts.client.guis.GuiMinecart;
import vswe.stevescarts.entitys.EntityMinecartModular;
import vswe.stevescarts.helpers.Localization;
import vswe.stevescarts.helpers.ResourceHelper;
import vswe.stevescarts.modules.IActivatorModule;

public class ModuleShield extends ModuleAddon implements IActivatorModule
{
    private boolean shield;
    private float shieldDistance;
    private float shieldAngle;
    private int[] buttonRect;
    private DataParameter<Boolean> STATUS;
    private boolean setup;

    public ModuleShield(final EntityMinecartModular cart)
    {
        super(cart);
        shield = true;
        shieldDistance = 18.0f;
        buttonRect = new int[]{20, 20, 24, 12};
    }

    protected boolean shieldSetting()
    {
        return getShieldStatus();
    }

    public float getShieldDistance()
    {
        return shieldDistance;
    }

    public float getShieldAngle()
    {
        return shieldAngle;
    }

    public boolean hasShield()
    {
        return shield;
    }

    @Override
    public void update()
    {
        super.update();
        if (getCart().level.isClientSide && !setup)
        {
            if (isPlaceholder() || !getDw(STATUS))
            {
                shieldDistance = 0;
                shield = false;
            }
            setup = true;
        }
        if (hasShield() && !getCart().hasFuelForModule() && !getCart().level.isClientSide)
        {
            setShieldStatus(false);
        }
        if (hasShield())
        {
            getCart().clearFire();
        }
        if (!getShieldStatus() && shieldDistance > 0.0f)
        {
            shieldDistance -= 0.25f;
            if (shieldDistance <= 0.0f)
            {
                shield = false;
            }
        }
        else if (getShieldStatus() && shieldDistance < 18.0f)
        {
            shieldDistance += 0.25f;
            shield = true;
        }
        if (shield)
        {
            shieldAngle = (float) ((shieldAngle + 0.125f) % 314.1592653589793);
        }
    }

    @Override
    public boolean receiveDamage(DamageSource source, float val)
    {
        return !hasShield();
    }

    @Override
    public boolean hasSlots()
    {
        return false;
    }

    @Override
    public boolean hasGui()
    {
        return true;
    }

    @Override
    public int guiWidth()
    {
        return 75;
    }

    @Override
    public int guiHeight()
    {
        return 35;
    }

    @Override
    public void drawForeground(MatrixStack matrixStack, GuiMinecart gui)
    {
        drawString(matrixStack, gui, getModuleName(), 8, 6, 4210752);
    }

    public void setShieldStatus(final boolean val)
    {
        if (!isPlaceholder())
        {
            updateDw(STATUS, val);
        }
    }

    private boolean getShieldStatus()
    {
        if (isPlaceholder())
        {
            return getSimInfo().getShieldActive();
        }
        return getDw(STATUS);
    }

    @Override
    public void drawBackground(MatrixStack matrixStack, GuiMinecart gui, final int x, final int y)
    {
        ResourceHelper.bindResource("/gui/shield.png");
        final int imageID = getShieldStatus() ? 1 : 0;
        int borderID = 0;
        if (inRect(x, y, buttonRect))
        {
            borderID = 1;
        }
        drawImage(matrixStack, gui, buttonRect, 0, buttonRect[3] * borderID);
        final int srcY = buttonRect[3] * 2 + imageID * (buttonRect[3] - 2);
        drawImage(matrixStack, gui, buttonRect[0] + 1, buttonRect[1] + 1, 0, srcY, buttonRect[2] - 2, buttonRect[3] - 2);
    }

    @Override
    public void drawMouseOver(MatrixStack matrixStack, GuiMinecart gui, final int x, final int y)
    {
        drawStringOnMouseOver(matrixStack, gui, getStateName(), x, y, buttonRect);
    }

    private String getStateName()
    {
        return Localization.MODULES.ADDONS.SHIELD.translate(getShieldStatus() ? "1" : "0");
    }

    @Override
    public void mouseClicked(final GuiMinecart gui, final int x, final int y, final int button)
    {
        if (button == 0 && inRect(x, y, buttonRect))
        {
            sendPacket(0);
        }
    }

    @Override
    protected void receivePacket(final int id, final byte[] data, final PlayerEntity player)
    {
        if (id == 0)
        {
            updateDw(STATUS, !getShieldStatus());
        }
    }

    @Override
    public int numberOfPackets()
    {
        return 1;
    }

    @Override
    public int numberOfDataWatchers()
    {
        return 2;
    }

    @Override
    public void initDw()
    {
        STATUS = createDw(DataSerializers.BOOLEAN);
        registerDw(STATUS, false);
    }

    @Override
    public int getConsumption(final boolean isMoving)
    {
        return hasShield() ? 20 : super.getConsumption(isMoving);
    }

    @Override
    protected void Save(final CompoundNBT tagCompound, final int id)
    {
        tagCompound.putBoolean(generateNBTName("Shield", id), getShieldStatus());
    }

    @Override
    protected void Load(final CompoundNBT tagCompound, final int id)
    {
        setShieldStatus(tagCompound.getBoolean(generateNBTName("Shield", id)));
    }

    @Override
    public void doActivate(final int id)
    {
        setShieldStatus(true);
    }

    @Override
    public void doDeActivate(final int id)
    {
        setShieldStatus(false);
    }

    @Override
    public boolean isActive(final int id)
    {
        return getShieldStatus();
    }
}
