package vswe.stevescarts.helpers;

import java.util.ArrayList;

public class HeightControlOre
{
    public final String name;
    public final boolean useDefaultTexture;
    public final String specialTexture;
    public final int srcX;
    public final int srcY;
    public final int spanHighest;
    public final int spanLowest;
    public final int bestHighest;
    public final int bestLowest;
    public static final ArrayList<HeightControlOre> ores;

    public HeightControlOre(final String name, final int textureid, final int spanLowest, final int spanHighest, final int bestLowest, final int bestHighest)
    {
        this.name = name;
        useDefaultTexture = true;
        specialTexture = "";
        this.spanHighest = spanHighest;
        this.spanLowest = spanLowest;
        this.bestHighest = bestHighest;
        this.bestLowest = bestLowest;
        srcX = 0;
        srcY = (textureid * 2 + 1) * 4;
        HeightControlOre.ores.add(this);
    }

    public HeightControlOre(final String name, final String texture, final int srcX, final int srcY, final int spanHighest, final int spanLowest, final int bestHighest, final int bestLowest)
    {
        this.name = name;
        useDefaultTexture = false;
        specialTexture = texture;
        this.spanHighest = spanHighest;
        this.spanLowest = spanLowest;
        this.bestHighest = bestHighest;
        this.bestLowest = bestLowest;
        this.srcX = srcX;
        this.srcY = srcY;
        HeightControlOre.ores.add(this);
    }

    static
    {
        //TODO the "Optimal" ranges need some work.
        ores = new ArrayList<>();
        new HeightControlOre("Diamond", 0, -64, 16, -60, -58);
        new HeightControlOre("Redstone", 1, -64, 16, -60, -58);
        new HeightControlOre("Gold", 2, -64, 32, -17, -15);
        new HeightControlOre("Lapis Lazuli", 3, -64, 64, -2, 0);
        new HeightControlOre("Iron", 4, -64, 320, 0, 40);
        new HeightControlOre("Coal", 5, 0, 320, 44, 95);
        new HeightControlOre("Emerald", 6, -16, 320, 235, 236);
        new HeightControlOre("Copper", 7, -16, 112, 47, 49);
    }
}
