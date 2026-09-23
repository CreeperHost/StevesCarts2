package vswe.stevescarts.modules.realtimers;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import vswe.stevescarts.api.modules.ModuleBase;
import vswe.stevescarts.entities.ModularMinecart;

import javax.annotation.Nonnull;
import java.util.List;

public class ModuleCleaner extends ModuleBase {
    private static final double MAX_SUCTION_SPEED = 0.35D;

    public ModuleCleaner(ModularMinecart cart) {
        super(cart);
    }

    @Override
    public void update() {
        super.update();
        if (getCart().level().isClientSide()) {
            return;
        }
        if (getCart().hasFuel()) {
            suck();
        }
        clean();
    }

    private void suck() {
        final List<ItemEntity> list = getCart().level().getEntitiesOfClass(ItemEntity.class, getCart().getBoundingBox().inflate(3.0, 1.0, 3.0));
        for (ItemEntity eItem : list) {
            Vec3 direction = getCart().position().add(0.0D, 0.35D, 0.0D).subtract(eItem.position());
            if (direction.lengthSqr() < 0.01D) continue;

            Vec3 motion = eItem.getDeltaMovement().scale(0.8D).add(direction.normalize().scale(0.12D));
            if (motion.lengthSqr() > MAX_SUCTION_SPEED * MAX_SUCTION_SPEED) {
                motion = motion.normalize().scale(MAX_SUCTION_SPEED);
            }
            eItem.setDeltaMovement(motion);
        }
    }

    private void clean() {
        final List<ItemEntity> list = getCart().level().getEntitiesOfClass(ItemEntity.class, getCart().getBoundingBox().inflate(0.6D, 0.75D, 0.6D));
        for (ItemEntity eItem : list) {
            if (!eItem.isPickable() && !eItem.isRemoved()) {
                int stackSize = eItem.getItem().getCount();
                getCart().addItemToChest(eItem.getItem());
                if (stackSize != eItem.getItem().getCount()) {
                    getCart().level().playSound(null, getCart().blockPosition(), SoundEvents.ITEM_PICKUP, SoundSource.NEUTRAL, 0.2f, ((this.getCart().getRandom().nextFloat() - this.getCart().getRandom().nextFloat()) * 0.7f + 1.0f) * 2.0f);
                    if (eItem.getItem().getCount() <= 0) {
                        eItem.remove(Entity.RemovalReason.DISCARDED);
                    }
                } else if (failPickup(eItem.getItem())) {
                    eItem.remove(Entity.RemovalReason.DISCARDED);
                }
            }
        }
    }

    private boolean failPickup(@Nonnull ItemStack item) {
        int x = normalize(getCart().xo);
        int z = normalize(getCart().zo);
        if (x == 0 && z == 0) {
            return false;
        }

        ItemEntity entityitem = new ItemEntity(getCart().level(), getCart().x(), getCart().y(), getCart().z(), item.copy());
        getCart().level().addFreshEntity(entityitem);
        return true;
    }

    private int normalize(double val) {
        return Double.compare(val, 0.0);
    }
}
