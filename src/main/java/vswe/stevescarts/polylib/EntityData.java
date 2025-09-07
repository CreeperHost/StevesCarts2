package vswe.stevescarts.polylib;

import net.creeperhost.polylib.data.serializable.AbstractDataStore;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.network.PacketDistributor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import vswe.stevescarts.network.packets.PacketEntityData;

/**
 * Created by brandon3055 on 27/07/2024
 */
public class EntityData<T> {
    public static Logger LOGGER = LogManager.getLogger();

    private final DataEntity entity;
    private final AbstractDataStore<T> dataStore;
    private T previousValue;

    public EntityData(DataEntity entity, AbstractDataStore<T> dataStore) {
        this.entity = entity;
        this.dataStore = dataStore;
        this.previousValue = dataStore.get();
        entity.registerEntityData(this);
    }

    public T get() {
        return dataStore.get();
    }

    private Entity getEntity() {
        return (Entity) entity;
    }

    public void set(T value) {
        set(value, false);
    }

    public void set(T value, boolean force) {
        if (getEntity().level() != null && getEntity().level().isClientSide()) {
            return;
        }
        dataStore.set(value);
        detectAndSend(force);
    }

    public AbstractDataStore<T> getStorage() {
        return dataStore;
    }

    public void detectAndSend() {
        detectAndSend(false);
    }

    public void detectAndSend(boolean force) {
        if (!(getEntity().level() instanceof ServerLevel) || (dataStore.isSameValue(previousValue) && !force)) {
            return;
        }

        previousValue = dataStore.get();
        int index = entity.getEntityDataList().indexOf(this);
        if (index == -1) {
            LOGGER.warn("Invalid entity data found on entity ()", entity);
            return;
        }

        PacketDistributor.sendToPlayersTrackingEntity(getEntity(), new PacketEntityData(getEntity().getId(), index, dataStore));
    }

    public void toBytes(RegistryFriendlyByteBuf buffer) {
        dataStore.toBytes(buffer);
    }

    public void fromBytes(RegistryFriendlyByteBuf buffer) {
        dataStore.fromBytes(buffer);
    }

    public void save(String name, ValueOutput output) {
        dataStore.toTag(output.child(name));
    }

    public void load(String name, ValueInput input) {
        dataStore.fromTag(input.childOrEmpty(name));
    }
}
