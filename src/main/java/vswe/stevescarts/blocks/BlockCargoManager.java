package vswe.stevescarts.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.network.NetworkHooks;
import vswe.stevescarts.blocks.tileentities.TileEntityCargo;

import javax.annotation.Nullable;

public class BlockCargoManager extends BlockContainerBase
{

    public BlockCargoManager()
    {
        super(Properties.of(Material.STONE).strength(2.0F).harvestTool(ToolType.PICKAXE));
    }

    @Override
    public ActionResultType use(BlockState blockState, World level, BlockPos blockPos, PlayerEntity playerEntity, Hand hand, BlockRayTraceResult blockRayTraceResult)
    {
        if (!level.isClientSide)
        {
            NetworkHooks.openGui((ServerPlayerEntity) playerEntity, (INamedContainerProvider) level.getBlockEntity(blockPos), blockPos);
            return ActionResultType.SUCCESS;
        }
        return ActionResultType.SUCCESS;
    }

    @Override
    public void onRemove(BlockState blockState1, World world, BlockPos blockPos, BlockState blockState, boolean p_196243_5_)
    {
        final TileEntityCargo tile = (TileEntityCargo) world.getBlockEntity(blockPos);
        if (tile != null)
        {
            InventoryHelper.dropContents(world, blockPos, tile);
        }
        super.onRemove(blockState1, world, blockPos, blockState, p_196243_5_);
    }

    @Nullable
    @Override
    public TileEntity newBlockEntity(IBlockReader p_196283_1_)
    {
        return new TileEntityCargo();
    }
}
