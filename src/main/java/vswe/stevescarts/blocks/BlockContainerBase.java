package vswe.stevescarts.blocks;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.ContainerBlock;

public abstract class BlockContainerBase extends ContainerBlock
{
    public BlockContainerBase(AbstractBlock.Properties properties)
    {
        super(properties);
    }

    @Override
    public BlockRenderType getRenderShape(BlockState p_149645_1_)
    {
        return BlockRenderType.MODEL;
    }
}
