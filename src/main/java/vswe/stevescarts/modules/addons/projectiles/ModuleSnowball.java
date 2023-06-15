package vswe.stevescarts.modules.addons.projectiles;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.Snowball;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import vswe.stevescarts.entities.EntityMinecartModular;

import javax.annotation.Nonnull;

public class ModuleSnowball extends ModuleProjectile
{
    public ModuleSnowball(final EntityMinecartModular cart)
    {
        super(cart);
    }

    @Override
    public boolean isValidProjectile(@Nonnull ItemStack item)
    {
        return item.getItem() == Items.SNOWBALL;
    }

    @Override
    public Entity createProjectile(final Entity target, @Nonnull ItemStack item)
    {
        return new Snowball(getCart().level(), 0, 0, 0);
    }
}
