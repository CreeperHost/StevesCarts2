package vswe.stevescarts.modules.storages.tanks;

import net.creeperhost.polylib.data.serializable.IntData;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.IFluidTank;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import vswe.stevescarts.StevesCarts;
import vswe.stevescarts.StevesCartsClient;
import vswe.stevescarts.api.modules.template.ModuleStorage;
import vswe.stevescarts.api.slots.SlotStevesCarts;
import vswe.stevescarts.client.guis.GuiMinecart;
import vswe.stevescarts.containers.slots.SlotLiquidInput;
import vswe.stevescarts.containers.slots.SlotLiquidOutput;
import vswe.stevescarts.entities.ModularMinecart;
import vswe.stevescarts.helpers.Localization;
import vswe.stevescarts.helpers.ResourceHelper;
import vswe.stevescarts.helpers.storages.ITankHolder;
import vswe.stevescarts.helpers.storages.SCTank;
import vswe.stevescarts.polylib.EntityData;
import vswe.stevescarts.polylib.FluidDataNeo;
import vswe.stevescarts.polylib.StringData;

import javax.annotation.Nonnull;
import java.util.Locale;

public class ModuleTank extends ModuleStorage implements IFluidTank, ITankHolder {
    private final EntityData<String> fluidName = new EntityData<>(getCart(), new StringData(""));
    private final EntityData<Integer> fluidAmount = new EntityData<>(getCart(), new IntData(-1));
    private final EntityData<FluidStack> locked = new EntityData<>(getCart(), new FluidDataNeo());
    protected SCTank tank;
    protected int[] tankBounds;
    private int tick;

    public ModuleTank(ModularMinecart cart) {
        super(cart);
        tankBounds = new int[]{35, 20, 36, 51};
        tank = new SCTank(this, getTankSize(), 0);
    }

    protected int getTankSize() {
        return 0;
    }

    @Override
    public boolean hasGui() {
        return true;
    }

    @Override
    protected SlotStevesCarts getSlot(final int slotId, final int x, final int y) {
        if (y == 0) {
            return new SlotLiquidInput(getCart(), tank, -1, slotId, 8 + x * 18, 24 + y * 24);
        }
        return new SlotLiquidOutput(getCart(), slotId, 8 + x * 18, 24 + y * 24);
    }

    @Override
    public void drawForeground(GuiGraphicsExtractor GuiGraphicsExtractor, final GuiMinecart gui) {
        drawString(GuiGraphicsExtractor, gui, getModuleName(), 8, 6, 4210752);
    }

    @Override
    public int getInventoryWidth() {
        return 1;
    }

    @Override
    public int getInventoryHeight() {
        return 2;
    }

    @Override
    public int guiWidth() {
        return 100;
    }

    @Override
    public int guiHeight() {
        return 80;
    }

    public boolean hasVisualTank() {
        return true;
    }

    @Override
    public void update() {
        super.update();
        if (tick-- <= 0) {
            tick = 5;
            if (!getCart().level().isClientSide()) {
                tank.containerTransfer(locked.get());
            } else if (!isPlaceholder()) {
                if (fluidName.get().isEmpty()) {
                    tank.setFluid(FluidStack.EMPTY);
                } else {
                    try {
                        Fluid fluid = BuiltInRegistries.FLUID.getValue(Identifier.parse(fluidName.get().toLowerCase(Locale.ROOT)));
                        if (fluid != null && fluid != Fluids.EMPTY) {
                            tank.setFluid(new FluidStack(fluid, fluidAmount.get()));
                        }
                    } catch (Exception e) {
                        StevesCarts.LOGGER.error("Failed to load fluid from dw");
                        StevesCarts.LOGGER.error(e);
                    }
                }
            }
        }
    }

    @Override
    @Nonnull
    public ItemStack getInputContainer(final int tankid) {
        return getStack(0);
    }

    @Override
    public void setInputContainer(final int tankid, ItemStack stack) {
        setStack(0, stack);
    }

    @Override
    public void addToOutputContainer(final int tankid, @Nonnull ItemStack item) {
        addStack(1, item);
    }

    @Override
    public void onFluidUpdated(final int tankid) {
        if (getCart().level().isClientSide()) {
            return;
        }
        updateData();
    }

    @Override
    public void drawBackground(GuiGraphicsExtractor GuiGraphicsExtractor, final GuiMinecart gui, final int x, final int y) {
        tank.drawFluid(GuiGraphicsExtractor, gui.getLeftPos(), gui.getGuiTop(), tankBounds[0], tankBounds[1]);
        drawImage(GuiGraphicsExtractor, ResourceHelper.getResource("/gui/tank.png"), gui, tankBounds, 0, 0);
    }

    @Override
    public void drawMouseOver(GuiGraphicsExtractor GuiGraphicsExtractor, GuiMinecart gui, final int x, final int y) {
        drawStringOnMouseOver(GuiGraphicsExtractor, gui, getTankInfo(), x, y, tankBounds);
    }

    protected String getTankInfo() {
        String str = tank.getMouseOver();
        if (!locked.get().isEmpty()) {
            str = str + "\n\n" + Localization.MODULES.TANKS.LOCKED.translate() + " " + locked.get().getHoverName().getString() + "\n" + Localization.MODULES.TANKS.UNLOCK.translate();
        } else if (!tank.getFluid().isEmpty()) {
            str = str + "\n\n" + Localization.MODULES.TANKS.LOCK.translate();
        }
        return str;
    }

    @Override
    public FluidStack getFluid() {
        return (tank.getFluid().isEmpty()) ? FluidStack.EMPTY : tank.getFluid().copy();
    }

    public void setFluid(FluidStack fluidStack) {
        tank.setFluid(fluidStack);
    }

    @Override
    public int getCapacity() {
        return getTankSize();
    }

    @Override
    public boolean isFluidValid(FluidStack stack) {
        if (!locked.get().isEmpty() && !locked.get().is(stack.getFluid())) {
            return false;
        }
        return tank.isFluidValid(stack);
    }

    @Override
    public int fill(FluidStack resource, IFluidHandler.FluidAction action) {
        if (!locked.get().isEmpty() && !locked.get().is(resource.getFluid())) {
            return 0;
        }
        return tank.fill(resource, action);
    }

    @Nonnull
    @Override
    public FluidStack drain(int maxDrain, IFluidHandler.FluidAction action) {
        return tank.drain(maxDrain, action);
    }

    @Nonnull
    @Override
    public FluidStack drain(FluidStack resource, IFluidHandler.FluidAction action) {
        return tank.drain(resource, action);
    }

    @Override
    protected void save(ValueOutput output, int id) {
        tank.serialize(output.child(generateNBTName("Fluid", id)));
        locked.save(generateNBTName("LockedStack", id), output);
    }

    @Override
    protected void load(ValueInput input, int id) {
        tank.deserialize(input.childOrEmpty(generateNBTName("Fluid", id)));
        locked.load(generateNBTName("LockedStack", id), input);
        updateData();
    }

    protected void updateData() {
        fluidName.set(tank.getFluid().isEmpty() ? "" : getFluidName(tank.getFluid().getFluid()));
        fluidAmount.set(tank.getFluid().isEmpty() ? -1 : tank.getFluid().getAmount());
    }

    public String getFluidName(Fluid fluid) {
        return BuiltInRegistries.FLUID.getKey(fluid).toString();
    }

    public float getFluidRenderHeight() {
        if (tank.getFluid().isEmpty()) {
            return 0.0f;
        }
        return tank.getFluidAmount() / getTankSize();
    }

    public boolean isCompletelyFilled() {
        return !getFluid().isEmpty() && getFluidAmount() >= getTankSize();
    }

    public boolean isCompletelyEmpty() {
        return getFluid().isEmpty() || getFluidAmount() == 0;
    }

    @Override
    public int getFluidAmount() {
        return (getFluid().isEmpty()) ? 0 : tank.getFluidAmount();
    }

    @Override
    protected int numberOfPackets() {
        return 1;
    }

    @Override
    protected void receivePacket(final int id, final byte[] data, final Player player) {
        if ((!getFluid().isEmpty() || !locked.get().isEmpty())) {
            if (locked.get().isEmpty() && !tank.getFluid().isEmpty()) {
                setLocked(new FluidStack(tank.getFluid().getFluid(), 1000));
            } else {
                setLocked(FluidStack.EMPTY);
            }
            updateData();
        }
    }

    @Override
    public int numberOfGuiData() {
        return 1;
    }

    private void setLocked(FluidStack val) {
        if (!isPlaceholder()) {
            locked.set(val);
        }
    }

    @Override
    public void mouseClicked(final GuiMinecart gui, final int x, final int y, final int button) {
        if (inRect(x, y, tankBounds)) {
            byte data = (byte) button;
            if (StevesCartsClient.hasShiftDown()) {
                data |= 0x2;
            }
            sendPacket(0, data);
        }
    }

    @Override
    public void drawImage(GuiGraphicsExtractor GuiGraphicsExtractor, int tankid, int guiLeft, int guiTop, TextureAtlasSprite sprite, int targetX, int targetY, int width, int height, int colour) {
        int[] rect = cloneRect(new int[]{targetX, targetY, width, height});
        if (!doStealInterface()) {
            handleScroll(rect);
        }
        if (rect[3] > 0) {
            GuiGraphicsExtractor.blitSprite(RenderPipelines.GUI_TEXTURED, sprite, guiLeft + rect[0] + getX(), guiTop + rect[1] + getY(), rect[2], rect[3], colour);
        }
    }
}
