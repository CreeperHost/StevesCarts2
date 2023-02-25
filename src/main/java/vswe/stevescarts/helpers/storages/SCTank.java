package vswe.stevescarts.helpers.storages;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.architectury.hooks.fluid.FluidStackHooks;
import net.creeperhost.polylib.client.fluid.ScreenFluidRenderer;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import vswe.stevescarts.client.guis.GuiBase;
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
        if (!itemStack.isEmpty()) {
            FluidUtil.getFluidHandler(itemStack).ifPresent(iFluidHandlerItem ->
            {
                FluidActionResult fluidActionResult = FluidUtil.tryEmptyContainer(itemStack, this, 1000, null, true);
                if ((fluidActionResult.isSuccess())) {
                    ItemStack containerStack = fluidActionResult.getResult();
                    owner.setInputContainer(tankid, containerStack);
                    onContentsChanged();
                }
            });
        }
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
    public void drawFluid(PoseStack matrixStack, AbstractContainerScreen<?> gui, final int startX, final int startY) {
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
                owner.drawImage(tankid, gui, icon, startX + 2 + 16 * x, startY + 1 + 16 * y + (16 - pixels)/*, 0, (16 - pixels)*/, 16, pixels);
            }
        }
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1F);
    }
}
