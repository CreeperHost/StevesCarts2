package vswe.stevescarts.helpers;


import net.minecraft.util.Direction;
import vswe.stevescarts.blocks.tileentities.TileEntityDistributor;

public class DistributorSide
{
    private int id;
    private Localization.GUI.DISTRIBUTOR name;
    private Direction side;
    private int data;

    public DistributorSide(final int id, final Localization.GUI.DISTRIBUTOR name, final Direction side)
    {
        this.name = name;
        this.id = id;
        this.side = side;
        data = 0;
    }

    public void setData(final int data)
    {
        this.data = data;
    }

    public int getId()
    {
        return id;
    }

    public String getName()
    {
        return name.translate();
    }

    public Direction getSide()
    {
        return side;
    }

    public Direction getFacing()
    {
        return side;
    }

    public int getData()
    {
        return data;
    }

    public boolean isEnabled(final TileEntityDistributor distributor)
    {
        if (distributor.getInventories().length == 0)
        {
            return false;
        }
        if (getSide() == Direction.DOWN)
        {
            return !distributor.hasBot;
        }
        return getSide() != Direction.UP || !distributor.hasTop;
    }

    public boolean isSet(final int id)
    {
        return (data & 1 << id) != 0x0;
    }

    public void set(final int id)
    {
        int count = 0;
        for (final DistributorSetting setting : DistributorSetting.settings)
        {
            if (isSet(setting.getId()))
            {
                ++count;
            }
        }
        if (count < 11)
        {
            data |= 1 << id;
        }
    }

    public void reset(final int id)
    {
        data &= ~(1 << id);
    }

    public short getLowShortData()
    {
        return (short) (getData() & 0xFFFF);
    }

    public short getHighShortData()
    {
        return (short) (getData() >> 16 & 0xFFFF);
    }

    public void setLowShortData(final short data)
    {
        this.data = (fixSignedIssue(getHighShortData()) << 16 | fixSignedIssue(data));
    }

    public void setHighShortData(final short data)
    {
        this.data = (fixSignedIssue(getLowShortData()) | fixSignedIssue(data) << 16);
    }

    private int fixSignedIssue(final short val)
    {
        if (val < 0)
        {
            return val + 65536;
        }
        return val;
    }

    public String getInfo()
    {
        return Localization.GUI.DISTRIBUTOR.SIDE_TOOL_TIP.translate(getName());
    }
}
