package vswe.stevescarts.init;

import net.minecraft.network.FriendlyByteBuf;
import net.neoforged.bus.api.IEventBus;
import org.apache.commons.lang3.ArrayUtils;

public class ModSerializers {
    public static void init(IEventBus bus) {
        //NO-OP
    }

    public static class BoolArray {
        private final byte[] storage;

        BoolArray(byte[] storage) {
            this.storage = storage;
        }

        public BoolArray(int size) {
            storage = new byte[(int) Math.ceil(size / 8D)];
        }

        public static BoolArray read(FriendlyByteBuf buf) {
            return new BoolArray(buf.readByteArray());
        }

        public static BoolArray fromBytes(byte[] bytes) {
            return new BoolArray(bytes);
        }

        public static BoolArray fromBytes(Byte[] bytes) {
            return new BoolArray(ArrayUtils.toPrimitive(bytes));
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

        public void write(FriendlyByteBuf buf) {
            buf.writeByteArray(storage);
        }

        public byte[] getBytes() {
            return storage;
        }
    }

    public static class ShortArray {
        private final short[] storage;

        public ShortArray(short[] storage) {
            this.storage = storage;
        }

        public ShortArray(Short[] storage) {
            this.storage = ArrayUtils.toPrimitive(storage);
        }

        public ShortArray(int size) {
            storage = new short[size];
        }

        public static ShortArray read(FriendlyByteBuf buf) {
            short[] shorts = new short[buf.readVarInt()];
            for (int i = 0; i < shorts.length; i++) {
                shorts[i] = buf.readShort();
            }
            return new ShortArray(shorts);
        }

        public short get(int index) {
            return storage[index];
        }

        public ShortArray set(int index, short value) {
            storage[index] = value;
            return this;
        }

        public void write(FriendlyByteBuf buf) {
            buf.writeVarInt(storage.length);
            for (short s : storage) {
                buf.writeShort(s);
            }
        }

        public short[] getArray() {
            return storage;
        }
    }
}
