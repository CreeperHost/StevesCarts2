package vswe.stevescarts.blocks;

import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import vswe.stevescarts.blocks.tileentities.TileEntityBase;

public abstract class BlockContainerBase extends BaseEntityBlock
{
    public BlockContainerBase(Block.Properties properties)
    {
        super(properties);
    }

    @Override
    public RenderShape getRenderShape(BlockState state)
    {
        return RenderShape.MODEL;
    }

    @org.jetbrains.annotations.Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return (level1, blockPos, blockState, t) ->
        {
            if (t instanceof TileEntityBase tile)
            {
                tile.tick();
            }
        };
    }
}
