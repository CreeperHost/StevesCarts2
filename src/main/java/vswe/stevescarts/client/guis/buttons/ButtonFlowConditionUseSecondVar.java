package vswe.stevescarts.client.guis.buttons;

import net.minecraft.entity.player.PlayerEntity;
import vswe.stevescarts.computer.ComputerTask;
import vswe.stevescarts.modules.workers.ModuleComputer;

public class ButtonFlowConditionUseSecondVar extends ButtonFlowCondition
{
    private boolean use;

    public ButtonFlowConditionUseSecondVar(final ModuleComputer module, final LOCATION loc, final boolean use)
    {
        super(module, loc);
        this.use = use;
    }

    @Override
    public String toString()
    {
        return use ? "Use second variable" : "Use integer";
    }

    @Override
    public int texture()
    {
        return use ? 38 : 39;
    }

    @Override
    public boolean isEnabled()
    {
        for (final ComputerTask task : ((ModuleComputer) module).getSelectedTasks())
        {
            if (use != task.getFlowConditionUseSecondVar())
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
            task.setFlowConditionUseSecondVar(use);
        }
    }
}
