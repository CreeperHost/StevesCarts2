package vswe.stevescarts.client.guis.buttons;

import net.minecraft.entity.player.PlayerEntity;
import vswe.stevescarts.computer.ComputerProg;
import vswe.stevescarts.computer.ComputerTask;
import vswe.stevescarts.modules.workers.ModuleComputer;

public class ButtonTaskType extends ButtonAssembly
{
    private int typeId;

    public ButtonTaskType(final ModuleComputer module, final LOCATION loc, final int id)
    {
        super(module, loc);
        typeId = id;
    }

    @Override
    public String toString()
    {
        if (haveTasks())
        {
            return "Change to " + ComputerTask.getTypeName(typeId);
        }
        return "Add " + ComputerTask.getTypeName(typeId) + " task";
    }

    @Override
    public boolean isVisible()
    {
        return super.isVisible();
    }

    @Override
    public int texture()
    {
        if (typeId < 4)
        {
            return typeId * 2 + (haveTasks() ? 1 : 0);
        }
        if (typeId == 4)
        {
            return 66 + (haveTasks() ? 1 : 0);
        }
        return typeId * 2 + (haveTasks() ? 1 : 0) - 2;
    }

    @Override
    public boolean isEnabled()
    {
        if (!(module instanceof ModuleComputer) || ((ModuleComputer) module).getCurrentProg() == null)
        {
            return false;
        }
        if (haveTasks())
        {
            for (final ComputerTask task : ((ModuleComputer) module).getSelectedTasks())
            {
                if (task.getType() != typeId)
                {
                    return true;
                }
            }
            return false;
        }
        return true;
    }

    private boolean haveTasks()
    {
        return ((ModuleComputer) module).getSelectedTasks().size() > 0;
    }

    @Override
    public void onServerClick(final PlayerEntity player, final int mousebutton, final boolean ctrlKey, final boolean shiftKey)
    {
        if (haveTasks())
        {
            for (final ComputerTask task : ((ModuleComputer) module).getSelectedTasks())
            {
                task.setType(typeId);
            }
        }
        else
        {
            final ComputerProg program = ((ModuleComputer) module).getCurrentProg();
            if (program != null)
            {
                final ComputerTask task = new ComputerTask((ModuleComputer) module, program);
                task.setType(typeId);
                program.getTasks().add(task);
            }
        }
    }
}
