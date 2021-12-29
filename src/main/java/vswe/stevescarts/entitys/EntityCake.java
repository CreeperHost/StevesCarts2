package vswe.stevescarts.entitys;

import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.EggEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.world.World;
import vswe.stevescarts.init.ModEntities;

public class EntityCake extends EggEntity
{
    public EntityCake(final World world)
    {
        super(ModEntities.CAKE.get(), world);
    }

    public EntityCake(final World world, final LivingEntity thrower)
    {
        super(world, thrower);
    }

    public EntityCake(final World world, final double x, final double y, final double z)
    {
        super(world, x, y, z);
    }

    public EntityCake(EntityType<EntityCake> entityCakeEntityType, World world)
    {
        super(entityCakeEntityType, world);
    }

    @Override
    protected void onHitEntity(EntityRayTraceResult data)
    {
        if (data.getEntity() != null)
        {
            if (data.getEntity() instanceof PlayerEntity)
            {
                final PlayerEntity player = (PlayerEntity) data.getEntity();
                player.getFoodData().eat(14, 0.7f);
            }
        }
        if (!level.isClientSide)
        {
            kill();
        }
    }

    @Override
    protected void onHitBlock(BlockRayTraceResult p_230299_1_)
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
