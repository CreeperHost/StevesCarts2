package vswe.stevescarts.client.guis.buttons;

import vswe.stevescarts.computer.ComputerTask;
import vswe.stevescarts.modules.workers.ModuleComputer;

public class ButtonVarSecondVar extends ButtonVarVar
{
    public ButtonVarSecondVar(final ModuleComputer module, final LOCATION loc, final boolean increase)
    {
        super(module, loc, increase);
    }

    @Override
    protected int getIndex(final ComputerTask task)
    {
        return task.getVarSecondVarIndex();
    }

    @Override
    protected void setIndex(final ComputerTask task, final int val)
    {
        task.setVarSecondVar(val);
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
    protected boolean isSecondValue()
    {
        return true;
    }
}
