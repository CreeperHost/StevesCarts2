package vswe.stevescarts.modules.addons.projectiles;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import vswe.stevescarts.entitys.EntityCake;
import vswe.stevescarts.entitys.EntityMinecartModular;

import javax.annotation.Nonnull;

public class ModuleCake extends ModuleProjectile
{
    public ModuleCake(final EntityMinecartModular cart)
    {
        super(cart);
    }

    @Override
    public boolean isValidProjectile(@Nonnull ItemStack item)
    {
        return item.getItem() == Items.CAKE;
    }

    @Override
    public Entity createProjectile(final Entity target, @Nonnull ItemStack item)
    {
        return new EntityCake(getCart().level);
    }
}
