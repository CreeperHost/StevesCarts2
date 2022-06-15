package vswe.stevescarts.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraft.world.level.material.Material;
import vswe.stevescarts.blocks.tileentities.TileEntityActivator;
import vswe.stevescarts.blocks.tileentities.TileEntityManager;
import vswe.stevescarts.entitys.EntityMinecartModular;
import vswe.stevescarts.init.ModBlocks;

public class BlockRailAdvDetector extends BaseRailBlock
{
    public static final EnumProperty<RailShape> SHAPE = BlockStateProperties.RAIL_SHAPE_STRAIGHT;

    public BlockRailAdvDetector()
    {
        this(Properties.of(Material.DECORATION).noCollission().strength(0.7F).sound(SoundType.METAL));
    }

    private BlockRailAdvDetector(Properties builder)
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
    public void onMinecartPass(BlockState state, Level world, BlockPos pos, AbstractMinecart entityMinecart)
    {
        if (world.isClientSide || !(entityMinecart instanceof EntityMinecartModular))
        {
            return;
        }
        EntityMinecartModular cart = (EntityMinecartModular) entityMinecart;
        //		if (world.getBlockState(pos.below()).getBlock() == ModBlocks.DETECTOR_UNIT.getBlock() && DetectorType.getTypeFromSate(world.getBlockState(pos.down())).canInteractWithCart()) {
        //			TileEntity tileentity = world.getTileEntity(pos.down());
        //			if (tileentity instanceof TileEntityDetector) {
        //				TileEntityDetector detector = (TileEntityDetector) tileentity;
        //				detector.handleCart(cart);
        //			}
        //			return;
        //		}
        if (!isCartReadyForAction(cart, pos))
        {
            return;
        }
        int side = 0;
        for (int i = -1; i <= 1; ++i)
        {
            for (int j = -1; j <= 1; ++j)
            {
                if (Math.abs(i) != Math.abs(j))
                {
                    BlockPos offset = pos.offset(i, 0, j);
                    Block block = world.getBlockState(offset).getBlock();
                    if (block == ModBlocks.CARGO_MANAGER.get() || block == ModBlocks.LIQUID_MANAGER.get())
                    {
                        BlockEntity tileentity = world.getBlockEntity(offset);
                        if (tileentity instanceof TileEntityManager)
                        {
                            TileEntityManager manager = (TileEntityManager) tileentity;
                            if (manager.getCart() == null)
                            {
                                manager.setCart(cart);
                                manager.setSide(side);
                            }
                        }
                        return;
                    }
                    if (block == ModBlocks.MODULE_TOGGLER.get())
                    {
                        BlockEntity tileentity = world.getBlockEntity(offset);
                        if (tileentity instanceof TileEntityActivator)
                        {
                            TileEntityActivator activator = (TileEntityActivator) tileentity;
                            boolean isOrange = false;
                            if (cart.temppushX == 0.0 == (cart.temppushZ == 0.0))
                            {
                                continue;
                            }
                            if (i == 0)
                            {
                                if (j == -1)
                                {
                                    isOrange = (cart.temppushX < 0.0);
                                }
                                else
                                {
                                    isOrange = (cart.temppushX > 0.0);
                                }
                            }
                            else if (j == 0)
                            {
                                if (i == -1)
                                {
                                    isOrange = (cart.temppushZ > 0.0);
                                }
                                else
                                {
                                    isOrange = (cart.temppushZ < 0.0);
                                }
                            }
                            boolean isBlueBerry = false;
                            activator.handleCart(cart, isOrange);
                            cart.releaseCart();
                        }
                        return;
                    }
                    //					if (block == ModBlocks.UPGRADE.getBlock()) {
                    //						TileEntity tileentity = world.getTileEntity(offset);
                    //						TileEntityUpgrade upgrade = (TileEntityUpgrade) tileentity;
                    //						if (upgrade != null && upgrade.getUpgrade() != null) {
                    //							for (BaseEffect effect : upgrade.getUpgrade().getEffects()) {
                    //								if (effect instanceof Transposer) {
                    //									Transposer transposer = (Transposer) effect;
                    //									if (upgrade.getMaster() == null) {
                    //										continue;
                    //									}
                    //									for (TileEntityUpgrade tile : upgrade.getMaster().getUpgradeTiles()) {
                    //										if (tile.getUpgrade() != null) {
                    //											for (BaseEffect effect2 : tile.getUpgrade().getEffects()) {
                    //												if (effect2 instanceof Disassemble) {
                    //													Disassemble disassembler = (Disassemble) effect2;
                    //													if (tile.getStackInSlot(0).isEmpty()) {
                    //														tile.setInventorySlotContents(0, ModuleData.createModularCart(cart));
                    //														upgrade.getMaster().managerInteract(cart, false);
                    //														for (int p = 0; p < cart.getSizeInventory(); ++p) {
                    //															@Nonnull ItemStack item = cart.removeStackFromSlot(p);
                    //															if (!item.isEmpty()) {
                    //																upgrade.getMaster().puke(item);
                    //															}
                    //														}
                    //														cart.setDead();
                    //														return;
                    //													}
                    //													continue;
                    //												}
                    //											}
                    //										}
                    //									}
                    //								}
                    //							}
                    //						}
                    //					}
                    ++side;
                }
            }
        }
        //		int power = world.getRedstonePowerFromNeighbors(pos);
        //		if (power > 0) {
        //			cart.releaseCart();
        //		}
    }

    //	@Override
    //	public boolean canConnectRedstone(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
    //		IBlockState blockState = world.getBlockState(pos.down());
    //		if (world.getBlockState(pos.down()) == ModBlocks.DETECTOR_UNIT.getBlock() && DetectorType.getTypeFromSate(blockState).canInteractWithCart()) {
    //			return false;
    //		}
    //		for (EnumFacing facing: EnumFacing.HORIZONTALS) {
    //			BlockPos posOther = pos.offset(facing);
    //			Block block = world.getBlockState(posOther).getBlock();
    //			if (block == ModBlocks.CARGO_MANAGER.getBlock() || block == ModBlocks.LIQUID_MANAGER.getBlock() || block == ModBlocks.MODULE_TOGGLER.getBlock()) {
    //				return false;
    //			}
    //			if (block == ModBlocks.UPGRADE.getBlock()) {
    //				TileEntity tileentity = world.getTileEntity(posOther);
    //				TileEntityUpgrade upgrade = (TileEntityUpgrade) tileentity;
    //				if (upgrade != null && upgrade.getUpgrade() != null) {
    //					for (BaseEffect effect : upgrade.getUpgrade().getEffects()) {
    //						if (effect instanceof Transposer && upgrade.getMaster() != null) {
    //							for (TileEntityUpgrade tile : upgrade.getMaster().getUpgradeTiles()) {
    //								if (tile.getUpgrade() != null) {
    //									for (BaseEffect effect2 : tile.getUpgrade().getEffects()) {
    //										if (effect2 instanceof Disassemble) {
    //											return false;
    //										}
    //									}
    //								}
    //							}
    //						}
    //					}
    //				}
    //			}
    //		}
    //		return true;
    //	}
    //
    private boolean isCartReadyForAction(EntityMinecartModular cart, BlockPos pos)
    {
        return cart.disabledPos != null && cart.disabledPos.equals(pos) && cart.isDisabled();
    }

    //	@Override
    //	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
    //		IBlockState blockState = world.getBlockState(pos = pos.down());
    //		return blockState.getBlock() == ModBlocks.DETECTOR_UNIT.getBlock() && ModBlocks.DETECTOR_UNIT.getBlock().onBlockActivated(world, pos, blockState, player, hand, side, hitX, hitY, hitZ);
    //	}
    //
    //	public void refreshState(World world, BlockPos pos, IBlockState state, boolean flag) {
    //		new BlockRailBase.Rail(world, pos, state).place(flag, false);
    //	}
}