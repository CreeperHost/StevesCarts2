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
    public static final EntityDataSerializer<ShortArray> SHORT_ARRAY = EntityDataSerializer.simple((friendlyByteBuf, shortArray) -> shortArray.write(friendlyByteBuf), ShortArray::read);

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

        public byte[] getBytes() {
            return storage;
        }

        public static BoolArray fromBytes(byte[] bytes) {
            return new BoolArray(bytes);
        }
    }

    public static class ShortArray {
        private final short[] storage;

        public ShortArray(short[] storage) {
            this.storage = storage;
        }

        public ShortArray(int size) {
            storage = new short[size];
        }

        public short get(int index) {
            return storage[index];
        }

        public ShortArray set(int index, short value) {
            storage[index] = value;
            return this;
        }

        private void write(FriendlyByteBuf buf) {
            buf.writeVarInt(storage.length);
            for (short s : storage) {
                buf.writeShort(s);
            }
        }

        private static ShortArray read(FriendlyByteBuf buf) {
            short[] shorts = new short[buf.readVarInt()];
            for (int i = 0; i < shorts.length; i++) {
                shorts[i] = buf.readShort();
            }
            return new ShortArray(shorts);
        }

        public short[] getArray() {
            return storage;
        }
    }


    static {
        EntityDataSerializers.registerSerializer(BOOL_ARRAY);
        EntityDataSerializers.registerSerializer(SHORT_ARRAY);
    }
}
