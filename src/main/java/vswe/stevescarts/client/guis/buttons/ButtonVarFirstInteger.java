package vswe.stevescarts.client.guis.buttons;

import vswe.stevescarts.computer.ComputerTask;
import vswe.stevescarts.modules.workers.ModuleComputer;

public class ButtonVarFirstInteger extends ButtonVarInteger
{
    public ButtonVarFirstInteger(final ModuleComputer module, final LOCATION loc, final int dif)
    {
        super(module, loc, dif);
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

    @Override
    protected int getInteger(final ComputerTask task)
    {
        return task.getVarFirstInteger();
    }

    @Override
    protected void setInteger(final ComputerTask task, final int val)
    {
        task.setVarFirstInteger(val);
    }
}
