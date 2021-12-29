package vswe.stevescarts.entitys;

import net.minecraft.entity.Entity;
import net.minecraft.network.datasync.EntityDataManager;

import java.util.List;

public class EntityDataManagerLockable extends EntityDataManager
{
    private boolean isLocked;
    private List<DataEntry<?>> lockedList;

    public EntityDataManagerLockable(Entity entity)
    {
        super(entity);
        for (DataEntry entry : entity.getEntityData().getAll())
        {
            //TODO
            //			register(entry.getKey(), entry.getValue());
        }
        isLocked = true;
    }

    public void release()
    {
        isLocked = false;
        if (lockedList != null)
        {
            //TODO
            //			setEntryValues(lockedList);
        }
    }

    @Override
    public void assignValues(List<DataEntry<?>> entriesIn)
    {
        if (isLocked)
        {
            lockedList = entriesIn;
        }
        else
        {
            super.assignValues(entriesIn);
        }
    }
}
