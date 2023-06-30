package vswe.stevescarts.helpers.storages;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.architectury.hooks.fluid.FluidStackHooks;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.SoundActions;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.ItemHandlerHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import vswe.stevescarts.helpers.Localization;

import java.text.NumberFormat;

public class SCTank extends FluidTank {
    private final ITankHolder owner;
    private final int tankid;
    private boolean isLocked;

    public SCTank(final ITankHolder owner, final int tankSize, final int tankid) {
        super(tankSize);
        this.owner = owner;
        this.tankid = tankid;
    }

    public SCTank copy() {
        final SCTank tank = new SCTank(owner, capacity, tankid);
        if (!getFluid().isEmpty()) {
            tank.setFluid(getFluid().copy());
        }
        return tank;
    }

    public void containerTransfer() {
        ItemStack itemStack = owner.getInputContainer(tankid);
        if (itemStack.isEmpty()) return;

        FluidUtil.getFluidHandler(itemStack).ifPresent(itemHandler -> {
            FluidStack fluidStack = itemHandler.drain(Integer.MAX_VALUE, FluidAction.SIMULATE);
            FluidActionResult result;
            if (!fluidStack.isEmpty()) {
                //Simulate Bucket Empty
                result = FluidUtil.tryEmptyContainer(itemStack, this, FluidType.BUCKET_VOLUME, null, false);
                if (result.isSuccess()) {
                    ItemStack container = result.getResult();
                    LazyOptional<IFluidHandlerItem> opt = FluidUtil.getFluidHandler(container);
                    if (opt.isPresent()) {
                        IFluidHandlerItem handler = opt.orElseThrow(RuntimeException::new);
                        fluidStack = handler.drain(FluidType.BUCKET_VOLUME, FluidAction.SIMULATE);
                        //I believe this is for things like creative fluid containers.
                        if (!fluidStack.isEmpty() && fluidStack.getAmount() == FluidType.BUCKET_VOLUME) {
                            FluidUtil.tryEmptyContainer(itemStack, this, FluidType.BUCKET_VOLUME, null, true);
                            owner.setInputContainer(tankid, container);
                            return;
                        }
                    }
                    if (!container.isEmpty()) {
                        owner.addToOutputContainer(tankid, container);
                    }
                    //Only 'Actually' do the transfer if we were able to insert the resulting container.
                    if (container.getCount() == 0) {
                        FluidUtil.tryEmptyContainer(itemStack, this, FluidType.BUCKET_VOLUME, null, true);

                        itemStack.shrink(1);
                        if (itemStack.isEmpty()){
                            owner.setInputContainer(tankid, ItemStack.EMPTY);
                        }
                    }
                }
            } else {
                result = tryFillContainer(itemStack, this, Integer.MAX_VALUE, null, false);
                if (result.isSuccess()) {
                    ItemStack container = result.getResult();
                    if (!container.isEmpty()) {
                        owner.addToOutputContainer(tankid, container);
                        //Only 'Actually' do the transfer if we were able to insert the resulting container.
                        if (container.getCount() == 0) {
                            FluidUtil.tryFillContainer(itemStack, this, FluidType.BUCKET_VOLUME, null, true);

                            itemStack.shrink(1);
                            if (itemStack.isEmpty()){
                                owner.setInputContainer(tankid, ItemStack.EMPTY);
                            }
                        }
                    }
                }
            }
        });
    }

    @Deprecated //This is only needed until forge accepts my PR to fix this 4 year old bug
    public static FluidActionResult tryFillContainer(@NotNull ItemStack container, IFluidHandler fluidSource, int maxAmount, @Nullable Player player, boolean doFill) {
        ItemStack containerCopy = ItemHandlerHelper.copyStackWithSize(container, 1); // do not modify the input
        return FluidUtil.getFluidHandler(containerCopy)
                .map(containerFluidHandler -> {
                    FluidStack simulatedTransfer = FluidUtil.tryFluidTransfer(containerFluidHandler, fluidSource, maxAmount, false);
                    if (!simulatedTransfer.isEmpty()) {
                        if (doFill) {
                            FluidUtil.tryFluidTransfer(containerFluidHandler, fluidSource, maxAmount, true);
                            if (player != null) {
                                SoundEvent soundevent = simulatedTransfer.getFluid().getFluidType().getSound(simulatedTransfer, SoundActions.BUCKET_FILL);
                                player.level().playSound(null, player.getX(), player.getY() + 0.5, player.getZ(), soundevent, SoundSource.BLOCKS, 1.0F, 1.0F);
                            }
                        } else {
                            containerFluidHandler.fill(simulatedTransfer, FluidAction.EXECUTE);
                        }

                        ItemStack resultContainer = containerFluidHandler.getContainer();
                        return new FluidActionResult(resultContainer);
                    }
                    return FluidActionResult.FAILURE;
                })
                .orElse(FluidActionResult.FAILURE);
    }

    @Override
    public FluidStack drain(int maxDrain, FluidAction action) {
        if (fluid.isEmpty() || maxDrain <= 0) {
            return FluidStack.EMPTY;
        }

        int drained = maxDrain;
        if (fluid.getAmount() < drained) {
            drained = fluid.getAmount();
        }

        FluidStack stack = new FluidStack(fluid, drained);
        if (action == FluidAction.EXECUTE) {
            fluid.shrink(drained);
            if (fluid.getAmount() <= 0 && !isLocked) {
                fluid = FluidStack.EMPTY;
            }

            onContentsChanged();
        }
        return stack;
    }

    public void setLocked(final boolean val) {
        isLocked = val;
    }

    public boolean isLocked() {
        return isLocked;
    }

    public String getMouseOver() {
        String name = Localization.MODULES.TANKS.EMPTY.translate();
        int amount = 0;
        if (!fluid.isEmpty()) {
            name = fluid.getDisplayName().getString();
            amount = fluid.getAmount();
        }
        NumberFormat format = NumberFormat.getInstance();
        return name + ": " + format.format(amount) + " / " + format.format(capacity);
    }

    @Override
    protected void onContentsChanged() {
        owner.onFluidUpdated(tankid);
    }

    public static void applyColour(FluidStack fluidStack) {
        int fluidColor = FluidStackHooks.getColor(fluidStack.getFluid());
        float red = (fluidColor >> 16 & 0xFF) / 255.0F;
        float green = (fluidColor >> 8 & 0xFF) / 255.0F;
        float blue = (fluidColor & 0xFF) / 255.0F;
        float alpha = ((fluidColor >> 24) & 0xFF) / 255F;
        RenderSystem.setShaderColor(red, green, blue, alpha);
    }

    //I spent a couple of hours trying to find a way to make this work with ScreenFluidRenderer. But without overhauling the SC GUI system i could not find a good solution.
    @OnlyIn(Dist.CLIENT)
    public void drawFluid(GuiGraphics guiGraphics, AbstractContainerScreen<?> gui, final int startX, final int startY) {
        if (fluid.isEmpty()) return;
        int fluidLevel = (int) (48 * ((float) fluid.getAmount() / (float) capacity));

        TextureAtlasSprite icon = FluidStackHooks.getStillTexture(fluid.getFluid());
        if (icon == null) return;

        RenderSystem.setShaderTexture(0, InventoryMenu.BLOCK_ATLAS);
        applyColour(fluid);

        for (int y = 0; y < 3; y++) {
            int pixels = fluidLevel - (2 - y) * 16;

            if (pixels <= 0) {
                continue;
            } else if (pixels > 16) {
                pixels = 16;
            }

            for (int x = 0; x < 2; x++) {
                owner.drawImage(guiGraphics, tankid, gui, icon, startX + 2 + 16 * x, startY + 1 + 16 * y + (16 - pixels)/*, 0, (16 - pixels)*/, 16, pixels);
            }
        }
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1F);
    }
}
