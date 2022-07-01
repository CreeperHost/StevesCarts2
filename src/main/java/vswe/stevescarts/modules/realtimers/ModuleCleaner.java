package vswe.stevescarts.modules.realtimers;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import vswe.stevescarts.entitys.EntityMinecartModular;
import vswe.stevescarts.api.modules.ModuleBase;

import javax.annotation.Nonnull;
import java.util.List;

public class ModuleCleaner extends ModuleBase
{
    public ModuleCleaner(final EntityMinecartModular cart)
    {
        super(cart);
    }

    @Override
    public void update()
    {
        super.update();
        if (getCart().level.isClientSide)
        {
            return;
        }
        if (getCart().hasFuel())
        {
            suck();
        }
        clean();
    }

    private double calculatemotion(final double dif)
    {
        if (dif > -0.5D && dif < 0.5D)
        {
            return 0;
        }

        return 1 / (dif * 2);
    }

    private void suck()
    {
        final List<ItemEntity> list = getCart().level.getEntitiesOfClass(ItemEntity.class, getCart().getBoundingBox().inflate(3.0, 1.0, 3.0));
        for (ItemEntity eItem : list)
        {
            if (!eItem.isPickable())
            {
                double difX = getCart().getExactPosition().getX() - eItem.blockPosition().getX();
                double difY = getCart().getExactPosition().getY() - eItem.blockPosition().getY();
                double difZ = getCart().getExactPosition().getZ() - eItem.blockPosition().getZ();
                //				eItem.moveTo(calculatemotion(difX), calculatemotion(difY), calculatemotion(difZ));
                //				eItem.xOld += calculatemotion(difX);
                //				eItem.yo += calculatemotion(difY);
                //				eItem.zo += calculatemotion(difZ);
            }
        }
    }

    private void clean()
    {
        final List<ItemEntity> list = getCart().level.getEntitiesOfClass(ItemEntity.class, getCart().getBoundingBox().inflate(3.0, 1.0, 3.0));
        for (ItemEntity eItem : list)
        {
            if (!eItem.isPickable() && !eItem.isRemoved())
            {
                int stackSize = eItem.getItem().getCount();
                getCart().addItemToChest(eItem.getItem());
                if (stackSize != eItem.getItem().getCount())
                {
                    getCart().level.playSound(null, getCart().blockPosition(), SoundEvents.ITEM_PICKUP, SoundSource.NEUTRAL, 0.2f, ((this.getCart().random.nextFloat() - this.getCart().random.nextFloat()) * 0.7f + 1.0f) * 2.0f);
                    if (eItem.getItem().getCount() <= 0)
                    {
                        eItem.remove(Entity.RemovalReason.DISCARDED);
                    }
                }
                else if (failPickup(eItem.getItem()))
                {
                    eItem.remove(Entity.RemovalReason.DISCARDED);
                }
            }
        }
    }

    private boolean failPickup(@Nonnull ItemStack item)
    {
        int x = normalize(getCart().xo);
        int z = normalize(getCart().zo);
        if (x == 0 && z == 0)
        {
            return false;
        }

        ItemEntity entityitem = new ItemEntity(getCart().level, getCart().x(), getCart().y(), getCart().z(), item.copy());
        getCart().level.addFreshEntity(entityitem);
        return true;
    }

    private int normalize(double val)
    {
        return Double.compare(val, 0.0);
    }
}
