package vswe.stevescarts.modules.realtimers;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Explosion;
import vswe.stevescarts.SCConfig;
import vswe.stevescarts.containers.slots.SlotBase;
import vswe.stevescarts.containers.slots.SlotCakeDynamite;
import vswe.stevescarts.entities.EntityMinecartModular;

import javax.annotation.Nonnull;

public class ModuleCakeServerDynamite extends ModuleCakeServer
{
    private int dynamiteCount;

    private int getMaxDynamiteCount()
    {
        return Math.min(SCConfig.maxDynamites.get(), 25);
    }

    public ModuleCakeServerDynamite(final EntityMinecartModular cart)
    {
        super(cart);
    }

    @Override
    protected SlotBase getSlot(final int slotId, final int x, final int y)
    {
        return new SlotCakeDynamite(getCart(), slotId, 8 + x * 18, 38 + y * 18);
    }

    @Override
    public boolean dropOnDeath()
    {
        return dynamiteCount == 0;
    }

    @Override
    public void onDeath()
    {
        if (dynamiteCount > 0)
        {
            explode();
        }
    }

    private void explode()
    {
        getCart().level.explode(null, getCart().getExactPosition().getX(), getCart().getExactPosition().getY(), getCart().getExactPosition().getZ(), dynamiteCount * .08f, Explosion.BlockInteraction.NONE);
    }

    @Override
    public void update()
    {
        super.update();
        if (!getCart().level.isClientSide)
        {
            @Nonnull ItemStack item = getStack(0);
            //			if (!item.isEmpty() && item.getItem().equals(ModItems.COMPONENTS.get()) && dynamiteCount < getMaxDynamiteCount()) {
            //				final int count = Math.min(getMaxDynamiteCount() - dynamiteCount, item.getCount());
            //				dynamiteCount += count;
            //				@Nonnull
            //				ItemStack itemStack = item;
            //				itemStack.shrink(count);
            //				if (item.getCount() == 0) {
            //					setStack(0, ItemStack.EMPTY);
            //				}
            //			}
        }
    }

    @Override
    public boolean onInteractFirst(final Player entityplayer)
    {
        if (dynamiteCount > 0)
        {
            explode();
            getCart().remove(Entity.RemovalReason.KILLED);
            return true;
        }
        return super.onInteractFirst(entityplayer);
    }
}
