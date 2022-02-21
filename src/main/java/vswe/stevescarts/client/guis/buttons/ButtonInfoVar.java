package vswe.stevescarts.client.guis.buttons;

import net.minecraft.world.entity.player.Player;
import vswe.stevescarts.computer.ComputerTask;
import vswe.stevescarts.modules.workers.ModuleComputer;

public class ButtonInfoVar extends ButtonAssembly
{
    protected boolean increase;

    public ButtonInfoVar(final ModuleComputer module, final LOCATION loc, final boolean increase)
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
                if (!ComputerTask.isInfo(task.getType()) || task.isInfoEmpty())
                {
                    return false;
                }
            }
            return true;
        }
        return false;
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
            if (increase && task.getInfoVarIndex() < task.getProgram().getVars().size() - 1)
            {
                return true;
            }
            if (!increase && task.getInfoVarIndex() > -1)
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
            task.setInfoVar(task.getInfoVarIndex() + (increase ? 1 : -1));
        }
    }
}
