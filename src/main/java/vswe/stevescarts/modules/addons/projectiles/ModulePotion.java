package vswe.stevescarts.modules.addons.projectiles;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import vswe.stevescarts.entitys.EntityMinecartModular;

import javax.annotation.Nonnull;

public class ModulePotion extends ModuleProjectile
{
    public ModulePotion(final EntityMinecartModular cart)
    {
        super(cart);
    }

    @Override
    public boolean isValidProjectile(@Nonnull ItemStack item)
    {
        return item.getItem() == Items.SPLASH_POTION;
    }

    @Override
    public Entity createProjectile(final Entity target, @Nonnull ItemStack item)
    {
        ThrownPotion potionEntity = new ThrownPotion(getCart().level, 0, 0, 0);
        potionEntity.setItem(item);
        return potionEntity;
    }
}
