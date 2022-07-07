package vswe.stevescarts.helpers.storages;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.architectury.hooks.fluid.FluidStackHooks;
import net.creeperhost.polylib.client.fluid.ScreenFluidRenderer;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import vswe.stevescarts.helpers.Localization;

import java.text.NumberFormat;

public class SCTank extends FluidTank
{
    private final ITankHolder owner;
    private final int tankid;
    private boolean isLocked;

    public SCTank(final ITankHolder owner, final int tankSize, final int tankid)
    {
        super(tankSize);
        this.owner = owner;
        this.tankid = tankid;
    }

    public SCTank copy()
    {
        final SCTank tank = new SCTank(owner, capacity, tankid);
        if (!getFluid().isEmpty())
        {
            tank.setFluid(getFluid().copy());
        }
        return tank;
    }

    public void containerTransfer()
    {
        ItemStack itemStack = owner.getInputContainer(tankid);
        if (!itemStack.isEmpty())
        {
            FluidUtil.getFluidHandler(itemStack).ifPresent(iFluidHandlerItem ->
            {
                FluidActionResult fluidActionResult = FluidUtil.tryEmptyContainer(itemStack, this, 1000, null, true);
                if ((fluidActionResult.isSuccess()))
                {
                    ItemStack containerStack = fluidActionResult.getResult();
                    owner.setInputContainer(tankid, containerStack);
                    onContentsChanged();
                }
            });
        }
    }

    @Override
    public FluidStack drain(int maxDrain, FluidAction action)
    {
        if (fluid.isEmpty() || maxDrain <= 0)
        {
            return FluidStack.EMPTY;
        }

        int drained = maxDrain;
        if (fluid.getAmount() < drained)
        {
            drained = fluid.getAmount();
        }

        FluidStack stack = new FluidStack(fluid, drained);
        if (action == FluidAction.EXECUTE)
        {
            fluid.shrink(drained);
            if (fluid.getAmount() <= 0 && !isLocked)
            {
                fluid = FluidStack.EMPTY;
            }

            onContentsChanged();
        }
        return stack;
    }

    public void setLocked(final boolean val)
    {
        isLocked = val;
    }

    public boolean isLocked()
    {
        return isLocked;
    }

    public String getMouseOver()
    {
        String name = Localization.MODULES.TANKS.EMPTY.translate();
        int amount = 0;
        if (!fluid.isEmpty())
        {
            name = fluid.getDisplayName().getString();
            amount = fluid.getAmount();
        }
        NumberFormat format = NumberFormat.getInstance();
        return name + ": " + format.format(amount) + " / " + format.format(capacity);
    }

    @Override
    protected void onContentsChanged()
    {
        owner.onFluidUpdated(tankid);
    }


    @OnlyIn(Dist.CLIENT)
    public void drawFluid(PoseStack matrixStack, AbstractContainerScreen gui, final int startX, final int startY)
    {
        matrixStack.pushPose();
        ScreenFluidRenderer fluidRenderer = new ScreenFluidRenderer(capacity, 32, 47, 0);
        //TODO this might work? Maybe adding a way to the lib to convert between fluidstack types would be a good idea
        fluidRenderer.render(startX + 2, startY + 2, dev.architectury.fluid.FluidStack.create(fluid.getFluid(), fluid.getAmount()));
        matrixStack.popPose();
    }
}
