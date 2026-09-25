package vswe.stevescarts.api.detector;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Extension point for blocks which interact with a stopped modular cart next
 * to an advanced detector rail.
 * <p>
 * A block entity may implement this interface directly, or a handler can be
 * registered for a block with
 * {@link vswe.stevescarts.api.StevesCartsAPI#registerAdvancedDetectorHandler}.
 */
@FunctionalInterface
public interface AdvancedDetectorHandler {
    /**
     * Called each server tick while the cart is stopped on the rail.
     *
     * @return {@code true} when this block claimed the cart and the rail should
     * stop looking for another adjacent handler
     */
    boolean handleCart(AdvancedDetectorContext context);

    /**
     * Whether the advanced detector rail should avoid connecting redstone to
     * this handler, matching the behavior of the built-in cargo and liquid managers.
     */
    default boolean blocksRedstoneConnection(BlockGetter level, BlockPos pos, BlockState state) {
        return true;
    }
}
