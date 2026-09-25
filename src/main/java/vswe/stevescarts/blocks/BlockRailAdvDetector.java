package vswe.stevescarts.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.vehicle.minecart.AbstractMinecart;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import vswe.stevescarts.api.StevesCartsAPI;
import vswe.stevescarts.api.detector.AdvancedDetectorContext;
import vswe.stevescarts.api.detector.AdvancedDetectorHandler;
import vswe.stevescarts.api.modules.data.ModuleData;
import vswe.stevescarts.api.upgrades.BaseUpgradeEffect;
import vswe.stevescarts.blocks.tileentities.TileEntityActivator;
import vswe.stevescarts.blocks.tileentities.TileEntityUpgrade;
import vswe.stevescarts.entities.ModularMinecart;
import vswe.stevescarts.init.ModBlocks;
import vswe.stevescarts.upgrades.Disassemble;
import vswe.stevescarts.upgrades.Transposer;

import javax.annotation.Nonnull;

public class BlockRailAdvDetector extends BaseRailBlock {
    public static final EnumProperty<RailShape> SHAPE = BlockStateProperties.RAIL_SHAPE_STRAIGHT;

    public BlockRailAdvDetector(Properties builder) {
        super(true, builder);
        this.registerDefaultState(this.stateDefinition.any().setValue(SHAPE, RailShape.NORTH_SOUTH).setValue(WATERLOGGED, Boolean.FALSE));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(SHAPE, WATERLOGGED);
    }

    @Override
    public @NotNull Property<RailShape> getShapeProperty() {
        return SHAPE;
    }

    @Override
    public boolean canMakeSlopes(BlockState state, BlockGetter world, BlockPos pos) {
        return false;
    }

    //TODO, Switch back to forge's onMinecartPass if it gets fixed.
    public void onMinecartPassSC(BlockState state, Level world, BlockPos pos, AbstractMinecart entityMinecart) {
        if (!(world instanceof ServerLevel serverLevel) || !(entityMinecart instanceof ModularMinecart cart)) {
            return;
        }
        if (!isCartReadyForAction(cart, pos)) {
            return;
        }
        int side = 0;
        for (int i = -1; i <= 1; ++i) {
            for (int j = -1; j <= 1; ++j) {
                if (Math.abs(i) != Math.abs(j)) {
                    BlockPos offset = pos.offset(i, 0, j);
                    BlockState handlerState = world.getBlockState(offset);
                    Block block = handlerState.getBlock();
                    BlockEntity tileentity = world.getBlockEntity(offset);
                    AdvancedDetectorHandler handler = getAdvancedDetectorHandler(block, tileentity);
                    if (handler != null) {
                        Direction direction = directionFromOffset(i, j);
                        AdvancedDetectorContext context = new AdvancedDetectorContext(
                                serverLevel, pos, offset, handlerState, direction, side, cart);
                        if (handler.handleCart(context)) {
                            return;
                        }
                    }
                    if (block == ModBlocks.MODULE_TOGGLER.get()) {
                        if (tileentity instanceof TileEntityActivator activator) {
                            Vec3 velocity = cart.getEffectiveVelocity();
                            boolean isOrange = false;
                            if (velocity.x == 0 && velocity.z == 0) {
                                continue;
                            }
                            if (i == 0) {
                                if (j == -1) {
                                    isOrange = (velocity.x < 0.0);
                                } else {
                                    isOrange = (velocity.x > 0.0);
                                }
                            } else if (j == 0) {
                                if (i == -1) {
                                    isOrange = (velocity.z > 0.0);
                                } else {
                                    isOrange = (velocity.z < 0.0);
                                }
                            }
                            activator.handleCart(cart, isOrange);
                            cart.releaseCart();
                        }
                        return;
                    }
                    if (block instanceof BlockUpgrade && tileentity instanceof TileEntityUpgrade upgrade) {
                        if (upgrade.getUpgrade() != null) {
                            for (BaseUpgradeEffect effect : upgrade.getUpgrade().getEffects()) {
                                if (effect instanceof Transposer) {
                                    if (upgrade.getMaster() == null) {
                                        continue;
                                    }
                                    for (TileEntityUpgrade tile : upgrade.getMaster().getUpgradeTiles()) {
                                        if (tile.getUpgrade() != null) {
                                            for (BaseUpgradeEffect effect2 : tile.getUpgrade().getEffects()) {
                                                if (effect2 instanceof Disassemble) {
                                                    if (tile.getItem(0).isEmpty()) {
                                                        tile.setItem(0, ModuleData.createModularCart(cart));
                                                        upgrade.getMaster().managerInteract(cart, false);
                                                        for (int p = 0; p < cart.getContainerSize(); ++p) {
                                                            @Nonnull ItemStack item = cart.removeItem(p, 64);
                                                            if (!item.isEmpty()) {
                                                                upgrade.getMaster().puke(item);
                                                            }
                                                        }
                                                        cart.remove(Entity.RemovalReason.DISCARDED);
                                                        return;
                                                    }
                                                    continue;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    ++side;
                }
            }
        }
        int power = world.getBestNeighborSignal(pos);
        if (power > 0) {
            cart.releaseCart();
        }
    }

    private boolean isCartReadyForAction(ModularMinecart cart, BlockPos pos) {
        return cart.getDisabledPos() != null && cart.getDisabledPos().equals(pos) && cart.isDisabled();
    }

    public boolean canConnectRedstone(BlockState state, BlockGetter level, BlockPos pos, @Nullable Direction direction) {
        for (Direction facing : Direction.values()) {
            if (facing.getAxis() == Direction.Axis.Y) continue;
            BlockPos posOther = pos.relative(facing);
            BlockState otherState = level.getBlockState(posOther);
            Block block = otherState.getBlock();
            BlockEntity blockEntity = level.getBlockEntity(posOther);
            AdvancedDetectorHandler handler = getAdvancedDetectorHandler(block, blockEntity);
            if ((handler != null && handler.blocksRedstoneConnection(level, posOther, otherState))
                    || block == ModBlocks.MODULE_TOGGLER.get()) {
                return false;
            }
            if (blockEntity instanceof TileEntityUpgrade upgrade) {
                if (upgrade.getUpgrade() != null) {
                    for (BaseUpgradeEffect effect : upgrade.getUpgrade().getEffects()) {
                        if (effect instanceof Transposer && upgrade.getMaster() != null) {
                            for (TileEntityUpgrade tile : upgrade.getMaster().getUpgradeTiles()) {
                                if (tile.getUpgrade() != null) {
                                    for (BaseUpgradeEffect effect2 : tile.getUpgrade().getEffects()) {
                                        if (effect2 instanceof Disassemble) {
                                            return false;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return true;
    }

    @Nullable
    private static AdvancedDetectorHandler getAdvancedDetectorHandler(Block block, @Nullable BlockEntity blockEntity) {
        if (blockEntity instanceof AdvancedDetectorHandler handler) {
            return handler;
        }
        return StevesCartsAPI.getAdvancedDetectorHandler(block);
    }

    private static Direction directionFromOffset(int xOffset, int zOffset) {
        if (xOffset < 0) {
            return Direction.WEST;
        }
        if (xOffset > 0) {
            return Direction.EAST;
        }
        return zOffset < 0 ? Direction.NORTH : Direction.SOUTH;
    }
}
