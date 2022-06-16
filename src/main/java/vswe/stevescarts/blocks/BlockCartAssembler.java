package vswe.stevescarts.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;
import vswe.stevescarts.blocks.tileentities.TileEntityCartAssembler;
import vswe.stevescarts.blocks.tileentities.TileEntityUpgrade;
import vswe.stevescarts.helpers.Pair;

import java.util.ArrayList;


public class BlockCartAssembler extends BlockContainerBase
{

    public BlockCartAssembler()
    {
        super(Properties.of(Material.STONE).strength(2.0F));
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


    public void updateMultiBlock(final Level world, final BlockPos pos)
    {
        BlockEntity master = world.getBlockEntity(pos);
        if (master instanceof TileEntityCartAssembler)
        {
            ((TileEntityCartAssembler) master).clearUpgrades();
        }
        checkForUpgrades(world, pos);
        if (master instanceof TileEntityCartAssembler)
        {
            ((TileEntityCartAssembler) master).onUpgradeUpdate();
        }
    }

    private void checkForUpgrades(final Level world, final BlockPos pos)
    {
        for (Direction facing : Direction.values())
        {
            checkForUpgrade(world, pos.relative(facing));
        }
    }

    private TileEntityCartAssembler checkForUpgrade(final Level world, final BlockPos pos)
    {
        final BlockEntity tile = world.getBlockEntity(pos);
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
            world.blockEntityChanged(pos);
            for (final Pair<TileEntityCartAssembler, Direction> master2 : masters)
            {
                master2.first().removeUpgrade(upgrade);
            }
            upgrade.setMaster(null, null);
        }
        return null;
    }

    private ArrayList<Pair<TileEntityCartAssembler, Direction>> getMasters(final Level world, final BlockPos pos)
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

    private TileEntityCartAssembler getValidMaster(final Level world, final BlockPos pos)
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

    private TileEntityCartAssembler getMaster(final Level world, final BlockPos pos)
    {
        final BlockEntity tile = world.getBlockEntity(pos);
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

    public void addUpgrade(final Level world, final BlockPos pos)
    {
        final TileEntityCartAssembler master = getValidMaster(world, pos);
        if (master != null)
        {
            updateMultiBlock(world, master.getBlockPos());
        }
    }

    public void removeUpgrade(final Level world, final BlockPos pos)
    {
        final TileEntityCartAssembler master = getValidMaster(world, pos);
        if (master != null)
        {
            updateMultiBlock(world, master.getBlockPos());
        }
    }

    @Override
    public void neighborChanged(BlockState blockState, Level world, BlockPos blockPos, Block block, BlockPos blockPos1, boolean p_220069_6_)
    {
        super.neighborChanged(blockState, world, blockPos, block, blockPos, p_220069_6_);
        updateMultiBlock(world, blockPos);
    }


    @org.jetbrains.annotations.Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState)
    {
        return new TileEntityCartAssembler(blockPos, blockState);
    }
}
