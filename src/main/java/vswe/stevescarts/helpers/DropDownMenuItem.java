package vswe.stevescarts.helpers;

import vswe.stevescarts.api.modules.ModuleBase;

public class DropDownMenuItem
{
    private final String name;
    private final int imageID;
    private final VALUETYPE type;
    private boolean isLarge;
    private boolean subOpen;
    private byte value;
    private final Class<? extends ModuleBase> moduleClass;
    private final Class<? extends ModuleBase> excludedClass;
    private int multiCount;
    private int intMinValue;
    private int intMaxValue;

    public DropDownMenuItem(final String name, final int imageID, final VALUETYPE type, final Class<? extends ModuleBase> moduleClass)
    {
        this(name, imageID, type, moduleClass, null);
    }

    public DropDownMenuItem(final String name, final int imageID, final VALUETYPE type, final Class<? extends ModuleBase> moduleClass, final Class<? extends ModuleBase> excludedClass)
    {
        this.name = name;
        this.imageID = imageID;
        this.type = type;
        this.moduleClass = moduleClass;
        this.excludedClass = excludedClass;
        isLarge = false;
        subOpen = false;
        value = 0;
    }

    public String getName()
    {
        return name;
    }

    public Class<? extends ModuleBase> getModuleClass()
    {
        return moduleClass;
    }

    public Class<? extends ModuleBase> getExcludedClass()
    {
        return excludedClass;
    }

    public int getImageID()
    {
        return imageID;
    }

    public boolean hasSubmenu()
    {
        return type != VALUETYPE.BOOL;
    }

    public boolean getIsSubMenuOpen()
    {
        return subOpen;
    }

    public void setIsSubMenuOpen(final boolean val)
    {
        subOpen = val;
    }

    public boolean getIsLarge()
    {
        return isLarge;
    }

    public void setIsLarge(final boolean val)
    {
        isLarge = val;
    }

    public int[] getRect(final int menuX, final int menuY, final int id)
    {
        if (getIsLarge())
        {
            return new int[]{menuX, menuY + id * 20, 130, 20};
        }
        return new int[]{menuX, menuY + id * 20, 54, 20};
    }

    public int[] getSubRect(final int menuX, final int menuY, final int id)
    {
        if (getIsSubMenuOpen())
        {
            return new int[]{menuX - 43, menuY + id * 20 + 2, 52, 16};
        }
        return new int[]{menuX, menuY + id * 20 + 2, 9, 16};
    }

    public VALUETYPE getType()
    {
        return type;
    }

    public boolean getBOOL()
    {
        return value != 0;
    }

    public void setBOOL(final boolean val)
    {
        value = (byte) (val ? 1 : 0);
    }

    public int getINT()
    {
        return value;
    }

    public void setINT(int val)
    {
        if (val < intMinValue)
        {
            val = intMinValue;
        }
        else if (val > intMaxValue)
        {
            val = intMaxValue;
        }
        value = (byte) val;
    }

    public void setMULTIBOOL(final byte val)
    {
        value = val;
    }

    public void setMULTIBOOL(final int i, final boolean val)
    {
        value = (byte) ((value & ~(1 << i)) | (val ? 1 : 0) << i);
    }

    public byte getMULTIBOOL()
    {
        return value;
    }

    public boolean getMULTIBOOL(final int i)
    {
        return (value & 1 << i) != 0x0;
    }

    public void setMULTIBOOLCount(int val)
    {
        if (val > 4)
        {
            val = 4;
        }
        else if (val < 2)
        {
            val = 2;
        }
        multiCount = val;
    }

    public int getMULTIBOOLCount()
    {
        return multiCount;
    }

    public void setINTLimit(final int min, final int max)
    {
        intMinValue = min;
        intMaxValue = max;
        setINT(getINT());
    }

    public enum VALUETYPE
    {
        BOOL, INT, MULTIBOOL
    }
}
