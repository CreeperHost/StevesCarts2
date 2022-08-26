package vswe.stevescarts.modules.engines;

import com.mojang.blaze3d.vertex.PoseStack;
import net.creeperhost.polylib.helpers.FuelHelper;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import vswe.stevescarts.api.modules.template.ModuleEngine;
import vswe.stevescarts.client.guis.GuiMinecart;
import vswe.stevescarts.containers.slots.SlotBase;
import vswe.stevescarts.containers.slots.SlotFuel;
import vswe.stevescarts.entitys.EntityMinecartModular;
import vswe.stevescarts.helpers.Localization;

import javax.annotation.Nonnull;

public abstract class ModuleCoalBase extends ModuleEngine
{
    private int fireCoolDown;
    private int fireIndex;

    private EntityDataAccessor<Integer> PRIORITY;

    public ModuleCoalBase(final EntityMinecartModular cart)
    {
        super(cart);
    }

    @Override
    protected EntityDataAccessor<Integer> getPriorityDw()
    {
        return PRIORITY;
    }

    @Override
    public void initDw()
    {
        PRIORITY = createDw(EntityDataSerializers.INT);
        super.initDw();
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
                setFuelLevel(getFuelLevel() + FuelHelper.getItemBurnTime(getStack(i)));
                if (getFuelLevel() > consumption)
                {
                    if (getStack(i).isEmpty())
                    {
                        break;
                    }
                    if (getStack(i).hasCraftingRemainingItem())
                    {
                        setStack(i, new ItemStack(getStack(i).getItem().getCraftingRemainingItem()));
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
                totalfuel += FuelHelper.getItemBurnTime(getStack(i)) * getStack(i).getCount();
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
    public void smoke()
    {
        double oX = 0.0;
        double oZ = 0.0;
        if (getCart().getDeltaMovement().x != 0.0)
        {
            oX = ((getCart().getDeltaMovement().x > 0.0) ? -1 : 1);
        }
        if (getCart().getDeltaMovement().z != 0.0)
        {
            oZ = ((getCart().getDeltaMovement().z > 0.0) ? -1 : 1);
        }
        if (getCart().random.nextInt(2) == 0)
        {
            getCart().level.addParticle(ParticleTypes.SMOKE, getCart().getExactPosition().getX() + oX * 0.85, getCart().getExactPosition().getY() + 0.12, getCart().getExactPosition().getZ() + oZ * 0.85, 0.0, 0.0, 0.0);
        }
        if (getCart().random.nextInt(30) == 0)
        {
            getCart().level.addParticle(ParticleTypes.FLAME, getCart().getExactPosition().getX() + oX * 0.75, getCart().getExactPosition().getY() + 0.15, getCart().getExactPosition().getZ() + oZ * 0.75, 0, 0, 0);
        }
    }

    @Override
    protected SlotBase getSlot(final int slotId, final int x, final int y)
    {
        return new SlotFuel(getCart(), slotId, 8 + x * 18, 23 + 18 * y);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void drawForeground(PoseStack matrixStack, GuiMinecart gui)
    {
        drawString(matrixStack, gui, Localization.MODULES.ENGINES.COAL.translate(), 8, 6, 4210752);
        String strfuel = Localization.MODULES.ENGINES.NO_FUEL.translate();
        if (getFuelLevel() > 0)
        {
            strfuel = "Fuel: " + getFuelLevel();//Localization.MODULES.ENGINES.FUEL.translate(String.valueOf(getFuelLevel()));
        }
        drawString(matrixStack, gui, strfuel, 8, 48, 4210752);
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
            fireIndex = getCart().random.nextInt(4) + 1;
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
    protected void Save(final CompoundTag tagCompound, final int id)
    {
        super.Save(tagCompound, id);
        tagCompound.putShort(generateNBTName("Fuel", id), (short) getFuelLevel());
    }

    @Override
    protected void Load(final CompoundTag tagCompound, final int id)
    {
        super.Load(tagCompound, id);
        setFuelLevel(tagCompound.getShort(generateNBTName("Fuel", id)));
        if (getFuelLevel() < 0)
        {
            setFuelLevel(getFuelLevel() + 65536);
        }
    }

    public abstract double getFuelMultiplier();
}
