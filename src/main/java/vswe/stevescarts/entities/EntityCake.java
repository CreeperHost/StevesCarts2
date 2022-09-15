package vswe.stevescarts.entities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrownEgg;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import org.jetbrains.annotations.NotNull;
import vswe.stevescarts.init.ModEntities;

public class EntityCake extends ThrownEgg
{
    public EntityCake(final Level world)
    {
        super(ModEntities.CAKE.get(), world);
    }

    public EntityCake(final Level world, final LivingEntity thrower)
    {
        super(world, thrower);
    }

    public EntityCake(final Level world, final double x, final double y, final double z)
    {
        super(world, x, y, z);
    }

    public EntityCake(EntityType<EntityCake> entityCakeEntityType, Level world)
    {
        super(entityCakeEntityType, world);
    }

    @Override
    protected void onHitEntity(EntityHitResult data)
    {
        if (data.getEntity() != null)
        {
            if (data.getEntity() instanceof Player player)
            {
                player.getFoodData().eat(14, 0.7f);
            }
        }
        if (!level.isClientSide)
        {
            kill();
        }
    }

    @Override
    protected void onHitBlock(@NotNull BlockHitResult blockHitResult)
    {
        BlockPos pos = blockPosition();

        if (level.getBlockState(pos).isAir() && Blocks.CAKE.canSurvive(Blocks.CAKE.defaultBlockState(), level, pos))//level.isSideSolid(pos.down(), EnumFacing.UP))
        {
            level.setBlock(pos, Blocks.CAKE.defaultBlockState(), 3);
        }
        for (int j = 0; j < 8; ++j)
        {
            level.addParticle(ParticleTypes.ITEM_SNOWBALL, getX(), getY(), getZ(), 0.0, 0.0, 0.0);
        }
        if (!level.isClientSide)
        {
            kill();
        }
    }
}
