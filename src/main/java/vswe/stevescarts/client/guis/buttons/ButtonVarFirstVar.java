package vswe.stevescarts.client.guis.buttons;

import vswe.stevescarts.computer.ComputerTask;
import vswe.stevescarts.modules.workers.ModuleComputer;

public class ButtonVarFirstVar extends ButtonVarVar
{
    public ButtonVarFirstVar(final ModuleComputer module, final LOCATION loc, final boolean increase)
    {
        super(module, loc, increase);
    }

    @Override
    protected int getIndex(final ComputerTask task)
    {
        return task.getVarFirstVarIndex();
    }

    @Override
    protected void setIndex(final ComputerTask task, final int val)
    {
        task.setVarFirstVar(val);
    }

    @Override
    protected String getName()
    {
        return "first";
    }

    @Override
    protected boolean isVarVisible(final ComputerTask task)
    {
        return task.getVarUseFirstVar();
    }
}
