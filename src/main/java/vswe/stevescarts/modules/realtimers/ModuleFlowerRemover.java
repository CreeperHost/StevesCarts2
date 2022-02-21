package vswe.stevescarts.modules.realtimers;

import com.mojang.math.Vector3d;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.IForgeShearable;
import vswe.stevescarts.entitys.EntityMinecartModular;
import vswe.stevescarts.modules.ModuleBase;

import javax.annotation.Nonnull;
import java.util.List;

public class ModuleFlowerRemover extends ModuleBase
{
    private int tick;
    private float bladeangle;
    private float bladespeed;

    public ModuleFlowerRemover(final EntityMinecartModular cart)
    {
        super(cart);
        bladespeed = 0.0f;
    }

    @Override
    public void update()
    {
        super.update();
        if (getCart().level.isClientSide)
        {
            bladeangle += getBladeSpindSpeed();
            if (getCart().hasFuel())
            {
                bladespeed = Math.min(1.0f, bladespeed + 0.005f);
            }
            else
            {
                bladespeed = Math.max(0.0f, bladespeed - 0.005f);
            }
            return;
        }
        if (getCart().hasFuel())
        {
            if (tick >= getInterval())
            {
                tick = 0;
                mownTheLawn();
                shearEntities();
            }
            else
            {
                ++tick;
            }
        }
    }

    protected int getInterval()
    {
        return 70;
    }

    protected int getBlocksOnSide()
    {
        return 7;
    }

    protected int getBlocksFromLevel()
    {
        return 1;
    }

    private void mownTheLawn()
    {
        BlockPos cartPos = getCart().getExactPosition();
        for (Direction direction : Direction.values())
        {
            if (direction == Direction.DOWN || direction == Direction.UP) continue;
            for (int i = 0; i < getBlocksOnSide(); i++)
            {
                BlockPos relative = cartPos.relative(direction, i);
                if (isFlower(relative))
                {
                    BlockState blockState = getCart().level.getBlockState(relative);
                    ServerLevel serverWorld = (ServerLevel) getCart().level;
                    LootContext.Builder lootcontext$builder = (new LootContext.Builder(serverWorld)).withRandom(serverWorld.random).withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(relative)).withParameter(LootContextParams.TOOL, ItemStack.EMPTY).withOptionalParameter(LootContextParams.THIS_ENTITY, getCart()).withOptionalParameter(LootContextParams.BLOCK_ENTITY, null);

                    List<ItemStack> drops = blockState.getDrops(lootcontext$builder);
                    addStuff(drops);
                    getCart().level.removeBlock(relative, true);
                }
            }
        }
    }

    private void shearEntities()
    {
        final List<LivingEntity> entities = getCart().level.getEntitiesOfClass(LivingEntity.class, getCart().getBoundingBox().inflate(getBlocksOnSide(), getBlocksFromLevel() + 2.0f, getBlocksOnSide()));
        for (LivingEntity target : entities)
        {
            if (target instanceof IForgeShearable)
            {
                BlockPos pos = target.blockPosition();
                final IForgeShearable shearable = (IForgeShearable) target;
                if (!shearable.isShearable(ItemStack.EMPTY, getCart().level, pos))
                {
                    continue;
                }
                addStuff(shearable.onSheared(null, ItemStack.EMPTY, getCart().level, pos, 0));
            }
        }
    }

    private boolean isFlower(BlockPos pos)
    {
        BlockState blockState = getCart().level.getBlockState(pos);
        if (blockState.getBlock() == Blocks.GRASS) return true;
        if (blockState.getBlock() == Blocks.TALL_GRASS) return true;

        return ItemTags.FLOWERS.contains(getCart().level.getBlockState(pos).getBlock().asItem());
    }

    private void addStuff(final List<ItemStack> stuff)
    {
        for (@Nonnull ItemStack iStack : stuff)
        {
            getCart().addItemToChest(iStack);
            if (iStack.getCount() != 0)
            {
                final ItemEntity entityitem = new ItemEntity(getCart().level, getCart().getExactPosition().getX(), getCart().getExactPosition().getY(), getCart().getExactPosition().getZ(), iStack);
                getCart().level.addFreshEntity(entityitem);
            }
        }
    }

    public float getBladeAngle()
    {
        return bladeangle;
    }

    public float getBladeSpindSpeed()
    {
        return bladespeed;
    }
}
