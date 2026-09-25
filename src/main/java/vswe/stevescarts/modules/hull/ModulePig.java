package vswe.stevescarts.modules.hull;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.pig.PigSoundVariants;
import net.minecraft.world.item.ItemStack;
import vswe.stevescarts.api.modules.template.ModuleHull;
import vswe.stevescarts.entities.ModularMinecart;

import javax.annotation.Nonnull;

public class ModulePig extends ModuleHull {
    private int oinkTimer;

    public ModulePig(ModularMinecart cart) {
        super(cart);
        oinkTimer = getRandomTimer();
    }

    private void oink() {
        float pitch = (getCart().getRandom().nextFloat() - getCart().getRandom().nextFloat()) * 0.2F + 1.0F;
        getCart().level().playSound(null, getCart().getX(), getCart().getY(), getCart().getZ(),
                SoundEvents.PIG_SOUNDS.get(PigSoundVariants.SoundSet.CLASSIC).adultSounds().ambientSound().value(),
                SoundSource.NEUTRAL, 1.0F, pitch);
    }

    private int getRandomTimer() {
        return getCart().getRandom().nextInt(900) + 300;
    }

    @Override
    public void update() {
        if (isPlaceholder() || getCart().level().isClientSide()) {
            return;
        }

        if (oinkTimer <= 0) {
            oink();
            oinkTimer = getRandomTimer();
        } else {
            --oinkTimer;
        }
    }

    @Nonnull
    public ItemStack getHelmet() {
        if (getCart().getPassengers().isEmpty()) {
            return ItemStack.EMPTY;
        }
        Entity rider = getCart().getPassengers().get(0);
        if (rider instanceof LivingEntity livingEntity) {
            return livingEntity.getItemBySlot(EquipmentSlot.HEAD);
        }
        return ItemStack.EMPTY;
    }

    @Override
    public int getConsumption(final boolean isMoving) {
        if (!isMoving) {
            return super.getConsumption(isMoving);
        }
        return 1;
    }
}
