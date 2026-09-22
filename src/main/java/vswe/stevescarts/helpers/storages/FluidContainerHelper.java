package vswe.stevescarts.helpers.storages;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import org.jspecify.annotations.Nullable;

public final class FluidContainerHelper {
    private FluidContainerHelper() {
    }

    @Nullable
    public static ResourceHandler<FluidResource> getHandler(ItemStack stack) {
        if (stack.isEmpty()) {
            return null;
        }
        return ItemAccess.forStack(stack).oneByOne().getCapability(Capabilities.Fluid.ITEM);
    }

    public static boolean hasHandler(ItemStack stack) {
        return getHandler(stack) != null;
    }
}
