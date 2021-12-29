package vswe.stevescarts.client.guis.buttons;

import vswe.stevescarts.computer.ComputerTask;
import vswe.stevescarts.modules.workers.ModuleComputer;

public class ButtonFlowForUseEndVar extends ButtonFlowForUseVar
{
    public ButtonFlowForUseEndVar(final ModuleComputer module, final LOCATION loc, final boolean use)
    {
        super(module, loc, use);
    }

    @Override
    protected boolean getUseVar(final ComputerTask task)
    {
        return task.getFlowForUseEndVar();
    }

    @Override
    protected void setUseVar(final ComputerTask task, final boolean val)
    {
        task.setFlowForUseEndVar(val);
    }

    @Override
    protected String getName()
    {
        return "end";
    }
}
