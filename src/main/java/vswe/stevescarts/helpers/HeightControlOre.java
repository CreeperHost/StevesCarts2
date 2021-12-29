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

    public HeightControlOre(final String name, final int textureid, final int spanHighest, final int bestHighest, final int bestLowest)
    {
        this.name = name;
        useDefaultTexture = true;
        specialTexture = "";
        this.spanHighest = spanHighest;
        spanLowest = 1;
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
        ores = new ArrayList<>();
        new HeightControlOre("Diamond", 0, 15, 12, 5);
        new HeightControlOre("Redstone", 1, 16, 13, 5);
        new HeightControlOre("Gold", 2, 33, 30, 5);
        new HeightControlOre("Lapis Lazuli", 3, 32, 18, 11);
        new HeightControlOre("Iron", 4, 67, 41, 5);
        new HeightControlOre("Coal", 5, 131, 40, 5);
        new HeightControlOre("Emerald", 6, 32, 29, 5);
    }
}
