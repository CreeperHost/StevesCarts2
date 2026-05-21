package vswe.stevescarts.modules.engines;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import vswe.stevescarts.StevesCarts;
import vswe.stevescarts.api.modules.template.ModuleEngine;
import vswe.stevescarts.client.guis.GuiMinecart;
import vswe.stevescarts.api.slots.SlotStevesCarts;
import vswe.stevescarts.containers.slots.SlotFuel;
import vswe.stevescarts.entities.ModularMinecart;
import vswe.stevescarts.helpers.Localization;
import vswe.stevescarts.polylib.FuelHelper;

import javax.annotation.Nonnull;

public abstract class ModuleCoalBase extends ModuleEngine
{
    private int fireCoolDown;
    private int fireIndex;

    public ModuleCoalBase(ModularMinecart cart)
    {
        super(cart);
    }

    @Override
    protected void loadFuel()
    {
        final int consumption = getCart().getConsumption(true) * 2;
        if (getFuelLevel() <= consumption)
        {
            int i = 0;
            while (i < getInventorySize())
            {
                setFuelLevel(getFuelLevel() + FuelHelper.getItemBurnTime(getStack(i), getCart().level()));
                if (getFuelLevel() > consumption)
                {
                    if (getStack(i).isEmpty())
                    {
                        break;
                    }
                    ItemStack remainder = getStack(i).getCraftingRemainder();
                    if (!remainder.isEmpty())
                    {
                        setStack(i, remainder);
                    }
                    else
                    {
                        @Nonnull ItemStack stack = getStack(i);
                        stack.shrink(1);
                    }
                    if (getStack(i).getCount() == 0)
                    {
                        setStack(i, ItemStack.EMPTY);
                        break;
                    }
                    break;
                }
                else
                {
                    ++i;
                }
            }
        }
    }

    @Override
    public int getTotalFuel()
    {
        int totalfuel = getFuelLevel();
        for (int i = 0; i < getInventorySize(); ++i)
        {
            if (!getStack(i).isEmpty())
            {
                totalfuel += FuelHelper.getItemBurnTime(getStack(i), getCart().level()) * getStack(i).getCount();
            }
        }
        return totalfuel;
    }

    @Override
    public float[] getGuiBarColor()
    {
        return new float[]{0.0f, 0.0f, 0.0f};
    }

    @Override
    public void smoke() {
        Vec3 velocity = getCart().getEffectiveVelocity().normalize();
        if (getCart().getRandom().nextInt(2) == 0) {
            getCart().level().addParticle(ParticleTypes.SMOKE, getCart().getX() - velocity.x * 0.85, getCart().getY() + 0.12, getCart().getZ() - velocity.z * 0.85, 0.0, 0.0, 0.0);
        }
        if (getCart().getRandom().nextInt(30) == 0) {
            getCart().level().addParticle(ParticleTypes.FLAME, getCart().getX() - velocity.x * 0.75, getCart().getY() + 0.15, getCart().getZ() - velocity.z * 0.75, 0, 0, 0);
        }
    }

    @Override
    protected SlotStevesCarts getSlot(final int slotId, final int x, final int y)
    {
        return new SlotFuel(getCart(), slotId, 8 + x * 18, 23 + 18 * y);
    }

    @Override
    public void drawForeground(GuiGraphics guiGraphics, GuiMinecart gui)
    {
        drawString(guiGraphics, gui, Localization.MODULES.ENGINES.COAL.translate(), 8, 6, 4210752);
        String strfuel = Localization.MODULES.ENGINES.NO_FUEL.translate();
        if (getFuelLevel() > 0)
        {
            strfuel = "Fuel: " + getFuelLevel();
        }
        drawString(guiGraphics, gui, strfuel, 8, 48, 4210752);
    }

    @Override
    public int numberOfGuiData()
    {
        return 1;
    }

    @Override
    protected void checkGuiData(final Object[] info)
    {
        updateGuiData(info, 0, (short) getFuelLevel());
    }

    @Override
    public void receiveGuiData(final int id, final short data)
    {
        if (id == 0)
        {
            setFuelLevel(data);
            if (getFuelLevel() < 0)
            {
                setFuelLevel(getFuelLevel() + 65536);
            }
        }
    }

    @Override
    public void update()
    {
        super.update();
        if (fireCoolDown <= 0)
        {
            fireIndex = getCart().getRandom().nextInt(4) + 1;
            fireCoolDown = 2;
        }
        else
        {
            --fireCoolDown;
        }
    }

    public int getFireIndex()
    {
        if (getCart().isEngineBurning())
        {
            return fireIndex;
        }
        return 0;
    }

    @Override
    protected void save(ValueOutput output, int id) {
        super.save(output, id);
        output.putShort(generateNBTName("Fuel", id), (short) getFuelLevel());
    }

    @Override
    protected void load(ValueInput input, int id) {
        super.load(input, id);
        setFuelLevel(input.getShortOr(generateNBTName("Fuel", id), (short) 0));
        if (getFuelLevel() < 0) {
            setFuelLevel(getFuelLevel() + 65536);
        }
    }

    public abstract double getFuelMultiplier();
}
