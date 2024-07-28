package vswe.stevescarts.polylib;

import java.util.List;

/**
 * Created by brandon3055 on 27/07/2024
 */
public interface DataEntity {

    List<EntityData<?>> getEntityDataList();

    void registerEntityData(EntityData<?> data);

}
