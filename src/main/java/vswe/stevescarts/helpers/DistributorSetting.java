package vswe.stevescarts.helpers;

import vswe.stevescarts.blocks.tileentities.TileEntityDistributor;
import vswe.stevescarts.blocks.tileentities.TileEntityManager;

import java.util.ArrayList;

public class DistributorSetting
{
    public static ArrayList<DistributorSetting> settings;
    private final int id;
    private final int imageId;
    private final boolean top;
    private final Localization.GUI.DISTRIBUTOR name;

    public DistributorSetting(final int id, final boolean top, final Localization.GUI.DISTRIBUTOR name)
    {
        this.id = id;
        this.top = top;
        this.name = name;
        imageId = id / 2;
    }

    public boolean isValid(final TileEntityManager manager, final int chunkId, final boolean top)
    {
        return top == this.top;
    }

    public int getId()
    {
        return id;
    }

    public int getImageId()
    {
        return imageId;
    }

    public String getName(final TileEntityManager[] manager)
    {
        if (manager != null && manager.length > 1)
        {
            return name.translate() + " (" + (getIsTop() ? Localization.GUI.DISTRIBUTOR.MANAGER_TOP.translate() : Localization.GUI.DISTRIBUTOR.MANAGER_BOT.translate()) + ")";
        }
        return name.translate();
    }

    public boolean getIsTop()
    {
        return top;
    }

    public boolean isEnabled(final TileEntityDistributor distributor)
    {
        if (distributor.getInventories().length == 0)
        {
            return false;
        }
        if (top)
        {
            return distributor.hasTop;
        }
        return distributor.hasBot;
    }

    static
    {
        (DistributorSetting.settings = new ArrayList<>()).add(new DistributorSetting(0, true, Localization.GUI.DISTRIBUTOR.SETTING_ALL));
        DistributorSetting.settings.add(new DistributorSetting(1, false, Localization.GUI.DISTRIBUTOR.SETTING_ALL));
        DistributorSetting.settings.add(new DistributorSettingColor(2, true, Localization.GUI.DISTRIBUTOR.SETTING_RED, 1));
        DistributorSetting.settings.add(new DistributorSettingColor(3, false, Localization.GUI.DISTRIBUTOR.SETTING_RED, 1));
        DistributorSetting.settings.add(new DistributorSettingColor(4, true, Localization.GUI.DISTRIBUTOR.SETTING_BLUE, 2));
        DistributorSetting.settings.add(new DistributorSettingColor(5, false, Localization.GUI.DISTRIBUTOR.SETTING_BLUE, 2));
        DistributorSetting.settings.add(new DistributorSettingColor(6, true, Localization.GUI.DISTRIBUTOR.SETTING_YELLOW, 3));
        DistributorSetting.settings.add(new DistributorSettingColor(7, false, Localization.GUI.DISTRIBUTOR.SETTING_YELLOW, 3));
        DistributorSetting.settings.add(new DistributorSettingColor(8, true, Localization.GUI.DISTRIBUTOR.SETTING_GREEN, 4));
        DistributorSetting.settings.add(new DistributorSettingColor(9, false, Localization.GUI.DISTRIBUTOR.SETTING_GREEN, 4));
        DistributorSetting.settings.add(new DistributorSettingChunk(10, true, Localization.GUI.DISTRIBUTOR.SETTING_TOP_LEFT, 0));
        DistributorSetting.settings.add(new DistributorSettingChunk(11, false, Localization.GUI.DISTRIBUTOR.SETTING_TOP_LEFT, 0));
        DistributorSetting.settings.add(new DistributorSettingChunk(12, true, Localization.GUI.DISTRIBUTOR.SETTING_TOP_RIGHT, 1));
        DistributorSetting.settings.add(new DistributorSettingChunk(13, false, Localization.GUI.DISTRIBUTOR.SETTING_TOP_RIGHT, 1));
        DistributorSetting.settings.add(new DistributorSettingChunk(14, true, Localization.GUI.DISTRIBUTOR.SETTING_BOTTOM_LEFT, 2));
        DistributorSetting.settings.add(new DistributorSettingChunk(15, false, Localization.GUI.DISTRIBUTOR.SETTING_BOTTOM_LEFT, 2));
        DistributorSetting.settings.add(new DistributorSettingChunk(16, true, Localization.GUI.DISTRIBUTOR.SETTING_BOTTOM_RIGHT, 3));
        DistributorSetting.settings.add(new DistributorSettingChunk(17, false, Localization.GUI.DISTRIBUTOR.SETTING_BOTTOM_RIGHT, 3));
        DistributorSetting.settings.add(new DistributorSettingDirection(18, true, Localization.GUI.DISTRIBUTOR.SETTING_TO_CART, true));
        DistributorSetting.settings.add(new DistributorSettingDirection(19, false, Localization.GUI.DISTRIBUTOR.SETTING_TO_CART, true));
        DistributorSetting.settings.add(new DistributorSettingDirection(20, true, Localization.GUI.DISTRIBUTOR.SETTING_FROM_CART, false));
        DistributorSetting.settings.add(new DistributorSettingDirection(21, false, Localization.GUI.DISTRIBUTOR.SETTING_FROM_CART, false));
    }

    private static class DistributorSettingColor extends DistributorSetting
    {
        private int color;

        public DistributorSettingColor(final int id, final boolean top, final Localization.GUI.DISTRIBUTOR name, final int color)
        {
            super(id, top, name);
            this.color = color;
        }

        @Override
        public boolean isValid(final TileEntityManager manager, final int chunkId, final boolean top)
        {
            if (manager.layoutType == 0)
            {
                return super.isValid(manager, chunkId, top);
            }
            return super.isValid(manager, chunkId, top) && manager.color[chunkId] == color;
        }
    }

    private static class DistributorSettingChunk extends DistributorSetting
    {
        private int chunk;

        public DistributorSettingChunk(final int id, final boolean top, final Localization.GUI.DISTRIBUTOR name, final int chunk)
        {
            super(id, top, name);
            this.chunk = chunk;
        }

        @Override
        public boolean isValid(final TileEntityManager manager, final int chunkId, final boolean top)
        {
            if (manager.layoutType == 0)
            {
                return super.isValid(manager, chunkId, top);
            }
            return super.isValid(manager, chunkId, top) && chunk == chunkId;
        }
    }

    private static class DistributorSettingDirection extends DistributorSetting
    {
        private boolean toCart;

        public DistributorSettingDirection(final int id, final boolean top, final Localization.GUI.DISTRIBUTOR name, final boolean toCart)
        {
            super(id, top, name);
            this.toCart = toCart;
        }

        @Override
        public boolean isValid(final TileEntityManager manager, final int chunkId, final boolean top)
        {
            if (manager.layoutType == 0)
            {
                return super.isValid(manager, chunkId, top);
            }
            return super.isValid(manager, chunkId, top) && manager.toCart[chunkId] == toCart;
        }
    }
}
