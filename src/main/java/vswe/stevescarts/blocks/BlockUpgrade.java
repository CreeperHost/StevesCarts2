package vswe.stevescarts.blocks;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import vswe.stevescarts.api.upgrades.BaseUpgradeEffect;
import vswe.stevescarts.blocks.tileentities.TileEntityCartAssembler;
import vswe.stevescarts.blocks.tileentities.TileEntityUpgrade;
import vswe.stevescarts.upgrades.AssemblerUpgrade;

import javax.annotation.Nullable;
import java.util.List;

public class BlockUpgrade extends BlockContainerBase
{
    //TODO, Figure out this codec stuff....
    public static final MapCodec<BlockUpgrade> CODEC = simpleCodec(BlockUpgrade::new);
    
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final BooleanProperty CONNECTED = BooleanProperty.create("connected");

    private static final VoxelShape[] BBS = new VoxelShape[6];
    private final AssemblerUpgrade assemblerUpgrade;

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

    public BlockUpgrade(Properties properties) {
        this(properties, null);
    }

    public BlockUpgrade(Block.Properties properties, AssemblerUpgrade assemblerUpgrade)
    {
        super(properties);
        this.assemblerUpgrade = assemblerUpgrade;
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(CONNECTED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        builder.add(FACING, CONNECTED);
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState state, @NotNull BlockGetter blockReader, @NotNull BlockPos blockPos, @NotNull CollisionContext selectionContext)
    {
        return BBS[state.getValue(FACING).getOpposite().ordinal()];
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockItemUseContext)
    {
        return this.defaultBlockState().setValue(FACING, blockItemUseContext.getHorizontalDirection()).setValue(CONNECTED, false);
    }

    @org.jetbrains.annotations.Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos blockPos, @NotNull BlockState blockState)
    {
        return new TileEntityUpgrade(assemblerUpgrade, blockPos, blockState);
    }


    @SuppressWarnings("deprication")
    @Override
    public boolean canSurvive(@NotNull BlockState blockState, LevelReader iWorldReader, BlockPos blockPos)
    {
        BlockPos offset = blockPos.relative(getFacing(blockState));
        return iWorldReader.getBlockEntity(offset) != null && iWorldReader.getBlockEntity(offset) instanceof TileEntityCartAssembler;
    }

    public Direction getFacing(BlockState blockState)
    {
        return blockState.getValue(FACING);
    }

    @Override
    public void randomTick(@NotNull BlockState blockState, @NotNull ServerLevel serverWorld, @NotNull BlockPos blockPos, @NotNull RandomSource p_225542_4_)
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
    public @NotNull InteractionResult use(@NotNull BlockState blockState, Level world, @NotNull BlockPos blockPos, @NotNull Player playerEntity, @NotNull InteractionHand hand, @NotNull BlockHitResult rayTraceResult)
    {
        if (!world.isClientSide)
        {
            if (!playerEntity.isCrouching())
            {
                playerEntity.openMenu((MenuProvider) world.getBlockEntity(blockPos), blockPos);
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public void appendHoverText(@NotNull ItemStack itemStack, @Nullable BlockGetter iBlockReader, @NotNull List<Component> tooltip, @NotNull TooltipFlag iTooltipFlag)
    {
        if (assemblerUpgrade != null)
        {
            for (final BaseUpgradeEffect effect : assemblerUpgrade.getEffects())
            {
                tooltip.add(Component.literal(effect.getName()));
            }
        }
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }
}
