package vswe.stevescarts.modules.hull;

import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import vswe.stevescarts.api.modules.template.ModuleHull;
import vswe.stevescarts.entities.ModularMinecart;

import javax.annotation.Nonnull;

public class ModulePig extends ModuleHull {
    private int oinkTimer;

    /**
     * Only used for getting the texture of the armor.
     */
    //	private LayerBipedArmor fakeArmorLayer = new LayerBipedArmor(null);
    public ModulePig(ModularMinecart cart) {
        super(cart);
        oinkTimer = getRandomTimer();
    }

    private void oink() {
        //		this.getCart().level.playSound((Entity) this.getCart(), SoundEvents.PIG_AMBIENT, 1.0f, (this.getCart().random.nextFloat() - this.getCart().random.nextFloat()) * 0.2f + 1.0f);
    }

    private int getRandomTimer() {
        return oinkTimer = getCart().getRandom().nextInt(900) + 300;
    }

    @Override
    public void update() {
        if (oinkTimer <= 0) {
            oink();
            oinkTimer = getRandomTimer();
        } else {
            --oinkTimer;
        }
    }

    @Nonnull
    private ItemStack getHelmet() {
        if (getCart().getPassengers().isEmpty()) {
            return ItemStack.EMPTY;
        }
        Entity rider = getCart().getPassengers().get(0);
        if (rider != null && rider instanceof LivingEntity) {
            return ((LivingEntity) rider).getItemBySlot(EquipmentSlot.HEAD);
        }
        return ItemStack.EMPTY;
    }

    public boolean hasHelment() {
        @Nonnull ItemStack item = getHelmet();
        return !item.isEmpty() && item.getEquipmentSlot() == EquipmentSlot.HEAD;
    }

    public Identifier getHelmetResource(final boolean isOverlay) {
        if (!hasHelment()) {
            return null;
        }
        @Nonnull ItemStack item = getHelmet();
        if (item.isEmpty()) {
            return null;
        }
        return Identifier.parse("");
        //		return fakeArmorLayer.getArmorResource((Entity) null, item, EntityEquipmentSlot.HEAD, isOverlay ? "overlay" : null);
    }

    public boolean hasHelmetColor(final boolean isOverlay) {
        return getHelmetColor(isOverlay) != -1;
    }

    public int getHelmetColor(final boolean isOverlay) {
//        if (hasHelment())
//        {
//            ItemStack item = getHelmet();
//            return Minecraft.getInstance().cologetItemColors().getColor(item, isOverlay ? 1 : 0);
//        }
        return -1;
    }

    @Override
    public int getConsumption(final boolean isMoving) {
        if (!isMoving) {
            return super.getConsumption(isMoving);
        }
        return 1;
    }
}
