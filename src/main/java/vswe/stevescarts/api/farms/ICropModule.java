package vswe.stevescarts.api.farms;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public interface ICropModule
{
    boolean isSeedValid(ItemStack itemStack);

    BlockState getCropFromSeed(ItemStack itemStack, Level world, BlockPos pos);

    boolean isReadyToHarvest(Level world, BlockPos pos);
}
