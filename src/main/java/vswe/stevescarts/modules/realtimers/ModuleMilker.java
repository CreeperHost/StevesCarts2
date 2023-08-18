package vswe.stevescarts.modules.realtimers;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;
import vswe.stevescarts.api.modules.ModuleBase;
import vswe.stevescarts.client.guis.GuiMinecart;
import vswe.stevescarts.api.slots.SlotStevesCarts;
import vswe.stevescarts.containers.slots.SlotMilker;
import vswe.stevescarts.entities.EntityMinecartModular;

import javax.annotation.Nonnull;

public class ModuleMilker extends ModuleBase
{
    int cooldown;
    int milkbuffer;

    public ModuleMilker(final EntityMinecartModular cart)
    {
        super(cart);
        cooldown = 0;
        milkbuffer = 0;
    }

    @Override
    public void update()
    {
        super.update();
        if (cooldown <= 0)
        {
            if (!getCart().level().isClientSide && getCart().hasFuel())
            {
                generateMilk();
                depositeMilk();
            }
            cooldown = 20;
        }
        else
        {
            --cooldown;
        }
    }

    private void depositeMilk()
    {
        if (milkbuffer > 0)
        {
            final FluidStack ret = FluidUtil.getFluidContained(new ItemStack(Items.MILK_BUCKET)).get();
            if (ret != null)
            {
                ret.setAmount(milkbuffer);
                milkbuffer -= getCart().fill(ret, IFluidHandler.FluidAction.EXECUTE);
            }
            if (milkbuffer == 1000)
            {
                for (int i = 0; i < getInventorySize(); ++i)
                {
                    @Nonnull ItemStack bucket = getStack(i);
                    if (!bucket.isEmpty() && bucket.getItem() == Items.BUCKET)
                    {
                        @Nonnull ItemStack milk = new ItemStack(Items.MILK_BUCKET);
                        getCart().addItemToChest(milk);
                        if (milk.getCount() <= 0)
                        {
                            milkbuffer = 0;
                            @Nonnull ItemStack itemStack = bucket;
                            itemStack.shrink(1);
                            if (itemStack.getCount() <= 0)
                            {
                                setStack(i, ItemStack.EMPTY);
                            }
                        }
                    }
                }
            }
        }
    }

    private void generateMilk()
    {
        if (milkbuffer < 1000)
        {
            if (!getCart().getPassengers().isEmpty())
            {
                final Entity rider = getCart().getPassengers().get(0);
                if (rider != null && rider instanceof Cow)
                {
                    milkbuffer = Math.min(milkbuffer + 75, 1000);
                }
            }
        }
    }

    @Override
    public boolean hasGui()
    {
        return true;
    }

    @Override
    protected int getInventoryWidth()
    {
        return 2;
    }

    @Override
    protected SlotStevesCarts getSlot(final int slotId, final int x, final int y)
    {
        return new SlotMilker(getCart(), slotId, 8 + x * 18, 23 + y * 18);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void drawForeground(GuiGraphics guiGraphics, GuiMinecart gui)
    {
        drawString(guiGraphics, gui, getModuleName(), 8, 6, 4210752);
    }

    @Override
    protected void Save(final CompoundTag tagCompound, final int id)
    {
        tagCompound.putShort(generateNBTName("Milk", id), (short) milkbuffer);
    }

    @Override
    protected void Load(final CompoundTag tagCompound, final int id)
    {
        milkbuffer = tagCompound.getShort(generateNBTName("Milk", id));
    }
}
