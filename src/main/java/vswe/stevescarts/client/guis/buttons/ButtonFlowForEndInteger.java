package vswe.stevescarts.client.guis.buttons;

import vswe.stevescarts.computer.ComputerTask;
import vswe.stevescarts.modules.workers.ModuleComputer;

public class ButtonFlowForEndInteger extends ButtonFlowForInteger
{
    public ButtonFlowForEndInteger(final ModuleComputer module, final LOCATION loc, final int dif)
    {
        super(module, loc, dif);
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

    @Override
    protected int getInteger(final ComputerTask task)
    {
        return task.getFlowForEndInteger();
    }

    @Override
    protected void setInteger(final ComputerTask task, final int val)
    {
        task.setFlowForEndInteger(val);
    }
}
