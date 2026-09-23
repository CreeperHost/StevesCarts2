package vswe.stevescarts.helpers;

import net.minecraft.network.chat.Component;

import vswe.stevescarts.blocks.tileentities.TileEntityDistributor;
import vswe.stevescarts.blocks.tileentities.TileEntityManager;

import java.util.ArrayList;

public class DistributorSetting {
    public static ArrayList<DistributorSetting> settings;

    static {
        (DistributorSetting.settings = new ArrayList<>()).add(new DistributorSetting(0, true, "gui.stevescarts.distributorAll"));
        DistributorSetting.settings.add(new DistributorSetting(1, false, "gui.stevescarts.distributorAll"));
        DistributorSetting.settings.add(new DistributorSettingColor(2, true, "gui.stevescarts.distributorRed", 1));
        DistributorSetting.settings.add(new DistributorSettingColor(3, false, "gui.stevescarts.distributorRed", 1));
        DistributorSetting.settings.add(new DistributorSettingColor(4, true, "gui.stevescarts.distributorBlue", 2));
        DistributorSetting.settings.add(new DistributorSettingColor(5, false, "gui.stevescarts.distributorBlue", 2));
        DistributorSetting.settings.add(new DistributorSettingColor(6, true, "gui.stevescarts.distributorYellow", 3));
        DistributorSetting.settings.add(new DistributorSettingColor(7, false, "gui.stevescarts.distributorYellow", 3));
        DistributorSetting.settings.add(new DistributorSettingColor(8, true, "gui.stevescarts.distributorGreen", 4));
        DistributorSetting.settings.add(new DistributorSettingColor(9, false, "gui.stevescarts.distributorGreen", 4));
        DistributorSetting.settings.add(new DistributorSettingChunk(10, true, "gui.stevescarts.distributorTopLeft", 0));
        DistributorSetting.settings.add(new DistributorSettingChunk(11, false, "gui.stevescarts.distributorTopLeft", 0));
        DistributorSetting.settings.add(new DistributorSettingChunk(12, true, "gui.stevescarts.distributorTopRight", 1));
        DistributorSetting.settings.add(new DistributorSettingChunk(13, false, "gui.stevescarts.distributorTopRight", 1));
        DistributorSetting.settings.add(new DistributorSettingChunk(14, true, "gui.stevescarts.distributorBottomLeft", 2));
        DistributorSetting.settings.add(new DistributorSettingChunk(15, false, "gui.stevescarts.distributorBottomLeft", 2));
        DistributorSetting.settings.add(new DistributorSettingChunk(16, true, "gui.stevescarts.distributorBottomRight", 3));
        DistributorSetting.settings.add(new DistributorSettingChunk(17, false, "gui.stevescarts.distributorBottomRight", 3));
        DistributorSetting.settings.add(new DistributorSettingDirection(18, true, "gui.stevescarts.distributorToCart", true));
        DistributorSetting.settings.add(new DistributorSettingDirection(19, false, "gui.stevescarts.distributorToCart", true));
        DistributorSetting.settings.add(new DistributorSettingDirection(20, true, "gui.stevescarts.distributorFromCart", false));
        DistributorSetting.settings.add(new DistributorSettingDirection(21, false, "gui.stevescarts.distributorFromCart", false));
    }

    private final int id;
    private final int imageId;
    private final boolean top;
    private final String name;

    public DistributorSetting(final int id, final boolean top, final String name) {
        this.id = id;
        this.top = top;
        this.name = name;
        imageId = id / 2;
    }

    public boolean isValid(final TileEntityManager manager, final int chunkId, final boolean top) {
        return top == this.top;
    }

    public int getId() {
        return id;
    }

    public int getImageId() {
        return imageId;
    }

    public String getName(final TileEntityManager[] manager) {
        if (manager != null && manager.length > 1) {
            return Component.translatable(name).getString() + " (" + (getIsTop() ? Component.translatable("gui.stevescarts.managerTop").getString() : Component.translatable("gui.stevescarts.managerBot").getString()) + ")";
        }
        return Component.translatable(name).getString();
    }

    public boolean getIsTop() {
        return top;
    }

    public boolean isEnabled(final TileEntityDistributor distributor) {
        if (distributor.getInventories().length == 0) {
            return false;
        }
        if (top) {
            return distributor.hasTop;
        }
        return distributor.hasBot;
    }

    private static class DistributorSettingColor extends DistributorSetting {
        private final int color;

        public DistributorSettingColor(final int id, final boolean top, final String name, final int color) {
            super(id, top, name);
            this.color = color;
        }

        @Override
        public boolean isValid(final TileEntityManager manager, final int chunkId, final boolean top) {
            if (manager.layoutType == 0) {
                return super.isValid(manager, chunkId, top);
            }
            return super.isValid(manager, chunkId, top) && manager.color[chunkId] == color;
        }
    }

    private static class DistributorSettingChunk extends DistributorSetting {
        private final int chunk;

        public DistributorSettingChunk(final int id, final boolean top, final String name, final int chunk) {
            super(id, top, name);
            this.chunk = chunk;
        }

        @Override
        public boolean isValid(final TileEntityManager manager, final int chunkId, final boolean top) {
            if (manager.layoutType == 0) {
                return super.isValid(manager, chunkId, top);
            }
            return super.isValid(manager, chunkId, top) && chunk == chunkId;
        }
    }

    private static class DistributorSettingDirection extends DistributorSetting {
        private final boolean toCart;

        public DistributorSettingDirection(final int id, final boolean top, final String name, final boolean toCart) {
            super(id, top, name);
            this.toCart = toCart;
        }

        @Override
        public boolean isValid(final TileEntityManager manager, final int chunkId, final boolean top) {
            if (manager.layoutType == 0) {
                return super.isValid(manager, chunkId, top);
            }
            return super.isValid(manager, chunkId, top) && manager.toCart[chunkId] == toCart;
        }
    }
}
