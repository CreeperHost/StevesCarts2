package vswe.stevescarts.modules.addons.projectiles;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.hurtingprojectile.SmallFireball;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;
import vswe.stevescarts.entities.ModularMinecart;

import javax.annotation.Nonnull;

public class ModuleFireball extends ModuleProjectile {
    public ModuleFireball(ModularMinecart cart) {
        super(cart);
    }

    @Override
    public boolean isValidProjectile(@Nonnull ItemStack item) {
        return item.getItem() == Items.FIRE_CHARGE;
    }

    @Override
    public Entity createProjectile(final Entity target, @Nonnull ItemStack item) {
        return new SmallFireball(getCart().level(), 0, 0, 0, Vec3.ZERO);
    }
}
