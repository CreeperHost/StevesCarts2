//package vswe.stevescarts.entitys;
//
//import net.minecraft.entity.EntityLivingBase;
//import net.minecraft.entity.item.EntityItem;
//import net.minecraft.entity.passive.EntityChicken;
//import net.minecraft.entity.passive.EntityPig;
//import net.minecraft.entity.projectile.EntityEgg;
//import net.minecraft.item.ItemStack;
//import net.minecraft.util.DamageSource;
//import net.minecraft.util.EnumParticleTypes;
//import net.minecraft.util.math.RayTraceResult;
//import net.minecraft.world.World;
//import vswe.stevescarts.helpers.GiftItem;
//
//import javax.annotation.Nonnull;
//import java.util.ArrayList;
//
//public class EntityEasterEgg extends EntityEgg {
//	public EntityEasterEgg(final World world) {
//		super(world);
//	}
//
//	public EntityEasterEgg(final World world, final EntityLivingBase thrower) {
//		super(world, thrower);
//	}
//
//	public EntityEasterEgg(final World world, final double x, final double y, final double z) {
//		super(world, x, y, z);
//	}
//
//	@Override
//	protected void onImpact(RayTraceResult result) {
//		if (result.entityHit != null) {
//			result.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, getThrower()), 0.0f);
//		}
//		if (!world.isRemote) {
//			if (rand.nextInt(8) == 0) {
//				if (rand.nextInt(32) == 0) {
//					final EntityPig entitypig = new EntityPig(world);
//					entitypig.setGrowingAge(-24000);
//					entitypig.setLocationAndAngles(posX, posY, posZ, rotationYaw, 0.0f);
//					world.spawnEntity(entitypig);
//				} else {
//					final EntityChicken entitychicken = new EntityChicken(world);
//					entitychicken.setGrowingAge(-24000);
//					entitychicken.setLocationAndAngles(posX, posY, posZ, rotationYaw, 0.0f);
//					world.spawnEntity(entitychicken);
//				}
//			} else {
//				final ArrayList<ItemStack> items = GiftItem.generateItems(rand, GiftItem.EasterList, 25 + rand.nextInt(300), 1);
//				for (
//					@Nonnull
//						ItemStack item : items) {
//					final EntityItem eItem = new EntityItem(world, posX, posY, posZ, item);
//					eItem.motionX = rand.nextGaussian() * 0.05000000074505806;
//					eItem.motionY = rand.nextGaussian() * 0.25;
//					eItem.motionZ = rand.nextGaussian() * 0.05000000074505806;
//					world.spawnEntity(eItem);
//				}
//			}
//		}
//		for (int j = 0; j < 8; ++j) {
//			world.spawnParticle(EnumParticleTypes.SNOWBALL, posX, posY, posZ, 0.0, 0.0, 0.0);
//		}
//		if (!world.isRemote) {
//			setDead();
//		}
//	}
//}
