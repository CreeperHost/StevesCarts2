package vswe.stevescarts.helpers.storages;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.block.FluidModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.client.resources.model.sprite.SpriteGetter;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidActionResult;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.fluids.FluidUtil;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import vswe.stevescarts.helpers.Localization;

import java.text.NumberFormat;
import java.util.Optional;

public class SCTank extends FluidTank {
    public static final Identifier BLOCK_ATLAS = Identifier.withDefaultNamespace("textures/atlas/blocks.png");
    private final ITankHolder owner;
    private final int tankid;

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
        containerTransfer(FluidStack.EMPTY);
    }

    public void containerTransfer(FluidStack restrictFluid) {
        ItemStack itemStack = owner.getInputContainer(tankid);
        if (itemStack.isEmpty()) return;

        FluidUtil.getFluidHandler(itemStack).ifPresent(itemHandler -> {
            FluidStack fluidStack = itemHandler.drain(Integer.MAX_VALUE, FluidAction.SIMULATE);
            FluidActionResult result;
            if (!fluidStack.isEmpty()) {
                if (!restrictFluid.isEmpty() && !restrictFluid.is(fluidStack.getFluid())) {
                    return;
                }
                //Simulate Bucket Empty
                result = FluidUtil.tryEmptyContainer(itemStack, this, FluidType.BUCKET_VOLUME, null, false);
                if (result.isSuccess()) {
                    ItemStack container = result.getResult();
                    Optional<IFluidHandlerItem> opt = FluidUtil.getFluidHandler(container);
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
                        if (itemStack.isEmpty()) {
                            owner.setInputContainer(tankid, ItemStack.EMPTY);
                        }
                    }
                }
            } else {
                result = FluidUtil.tryFillContainer(itemStack, this, Integer.MAX_VALUE, null, false);
                if (result.isSuccess()) {
                    ItemStack container = result.getResult();
                    if (!container.isEmpty()) {
                        owner.addToOutputContainer(tankid, container);
                        //Only 'Actually' do the transfer if we were able to insert the resulting container.
                        if (container.getCount() == 0) {
                            FluidUtil.tryFillContainer(itemStack, this, FluidType.BUCKET_VOLUME, null, true);

                            itemStack.shrink(1);
                            if (itemStack.isEmpty()) {
                                owner.setInputContainer(tankid, ItemStack.EMPTY);
                            }
                        }
                    }
                }
            }
        });
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

        FluidStack stack = new FluidStack(fluid.getFluid(), drained);
        if (action == FluidAction.EXECUTE) {
            fluid.shrink(drained);
            onContentsChanged();
        }
        return stack;
    }

    public String getMouseOver() {
        String name = Localization.MODULES.TANKS.EMPTY.translate();
        int amount = 0;
        if (!fluid.isEmpty()) {
            name = fluid.getHoverName().getString();
            amount = fluid.getAmount();
        }
        NumberFormat format = NumberFormat.getInstance();
        return name + ": " + format.format(amount) + " / " + format.format(capacity);
    }

    @Override
    protected void onContentsChanged() {
        owner.onFluidUpdated(tankid);
    }

    //I spent a couple of hours trying to find a way to make this work with ScreenFluidRenderer. But without overhauling the SC GUI system i could not find a good solution.
    public void drawFluid(GuiGraphicsExtractor GuiGraphicsExtractor, int guiLeft, int guiTop, final int startX, final int startY) {
        if (fluid.isEmpty()) return;
        int fluidLevel = (int) (48 * ((float) fluid.getAmount() / (float) capacity));

        FluidState fluidState = fluid.getFluid().defaultFluidState();
        FluidModel model = Minecraft.getInstance().getModelManager().getFluidStateModelSet().get(fluidState);
        Material.Baked material = model.stillMaterial();

        TextureAtlasSprite icon = material.sprite();
        if (icon == null) return;

        int fluidColor = model.fluidTintSource().color(fluidState);
        for (int y = 0; y < 3; y++) {
            int pixels = fluidLevel - (2 - y) * 16;

            if (pixels <= 0) {
                continue;
            } else if (pixels > 16) {
                pixels = 16;
            }

            for (int x = 0; x < 2; x++) {
                owner.drawImage(GuiGraphicsExtractor, tankid, guiLeft, guiTop, icon, startX + 2 + 16 * x, startY + 1 + 16 * y + (16 - pixels), 16, pixels, fluidColor);
            }
        }
    }
}
