package vswe.stevescarts.client.guis.buttons;

import net.minecraft.world.entity.player.Player;
import vswe.stevescarts.computer.ComputerTask;
import vswe.stevescarts.modules.workers.ModuleComputer;

public class ButtonInfoType extends ButtonAssembly
{
    private int typeId;

    public ButtonInfoType(final ModuleComputer module, final LOCATION loc, final int id)
    {
        super(module, loc);
        typeId = id;
    }

    @Override
    public String toString()
    {
        return "Change to " + ComputerTask.getInfoTypeName(typeId);
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
                if (!ComputerTask.isInfo(task.getType()))
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
        return ComputerTask.getInfoImage(typeId);
    }

    @Override
    public int ColorCode()
    {
        return 4;
    }

    @Override
    public boolean isEnabled()
    {
        for (final ComputerTask task : ((ModuleComputer) module).getSelectedTasks())
        {
            if (task.getInfoType() != typeId)
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
            task.setInfoType(typeId);
        }
    }
}
