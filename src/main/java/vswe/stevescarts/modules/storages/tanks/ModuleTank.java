package vswe.stevescarts.modules.storages.tanks;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.IFluidHandler;
import vswe.stevescarts.StevesCarts;
import vswe.stevescarts.client.guis.GuiBase;
import vswe.stevescarts.client.guis.GuiMinecart;
import vswe.stevescarts.containers.slots.SlotBase;
import vswe.stevescarts.containers.slots.SlotLiquidInput;
import vswe.stevescarts.containers.slots.SlotLiquidOutput;
import vswe.stevescarts.entitys.EntityMinecartModular;
import vswe.stevescarts.helpers.Localization;
import vswe.stevescarts.helpers.ResourceHelper;
import vswe.stevescarts.helpers.storages.ITankHolder;
import vswe.stevescarts.helpers.storages.SCTank;
import vswe.stevescarts.modules.storages.ModuleStorage;

import javax.annotation.Nonnull;
import java.util.Locale;

public class ModuleTank extends ModuleStorage implements IFluidTank, ITankHolder
{
    protected SCTank tank;
    private int tick;
    protected int[] tankBounds;
    private EntityDataAccessor<String> FLUID_NAME;
    private EntityDataAccessor<Integer> FLUID_AMOUNT;
    private EntityDataAccessor<Boolean> LOCKED;

    public ModuleTank(final EntityMinecartModular cart)
    {
        super(cart);
        tankBounds = new int[]{35, 20, 36, 51};
        tank = new SCTank(this, getTankSize(), 0);
    }

    protected int getTankSize()
    {
        return 0;
    }

    @Override
    public boolean hasGui()
    {
        return true;
    }

    @Override
    protected SlotBase getSlot(final int slotId, final int x, final int y)
    {
        if (y == 0)
        {
            return new SlotLiquidInput(getCart(), tank, -1, slotId, 8 + x * 18, 24 + y * 24);
        }
        return new SlotLiquidOutput(getCart(), slotId, 8 + x * 18, 24 + y * 24);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void drawForeground(PoseStack matrixStack, final GuiMinecart gui)
    {
        drawString(matrixStack, gui, getModuleName(), 8, 6, 4210752);
    }

    @Override
    public int getInventoryWidth()
    {
        return 1;
    }

    @Override
    public int getInventoryHeight()
    {
        return 2;
    }

    @Override
    public int guiWidth()
    {
        return 100;
    }

    @Override
    public int guiHeight()
    {
        return 80;
    }

    public boolean hasVisualTank()
    {
        return true;
    }

    @Override
    public void update()
    {
        super.update();
        if (tick-- <= 0)
        {
            tick = 5;
            if (!getCart().level.isClientSide)
            {
                tank.containerTransfer();
            }
            else if (!isPlaceholder())
            {
                if (getDw(FLUID_NAME).isEmpty())
                {
                    tank.setFluid(FluidStack.EMPTY);
                }
                else
                {
                    try
                    {
                        Fluid fluid = Registry.FLUID.get(new ResourceLocation(getDw(FLUID_NAME).toLowerCase(Locale.ROOT)));
                        if (fluid != null)
                        {
                            tank.setFluid(new FluidStack(fluid, getDw(FLUID_AMOUNT)));
                        }
                    } catch (Exception e)
                    {
                        StevesCarts.logger.error("Failed to load fluid from dw");
                        StevesCarts.logger.error(e);
                    }
                }
            }
        }
    }

    public void setFluid(FluidStack fluidStack)
    {
        tank.setFluid(fluidStack);
    }

    @Override
    @Nonnull
    public ItemStack getInputContainer(final int tankid)
    {
        return getStack(0);
    }

    @Override
    public void setInputContainer(final int tankid, ItemStack stack)
    {
        setStack(0, stack);
    }

    @Override
    public void addToOutputContainer(final int tankid, @Nonnull ItemStack item)
    {
        addStack(1, item);
    }

    @Override
    public void onFluidUpdated(final int tankid)
    {
        if (getCart().level.isClientSide)
        {
            return;
        }
        updateDw();
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void drawBackground(PoseStack matrixStack, final GuiMinecart gui, final int x, final int y)
    {
        tank.drawFluid(matrixStack, gui, gui.getGuiLeft() + getX() + tankBounds[0], gui.getGuiTop() + getY() + tankBounds[1]);
        ResourceHelper.bindResource("/gui/tank.png");
        drawImage(matrixStack, gui, tankBounds, 0, 0);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void drawMouseOver(PoseStack matrixStack, GuiMinecart gui, final int x, final int y)
    {
        drawStringOnMouseOver(matrixStack, gui, getTankInfo(), x, y, tankBounds);
    }

    protected String getTankInfo()
    {
        String str = tank.getMouseOver();
        if (getDw(LOCKED))
        {
            str = str + "\n\n" + Localization.MODULES.TANKS.LOCKED.translate() + "\n" + Localization.MODULES.TANKS.UNLOCK.translate();
        }
        else if (!tank.getFluid().isEmpty())
        {
            str = str + "\n\n" + Localization.MODULES.TANKS.LOCK.translate();
        }
        return str;
    }

    @Override
    public FluidStack getFluid()
    {
        return (tank.getFluid().isEmpty()) ? FluidStack.EMPTY : tank.getFluid().copy();
    }

    @Override
    public int getCapacity()
    {
        return getTankSize();
    }

    @Override
    public boolean isFluidValid(FluidStack stack)
    {
        return tank.isFluidValid(stack);
    }

    @Override
    public int fill(FluidStack resource, IFluidHandler.FluidAction action)
    {
        return tank.fill(resource, action);
    }

    @Nonnull
    @Override
    public FluidStack drain(int maxDrain, IFluidHandler.FluidAction action)
    {
        return tank.drain(maxDrain, action);
    }

    @Nonnull
    @Override
    public FluidStack drain(FluidStack resource, IFluidHandler.FluidAction action)
    {
        return tank.drain(resource, action);
    }

    @Override
    protected void Save(final CompoundTag tagCompound, final int id)
    {
        final CompoundTag compound = new CompoundTag();
        tank.getFluid().writeToNBT(compound);
        tagCompound.put(generateNBTName("Fluid", id), compound);
        tagCompound.putBoolean(generateNBTName("Locked", id), getDw(LOCKED));
    }

    @Override
    protected void Load(final CompoundTag tagCompound, final int id)
    {
        FluidStack fluidStack = FluidStack.loadFluidStackFromNBT(tagCompound.getCompound(generateNBTName("Fluid", id)));
        tank.setFluid(fluidStack);
        updateDw(LOCKED, tagCompound.getBoolean(generateNBTName("Locked", id)));
        updateDw();
    }

    @Override
    public int numberOfDataWatchers()
    {
        return 3;
    }

    protected void updateDw()
    {
        updateDw(FLUID_NAME, (tank.getFluid().isEmpty()) ? "" : tank.getFluid().getFluid().getRegistryName().toString());
        updateDw(FLUID_AMOUNT, (tank.getFluid().isEmpty()) ? -1 : tank.getFluid().getAmount());
    }

    @Override
    public void initDw()
    {
        FLUID_NAME = createDw(EntityDataSerializers.STRING);
        FLUID_AMOUNT = createDw(EntityDataSerializers.INT);
        LOCKED = createDw(EntityDataSerializers.BOOLEAN);
        registerDw(FLUID_NAME, (tank.getFluid().isEmpty()) ? "" : tank.getFluid().getFluid().getRegistryName().toString());
        registerDw(FLUID_AMOUNT, (tank.getFluid().isEmpty()) ? -1 : tank.getFluid().getAmount());
        registerDw(LOCKED, false);
    }

    public float getFluidRenderHeight()
    {
        if (tank.getFluid().isEmpty())
        {
            return 0.0f;
        }
        return tank.getFluidAmount() / getTankSize();
    }

    public boolean isCompletelyFilled()
    {
        return !getFluid().isEmpty() && getFluidAmount() >= getTankSize();
    }

    public boolean isCompletelyEmpty()
    {
        return getFluid().isEmpty() || getFluidAmount() == 0;
    }

    @Override
    public int getFluidAmount()
    {
        return (getFluid().isEmpty()) ? 0 : tank.getFluidAmount();
    }

    @Override
    protected int numberOfPackets()
    {
        return 1;
    }

    @Override
    protected void receivePacket(final int id, final byte[] data, final Player player)
    {
        if ((!getFluid().isEmpty() || getDw(LOCKED)))
        {
            setLocked(!getDw(LOCKED));

            if (!getDw(LOCKED) && !tank.getFluid().isEmpty() && tank.getFluid().getAmount() <= 0)
            {
                tank.setFluid(null);
                updateDw();
            }
        }
    }

    @Override
    public int numberOfGuiData()
    {
        return 1;
    }

    @Override
    protected void checkGuiData(final Object[] info)
    {
        updateGuiData(info, 0, (short) (tank.isLocked() ? 1 : 0));
        updateDw();
    }

    private void setLocked(boolean val)
    {
        if (!isPlaceholder())
        {
            updateDw(LOCKED, val);
        }
    }

    @Override
    public void mouseClicked(final GuiMinecart gui, final int x, final int y, final int button)
    {
        if (inRect(x, y, tankBounds))
        {
            byte data = (byte) button;
            if (Screen.hasShiftDown())
            {
                data |= 0x2;
            }
            sendPacket(0, data);
        }
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void drawImage(int tankid, GuiBase gui, TextureAtlasSprite sprite, int targetX, int targetY, int srcX, int srcY, int width, int height)
    {
        //TODO
        //		drawImage((GuiMinecart)gui, sprite, targetX, targetY, srcX, srcY, width, height);
    }
}
