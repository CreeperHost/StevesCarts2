package vswe.stevescarts.computer;

import vswe.stevescarts.modules.workers.ModuleComputer;

public class ComputerVar implements IWriting
{
    private ModuleComputer module;
    private short info;
    private short val;
    private String name;
    private static final String validChars = "? ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    public ComputerVar(final ModuleComputer module)
    {
        name = "??????";
        this.module = module;
    }

    @Override
    public String getText()
    {
        return name.replace("?", "");
    }

    @Override
    public int getMaxLength()
    {
        return 6;
    }

    @Override
    public void addChar(final char c)
    {
        name = name.replace("?", "");
        name += c;
        while (name.length() < getMaxLength())
        {
            name += "?";
        }
    }

    @Override
    public void removeChar()
    {
        name = name.replace("?", "");
        name = name.substring(0, name.length() - 1);
        while (name.length() < getMaxLength())
        {
            name += "?";
        }
    }

    public int getByteValue()
    {
        final byte val = (byte) (this.val & 0xFF);
        return val;
    }

    public void setByteValue(int val)
    {
        if (val < -128)
        {
            val = -128;
        }
        else if (val > 127)
        {
            val = 127;
        }
        if (val < 0)
        {
            val += 256;
        }
        this.val = (short) val;
    }

    public String getFullInfo()
    {
        return getText() + " = " + getByteValue();
    }

    public boolean isEditing()
    {
        return (info & 0x1) != 0x0;
    }

    public void setEditing(final boolean val)
    {
        info &= 0xFFFFFFFE;
        info |= (short) (val ? 1 : 0);
        if (val)
        {
            module.setWriting(this);
        }
        else if (module.getWriting() == this)
        {
            module.setWriting(null);
        }
    }

    public void setInfo(int id, final short val)
    {
        if (id == 0)
        {
            info = val;
            setEditing(isEditing());
        }
        else if (id == 1)
        {
            this.val = val;
        }
        else
        {
            id -= 2;
            final byte char1 = (byte) ((val & 0xFF00) >> 8);
            final byte char2 = (byte) (val & 0xFF);
            name = name.substring(0, id * 2) + getChar(char1) + getChar(char2) + name.substring(id * 2 + 2);
        }
    }

    private char getChar(int index)
    {
        if (index < 0)
        {
            index += 256;
        }
        if (index >= "? ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789".length())
        {
            index = 0;
        }
        return "? ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789".charAt(index);
    }

    public short getInfo(int id)
    {
        if (id == 0)
        {
            return info;
        }
        if (id == 1)
        {
            return val;
        }
        id -= 2;
        final int char1 = getCode(name.charAt(id * 2));
        final int char2 = getCode(name.charAt(id * 2 + 1));
        return (short) (char1 << 8 | char2);
    }

    private int getCode(final char c)
    {
        int index = "? ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789".indexOf(c);
        if (index == -1)
        {
            index = 0;
        }
        return index;
    }
}
