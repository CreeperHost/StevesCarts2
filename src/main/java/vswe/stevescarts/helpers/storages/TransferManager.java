package vswe.stevescarts.helpers.storages;

import vswe.stevescarts.entitys.EntityMinecartModular;

public class TransferManager
{
    private int side;
    private int setting;
    private int lastsetting;
    private int lowestsetting;
    private int workload;
    private EntityMinecartModular cart;
    private boolean toCartEnabled;
    private boolean fromCartEnabled;

    public TransferManager()
    {
        reset();
    }

    public void reset()
    {
        side = 0;
        setting = -1;
        lastsetting = 0;
        lowestsetting = 0;
        workload = 0;
        cart = null;
        toCartEnabled = true;
        fromCartEnabled = true;
    }

    public int getSetting()
    {
        return setting;
    }

    public void setSetting(final int val)
    {
        setting = val;
    }

    public int getSide()
    {
        return side;
    }

    public void setSide(final int val)
    {
        side = val;
    }

    public int getLastSetting()
    {
        return lastsetting;
    }

    public void setLastSetting(final int val)
    {
        lastsetting = val;
    }

    public int getLowestSetting()
    {
        return lowestsetting;
    }

    public void setLowestSetting(final int val)
    {
        lowestsetting = val;
    }

    public int getWorkload()
    {
        return workload;
    }

    public void setWorkload(final int val)
    {
        workload = val;
    }

    public EntityMinecartModular getCart()
    {
        return cart;
    }

    public void setCart(final EntityMinecartModular val)
    {
        cart = val;
    }

    public boolean getFromCartEnabled()
    {
        return fromCartEnabled;
    }

    public void setFromCartEnabled(final boolean val)
    {
        fromCartEnabled = val;
    }

    public boolean getToCartEnabled()
    {
        return toCartEnabled;
    }

    public void setToCartEnabled(final boolean val)
    {
        toCartEnabled = val;
    }
}
