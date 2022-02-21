package vswe.stevescarts.client.guis.buttons;

import net.minecraft.world.entity.player.Player;
import vswe.stevescarts.computer.ComputerTask;
import vswe.stevescarts.modules.workers.ModuleComputer;

public class ButtonFlowConditionOperator extends ButtonFlowCondition
{
    private int typeId;

    public ButtonFlowConditionOperator(final ModuleComputer module, final LOCATION loc, final int typeId)
    {
        super(module, loc);
        this.typeId = typeId;
    }

    @Override
    public String toString()
    {
        return "Change to " + ComputerTask.getFlowOperatorName(typeId, true);
    }

    @Override
    public int texture()
    {
        return 32 + typeId;
    }

    @Override
    public boolean isEnabled()
    {
        for (final ComputerTask task : ((ModuleComputer) module).getSelectedTasks())
        {
            if (typeId != task.getFlowConditionOperator())
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
            task.setFlowConditionOperator(typeId);
        }
    }
}
