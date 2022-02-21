package vswe.stevescarts.modules.hull;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import vswe.stevescarts.entitys.EntityMinecartModular;

import javax.annotation.Nonnull;

public class ModulePig extends ModuleHull
{
    private int oinkTimer;

    /**
     * Only used for getting the texture of the armor.
     */
    //	private LayerBipedArmor fakeArmorLayer = new LayerBipedArmor(null);
    public ModulePig(final EntityMinecartModular cart)
    {
        super(cart);
        oinkTimer = getRandomTimer();
    }

    private void oink()
    {
        //		this.getCart().level.playSound((Entity) this.getCart(), SoundEvents.PIG_AMBIENT, 1.0f, (this.getCart().random.nextFloat() - this.getCart().random.nextFloat()) * 0.2f + 1.0f);
    }

    private int getRandomTimer()
    {
        return oinkTimer = getCart().random.nextInt(900) + 300;
    }

    @Override
    public void update()
    {
        if (oinkTimer <= 0)
        {
            oink();
            oinkTimer = getRandomTimer();
        }
        else
        {
            --oinkTimer;
        }
    }

    @Nonnull
    private ItemStack getHelmet()
    {
        if (getCart().getPassengers().isEmpty())
        {
            return ItemStack.EMPTY;
        }
        Entity rider = getCart().getPassengers().get(0);
        if (rider != null && rider instanceof LivingEntity)
        {
            //TODO
            //			return ((LivingEntity) rider).getItemStackFromSlot(EntityEquipmentSlot.HEAD);
        }
        return ItemStack.EMPTY;
    }

    public boolean hasHelment()
    {
        @Nonnull ItemStack item = getHelmet();
        return !item.isEmpty() && item.getItem() instanceof ArmorItem && ((ArmorItem) item.getItem()).getSlot() == EquipmentSlot.HEAD;
    }

    public ResourceLocation getHelmetResource(final boolean isOverlay)
    {
        if (!hasHelment())
        {
            return null;
        }
        @Nonnull ItemStack item = getHelmet();
        if (item.isEmpty())
        {
            return null;
        }
        return new ResourceLocation("");
        //		return fakeArmorLayer.getArmorResource((Entity) null, item, EntityEquipmentSlot.HEAD, isOverlay ? "overlay" : null);
    }

    public boolean hasHelmetColor(final boolean isOverlay)
    {
        return getHelmetColor(isOverlay) != -1;
    }

    public int getHelmetColor(final boolean isOverlay)
    {
        if (hasHelment())
        {
            @Nonnull ItemStack item = getHelmet();
            return Minecraft.getInstance().getItemColors().getColor(item, isOverlay ? 1 : 0);
        }
        return -1;
    }

    public boolean getHelmetMultiRender()
    {
        if (hasHelment())
        {
            @Nonnull ItemStack item = getHelmet();
            //TODO: Do we need this still
            //return ((ItemArmor) item.getItem()).requiresMultipleRenderPasses();
        }
        return false;
    }

    @Override
    public int getConsumption(final boolean isMoving)
    {
        if (!isMoving)
        {
            return super.getConsumption(isMoving);
        }
        return 1;
    }
}
