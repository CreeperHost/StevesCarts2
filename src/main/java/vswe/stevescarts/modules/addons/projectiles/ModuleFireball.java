package vswe.stevescarts.modules.addons.projectiles;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.Fireball;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import vswe.stevescarts.entitys.EntityMinecartModular;

import javax.annotation.Nonnull;

public class ModuleFireball extends ModuleProjectile
{
    public ModuleFireball(final EntityMinecartModular cart)
    {
        super(cart);
    }

    @Override
    public boolean isValidProjectile(@Nonnull ItemStack item)
    {
        return item.getItem() == Items.FIRE_CHARGE;
    }

    @Override
    public Entity createProjectile(final Entity target, @Nonnull ItemStack item)
    {
        return new Fireball(getCart().level, 0, 0, 0, 0, 0, 0);
    }
}
