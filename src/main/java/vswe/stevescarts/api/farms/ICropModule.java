package vswe.stevescarts.api.farms;

import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface ICropModule
{
    boolean isSeedValid(ItemStack itemStack);

    BlockState getCropFromSeed(ItemStack itemStack, World world, BlockPos pos);

    boolean isReadyToHarvest(World world, BlockPos pos);
}
