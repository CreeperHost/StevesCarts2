package vswe.stevescarts.computer;

import vswe.stevescarts.modules.workers.ModuleComputer;

import java.util.ArrayList;

public class ComputerProg
{
    private ModuleComputer module;
    private int activeTaskId;
    private ArrayList<ComputerTask> tasks;
    private ArrayList<ComputerVar> vars;
    private short info;
    private String myName;

    public ComputerProg(final ModuleComputer module)
    {
        this.module = module;
        tasks = new ArrayList<>();
        vars = new ArrayList<>();
        info = 1;
    }

    public void start()
    {
        module.setActiveProgram(this);
        if (activeTaskId < 0 || activeTaskId >= tasks.size())
        {
            activeTaskId = 0;
            return;
        }
        activeTaskId = tasks.get(activeTaskId).preload(this, activeTaskId);
    }

    public int getActiveId()
    {
        return activeTaskId;
    }

    public void setActiveId(final int val)
    {
        activeTaskId = val;
    }

    public int getRunTime()
    {
        if (activeTaskId < 0 || activeTaskId >= tasks.size())
        {
            return activeTaskId = 0;
        }
        return tasks.get(activeTaskId).getTime();
    }

    public boolean run()
    {
        if (activeTaskId < 0 || activeTaskId >= tasks.size())
        {
            activeTaskId = 0;
            return false;
        }
        final int result = tasks.get(activeTaskId).run(this, activeTaskId);
        if (result == -1)
        {
            ++activeTaskId;
        }
        else
        {
            activeTaskId = result;
        }
        if (activeTaskId < 0 || activeTaskId >= tasks.size())
        {
            activeTaskId = 0;
            return false;
        }
        if (result == -1)
        {
            activeTaskId = tasks.get(activeTaskId).preload(this, activeTaskId);
        }
        return true;
    }

    public ArrayList<ComputerTask> getTasks()
    {
        return tasks;
    }

    public ArrayList<ComputerVar> getVars()
    {
        return vars;
    }

    public void setTaskCount(final int count)
    {
        while (tasks.size() > count)
        {
            tasks.remove(tasks.size() - 1);
        }
        while (tasks.size() < count)
        {
            tasks.add(new ComputerTask(module, this));
        }
    }

    public void setVarCount(final int count)
    {
        while (vars.size() > count)
        {
            vars.remove(vars.size() - 1);
        }
        while (vars.size() < count)
        {
            vars.add(new ComputerVar(module));
        }
    }

    public short getInfo()
    {
        return info;
    }

    public void setInfo(final short val)
    {
        info = val;
    }

    public void setName(final String name)
    {
        myName = name;
    }

    public String getName()
    {
        return myName;
    }

    @Override
    public String toString()
    {
        return getName();
    }
}
