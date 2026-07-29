package vswe.stevescarts.modules.addons.projectiles;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import vswe.stevescarts.api.modules.template.ModuleAddon;
import vswe.stevescarts.entities.ModularMinecart;

import javax.annotation.Nonnull;

public abstract class ModuleProjectile extends ModuleAddon {
    public ModuleProjectile(ModularMinecart cart) {
        super(cart);
    }

    public abstract boolean isValidProjectile(@Nonnull ItemStack p0);

    public abstract Entity createProjectile(final Entity p0, @Nonnull ItemStack p1);
}
