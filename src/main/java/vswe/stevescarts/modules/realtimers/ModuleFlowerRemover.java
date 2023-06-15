package vswe.stevescarts.modules.realtimers;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.IForgeShearable;
import vswe.stevescarts.entities.EntityMinecartModular;
import vswe.stevescarts.api.modules.ModuleBase;

import java.util.List;

public class ModuleFlowerRemover extends ModuleBase {
    private int tick;
    private float bladeangle;
    private float bladespeed;

    public ModuleFlowerRemover(final EntityMinecartModular cart) {
        super(cart);
        bladespeed = 0.0f;
    }

    @Override
    public void update() {
        super.update();
        if (getCart().level().isClientSide) {
            bladeangle += getBladeSpeed();
            if (getCart().hasFuel()) {
                bladespeed = Math.min(1.0f, bladespeed + 0.005f);
            } else {
                bladespeed = Math.max(0.0f, bladespeed - 0.005f);
            }
            return;
        }
        if (getCart().hasFuel()) {
            if (tick >= getInterval()) {
                tick = 0;
                mownTheLawn();
                shearEntities();
            } else {
                ++tick;
            }
        }
    }

    protected int getInterval() {
        return 70;
    }

    protected int getBlocksOnSide() {
        return 7;
    }

    protected int getBlocksFromLevel() {
        return 1;
    }

    private void mownTheLawn() {
        BlockPos cartPos = getCart().getExactPosition();
        BlockPos minPos = cartPos.offset(-getBlocksOnSide(), -getBlocksFromLevel(), -getBlocksOnSide());
        BlockPos maxPos = cartPos.offset(getBlocksOnSide(), getBlocksFromLevel(), getBlocksOnSide());
        ServerLevel serverLevel = (ServerLevel) getCart().level();

        for (BlockPos pos : BlockPos.betweenClosed(minPos, maxPos)) {
            if (!isMowable(pos)) continue;
            BlockState state = getCart().level().getBlockState(pos);
            List<ItemStack> drops = Block.getDrops(state, serverLevel, pos, serverLevel.getBlockEntity(pos));
            addStuff(drops);
            getCart().level().removeBlock(pos, true);
        }
    }

    private void shearEntities() {
        final List<LivingEntity> entities = getCart().level().getEntitiesOfClass(LivingEntity.class, getCart().getBoundingBox().inflate(getBlocksOnSide(), getBlocksFromLevel() + 2.0f, getBlocksOnSide()));
        for (LivingEntity target : entities) {
            if (target instanceof IForgeShearable shearable) {
                BlockPos pos = target.blockPosition();
                if (!shearable.isShearable(ItemStack.EMPTY, getCart().level(), pos)) {
                    continue;
                }
                addStuff(shearable.onSheared(null, ItemStack.EMPTY, getCart().level(), pos, 0));
            }
        }
    }

    private boolean isMowable(BlockPos pos) {
        BlockState blockState = getCart().level().getBlockState(pos);
        return blockState.is(BlockTags.FLOWERS) || blockState.is(BlockTags.REPLACEABLE);
    }

    private void addStuff(List<ItemStack> stuff) {
        for (ItemStack stack : stuff) {
            getCart().addItemToChest(stack);
            if (stack.getCount() != 0) {
                ItemEntity entityitem = new ItemEntity(getCart().level(), getCart().getExactPosition().getX(), getCart().getExactPosition().getY(), getCart().getExactPosition().getZ(), stack);
                getCart().level().addFreshEntity(entityitem);
            }
        }
    }

    public float getBladeAngle() {
        return bladeangle;
    }

    public float getBladeSpeed() {
        return bladespeed;
    }
}
