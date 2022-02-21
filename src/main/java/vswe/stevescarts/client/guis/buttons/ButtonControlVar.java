package vswe.stevescarts.client.guis.buttons;

import net.minecraft.world.entity.player.Player;
import vswe.stevescarts.computer.ComputerTask;
import vswe.stevescarts.modules.workers.ModuleComputer;

public class ButtonControlVar extends ButtonControl
{
    protected boolean increase;

    public ButtonControlVar(final ModuleComputer module, final LOCATION loc, final boolean increase)
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
        if (((ModuleComputer) module).getSelectedTasks() != null)
        {
            for (final ComputerTask task : ((ModuleComputer) module).getSelectedTasks())
            {
                if (!task.getControlUseVar())
                {
                    return false;
                }
            }
        }
        return super.isVisible();
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
            if (increase && task.getControlVarIndex() < task.getProgram().getVars().size() - 1)
            {
                return true;
            }
            if (!increase && task.getControlVarIndex() > -1)
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
            task.setControlVar(task.getControlVarIndex() + (increase ? 1 : -1));
        }
    }
}
