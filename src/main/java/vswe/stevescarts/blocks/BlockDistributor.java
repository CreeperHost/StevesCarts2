package vswe.stevescarts.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;
import vswe.stevescarts.blocks.tileentities.TileEntityDistributor;

public class BlockDistributor extends BlockContainerBase
{
    public BlockDistributor()
    {
        super(Properties.of(Material.STONE).strength(2.0F));
    }

    @Override
    public @NotNull InteractionResult use(@NotNull BlockState blockState, Level world, @NotNull BlockPos blockPos, @NotNull Player playerEntity, @NotNull InteractionHand hand, @NotNull BlockHitResult rayTraceResult)
    {
        if (!world.isClientSide)
        {
            if (!playerEntity.isCrouching())
            {
                NetworkHooks.openScreen((ServerPlayer) playerEntity, (MenuProvider) world.getBlockEntity(blockPos), blockPos);
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.SUCCESS;
    }

    @org.jetbrains.annotations.Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos blockPos, @NotNull BlockState blockState)
    {
        return new TileEntityDistributor(blockPos, blockState);
    }
}
