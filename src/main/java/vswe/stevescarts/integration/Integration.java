package vswe.stevescarts.integration;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.fml.ModList;

/**
 * Created by brandon3055 on 07/06/2025
 */
public class Integration {

    public static boolean canEditBlock(ServerPlayer player, BlockPos pos) {
        if (ModList.get().isLoaded("ftbchunks")) {
            return FTBChunksIntegration.canEditBlock(player, pos);
        }
        return true;
    }

}
