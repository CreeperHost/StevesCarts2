package vswe.stevescarts.api.detector;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;
import vswe.stevescarts.entities.ModularMinecart;

/**
 * Information supplied when an advanced detector rail finds a compatible
 * adjacent block.
 *
 * @param level server level containing the rail and handler
 * @param railPos position of the advanced detector rail
 * @param handlerPos position of the adjacent handler block
 * @param handlerState current state of the adjacent handler block
 * @param direction direction from the rail to the handler block
 * @param managerSide legacy manager side index: west 0, north 1, south 2, east 3
 * @param cart modular cart stopped on the detector rail
 */
public record AdvancedDetectorContext(
        ServerLevel level,
        BlockPos railPos,
        BlockPos handlerPos,
        BlockState handlerState,
        Direction direction,
        int managerSide,
        ModularMinecart cart
) {
}
