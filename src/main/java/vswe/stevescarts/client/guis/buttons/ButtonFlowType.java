package vswe.stevescarts.client.guis.buttons;

import net.minecraft.world.entity.player.Player;
import vswe.stevescarts.computer.ComputerTask;
import vswe.stevescarts.modules.workers.ModuleComputer;

public class ButtonFlowType extends ButtonAssembly
{
    private int typeId;

    public ButtonFlowType(final ModuleComputer module, final LOCATION loc, final int id)
    {
        super(module, loc);
        typeId = id;
    }

    @Override
    public String toString()
    {
        return "Change to " + ComputerTask.getFlowTypeName(typeId);
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
                if (!ComputerTask.isFlow(task.getType()))
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
        return ComputerTask.getFlowImage(typeId);
    }

    @Override
    public int ColorCode()
    {
        return 1;
    }

    @Override
    public boolean isEnabled()
    {
        for (final ComputerTask task : ((ModuleComputer) module).getSelectedTasks())
        {
            if (task.getFlowType() != typeId)
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
            task.setFlowType(typeId);
        }
    }
}
