package vswe.stevescarts.entitys;

import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;

import java.util.List;

public class EntityDataManagerLockable extends SynchedEntityData
{
    private boolean isLocked;
    private List<DataItem<?>> lockedList;

    public EntityDataManagerLockable(Entity entity)
    {
        super(entity);
        for (DataItem<?> entry : entity.getEntityData().getAll())
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
    public void assignValues(List<DataItem<?>> entriesIn)
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
