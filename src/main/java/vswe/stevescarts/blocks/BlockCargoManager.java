package vswe.stevescarts.blocks;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import vswe.stevescarts.blocks.tileentities.TileEntityCargo;

public class BlockCargoManager extends BlockContainerBase
{
    public static final MapCodec<BlockCargoManager> CODEC = simpleCodec(BlockCargoManager::new);

    public BlockCargoManager(Block.Properties properties)
    {
        super(properties);
    }

    @Override
    public @NotNull InteractionResult use(@NotNull BlockState blockState, Level level, @NotNull BlockPos blockPos, @NotNull Player playerEntity, @NotNull InteractionHand hand, @NotNull BlockHitResult blockRayTraceResult)
    {
        if (!level.isClientSide)
        {
            playerEntity.openMenu((MenuProvider) level.getBlockEntity(blockPos), blockPos);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public void onRemove(@NotNull BlockState blockState1, @NotNull Level world, @NotNull BlockPos blockPos, @NotNull BlockState blockState, boolean p_196243_5_)
    {
        final TileEntityCargo tile = (TileEntityCargo) world.getBlockEntity(blockPos);
        if (tile != null)
        {
            BlockContainerBase.dropResources(blockState, world, blockPos);
        }
        super.onRemove(blockState1, world, blockPos, blockState, p_196243_5_);
    }

    @org.jetbrains.annotations.Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos blockPos, @NotNull BlockState blockState)
    {
        return new TileEntityCargo(blockPos, blockState);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }
}
