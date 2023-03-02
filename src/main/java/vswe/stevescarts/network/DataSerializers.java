package vswe.stevescarts.network;

import net.minecraft.nbt.ByteArrayTag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;

/**
 * Created by brandon3055 on 27/02/2023
 */
public class DataSerializers {

    public static final EntityDataSerializer<BoolArray> BOOL_ARRAY = EntityDataSerializer.simple((friendlyByteBuf, boolArray) -> boolArray.write(friendlyByteBuf), BoolArray::read);

    public static class BoolArray {
        private final byte[] storage;

        BoolArray(byte[] storage) {
            this.storage = storage;
        }

        public BoolArray(int size) {
            storage = new byte[(int) Math.ceil(size / 8D)];
        }

        public boolean get(int index) {
            if (index < 0 || index / 8 >= storage.length) return false;
            return (storage[index / 8] & (1 << index % 8)) != 0;
        }

        public BoolArray set(int index, boolean value) {
            if (index < 0 || index / 8 >= storage.length || value == get(index)) return this;
            if (value) {
                storage[index / 8] |= (1 << index % 8);
            } else {
                storage[index / 8] &= ~(1 << index % 8);
            }
            return this;
        }

        private void write(FriendlyByteBuf buf) {
            buf.writeByteArray(storage);
        }

        private static BoolArray read(FriendlyByteBuf buf) {
            return new BoolArray(buf.readByteArray());
        }

        public byte[] getBytes(){
            return storage;
        }

        public static BoolArray fromBytes(byte[] bytes) {
            return new BoolArray(bytes);
        }
    }


    static {
        EntityDataSerializers.registerSerializer(BOOL_ARRAY);
    }
}
