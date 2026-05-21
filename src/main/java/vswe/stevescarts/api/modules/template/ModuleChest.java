package vswe.stevescarts.api.modules.template;

import net.creeperhost.polylib.data.serializable.BooleanData;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import vswe.stevescarts.api.slots.SlotChest;
import vswe.stevescarts.api.slots.SlotStevesCarts;
import vswe.stevescarts.client.guis.GuiMinecart;
import vswe.stevescarts.entities.ModularMinecart;
import vswe.stevescarts.polylib.EntityData;

public abstract class ModuleChest extends ModuleStorage
{
    private float chestAngle;
    private final EntityData<Boolean> isOpen = new EntityData<>(getCart(), new BooleanData(false));

    public ModuleChest(ModularMinecart cart)
    {
        super(cart);
    }

    @Override
    public void update()
    {
        super.update();
        handleChest();
    }

    @Override
    public boolean hasGui()
    {
        return true;
    }

    @Override
    protected SlotStevesCarts getSlot(final int slotId, final int x, final int y)
    {
        return new SlotChest(getCart(), slotId, 8 + x * 18, 16 + y * 18);
    }

    @Override
    public void drawForeground(GuiGraphics guiGraphics, GuiMinecart gui)
    {
        drawString(guiGraphics, gui, getModuleName(), 8, 6, 4210752);
    }

    @Override
    public int guiWidth()
    {
        return 15 + getInventoryWidth() * 18;
    }

    @Override
    public int guiHeight()
    {
        return 20 + getInventoryHeight() * 18;
    }

    public float getChestAngle()
    {
        return chestAngle;
    }

    protected boolean lidClosed()
    {
        return chestAngle <= 0.0f;
    }

    protected float getLidSpeed()
    {
        return 0.15707964f;
    }

    protected float chestFullyOpenAngle()
    {
        return 1.3744469f;
    }

    protected boolean hasVisualChest()
    {
        return true;
    }

    protected boolean playChestSound()
    {
        return hasVisualChest();
    }

    public void openChest()
    {
        if (hasVisualChest())
        {
            isOpen.set(true);
        }
    }

    public void closeChest()
    {
        if (hasVisualChest())
        {
            isOpen.set(false);
        }
    }

    protected boolean isChestActive()
    {
        if (!hasVisualChest())
        {
            return false;
        }
        if (isPlaceholder())
        {
            return getSimInfo().getChestActive();
        }
        return isOpen.get();
    }

    protected void handleChest()
    {
        if (!hasVisualChest())
        {
            return;
        }
        if (isChestActive() && lidClosed() && playChestSound())
        {
            getCart().level().playSound(null, getCart().blockPosition(), SoundEvents.CHEST_OPEN, SoundSource.PLAYERS, 0.5F, getCart().level().random.nextFloat() * 0.1f + 0.9f);
        }
        if (isChestActive() && chestAngle < chestFullyOpenAngle())
        {
            chestAngle += getLidSpeed();
            if (chestAngle > chestFullyOpenAngle())
            {
                chestAngle = chestFullyOpenAngle();
            }
        }
        else if (!isChestActive() && !lidClosed())
        {
            final float lastAngle = chestAngle;
            chestAngle -= getLidSpeed();
            if (chestAngle < 1.1780972450961724 && lastAngle >= 1.1780972450961724 && playChestSound())
            {
                getCart().level().playSound(null, getCart().blockPosition(), SoundEvents.CHEST_CLOSE, SoundSource.PLAYERS, 0.5F, getCart().level().random.nextFloat() * 0.1f + 0.9f);
            }
            if (chestAngle < 0.0f)
            {
                chestAngle = 0.0f;
            }
        }
    }

    public boolean isCompletelyFilled()
    {
        for (int i = 0; i < getInventorySize(); ++i)
        {
            if (getStack(i).isEmpty())
            {
                return false;
            }
        }
        return true;
    }

    public boolean isCompletelyEmpty()
    {
        for (int i = 0; i < getInventorySize(); ++i)
        {
            if (!getStack(i).isEmpty())
            {
                return false;
            }
        }
        return true;
    }
}
