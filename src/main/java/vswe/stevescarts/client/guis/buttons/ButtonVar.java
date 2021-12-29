package vswe.stevescarts.client.guis.buttons;

import vswe.stevescarts.computer.ComputerTask;
import vswe.stevescarts.modules.workers.ModuleComputer;

public abstract class ButtonVar extends ButtonAssembly
{
    public ButtonVar(final ModuleComputer module, final LOCATION loc)
    {
        super(module, loc);
    }

    @Override
    public boolean isVisible()
    {
        if (!super.isVisible())
        {
            return false;
        }
        if (((ModuleComputer) module).getSelectedTasks() != null && ((ModuleComputer) module).getSelectedTasks().size() > 0)
        {
            for (final ComputerTask task : ((ModuleComputer) module).getSelectedTasks())
            {
                if (!ComputerTask.isVar(task.getType()) || task.isVarEmpty() || (isSecondValue() && !task.hasTwoValues()))
                {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    protected boolean isSecondValue()
    {
        return false;
    }
}
