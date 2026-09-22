package vswe.stevescarts.helpers.storages;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.block.FluidModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.transfer.ResourceHandlerUtil;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.fluid.FluidStacksResourceHandler;
import net.neoforged.neoforge.transfer.fluid.FluidUtil;
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import vswe.stevescarts.helpers.Localization;

import java.text.NumberFormat;

public class SCTank extends FluidStacksResourceHandler implements IFluidTank {
    public static final Identifier BLOCK_ATLAS = Identifier.withDefaultNamespace("textures/atlas/blocks.png");
    private final ITankHolder owner;
    private final int tankid;

    public SCTank(final ITankHolder owner, final int tankSize, final int tankid) {
        super(1, tankSize);
        this.owner = owner;
        this.tankid = tankid;
    }

    public SCTank copy() {
        final SCTank tank = new SCTank(owner, capacity, tankid);
        tank.setFluid(getFluid());
        return tank;
    }

    @Override
    public FluidStack getFluid() {
        return FluidUtil.getStack(this, 0);
    }

    public void setFluid(FluidStack stack) {
        FluidResource resource = FluidResource.of(stack);
        set(0, resource, stack.isEmpty() ? 0 : stack.getAmount());
    }

    @Override
    public int getFluidAmount() {
        return getAmountAsInt(0);
    }

    @Override
    public int getCapacity() {
        return capacity;
    }

    @Override
    public boolean isFluidValid(FluidStack stack) {
        return !stack.isEmpty() && isValid(0, FluidResource.of(stack));
    }

    @Override
    public int fill(FluidStack resource, FluidAction action) {
        if (resource.isEmpty()) {
            return 0;
        }
        try (Transaction transaction = Transaction.openRoot()) {
            int inserted = insert(FluidResource.of(resource), resource.getAmount(), transaction);
            if (action.execute()) {
                transaction.commit();
            }
            return inserted;
        }
    }

    @Override
    public FluidStack drain(FluidStack resource, FluidAction action) {
        if (resource.isEmpty()) {
            return FluidStack.EMPTY;
        }
        return drain(FluidResource.of(resource), resource.getAmount(), action);
    }

    @Override
    public FluidStack drain(int maxDrain, FluidAction action) {
        FluidResource resource = getResource(0);
        return resource.isEmpty() ? FluidStack.EMPTY : drain(resource, maxDrain, action);
    }

    private FluidStack drain(FluidResource resource, int maxDrain, FluidAction action) {
        try (Transaction transaction = Transaction.openRoot()) {
            int extracted = extract(resource, maxDrain, transaction);
            if (action.execute()) {
                transaction.commit();
            }
            return extracted == 0 ? FluidStack.EMPTY : resource.toStack(extracted);
        }
    }

    public void containerTransfer() {
        containerTransfer(FluidStack.EMPTY);
    }

    public void containerTransfer(FluidStack restrictFluid) {
        ItemStack input = owner.getInputContainer(tankid);
        if (input.isEmpty()) {
            return;
        }

        ItemStacksResourceHandler singleItem = new ItemStacksResourceHandler(NonNullList.of(ItemStack.EMPTY, input.copyWithCount(1)));
        ItemAccess itemAccess = ItemAccess.forHandlerIndexStrict(singleItem, 0).oneByOne();
        var itemFluidHandler = itemAccess.getCapability(Capabilities.Fluid.ITEM);
        if (itemFluidHandler == null) {
            return;
        }

        try (Transaction transaction = Transaction.openRoot()) {
            var moved = ResourceHandlerUtil.moveFirst(
                    itemFluidHandler,
                    this,
                    resource -> restrictFluid.isEmpty() || resource.matches(restrictFluid),
                    FluidType.BUCKET_VOLUME,
                    transaction);

            if (moved == null) {
                moved = ResourceHandlerUtil.moveFirst(this, itemFluidHandler, resource -> true, FluidType.BUCKET_VOLUME, transaction);
            }
            if (moved == null) {
                return;
            }

            ItemStack result = singleItem.copyToList().getFirst().copy();
            if (!result.isEmpty()) {
                owner.addToOutputContainer(tankid, result);
                if (!result.isEmpty()) {
                    return;
                }
            }

            transaction.commit();
            input.shrink(1);
            owner.setInputContainer(tankid, input.isEmpty() ? ItemStack.EMPTY : input);
        }
    }

    @Override
    public void serialize(ValueOutput output) {
        super.serialize(output);
    }

    @Override
    public void deserialize(ValueInput input) {
        super.deserialize(input);
    }

    public String getMouseOver() {
        FluidStack fluid = getFluid();
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
    protected void onContentsChanged(int index, FluidStack previousContents) {
        owner.onFluidUpdated(tankid);
    }

    public void drawFluid(GuiGraphicsExtractor guiGraphics, int guiLeft, int guiTop, final int startX, final int startY) {
        FluidStack fluid = getFluid();
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
                owner.drawImage(guiGraphics, tankid, guiLeft, guiTop, icon, startX + 2 + 16 * x, startY + 1 + 16 * y + (16 - pixels), 16, pixels, fluidColor);
            }
        }
    }
}
