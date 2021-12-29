package vswe.stevescarts.items;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import vswe.stevescarts.client.creativetabs.CreativeTabSC2Items;
import vswe.stevescarts.helpers.ComponentTypes;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Locale;

public class ItemCartComponent extends Item
{

    public static int size()
    {
        return ComponentTypes.values().length;
    }

    public ComponentTypes componentType;

    public ItemCartComponent(ComponentTypes componentType)
    {
        super(new Item.Properties().tab(CreativeTabSC2Items.INSTANCE));
        this.componentType = componentType;
        setRegistryName(getComponentName().trim().replace(" ", "_").replace("'", "_").toLowerCase(Locale.ROOT));
    }

    @Override
    public ITextComponent getName(ItemStack stack)
    {
        return new TranslationTextComponent(componentType.getName());
    }

    public String getComponentName()
    {
        if (componentType == null || componentType.getName() == null)
        {
            return "Unknown_SC2_Component";
        }
        return "component_" + componentType.getName();
    }

    @Override
    public int getEntityLifespan(ItemStack itemStack, World world) {
        return super.getEntityLifespan(itemStack, world);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable World p_77624_2_, List<ITextComponent> tooltip, ITooltipFlag p_77624_4_)
    {
        if (!stack.isEmpty())
        {
            ItemCartComponent itemCartComponent = (ItemCartComponent) stack.getItem();
            int id = itemCartComponent.componentType.getId();
            tooltip.add(new TranslationTextComponent("Component id " + id));
        }
    }

    //	public boolean isValid(@Nonnull ItemStack item) {
    //		if (item.isEmpty() || !(item.getItem() instanceof ItemCartComponent) || getName(item.getItemDamage()) == null) {
    //			return false;
    //		}
    //		if (item.getItemDamage() >= 50 && item.getItemDamage() < 58) {
    //			return Constants.isChristmas;
    //		}
    //		if (item.getItemDamage() >= 66 && item.getItemDamage() < 72) {
    //			return Constants.isEaster;
    //		}
    //		return item.getItemDamage() < 72 || item.getItemDamage() >= 80;
    //	}
    //
    //	public static ItemStack getWood(final int type, final boolean isLog) {
    //		return getWood(type, isLog, 1);
    //	}
    //
    //	public static ItemStack getWood(final int type, final boolean isLog, final int count) {
    //		return new ItemStack(ModItems.COMPONENTS, count, 72 + type * 2 + (isLog ? 0 : 1));
    //	}
    //
    //	public static boolean isWoodLog(@Nonnull ItemStack item) {
    //		return !item.isEmpty() && item.getItemDamage() >= 72 && item.getItemDamage() < 80 && (item.getItemDamage() - 72) % 2 == 0;
    //	}
    //
    //	public static boolean isWoodTwig(@Nonnull ItemStack item) {
    //		return !item.isEmpty() && item.getItemDamage() >= 72 && item.getItemDamage() < 80 && (item.getItemDamage() - 72) % 2 == 1;
    //	}
    //
    //	private boolean isEdibleEgg(@Nonnull ItemStack item) {
    //		return !item.isEmpty() && item.getItemDamage() >= 66 && item.getItemDamage() < 70;
    //	}
    //
    //	private boolean isThrowableEgg(@Nonnull ItemStack item) {
    //		return !item.isEmpty() && item.getItemDamage() == 70;
    //	}
    //
    //	@Override
    //	@Nonnull
    //	public ItemStack onItemUseFinish(@Nonnull ItemStack item, World world, EntityLivingBase entity) {
    //		if (entity instanceof EntityPlayer && isEdibleEgg(item)) {
    //			EntityPlayer player = (EntityPlayer) entity;
    //			if (item.getItemDamage() == ComponentTypes.EXPLOSIVE_EASTER_EGG.getId()) {
    //				world.createExplosion(null, entity.posX, entity.posY, entity.posZ, 0.1f, false);
    //			} else if (item.getItemDamage() == ComponentTypes.BURNING_EASTER_EGG.getId()) {
    //				entity.setFire(5);
    //				if (!world.isRemote) {
    //					entity.addPotionEffect(new PotionEffect(MobEffects.WATER_BREATHING, 600, 0));
    //				}
    //			} else if (item.getItemDamage() == ComponentTypes.GLISTERING_EASTER_EGG.getId()) {
    //				if (!world.isRemote) {
    //					entity.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 50, 2));
    //				}
    //			} else if (item.getItemDamage() == ComponentTypes.CHOCOLATE_EASTER_EGG.getId()) {
    //				if (!world.isRemote) {
    //					entity.addPotionEffect(new PotionEffect(MobEffects.SPEED, 300, 4));
    //				}
    //			} else if (item.getItemDamage() == ComponentTypes.PAINTED_EASTER_EGG.getId()) {}
    //			if (!player.capabilities.isCreativeMode) {
    //				item.shrink(1);
    //			}
    //			world.playSound((EntityPlayer) entity, entity.posX, entity.posY, entity.posZ, SoundEvents.ENTITY_PLAYER_BURP, SoundCategory.PLAYERS, 0.5F, world.rand.nextFloat() * 0.1F + 0.9F);
    //			player.getFoodStats().addStats(2, 0.0f);
    //			return item;
    //		}
    //		return super.onItemUseFinish(item, world, entity);
    //	}
    //
    //	@Override
    //	public int getMaxItemUseDuration(@Nonnull ItemStack item) {
    //		return isEdibleEgg(item) ? 32 : super.getMaxItemUseDuration(item);
    //	}
    //
    //	@Override
    //	public EnumAction getItemUseAction(@Nonnull ItemStack item) {
    //		return isEdibleEgg(item) ? EnumAction.EAT : super.getItemUseAction(item);
    //	}
    //
    //	@Override
    //	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
    //		ItemStack item = player.getHeldItem(hand);
    //		if (isEdibleEgg(item)) {
    //			player.setActiveHand(hand);
    //			return ActionResult.newResult(EnumActionResult.SUCCESS, item);
    //		}
    //		if (isThrowableEgg(item)) {
    //			if (!player.capabilities.isCreativeMode) {
    //				item.shrink(1);
    //			}
    //			world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.NEUTRAL, 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
    //			if (!world.isRemote) {
    //				world.spawnEntity(new EntityEasterEgg(world, player));
    //			}
    //			return ActionResult.newResult(EnumActionResult.SUCCESS, item);
    //		}
    //		return super.onItemRightClick(world, player, hand);
    //	}
}
