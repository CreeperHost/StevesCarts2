package vswe.stevescarts.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.block.material.Material;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.network.NetworkHooks;
import vswe.stevescarts.blocks.tileentities.TileEntityCartAssembler;
import vswe.stevescarts.blocks.tileentities.TileEntityUpgrade;
import vswe.stevescarts.upgrades.AssemblerUpgrade;
import vswe.stevescarts.upgrades.BaseEffect;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class BlockUpgrade extends BlockContainerBase
{
    public static final DirectionProperty FACING = HorizontalBlock.FACING;
    public static final BooleanProperty CONNECTED = BooleanProperty.create("connected");

    private static VoxelShape[] BBS = new VoxelShape[6];
    private AssemblerUpgrade assemblerUpgrade;

    static
    {
        float thickness = 2.0F;
        BBS[Direction.UP.ordinal()] = Block.box(0, 0, 0, 16, thickness, 16);
        BBS[Direction.DOWN.ordinal()] = Block.box(0, 16 - thickness, 0, 16, 16, 16);
        BBS[Direction.EAST.ordinal()] = Block.box(0, 0, 0, thickness, 16, 16);
        BBS[Direction.WEST.ordinal()] = Block.box(16 - thickness, 0, 0, 16, 16, 16);
        BBS[Direction.NORTH.ordinal()] = Block.box(0, 0, 16 - thickness, 16, 16, 16);
        BBS[Direction.SOUTH.ordinal()] = Block.box(0, 0, 0, 16, 16, thickness);
    }

    public BlockUpgrade(AssemblerUpgrade assemblerUpgrade)
    {
        super(Properties.of(Material.STONE).noOcclusion().randomTicks().strength(2.0F).harvestTool(ToolType.PICKAXE));
        this.assemblerUpgrade = assemblerUpgrade;
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(CONNECTED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder)
    {
        builder.add(FACING, CONNECTED);
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader blockReader, BlockPos blockPos, ISelectionContext selectionContext)
    {
        return BBS[state.getValue(FACING).getOpposite().ordinal()];
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext blockItemUseContext)
    {
        return this.defaultBlockState().setValue(FACING, blockItemUseContext.getHorizontalDirection()).setValue(CONNECTED, false);
    }

    @Nullable
    @Override
    public TileEntity newBlockEntity(IBlockReader blockReader)
    {
        return new TileEntityUpgrade(assemblerUpgrade);
    }

    @Override
    public boolean canSurvive(BlockState blockState, IWorldReader iWorldReader, BlockPos blockPos)
    {
        BlockPos offset = blockPos.relative(getFacing(blockState));
        return iWorldReader.getBlockEntity(offset) != null && iWorldReader.getBlockEntity(offset) instanceof TileEntityCartAssembler;
    }

    public Direction getFacing(BlockState blockState)
    {
        return blockState.getValue(FACING);
    }

    @Override
    public void randomTick(BlockState blockState, ServerWorld serverWorld, BlockPos blockPos, Random p_225542_4_)
    {
        if (!canSurvive(blockState, serverWorld, blockPos))
        {
            if (serverWorld.removeBlock(blockPos, true))
            {
                ItemEntity item = new ItemEntity(serverWorld, blockPos.getX(), blockPos.getY(), blockPos.getZ(), new ItemStack(this));
                serverWorld.addFreshEntity(item);
            }
        }
        super.randomTick(blockState, serverWorld, blockPos, p_225542_4_);
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
    public void appendHoverText(ItemStack itemStack, @Nullable IBlockReader iBlockReader, List<ITextComponent> tooltip, ITooltipFlag iTooltipFlag)
    {
        if (assemblerUpgrade != null)
        {
            for (final BaseEffect effect : assemblerUpgrade.getEffects())
            {
                tooltip.add(new StringTextComponent(effect.getName()));
            }
        }
    }
}
