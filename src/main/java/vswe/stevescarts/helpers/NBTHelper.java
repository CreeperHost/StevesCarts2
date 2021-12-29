package vswe.stevescarts.helpers;

public enum NBTHelper
{
    END, BYTE, SHORT, INT, LONG, FLOAT, DOUBLE, BYTE_ARRAY, STRING, LIST, COMPOUND, INT_ARRAY;

    public int getId()
    {
        return ordinal();
    }
}
