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
import vswe.stevescarts.blocks.tileentities.TileEntityCargo;

import javax.annotation.Nullable;

public class BlockCargoManager extends BlockContainerBase
{

    public BlockCargoManager()
    {
        super(Properties.of(Material.STONE).strength(2.0F));
    }


    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player playerEntity, InteractionHand hand, BlockHitResult blockRayTraceResult)
    {
        if (!level.isClientSide)
        {
            NetworkHooks.openGui((ServerPlayer) playerEntity, (MenuProvider) level.getBlockEntity(blockPos), blockPos);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public void onRemove(BlockState blockState1, Level world, BlockPos blockPos, BlockState blockState, boolean p_196243_5_)
    {
        final TileEntityCargo tile = (TileEntityCargo) world.getBlockEntity(blockPos);
        if (tile != null)
        {
            //TODO
//            InventoryHelper.dropContents(world, blockPos, tile);
        }
        super.onRemove(blockState1, world, blockPos, blockState, p_196243_5_);
    }

    @org.jetbrains.annotations.Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState)
    {
        return new TileEntityCargo();
    }
}
