package vswe.stevescarts.client.guis.buttons;

import vswe.stevescarts.computer.ComputerTask;
import vswe.stevescarts.modules.workers.ModuleComputer;

public class ButtonFlowForStartInteger extends ButtonFlowForInteger
{
    public ButtonFlowForStartInteger(final ModuleComputer module, final LOCATION loc, final int dif)
    {
        super(module, loc, dif);
    }

    @Override
    protected String getName()
    {
        return "start";
    }

    @Override
    protected boolean isVarVisible(final ComputerTask task)
    {
        return task.getFlowForUseStartVar();
    }

    @Override
    protected int getInteger(final ComputerTask task)
    {
        return task.getFlowForStartInteger();
    }

    @Override
    protected void setInteger(final ComputerTask task, final int val)
    {
        task.setFlowForStartInteger(val);
    }
}
