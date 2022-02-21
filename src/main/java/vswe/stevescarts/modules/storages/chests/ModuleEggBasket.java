package vswe.stevescarts.modules.storages.chests;

import net.minecraft.world.item.ItemStack;
import vswe.stevescarts.entitys.EntityMinecartModular;
import vswe.stevescarts.helpers.ComponentTypes;

import javax.annotation.Nonnull;
import java.util.Random;

public class ModuleEggBasket extends ModuleChest
{
    public ModuleEggBasket(final EntityMinecartModular cart)
    {
        super(cart);
    }

    @Override
    protected int getInventoryWidth()
    {
        return 6;
    }

    @Override
    protected int getInventoryHeight()
    {
        return 4;
    }

    @Override
    protected boolean playChestSound()
    {
        return false;
    }

    @Override
    protected float getLidSpeed()
    {
        return 0.02094395f;
    }

    @Override
    protected float chestFullyOpenAngle()
    {
        return 0.3926991f;
    }

    @Override
    public byte getExtraData()
    {
        return 0;
    }

    @Override
    public boolean hasExtraData()
    {
        return true;
    }

    @Override
    public void setExtraData(final byte b)
    {
        if (b == 0)
        {
            return;
        }
        final Random rand = getCart().random;
        final int eggs = 1 + rand.nextInt(4) + rand.nextInt(4);
        @Nonnull ItemStack easterEgg = ComponentTypes.PAINTED_EASTER_EGG.getItemStack(eggs);
        setStack(0, easterEgg);
    }
}
