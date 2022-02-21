package vswe.stevescarts.blocks;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Material;

public class BlockMetalStorage extends Block
{
    public BlockMetalStorage()
    {
        super(Properties.of(Material.METAL).strength(2.0F));
    }
}
