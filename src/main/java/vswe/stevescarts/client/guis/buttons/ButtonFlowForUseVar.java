package vswe.stevescarts.client.guis.buttons;

import net.minecraft.world.entity.player.Player;
import vswe.stevescarts.computer.ComputerTask;
import vswe.stevescarts.modules.workers.ModuleComputer;

public abstract class ButtonFlowForUseVar extends ButtonFlowFor
{
    private boolean use;

    public ButtonFlowForUseVar(final ModuleComputer module, final LOCATION loc, final boolean use)
    {
        super(module, loc);
        this.use = use;
    }

    @Override
    public String toString()
    {
        return use ? ("Use " + getName() + " variable") : ("Use " + getName() + " integer");
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
            if (use != getUseVar(task))
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
            setUseVar(task, use);
        }
    }

    protected abstract boolean getUseVar(final ComputerTask p0);

    protected abstract void setUseVar(final ComputerTask p0, final boolean p1);

    protected abstract String getName();
}
