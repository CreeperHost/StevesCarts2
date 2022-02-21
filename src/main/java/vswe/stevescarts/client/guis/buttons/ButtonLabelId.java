package vswe.stevescarts.client.guis.buttons;

import net.minecraft.world.entity.player.Player;
import vswe.stevescarts.computer.ComputerTask;
import vswe.stevescarts.modules.workers.ModuleComputer;

public class ButtonLabelId extends ButtonAssembly
{
    private boolean increase;

    public ButtonLabelId(final ModuleComputer module, final LOCATION loc, final boolean increase)
    {
        super(module, loc);
        this.increase = increase;
    }

    @Override
    public String toString()
    {
        return increase ? "Increase ID" : "Decrease ID";
    }

    @Override
    public boolean isVisible()
    {
        if (!super.isVisible())
        {
            return false;
        }
        if (module instanceof ModuleComputer && ((ModuleComputer) module).getSelectedTasks() != null && ((ModuleComputer) module).getSelectedTasks().size() > 0)
        {
            for (final ComputerTask task : ((ModuleComputer) module).getSelectedTasks())
            {
                if (!task.isFlowLabel() && !task.isFlowGoto())
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
        return increase ? 23 : 24;
    }

    @Override
    public boolean isEnabled()
    {
        for (final ComputerTask task : ((ModuleComputer) module).getSelectedTasks())
        {
            if ((increase && task.getFlowLabelId() < 31) || (!increase && task.getFlowLabelId() > 0))
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
            task.setFlowLabelId(task.getFlowLabelId() + (increase ? 1 : -1));
        }
    }
}
