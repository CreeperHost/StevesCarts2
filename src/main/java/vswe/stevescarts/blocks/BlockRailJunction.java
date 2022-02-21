package vswe.stevescarts.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
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
import net.minecraft.world.level.material.Material;
import vswe.stevescarts.entitys.EntityMinecartModular;

import javax.annotation.Nullable;

public class BlockRailJunction extends BaseRailBlock
{
    public static final EnumProperty<RailShape> SHAPE = BlockStateProperties.RAIL_SHAPE_STRAIGHT;

    public BlockRailJunction()
    {
        this(Properties.of(Material.DECORATION).noCollission().strength(0.7F).sound(SoundType.METAL));
    }

    private BlockRailJunction(Properties builder)
    {
        super(true, builder);
        this.registerDefaultState(this.stateDefinition.any().setValue(SHAPE, RailShape.NORTH_SOUTH).setValue(WATERLOGGED, Boolean.valueOf(false)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(SHAPE, WATERLOGGED);
    }

    @Override
    public Property<RailShape> getShapeProperty()
    {
        return SHAPE;
    }

    @Override
    public boolean canMakeSlopes(BlockState state, BlockGetter world, BlockPos pos) {
        return false;
    }

    @Override
    public RailShape getRailDirection(BlockState state, BlockGetter world, BlockPos pos, @Nullable AbstractMinecart cart)
    {
        if (cart instanceof EntityMinecartModular)
        {
            final EntityMinecartModular entityMinecartModular = (EntityMinecartModular) cart;
            RailShape railShape = entityMinecartModular.getRailDirection(pos);
            if (railShape != null) return railShape;
        }
        return super.getRailDirection(state, world, pos, cart);
    }
}
