package vswe.stevescarts.client.guis.buttons;

import net.minecraft.world.entity.player.Player;
import vswe.stevescarts.computer.ComputerTask;
import vswe.stevescarts.modules.workers.ModuleComputer;

public class ButtonFlowConditionVar extends ButtonFlowCondition
{
    protected boolean increase;

    public ButtonFlowConditionVar(final ModuleComputer module, final LOCATION loc, final boolean increase)
    {
        super(module, loc);
        this.increase = increase;
    }

    @Override
    public String toString()
    {
        if (increase)
        {
            return "Next variable";
        }
        return "Previous variable";
    }

    @Override
    public int texture()
    {
        return increase ? 30 : 31;
    }

    @Override
    public boolean isEnabled()
    {
        for (final ComputerTask task : ((ModuleComputer) module).getSelectedTasks())
        {
            if (increase && getIndex(task) < task.getProgram().getVars().size() - 1)
            {
                return true;
            }
            if (!increase && getIndex(task) > -1)
            {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onServerClick(final Player player, final int mousebutton, final boolean ctrlKey, final boolean shiftKey)
    {
        for (final ComputerTask task : ((ModuleComputer) module).getSelectedTasks())
        {
            setIndex(task, getIndex(task) + (increase ? 1 : -1));
        }
    }

    protected int getIndex(final ComputerTask task)
    {
        return task.getFlowConditionVarIndex();
    }

    protected void setIndex(final ComputerTask task, final int val)
    {
        task.setFlowConditionVar(val);
    }
}
