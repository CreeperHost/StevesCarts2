package vswe.stevescarts.client.guis.buttons;

import net.minecraft.world.entity.player.Player;
import vswe.stevescarts.computer.ComputerTask;
import vswe.stevescarts.modules.workers.ModuleComputer;

public class ButtonFlowConditionInteger extends ButtonFlowCondition
{
    private int dif;

    public ButtonFlowConditionInteger(final ModuleComputer module, final LOCATION loc, final int dif)
    {
        super(module, loc);
        this.dif = dif;
    }

    @Override
    public String toString()
    {
        if (dif < 0)
        {
            return "Decrease by " + -1 * dif;
        }
        return "Increase by " + dif;
    }

    @Override
    public int texture()
    {
        if (dif == 1)
        {
            return 40;
        }
        if (dif == -1)
        {
            return 41;
        }
        if (dif == 10)
        {
            return 42;
        }
        if (dif == -10)
        {
            return 43;
        }
        return super.texture();
    }

    @Override
    public boolean isVisible()
    {
        if (((ModuleComputer) module).getSelectedTasks() != null)
        {
            for (final ComputerTask task : ((ModuleComputer) module).getSelectedTasks())
            {
                if (task.getFlowConditionUseSecondVar())
                {
                    return false;
                }
            }
        }
        return super.isVisible();
    }

    @Override
    public boolean isEnabled()
    {
        for (final ComputerTask task : ((ModuleComputer) module).getSelectedTasks())
        {
            if (-128 <= task.getFlowConditionInteger() + dif && task.getFlowConditionInteger() + dif <= 127)
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
            task.setFlowConditionInteger(task.getFlowConditionInteger() + dif);
        }
    }
}
