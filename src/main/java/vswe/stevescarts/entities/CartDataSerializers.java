package vswe.stevescarts.entities;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
import org.jetbrains.annotations.NotNull;

public class CartDataSerializers
{
    public static void init()
    {
        EntityDataSerializers.registerSerializer(VARINT);
    }

    public static final EntityDataSerializer<int[]> VARINT = new EntityDataSerializer<int[]>()
    {
        @Override
        public void write(FriendlyByteBuf buf, int @NotNull [] value)
        {
            buf.writeVarIntArray(value);
        }

        @Override
        public int @NotNull [] read(FriendlyByteBuf buf)
        {
            return buf.readVarIntArray();
        }

        @Override
        public int @NotNull [] copy(int @NotNull [] p_192717_1_)
        {
            return p_192717_1_;
        }

        @Override
        public @NotNull EntityDataAccessor<int[]> createAccessor(int id)
        {
            return new EntityDataAccessor<>(id, this);
        }
    };
}
