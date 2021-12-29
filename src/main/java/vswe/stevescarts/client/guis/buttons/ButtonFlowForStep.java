package vswe.stevescarts.client.guis.buttons;

import net.minecraft.entity.player.PlayerEntity;
import vswe.stevescarts.computer.ComputerTask;
import vswe.stevescarts.modules.workers.ModuleComputer;

public class ButtonFlowForStep extends ButtonFlowFor
{
    private boolean decrease;

    public ButtonFlowForStep(final ModuleComputer module, final LOCATION loc, final boolean decrease)
    {
        super(module, loc);
        this.decrease = decrease;
    }

    @Override
    public String toString()
    {
        return decrease ? "Set step to -1" : "Set step to +1";
    }

    @Override
    public int texture()
    {
        return decrease ? 45 : 44;
    }

    @Override
    public boolean isEnabled()
    {
        for (final ComputerTask task : ((ModuleComputer) module).getSelectedTasks())
        {
            if (decrease != task.getFlowForDecrease())
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
            task.setFlowForDecrease(decrease);
        }
    }
}
