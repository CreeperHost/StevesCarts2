package vswe.stevescarts.modules.addons.projectiles;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.ThrownSplashPotion;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import vswe.stevescarts.entities.ModularMinecart;

import javax.annotation.Nonnull;

public class ModulePotion extends ModuleProjectile
{
    public ModulePotion(ModularMinecart cart)
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
        ThrownSplashPotion potionEntity = new ThrownSplashPotion(getCart().level(), 0, 0, 0, item);
        potionEntity.setItem(item);
        return potionEntity;
    }
}
