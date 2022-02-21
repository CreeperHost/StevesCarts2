package vswe.stevescarts.client.guis.buttons;

import net.minecraft.world.entity.player.Player;
import vswe.stevescarts.computer.ComputerTask;
import vswe.stevescarts.modules.workers.ModuleComputer;

public class ButtonVarVar extends ButtonVar
{
    protected boolean increase;

    public ButtonVarVar(final ModuleComputer module, final LOCATION loc, final boolean increase)
    {
        super(module, loc);
        this.increase = increase;
    }

    @Override
    public String toString()
    {
        if (increase)
        {
            return "Next " + getName() + " variable";
        }
        return "Previous " + getName() + " variable";
    }

    @Override
    public boolean isVisible()
    {
        if (((ModuleComputer) module).getSelectedTasks() != null)
        {
            for (final ComputerTask task : ((ModuleComputer) module).getSelectedTasks())
            {
                if (!isVarVisible(task))
                {
                    return false;
                }
            }
        }
        return super.isVisible();
    }

    @Override
    public int texture()
    {
        return increase ? 30 : 31;
    }

    @Override
    public boolean isEnabled()
    {
        for (final ComputerTask task : ((ModuleComputer) module).getSelectedTasks())
        {
            if (increase && getIndex(task) < task.getProgram().getVars().size() - 1)
            {
                return true;
            }
            if (!increase && getIndex(task) > -1)
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
            setIndex(task, getIndex(task) + (increase ? 1 : -1));
        }
    }

    protected int getIndex(final ComputerTask task)
    {
        return task.getVarVarIndex();
    }

    protected void setIndex(final ComputerTask task, final int val)
    {
        task.setVarVar(val);
    }

    protected String getName()
    {
        return "main";
    }

    protected boolean isVarVisible(final ComputerTask task)
    {
        return true;
    }
}
