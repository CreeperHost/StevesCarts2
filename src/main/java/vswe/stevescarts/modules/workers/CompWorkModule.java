package vswe.stevescarts.modules.workers;

import vswe.stevescarts.api.modules.template.ModuleWorker;

import java.util.Comparator;

public class CompWorkModule implements Comparator
{
    @Override
    public int compare(final Object obj1, final Object obj2)
    {
        final ModuleWorker work1 = (ModuleWorker) obj1;
        final ModuleWorker work2 = (ModuleWorker) obj2;
        return (work1.getWorkPriority() < work2.getWorkPriority()) ? -1 : ((work1.getWorkPriority() > work2.getWorkPriority()) ? 1 : 0);
    }
}
