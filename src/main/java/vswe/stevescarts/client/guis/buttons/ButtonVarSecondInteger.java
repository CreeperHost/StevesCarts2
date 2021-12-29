package vswe.stevescarts.client.guis.buttons;

import vswe.stevescarts.computer.ComputerTask;
import vswe.stevescarts.modules.workers.ModuleComputer;

public class ButtonVarSecondInteger extends ButtonVarInteger
{
    public ButtonVarSecondInteger(final ModuleComputer module, final LOCATION loc, final int dif)
    {
        super(module, loc, dif);
    }

    @Override
    protected String getName()
    {
        return "second";
    }

    @Override
    protected boolean isVarVisible(final ComputerTask task)
    {
        return task.getVarUseSecondVar();
    }

    @Override
    protected int getInteger(final ComputerTask task)
    {
        return task.getVarSecondInteger();
    }

    @Override
    protected void setInteger(final ComputerTask task, final int val)
    {
        task.setVarSecondInteger(val);
    }

    @Override
    protected boolean isSecondValue()
    {
        return true;
    }
}
