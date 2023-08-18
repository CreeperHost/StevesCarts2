package vswe.stevescarts.modules.addons;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;
import vswe.stevescarts.api.modules.template.ModuleAddon;
import vswe.stevescarts.client.guis.GuiMinecart;
import vswe.stevescarts.api.slots.SlotStevesCarts;
import vswe.stevescarts.containers.slots.SlotIncinerator;
import vswe.stevescarts.entities.EntityMinecartModular;

import javax.annotation.Nonnull;

public class ModuleIncinerator extends ModuleAddon
{
    public ModuleIncinerator(final EntityMinecartModular cart)
    {
        super(cart);
    }

    public void incinerate(@Nonnull ItemStack item)
    {
        if (isItemValid(item))
        {
            if (getIncinerationCost() != 0)
            {
                int amount = item.getCount() * getIncinerationCost();
                item.shrink(amount);
            }
            else
            {
                item.setCount(0);
            }
        }
    }

    protected int getIncinerationCost()
    {
        return 3;
    }

    protected boolean isItemValid(@Nonnull ItemStack item)
    {
        if (!item.isEmpty())
        {
            for (int i = 0; i < getInventorySize(); ++i)
            {
                if (!getStack(i).isEmpty() && ItemStack.isSameItem(item, getStack(i)))
                {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean hasGui()
    {
        return true;
    }

    @Override
    public void drawForeground(GuiGraphics guiGraphics, GuiMinecart gui)
    {
        drawString(guiGraphics, gui, getModuleName(), 8, 6, 4210752);
    }

    @Override
    protected int getInventoryWidth()
    {
        return 4;
    }

    @Override
    protected SlotStevesCarts getSlot(final int slotId, final int x, final int y)
    {
        return new SlotIncinerator(getCart(), slotId, 8 + x * 18, 23 + y * 18);
    }
}
