package vswe.stevescarts.blocks;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.vehicle.minecart.AbstractMinecart;
import net.minecraft.world.entity.vehicle.minecart.NewMinecartBehavior;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.properties.RailShape;
import org.jetbrains.annotations.NotNull;
import vswe.stevescarts.entities.ModularMinecart;

import javax.annotation.Nullable;

public class BlockRailJunction extends BaseRailBlock
{
    public static final MapCodec<BlockRailJunction> CODEC = simpleCodec(BlockRailJunction::new);
    public static final EnumProperty<RailShape> SHAPE = BlockStateProperties.RAIL_SHAPE_STRAIGHT;

    public BlockRailJunction(Properties builder)
    {
        super(true, builder);
        this.registerDefaultState(this.stateDefinition.any().setValue(SHAPE, RailShape.NORTH_SOUTH).setValue(WATERLOGGED, Boolean.FALSE));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        builder.add(SHAPE, WATERLOGGED);
    }

    @Override
    public @NotNull Property<RailShape> getShapeProperty()
    {
        return SHAPE;
    }

    @Override
    public boolean canMakeSlopes(BlockState state, BlockGetter world, BlockPos pos)
    {
        return false;
    }

    @Override
    public @NotNull RailShape getRailDirection(@NotNull BlockState state, @NotNull BlockGetter world, @NotNull BlockPos pos, @Nullable AbstractMinecart cart) {
        if (cart instanceof ModularMinecart modularMinecart) {
            RailShape railShape = modularMinecart.getRailDirection(pos);
            if (railShape != null) {
                return railShape;
            }
        }
        if (cart != null) {
            Direction direction = cart.behavior.getMotionDirection();
            if (cart.behavior instanceof NewMinecartBehavior) {
                direction = direction.getCounterClockWise();//Why are the new cards rotated 90 degrees?...
            }
            return switch (direction) {
                case NORTH -> RailShape.NORTH_SOUTH;
                case SOUTH -> RailShape.NORTH_SOUTH;
                case WEST -> RailShape.EAST_WEST;
                case EAST -> RailShape.EAST_WEST;
                default -> super.getRailDirection(state, world, pos, cart);
            };
        }

        return super.getRailDirection(state, world, pos, cart);
    }

    @Override
    protected MapCodec<? extends BaseRailBlock> codec() {
        return CODEC;
    }
}
