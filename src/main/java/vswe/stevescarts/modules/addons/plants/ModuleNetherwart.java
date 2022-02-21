package vswe.stevescarts.modules.addons.plants;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.NetherWartBlock;
import net.minecraft.world.level.block.state.BlockState;
import vswe.stevescarts.api.farms.ICropModule;
import vswe.stevescarts.entitys.EntityMinecartModular;
import vswe.stevescarts.modules.addons.ModuleAddon;

import javax.annotation.Nonnull;

public class ModuleNetherwart extends ModuleAddon implements ICropModule
{
    public ModuleNetherwart(final EntityMinecartModular cart)
    {
        super(cart);
    }

    @Override
    public boolean isSeedValid(@Nonnull ItemStack seed)
    {
        return seed.getItem() == Items.NETHER_WART;
    }

    @Override
    public BlockState getCropFromSeed(@Nonnull ItemStack seed, Level world, BlockPos pos)
    {
        return Blocks.NETHER_WART.defaultBlockState();
    }

    @Override
    public boolean isReadyToHarvest(Level world, BlockPos pos)
    {
        BlockState blockState = world.getBlockState(pos);
        return blockState.getBlock() == Blocks.NETHER_WART && blockState.getValue(NetherWartBlock.AGE) == 3;
    }
}
