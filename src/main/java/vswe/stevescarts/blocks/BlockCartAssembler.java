package vswe.stevescarts.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.network.NetworkHooks;
import vswe.stevescarts.blocks.tileentities.TileEntityCartAssembler;
import vswe.stevescarts.blocks.tileentities.TileEntityUpgrade;
import vswe.stevescarts.helpers.Pair;

import javax.annotation.Nullable;
import java.util.ArrayList;

;

public class BlockCartAssembler extends BlockContainerBase
{

    public BlockCartAssembler()
    {
        super(Properties.of(Material.STONE).strength(2.0F).harvestTool(ToolType.PICKAXE));
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


    public void updateMultiBlock(final World world, final BlockPos pos)
    {
        TileEntity master = world.getBlockEntity(pos);
        if (master instanceof TileEntityCartAssembler)
        {
            ((TileEntityCartAssembler) master).clearUpgrades();
        }
        checkForUpgrades(world, pos);
        if (!world.isClientSide)
        {
            //TODO
            //			PacketStevesCarts.sendBlockInfoToClients(world, new byte[0], pos);
        }
        if (master instanceof TileEntityCartAssembler)
        {
            ((TileEntityCartAssembler) master).onUpgradeUpdate();
        }
    }

    private void checkForUpgrades(final World world, final BlockPos pos)
    {
        for (Direction facing : Direction.values())
        {
            checkForUpgrade(world, pos.relative(facing));
        }
    }

    private TileEntityCartAssembler checkForUpgrade(final World world, final BlockPos pos)
    {
        final TileEntity tile = world.getBlockEntity(pos);
        if (tile != null && tile instanceof TileEntityUpgrade)
        {
            final TileEntityUpgrade upgrade = (TileEntityUpgrade) tile;
            final ArrayList<Pair<TileEntityCartAssembler, Direction>> masters = getMasters(world, pos);
            if (masters.size() == 1)
            {
                Pair<TileEntityCartAssembler, Direction> pair = masters.get(0);
                TileEntityCartAssembler master = pair.first();
                master.addUpgrade(upgrade);
                upgrade.setMaster(master, pair.second().getOpposite());
                return master;
            }
            world.blockEntityChanged(pos, tile);
            for (final Pair<TileEntityCartAssembler, Direction> master2 : masters)
            {
                master2.first().removeUpgrade(upgrade);
            }
            upgrade.setMaster(null, null);
        }
        return null;
    }

    private ArrayList<Pair<TileEntityCartAssembler, Direction>> getMasters(final World world, final BlockPos pos)
    {
        final ArrayList<Pair<TileEntityCartAssembler, Direction>> masters = new ArrayList<>();
        for (Direction facing : Direction.values())
        {
            final TileEntityCartAssembler temp = getMaster(world, pos.relative(facing));
            if (temp != null)
            {
                masters.add(Pair.of(temp, facing));
            }
        }
        return masters;
    }

    private TileEntityCartAssembler getValidMaster(final World world, final BlockPos pos)
    {
        TileEntityCartAssembler master = null;
        for (Direction facing : Direction.values())
        {
            final TileEntityCartAssembler temp = getMaster(world, pos.relative(facing));
            if (temp != null)
            {
                if (master != null)
                {
                    return null;
                }
                master = temp;
            }
        }
        return master;
    }

    private TileEntityCartAssembler getMaster(final World world, final BlockPos pos)
    {
        final TileEntity tile = world.getBlockEntity(pos);
        if (tile != null && tile instanceof TileEntityCartAssembler)
        {
            final TileEntityCartAssembler master = (TileEntityCartAssembler) tile;
            if (!master.isDead)
            {
                return master;
            }
        }
        return null;
    }

    public void addUpgrade(final World world, final BlockPos pos)
    {
        final TileEntityCartAssembler master = getValidMaster(world, pos);
        if (master != null)
        {
            updateMultiBlock(world, master.getBlockPos());
        }
    }

    public void removeUpgrade(final World world, final BlockPos pos)
    {
        final TileEntityCartAssembler master = getValidMaster(world, pos);
        if (master != null)
        {
            updateMultiBlock(world, master.getBlockPos());
        }
    }

    @Override
    public void neighborChanged(BlockState blockState, World world, BlockPos blockPos, Block block, BlockPos blockPos1, boolean p_220069_6_)
    {
        super.neighborChanged(blockState, world, blockPos, block, blockPos, p_220069_6_);
        updateMultiBlock(world, blockPos);
    }


    //	@Override
    //	public void breakBlock(World world, BlockPos pos, IBlockState state) {
    //		final TileEntityCartAssembler tile = (TileEntityCartAssembler) world.getTileEntity(pos);
    //		tile.isDead = true;
    //		updateMultiBlock(world, pos);
    //		if (!tile.isEmpty()) {
    //			List<ItemStack> stacks = new ArrayList<>();
    //			for (int i = 0; i < tile.getSizeInventory(); i++) {
    //				ItemStack stack = tile.getStackInSlot(i);
    //				if(TileEntityCartAssembler.getSlotStatus(stack) > 0){
    //					stacks.add(stack);
    //				}
    //			}
    //			stacks.forEach(stack -> InventoryHelper.spawnItemStack(world, pos.getX(), pos.getY(), pos.getZ(), stack));
    //			ItemStack outputItem = tile.getOutputOnInterupt();
    //			if (!outputItem.isEmpty()) {
    //				final EntityItem eItem = new EntityItem(world, pos.getX() + 0.20000000298023224, pos.getY() + 0.20000000298023224, pos.getZ() + 0.2f, outputItem);
    //				eItem.motionX = (float) world.rand.nextGaussian() * 0.05f;
    //				eItem.motionY = (float) world.rand.nextGaussian() * 0.25f;
    //				eItem.motionZ = (float) world.rand.nextGaussian() * 0.05f;
    //				if (outputItem.hasTagCompound()) {
    //					eItem.getItem().setTagCompound(outputItem.getTagCompound().copy());
    //				}
    //				world.spawnEntity(eItem);
    //			}
    //		}
    //		super.breakBlock(world, pos, getDefaultState());
    //	}

    @Nullable
    @Override
    public TileEntity newBlockEntity(IBlockReader iBlockReader)
    {
        return new TileEntityCartAssembler();
    }
}
