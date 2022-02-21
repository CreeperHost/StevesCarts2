package vswe.stevescarts.client.guis.buttons;

import net.minecraft.world.entity.player.Player;
import vswe.stevescarts.computer.ComputerTask;
import vswe.stevescarts.modules.workers.ModuleComputer;

public class ButtonFlowEndType extends ButtonAssembly
{
    private int typeId;

    public ButtonFlowEndType(final ModuleComputer module, final LOCATION loc, final int typeId)
    {
        super(module, loc);
        this.typeId = typeId;
    }

    @Override
    public String toString()
    {
        return "Change to End " + ComputerTask.getEndTypeName(typeId);
    }

    @Override
    public int texture()
    {
        return ComputerTask.getEndImage(typeId);
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
                if (!task.isFlowEnd())
                {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean isEnabled()
    {
        for (final ComputerTask task : ((ModuleComputer) module).getSelectedTasks())
        {
            if (typeId != task.getFlowEndType())
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
            task.setFlowEndType(typeId);
        }
    }
}
