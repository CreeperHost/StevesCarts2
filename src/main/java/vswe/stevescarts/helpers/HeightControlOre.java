package vswe.stevescarts.helpers;

import java.util.ArrayList;

public record HeightControlOre(String name, boolean useDefaultTexture, String specialTexture, int srcX, int srcY,
                               int spanHighest, int spanLowest, int bestHighest, int bestLowest) {
    public static final ArrayList<HeightControlOre> ores;

    static {
        ores = new ArrayList<>();
        new HeightControlOre("Diamond", 0, -64, 16, -59, -58);
        new HeightControlOre("Redstone", 1, -64, 15, -59, -58);
        new HeightControlOre("Gold", 2, -64, 32, -16, -16);
        new HeightControlOre("Lapis Lazuli", 3, -64, 64, 0, 0);
        new HeightControlOre("Iron", 4, -64, 319, 16, 16);
        new HeightControlOre("Coal", 5, 0, 319, 96, 96);
        new HeightControlOre("Emerald", 6, -16, 319, 232, 232);
        new HeightControlOre("Copper", 7, -16, 112, 48, 48);
    }

    public HeightControlOre(final String name, final int textureid, final int spanLowest, final int spanHighest, final int bestLowest, final int bestHighest) {
        this(name, true, "", 0, (textureid * 2 + 1) * 4, spanHighest, spanLowest, bestHighest, bestLowest);
        HeightControlOre.ores.add(this);
    }

    public HeightControlOre(final String name, final String texture, final int srcX, final int srcY, final int spanHighest, final int spanLowest, final int bestHighest, final int bestLowest) {
        this(name, false, texture, srcX, srcY, spanHighest, spanLowest, bestHighest, bestLowest);
        HeightControlOre.ores.add(this);
    }
}
