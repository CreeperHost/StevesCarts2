package vswe.stevescarts.api.farms;

import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;
import vswe.stevescarts.entitys.EntityMinecartModular;

@Deprecated
public interface ITreeModule
{

    /**
     * @param blockState
     * @param pos
     * @param cart
     * @return true if the block is a valid leaf
     */
    EnumHarvestResult isLeaves(BlockState blockState, BlockPos pos, EntityMinecartModular cart);

    /**
     * @param blockState
     * @param pos
     * @param cart
     * @return if the block is a valid piece of wood
     */
    EnumHarvestResult isWood(BlockState blockState, BlockPos pos, EntityMinecartModular cart);

    /**
     * Only return true if the sapling can be planted with this module
     *
     * @param itemStack the item stack to check
     * @return if the sapling can be planted by this module
     */
    boolean isSapling(ItemStack itemStack);

    /**
     * Plants the sapling in world, the stack size should be decreased by one here, a stack size of 0 is handled.
     *
     * @param world
     * @param pos
     * @param stack
     * @param fakePlayer
     * @return
     */
    boolean plantSapling(World world, BlockPos pos, ItemStack stack, FakePlayer fakePlayer);

}
