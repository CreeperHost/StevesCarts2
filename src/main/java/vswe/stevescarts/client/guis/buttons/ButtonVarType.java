package vswe.stevescarts.client.guis.buttons;

import net.minecraft.entity.player.PlayerEntity;
import vswe.stevescarts.computer.ComputerTask;
import vswe.stevescarts.modules.workers.ModuleComputer;

public class ButtonVarType extends ButtonAssembly
{
    private int typeId;

    public ButtonVarType(final ModuleComputer module, final LOCATION loc, final int id)
    {
        super(module, loc);
        typeId = id;
    }

    @Override
    public String toString()
    {
        return "Change to " + ComputerTask.getVarTypeName(typeId);
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
                if (!ComputerTask.isVar(task.getType()))
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
        return ComputerTask.getVarImage(typeId);
    }

    @Override
    public int ColorCode()
    {
        return 2;
    }

    @Override
    public boolean isEnabled()
    {
        for (final ComputerTask task : ((ModuleComputer) module).getSelectedTasks())
        {
            if (task.getVarType() != typeId)
            {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onServerClick(final PlayerEntity player, final int mousebutton, final boolean ctrlKey, final boolean shiftKey)
    {
        for (final ComputerTask task : ((ModuleComputer) module).getSelectedTasks())
        {
            task.setVarType(typeId);
        }
    }
}
