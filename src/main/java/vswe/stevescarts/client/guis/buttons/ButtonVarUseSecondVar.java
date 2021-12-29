package vswe.stevescarts.client.guis.buttons;

import vswe.stevescarts.computer.ComputerTask;
import vswe.stevescarts.modules.workers.ModuleComputer;

public class ButtonVarUseSecondVar extends ButtonVarUseVar
{
    public ButtonVarUseSecondVar(final ModuleComputer module, final LOCATION loc, final boolean use)
    {
        super(module, loc, use);
    }

    @Override
    protected boolean getUseVar(final ComputerTask task)
    {
        return task.getVarUseSecondVar();
    }

    @Override
    protected void setUseVar(final ComputerTask task, final boolean val)
    {
        task.setVarUseSecondVar(val);
    }

    @Override
    protected String getName()
    {
        return "second";
    }

    @Override
    protected boolean isSecondValue()
    {
        return true;
    }
}
