package vswe.stevescarts.client.guis.buttons;

import net.minecraft.entity.player.PlayerEntity;
import vswe.stevescarts.modules.workers.ModuleComputer;

public class ButtonKeyboard extends ButtonAssembly
{
    private char key;
    protected int x;
    protected int y;

    protected ButtonKeyboard(final ModuleComputer module, final int x, final int y, final char key)
    {
        super(module, LOCATION.DEFINED);
        this.key = key;
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString()
    {
        return String.valueOf(getCasedChar(key));
    }

    @Override
    public boolean isVisible()
    {
        return ((ModuleComputer) module).isWriting();
    }

    @Override
    public boolean isEnabled()
    {
        return ((ModuleComputer) module).getWriting().getText().length() < ((ModuleComputer) module).getWriting().getMaxLength();
    }

    @Override
    public boolean hasText()
    {
        return true;
    }

    @Override
    public void onServerClick(final PlayerEntity player, final int mousebutton, final boolean ctrlKey, final boolean shiftKey)
    {
        ((ModuleComputer) module).getWriting().addChar(getCasedChar(key));
        ((ModuleComputer) module).disableShift();
    }

    private char getCasedChar(final char c)
    {
        if (((ModuleComputer) module).isLower())
        {
            return Character.toLowerCase(c);
        }
        return c;
    }

    @Override
    public int X()
    {
        return 70 + y * 10 + x * 25;
    }

    @Override
    public int Y()
    {
        return 40 + y * 25;
    }

    public static void generateKeyboard(final ModuleComputer module)
    {
        new ButtonKeyboard(module, 0, 0, '1');
        new ButtonKeyboard(module, 1, 0, '2');
        new ButtonKeyboard(module, 2, 0, '3');
        new ButtonKeyboard(module, 3, 0, '4');
        new ButtonKeyboard(module, 4, 0, '5');
        new ButtonKeyboard(module, 5, 0, '6');
        new ButtonKeyboard(module, 6, 0, '7');
        new ButtonKeyboard(module, 7, 0, '8');
        new ButtonKeyboard(module, 8, 0, '9');
        new ButtonKeyboard(module, 9, 0, '0');
        new ButtonKeyboard(module, 0, 1, 'Q');
        new ButtonKeyboard(module, 1, 1, 'W');
        new ButtonKeyboard(module, 2, 1, 'E');
        new ButtonKeyboard(module, 3, 1, 'R');
        new ButtonKeyboard(module, 4, 1, 'T');
        new ButtonKeyboard(module, 5, 1, 'Y');
        new ButtonKeyboard(module, 6, 1, 'U');
        new ButtonKeyboard(module, 7, 1, 'I');
        new ButtonKeyboard(module, 8, 1, 'O');
        new ButtonKeyboard(module, 9, 1, 'P');
        new ButtonKeyboard(module, 0, 2, 'A');
        new ButtonKeyboard(module, 1, 2, 'S');
        new ButtonKeyboard(module, 2, 2, 'D');
        new ButtonKeyboard(module, 3, 2, 'F');
        new ButtonKeyboard(module, 4, 2, 'G');
        new ButtonKeyboard(module, 5, 2, 'H');
        new ButtonKeyboard(module, 6, 2, 'J');
        new ButtonKeyboard(module, 7, 2, 'K');
        new ButtonKeyboard(module, 8, 2, 'L');
        new ButtonKeyboard(module, 0, 3, 'Z');
        new ButtonKeyboard(module, 1, 3, 'X');
        new ButtonKeyboard(module, 2, 3, 'C');
        new ButtonKeyboard(module, 3, 3, 'V');
        new ButtonKeyboard(module, 4, 3, 'B');
        new ButtonKeyboard(module, 5, 3, 'N');
        new ButtonKeyboard(module, 6, 3, 'M');
        new ButtonKeyboardSpecial(module, 11, 0, ButtonKeyboardSpecial.KEY.BACKSPACE);
        new ButtonKeyboardSpecial(module, 11, 1, ButtonKeyboardSpecial.KEY.ENTER);
        new ButtonKeyboardSpecial(module, 11, 2, ButtonKeyboardSpecial.KEY.ENTER);
        new ButtonKeyboardSpecial(module, -1, 2, ButtonKeyboardSpecial.KEY.CAPS);
        new ButtonKeyboardSpecial(module, -1, 3, ButtonKeyboardSpecial.KEY.SHIFT);
        new ButtonKeyboardSpecial(module, 11, 3, ButtonKeyboardSpecial.KEY.SHIFT);
    }
}
