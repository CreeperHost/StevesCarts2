package vswe.stevescarts.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraftforge.common.ToolType;

public class BlockMetalStorage extends Block
{

    //	public static final PropertyInteger TYPE = PropertyInteger.create("type", 0, ItemBlockStorage.blocks.length - 1);

    public BlockMetalStorage()
    {
        super(Properties.of(Material.METAL).strength(2.0F).harvestTool(ToolType.PICKAXE));
        //		setCreativeTab(StevesCarts.tabsSC2Blocks);
        //		setDefaultState(blockState.getBaseState().withProperty(TYPE, 0));
        //		setHardness(5.0F);
        //		setResistance(10.0F);
    }

    //	public int damageDropped(final int meta) {
    //		return meta;
    //	}
    //
    //	@Override
    //	public IBlockState getStateFromMeta(int meta) {
    //		return getDefaultState().withProperty(TYPE, meta);
    //	}
    //
    //	@Override
    //	public int getMetaFromState(IBlockState state) {
    //		return (state.getValue(TYPE));
    //	}
    //
    //	@Override
    //	protected BlockStateContainer createBlockState() {
    //		return new BlockStateContainer(this, TYPE);
    //	}
    //
    //	@Override
    //	public ItemBlock getItemBlock() {
    //		return new ItemBlockStorage(this);
    //	}
    //
    //	@Override
    //	public int getSubtypeNumber() {
    //		return ItemBlockStorage.blocks.length;
    //	}
    //
    //	@Override
    //	public String getSubtypeName(int meta) {
    //		return "block_storage_" + ItemBlockStorage.blocks[meta];
    //	}
}
