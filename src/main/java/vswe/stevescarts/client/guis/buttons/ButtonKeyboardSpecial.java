package vswe.stevescarts.client.guis.buttons;

import net.minecraft.entity.player.PlayerEntity;
import vswe.stevescarts.computer.ComputerVar;
import vswe.stevescarts.modules.workers.ModuleComputer;

public class ButtonKeyboardSpecial extends ButtonKeyboard
{
    private KEY key;

    protected ButtonKeyboardSpecial(final ModuleComputer module, final int x, final int y, final KEY key)
    {
        super(module, x, y, ' ');
        this.key = key;
    }

    @Override
    public String toString()
    {
        return key.toString();
    }

    @Override
    public boolean isEnabled()
    {
        if (key == KEY.BACKSPACE || key == KEY.ENTER)
        {
            return ((ModuleComputer) module).getWriting().getText().length() > 0;
        }
        return super.isEnabled();
    }

    @Override
    public int texture()
    {
        if (key == KEY.CAPS)
        {
            return 26;
        }
        if (key == KEY.SHIFT)
        {
            return 27;
        }
        if (key == KEY.BACKSPACE)
        {
            return 28;
        }
        if (key == KEY.ENTER)
        {
            return 29;
        }
        return super.texture();
    }

    @Override
    public int X()
    {
        final int temp = y;
        y = 0;
        final int temp2 = super.X();
        y = temp;
        return temp2;
    }

    @Override
    public boolean hasText()
    {
        return false;
    }

    @Override
    public int borderID()
    {
        if ((key == KEY.SHIFT && ((ModuleComputer) module).getShift()) || (key == KEY.CAPS && ((ModuleComputer) module).getCaps()))
        {
            return 3;
        }
        return super.borderID();
    }

    @Override
    public void onServerClick(final PlayerEntity player, final int mousebutton, final boolean ctrlKey, final boolean shiftKey)
    {
        if (key == KEY.BACKSPACE)
        {
            ((ModuleComputer) module).getWriting().removeChar();
        }
        else if (key == KEY.ENTER)
        {
            if (((ModuleComputer) module).getWriting() instanceof ComputerVar)
            {
                ((ComputerVar) ((ModuleComputer) module).getWriting()).setEditing(false);
            }
        }
        else if (key == KEY.SHIFT)
        {
            ((ModuleComputer) module).flipShift();
        }
        else if (key == KEY.CAPS)
        {
            ((ModuleComputer) module).flipCaps();
        }
    }

    public enum KEY
    {
        SHIFT, CAPS, BACKSPACE, ENTER
    }
}
