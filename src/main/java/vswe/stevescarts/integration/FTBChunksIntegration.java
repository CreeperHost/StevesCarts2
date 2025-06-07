package vswe.stevescarts.integration;

import dev.ftb.mods.ftbchunks.api.FTBChunksAPI;
import dev.ftb.mods.ftbchunks.api.Protection;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;

/**
 * Created by brandon3055 on 07/06/2025
 */
public class FTBChunksIntegration {
    public static boolean canEditBlock(ServerPlayer player, BlockPos pos) {
        var api = FTBChunksAPI.api();
        var manager = api.getManager();
        return !manager.shouldPreventInteraction(player, InteractionHand.MAIN_HAND, pos, Protection.EDIT_BLOCK, null);
    }

}
