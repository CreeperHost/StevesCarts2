package vswe.stevescarts.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.network.NetworkHooks;
import vswe.stevescarts.blocks.tileentities.TileEntityDetector;
import vswe.stevescarts.helpers.DetectorType;

import javax.annotation.Nullable;
import java.util.List;

public class BlockDetector extends BlockContainerBase
{
    public static EnumProperty<DetectorType> SATE = EnumProperty.create("detectortype", DetectorType.class);
    public static BooleanProperty POWERED = BooleanProperty.create("powered");

    public BlockDetector()
    {
        super(Properties.of(Material.STONE).strength(2.0F).harvestTool(ToolType.PICKAXE));
        this.registerDefaultState(this.stateDefinition.any().setValue(SATE, DetectorType.NORMAL).setValue(POWERED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder)
    {
        builder.add(SATE, POWERED);
    }

    @Override
    public ActionResultType use(BlockState blockState, World world, BlockPos blockPos, PlayerEntity playerEntity, Hand hand, BlockRayTraceResult rayTraceResult)
    {
        if (!world.isClientSide)
        {
            if (!playerEntity.isCrouching())
            {
                NetworkHooks.openGui((ServerPlayerEntity) playerEntity, (INamedContainerProvider) world.getBlockEntity(blockPos), blockPos);
                return ActionResultType.SUCCESS;
            }
        }
        return ActionResultType.SUCCESS;
    }

    @Override
    public void appendHoverText(ItemStack p_190948_1_, @Nullable IBlockReader p_190948_2_, List<ITextComponent> textComponents, ITooltipFlag p_190948_4_)
    {
        textComponents.add(new StringTextComponent(TextFormatting.RED + "WIP"));
        super.appendHoverText(p_190948_1_, p_190948_2_, textComponents, p_190948_4_);
    }

    //TODO
    //	@Override
    //	public void getSubBlocks(final CreativeTabs tab, final NonNullList<ItemStack> list) {
    //		for (final DetectorType type : DetectorType.values()) {
    //			list.add(new ItemStack(this, 1, type.getMeta()));
    //		}
    //	}
    //
    //	@Override
    //	public boolean isSideSolid(IBlockState base_state, IBlockAccess world, BlockPos pos, EnumFacing side) {
    //		return true;
    //	}
    //
    //	@Override
    //	public boolean isBlockNormalCube(IBlockState state) {
    //		return true;
    //	}
    //
    //	@Override
    //	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer entityPlayer, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
    //		if (entityPlayer.isSneaking()) {
    //			return false;
    //		}
    //		if (world.isRemote) {
    //			return true;
    //		}
    //		FMLNetworkHandler.openGui(entityPlayer, StevesCarts.instance, 6, world, pos.getX(), pos.getY(), pos.getZ());
    //		return true;
    //	}
    //
    //	@Override
    //	public int getWeakPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
    //		return blockState.getValue(POWERED) && DetectorType.getTypeFromSate(blockState).shouldEmitRedstone() ? 15 : 0;
    //	}
    //
    //	@Override
    //	public int getStrongPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
    //		return 0;
    //	}
    //
    //	@Override
    //	public boolean canProvidePower(IBlockState state) {
    //		return true;
    //	}
    //
    //	@Override
    //	public boolean canConnectRedstone(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
    //		if (side == null) {
    //			return false;
    //		}
    //		final DetectorType type = DetectorType.getTypeFromSate(state);
    //		return type.shouldEmitRedstone() || type == DetectorType.REDSTONE;
    //	}

    //	@Override
    //	public int damageDropped(IBlockState state) {
    //		return getMetaFromState(state);
    //	}
    //
    //	@Override
    //	public IBlockState getStateFromMeta(int meta) {
    //		boolean powered = false;
    //		if (meta > DetectorType.values().length) {
    //			powered = true;
    //		}
    //		return getDefaultState().withProperty(SATE, DetectorType.getTypeFromint(meta - (powered ? DetectorType.values().length + 1 : 0))).withProperty(POWERED, powered);
    //	}
    //
    //	@Override
    //	public int getMetaFromState(IBlockState state) {
    //		boolean powered = state.getValue(POWERED);
    //		return (state.getValue(SATE)).getMeta() + (powered ? DetectorType.values().length + 1 : 0);
    //	}
    //
    //	@Override
    //	protected BlockStateContainer createBlockState() {
    //		return new BlockStateContainer(this, SATE, POWERED);
    //	}
    //
    //	@Override
    //	public ItemBlock getItemBlock() {
    //		return new ItemBlockDetector(this);
    //	}
    //
    //	@Override
    //	public int getSubtypeNumber() {
    //		return DetectorType.values().length;
    //	}
    //
    //	@Override
    //	public String getSubtypeName(int meta) {
    //		return "block_detector_" + DetectorType.values()[meta].getName();
    //	}
    //
    //	@Override
    //	public void setStateMapper(StateMap.Builder builder) {
    //		builder.ignore(POWERED);
    //	}

    @Nullable
    @Override
    public TileEntity newBlockEntity(IBlockReader p_196283_1_)
    {
        return new TileEntityDetector();
    }
}
