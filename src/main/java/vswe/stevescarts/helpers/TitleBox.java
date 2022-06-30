package vswe.stevescarts.helpers;

public class TitleBox
{
    private final int id;
    private final int x;
    private final int y;
    private final int color;

    public TitleBox(final int id, final int y, final int color)
    {
        this(id, 16, y, color);
    }

    public TitleBox(final int id, final int x, final int y, final int color)
    {
        this.id = id;
        this.x = x;
        this.y = y;
        this.color = color;
    }

    public int getID()
    {
        return id;
    }

    public int getColor()
    {
        return color;
    }

    public int getX()
    {
        return x;
    }

    public int getY()
    {
        return y;
    }
}
