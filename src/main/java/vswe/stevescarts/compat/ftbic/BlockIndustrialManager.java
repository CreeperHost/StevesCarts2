//package vswe.stevescarts.compat.ftbic;
//
//import net.minecraft.block.BlockState;
//import net.minecraft.block.material.Material;
//import net.minecraft.client.util.ITooltipFlag;
//import net.minecraft.entity.player.PlayerEntity;
//import net.minecraft.entity.player.ServerPlayerEntity;
//import net.minecraft.inventory.container.INamedContainerProvider;
//import net.minecraft.item.ItemStack;
//import net.minecraft.tileentity.TileEntity;
//import net.minecraft.util.ActionResultType;
//import net.minecraft.util.Hand;
//import net.minecraft.util.math.BlockPos;
//import net.minecraft.util.math.BlockRayTraceResult;
//import net.minecraft.util.text.ITextComponent;
//import net.minecraft.util.text.StringTextComponent;
//import net.minecraft.world.IBlockReader;
//import net.minecraft.world.World;
//import net.minecraftforge.common.ToolType;
//import net.minecraftforge.fml.network.NetworkHooks;
//import vswe.stevescarts.blocks.BlockContainerBase;
//
//import javax.annotation.Nullable;
//import java.util.List;
//
//public class BlockIndustrialManager extends BlockContainerBase
//{
//    public BlockIndustrialManager()
//    {
//        super(Properties.of(Material.STONE).strength(2.0F).harvestTool(ToolType.PICKAXE));
//    }
//
//    @Override
//    public ActionResultType use(BlockState blockState, World level, BlockPos blockPos, PlayerEntity playerEntity, Hand hand, BlockRayTraceResult blockRayTraceResult)
//    {
//        if (!level.isClientSide)
//        {
//            NetworkHooks.openGui((ServerPlayerEntity) playerEntity, (INamedContainerProvider) level.getBlockEntity(blockPos), blockPos);
//            return ActionResultType.SUCCESS;
//        }
//        return ActionResultType.SUCCESS;
//    }
//
//    @Override
//    public void appendHoverText(ItemStack p_190948_1_, @Nullable IBlockReader p_190948_2_, List<ITextComponent> textComponents, ITooltipFlag p_190948_4_)
//    {
//        textComponents.add(new StringTextComponent("WIP"));
//        super.appendHoverText(p_190948_1_, p_190948_2_, textComponents, p_190948_4_);
//    }
//
//    @Nullable
//    @Override
//    public TileEntity newBlockEntity(IBlockReader iBlockReader) {
//        return new TileIndustrialManager();
//    }
//}
