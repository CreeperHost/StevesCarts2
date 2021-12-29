//package vswe.stevescarts.handlers;
//
//import net.minecraft.entity.Entity;
//import net.minecraft.entity.EntityLivingBase;
//import net.minecraft.entity.item.EntityItem;
//import net.minecraft.entity.monster.EntityBlaze;
//import net.minecraft.entity.monster.EntityMob;
//import net.minecraft.entity.passive.EntityVillager;
//import net.minecraft.entity.player.EntityPlayer;
//import net.minecraft.inventory.EntityEquipmentSlot;
//import net.minecraft.item.ItemStack;
//import net.minecraft.util.EnumHand;
//import net.minecraftforge.event.entity.living.LivingDeathEvent;
//import net.minecraftforge.event.entity.player.PlayerInteractEvent;
//import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
//import net.minecraftforge.fml.common.gameevent.TickEvent;
//import net.minecraftforge.fml.relauncher.ReflectionHelper;
//import net.minecraftforge.fml.relauncher.Side;
//import vswe.stevescarts.Constants;
//import vswe.stevescarts.helpers.ComponentTypes;
//import vswe.stevescarts.init.ModItems;
//
//import javax.annotation.Nonnull;
//
//public class EventHandlerChristmas {
//
//	@SubscribeEvent
//	public void onEntityLivingDeath(final LivingDeathEvent event) {
//		final EntityLivingBase monster = event.getEntityLiving();
//		if (monster.world.isRemote || !event.getSource().getDamageType().equals("player")) {
//			return;
//		}
//		if (monster instanceof EntityMob && Math.random() < 0.1) {
//			dropItem(monster, ComponentTypes.STOLEN_PRESENT.getItemStack());
//		}
//		if (monster instanceof EntityBlaze && Math.random() < 0.12) {
//			dropItem(monster, ComponentTypes.RED_WRAPPING_PAPER.getItemStack());
//		}
//	}
//
//	@SubscribeEvent
//	public void onEntityInteract(final PlayerInteractEvent.EntityInteract event) {
//		final EntityPlayer player = event.getEntityPlayer();
//		final Entity target = event.getTarget();
//		if (target instanceof EntityVillager) {
//			final EntityVillager villager = (EntityVillager) target;
//			if (villager.getProfessionForge() != TradeHandler.santaProfession) {
//				@Nonnull
//				ItemStack item = player.getHeldItem(EnumHand.MAIN_HAND);
//				if (!item.isEmpty() && item.getItem() == ModItems.COMPONENTS && item.getItemDamage() == ComponentTypes.WARM_HAT.getId()) {
//					if (!player.capabilities.isCreativeMode) {
//						@Nonnull
//						ItemStack itemStack = item;
//						itemStack.shrink(1);
//					}
//					if (!player.world.isRemote) {
//						villager.setProfession(TradeHandler.santaProfession);
//						villager.getRecipes(player).clear();
//						try {
//							ReflectionHelper.setPrivateValue(EntityVillager.class, villager, 0, "careerLevel", "field_175562_bw");
//							ReflectionHelper.findMethod(EntityVillager.class, "populateBuyingList", "func_175554_cu").invoke(villager);
//						} catch (Exception e) {
//							e.printStackTrace();
//						}
//					}
//					if (item.getCount() <= 0 && !player.capabilities.isCreativeMode) {
//						player.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, ItemStack.EMPTY);
//					}
//					event.setCanceled(true);
//				}
//			}
//		}
//	}
//
//	@SubscribeEvent
//	public void tickEnd(final TickEvent.PlayerTickEvent event) {
//		if (event.side == Side.SERVER) {
//			final EntityPlayer player = event.player;
//			if (Constants.isChristmas && player.isPlayerFullyAsleep()) {
//				for (int i = 0; i < player.inventory.getSizeInventory(); ++i) {
//					ItemStack item = player.inventory.getStackInSlot(i);
//					if (!item.isEmpty() && item.getItem() == ModItems.COMPONENTS && item.getItemDamage() == 56) {
//						item.setItemDamage(item.getItemDamage() + 1);
//					}
//				}
//			}
//		}
//	}
//
//	private void dropItem(final EntityLivingBase monster, @Nonnull ItemStack item) {
//		final EntityItem obj = new EntityItem(monster.world, monster.posX, monster.posY, monster.posZ, item);
//		obj.motionX = monster.world.rand.nextGaussian() * 0.05000000074505806;
//		obj.motionY = monster.world.rand.nextGaussian() * 0.05000000074505806 + 0.20000000298023224;
//		obj.motionZ = monster.world.rand.nextGaussian() * 0.05000000074505806;
//		monster.world.spawnEntity(obj);
//	}
//}
