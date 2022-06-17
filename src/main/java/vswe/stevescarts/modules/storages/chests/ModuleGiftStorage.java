package vswe.stevescarts.modules.storages.chests;

import net.minecraft.world.item.ItemStack;
import vswe.stevescarts.entitys.EntityMinecartModular;
import vswe.stevescarts.helpers.GiftItem;

import java.util.ArrayList;

@Deprecated(forRemoval = true)
public class ModuleGiftStorage extends ModuleChest
{
    public ModuleGiftStorage(final EntityMinecartModular cart)
    {
        super(cart);
    }

    @Override
    protected int getInventoryWidth()
    {
        return 9;
    }

    @Override
    protected int getInventoryHeight()
    {
        return 4;
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
        final ArrayList<ItemStack> items = GiftItem.generateItems(getCart().random, GiftItem.ChristmasList, 50 + getCart().random.nextInt(700), 1 + getCart().random.nextInt(5));
        for (int i = 0; i < items.size(); ++i)
        {
            setStack(i, items.get(i));
        }
    }
}
