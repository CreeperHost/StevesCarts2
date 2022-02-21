package vswe.stevescarts.entitys;

//import net.minecraft.network.PacketBuffer;
//import net.minecraft.network.datasync.DataParameter;
//import net.minecraft.network.datasync.DataSerializers;
//import net.minecraft.network.datasync.IDataSerializer;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;

public class CartDataSerializers
{
    public static void init()
    {
        EntityDataSerializers.registerSerializer(VARINT);
    }

    public static final EntityDataSerializer<int[]> VARINT = new EntityDataSerializer<int[]>()
    {
        @Override
        public void write(FriendlyByteBuf buf, int[] value)
        {
            buf.writeVarIntArray(value);
        }

        @Override
        public int[] read(FriendlyByteBuf buf)
        {
            return buf.readVarIntArray();
        }

        @Override
        public int[] copy(int[] p_192717_1_)
        {
            return p_192717_1_;
        }

        @Override
        public EntityDataAccessor<int[]> createAccessor(int id)
        {
            return new EntityDataAccessor<>(id, this);
        }
    };
}
