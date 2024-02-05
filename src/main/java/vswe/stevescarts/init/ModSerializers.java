package vswe.stevescarts.init;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.jetbrains.annotations.NotNull;
import vswe.stevescarts.Constants;

public class ModSerializers
{
    public static final DeferredRegister<EntityDataSerializer<?>> SERIAL_REGISTER = DeferredRegister.create(NeoForgeRegistries.ENTITY_DATA_SERIALIZERS, Constants.MOD_ID);

    public static void init(IEventBus bus) {
        SERIAL_REGISTER.register(bus);
    }

    public static DeferredHolder<EntityDataSerializer<?>, EntityDataSerializer<int[]>> INT_ARRAY = SERIAL_REGISTER.register("int_array", () -> new EntityDataSerializer<>() {
        @Override
        public void write(FriendlyByteBuf buf, int @NotNull [] value) {
            buf.writeVarIntArray(value);
        }

        @Override
        public int @NotNull [] read(FriendlyByteBuf buf) {
            return buf.readVarIntArray();
        }

        @Override
        public int @NotNull [] copy(int @NotNull [] p_192717_1_) {
            return p_192717_1_;
        }

        @Override
        public @NotNull EntityDataAccessor<int[]> createAccessor(int id) {
            return new EntityDataAccessor<>(id, this);
        }
    });
}
