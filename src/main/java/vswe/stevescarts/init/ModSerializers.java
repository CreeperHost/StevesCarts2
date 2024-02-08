package vswe.stevescarts.init;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import vswe.stevescarts.Constants;
import vswe.stevescarts.helpers.EnchantmentData;

public class ModSerializers
{
    public static final DeferredRegister<EntityDataSerializer<?>> SERIAL_REGISTER = DeferredRegister.create(NeoForgeRegistries.ENTITY_DATA_SERIALIZERS, Constants.MOD_ID);

    public static void init(IEventBus bus) {
        SERIAL_REGISTER.register(bus);
    }

    public static DeferredHolder<EntityDataSerializer<?>, EntityDataSerializer<int[]>> INT_ARRAY = SERIAL_REGISTER.register("int_array", () -> EntityDataSerializer.simple(FriendlyByteBuf::writeVarIntArray, FriendlyByteBuf::readVarIntArray));
    public static DeferredHolder<EntityDataSerializer<?>, EntityDataSerializer<BoolArray>> BOOL_ARRAY = SERIAL_REGISTER.register("bool_array", () -> EntityDataSerializer.simple((buf, bools) -> bools.write(buf), BoolArray::read));
    public static DeferredHolder<EntityDataSerializer<?>, EntityDataSerializer<ShortArray>> SHORT_ARRAY = SERIAL_REGISTER.register("short_array", () -> EntityDataSerializer.simple((buf, shorts) -> shorts.write(buf), ShortArray::read));
    public static DeferredHolder<EntityDataSerializer<?>, EntityDataSerializer<EnchantmentData>> ENCHANT_DATA = SERIAL_REGISTER.register("enchant_data", () -> EntityDataSerializer.simple((buf, data) -> data.write(buf), EnchantmentData::read));

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
}
