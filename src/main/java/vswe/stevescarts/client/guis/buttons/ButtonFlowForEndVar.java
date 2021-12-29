package vswe.stevescarts.client.guis.buttons;

import vswe.stevescarts.computer.ComputerTask;
import vswe.stevescarts.modules.workers.ModuleComputer;

public class ButtonFlowForEndVar extends ButtonFlowForVar
{
    public ButtonFlowForEndVar(final ModuleComputer module, final LOCATION loc, final boolean increase)
    {
        super(module, loc, increase);
    }

    @Override
    protected int getIndex(final ComputerTask task)
    {
        return task.getFlowForEndVarIndex();
    }

    @Override
    protected void setIndex(final ComputerTask task, final int val)
    {
        task.setFlowForEndVar(val);
    }

    @Override
    protected String getName()
    {
        return "end";
    }

    @Override
    protected boolean isVarVisible(final ComputerTask task)
    {
        return task.getFlowForUseEndVar();
    }
}
