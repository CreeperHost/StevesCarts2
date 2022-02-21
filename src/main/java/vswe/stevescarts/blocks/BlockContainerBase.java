package vswe.stevescarts.blocks;

import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;

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
}
