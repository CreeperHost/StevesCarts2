package vswe.stevescarts.blocks;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;
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
        super(Properties.of(Material.STONE).strength(2.0F));
        this.registerDefaultState(this.stateDefinition.any().setValue(SATE, DetectorType.NORMAL).setValue(POWERED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        builder.add(SATE, POWERED);
    }


    @Override
    public InteractionResult use(BlockState blockState, Level world, BlockPos blockPos, Player playerEntity, InteractionHand hand, BlockHitResult rayTraceResult)
    {
        if (!world.isClientSide)
        {
            if (!playerEntity.isCrouching())
            {
                NetworkHooks.openGui((ServerPlayer) playerEntity, (MenuProvider) world.getBlockEntity(blockPos), blockPos);
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public void appendHoverText(ItemStack p_190948_1_, @Nullable BlockGetter p_190948_2_, List<Component> textComponents, TooltipFlag p_190948_4_)
    {
        textComponents.add(new TextComponent(ChatFormatting.RED + "WIP"));
        super.appendHoverText(p_190948_1_, p_190948_2_, textComponents, p_190948_4_);
    }

    @org.jetbrains.annotations.Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos p_153215_, BlockState p_153216_) {
        return new TileEntityDetector();
    }
}
